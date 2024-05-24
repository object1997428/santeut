package com.santeut.guild.service.implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.santeut.guild.common.exception.FeignClientException;
import com.santeut.guild.dto.response.PartyMemberInfo;
import com.santeut.guild.dto.response.RankMembersInfoResponse;
import com.santeut.guild.dto.response.RankUserInfo;
import com.santeut.guild.feign.FeignResponseDto;
import com.santeut.guild.feign.UserFeign;
import com.santeut.guild.feign.dto.PartyMemberInfoRequest;
import com.santeut.guild.feign.dto.PartyMemberInfoResponse;
import com.santeut.guild.service.RankService;
import feign.FeignException;
import io.lettuce.core.ScoredValue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class RankServiceImpl implements RankService {
    private final ObjectMapper om;
    private final RedisTemplate<String, String> redisTemplate;
    private final UserFeign userFeign;
    private final int maxN = 5;

    public RankMembersInfoResponse getRankList(int loginUserId, int guildId, char type) {
        List<Integer> userList = new ArrayList<>();
        List<RankUserInfo> rankList = new ArrayList<>();
        List<ZSetOperations.TypedTuple<String>> rankHikers = new ArrayList<>();
        String scoreTail = "";
        switch (type) {
            case 'C':
                // 최다등반
                String key = "guild/" + guildId + "/mostHiking";
                rankHikers = getTopUsersFromSortedSetWithScores(key, 5);
                scoreTail = "번";
                break;
            case 'H':
                // 최고고도
                String bestHeightKey = "guild/" + guildId + "/bestHeight";
                rankHikers = getTopUsersFromSortedSetWithScores(bestHeightKey, 5);
                scoreTail = "m";
                break;
            case 'D':
                // 최장거리
                String bestDistanceKey = "guild/" + guildId + "/bestDistance";
                rankHikers = getTopUsersFromSortedSetWithScores(bestDistanceKey, 5);
                scoreTail = "km";
                break;
            default:
                break;
        }
        DecimalFormat formatter = new DecimalFormat("#,###");
        for (int i = 0; i < rankHikers.size(); i++) {

            ZSetOperations.TypedTuple<String> typedTuple = rankHikers.get(i);
            String formattedScore = (type=='C')?((int) Math.floor(typedTuple.getScore()) +""):formatter.format(typedTuple.getScore());
            String finalScore = formattedScore + scoreTail;
            RankUserInfo rankUserInfo = RankUserInfo.builder()
                    .order(i + 1)
                    .score(finalScore)
                    .userId(parseUserId(typedTuple.getValue()))
                    .build();
            rankList.add(rankUserInfo);
            userList.add(parseUserId(typedTuple.getValue()));
        }

        //Auth 서버한테 요청
        PartyMemberInfoRequest requestDto = PartyMemberInfoRequest.builder()
                .userIdList(userList)
                .build();
        ResponseEntity<FeignResponseDto<PartyMemberInfoResponse>> userInfos = userFeign.getPartyMemberInfo(requestDto);
        if (!userInfos.getStatusCode().is2xxSuccessful()) {
            log.error("[guild->auth] 랭킹 유저 정보를 불러오는 데 실패했습니다/userId {}", userInfos);
            throw new FeignClientException("[guild->auth] 랭킹 유저 정보를 불러오는 데 실패했습니다");
        }
        PartyMemberInfoResponse feinResp = userInfos.getBody().getData();
        for (int i = 0; i < feinResp.getPartyMembers().size(); i++) {
            rankList.get(i).setUserNickname(feinResp.getPartyMembers().get(i).getUserNickname());
            rankList.get(i).setUserProfile(feinResp.getPartyMembers().get(i).getUserProfile());
        }
        RankMembersInfoResponse resp = RankMembersInfoResponse.builder()
                .partyMembers(rankList)
                .build();
        return resp;
    }

    private List<ZSetOperations.TypedTuple<String>> getTopUsersFromSortedSetWithScores(String key, int topN) {
        Set<ZSetOperations.TypedTuple<String>> topUsers = redisTemplate.opsForZSet().reverseRangeWithScores(key, 0, topN - 1);
        return new ArrayList<>(topUsers);
    }


    private Integer parseUserId(String userId) {
        String cleanedUserId = userId.replaceAll("[^\\d]", "");
        try {
            return Integer.parseInt(cleanedUserId);
        } catch (NumberFormatException e) {
            log.error("사용자 ID 파싱 실패: 원본 값 '{}', 정제된 값 '{}'", userId, cleanedUserId, e);
            return null;
        }
    }
}
