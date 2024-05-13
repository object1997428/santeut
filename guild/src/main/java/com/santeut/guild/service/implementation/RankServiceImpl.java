package com.santeut.guild.service.implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.santeut.guild.common.exception.FeignClientException;
import com.santeut.guild.dto.response.PartyMemberInfo;
import com.santeut.guild.feign.FeignResponseDto;
import com.santeut.guild.feign.UserFeign;
import com.santeut.guild.feign.dto.PartyMemberInfoRequest;
import com.santeut.guild.feign.dto.PartyMemberInfoResponse;
import com.santeut.guild.service.RankService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class RankServiceImpl implements RankService {
    private final ObjectMapper om;
    private final RedisTemplate<String, String> redisTemplate;
    private final UserFeign userFeign;

    public PartyMemberInfoResponse getRankList(int loginUserId,int partyId,char type){
        List<Integer> userList=new ArrayList<>();
        switch (type) {
            case 'C':
                // 최다등반
                String key = "guild/" + partyId + "/mostHiking";
                List<String> topHikers = getTopUsersFromSortedSet(key, 5);
                topHikers.forEach(userId -> {
                    userList.add(parseUserId(userId));
                });
                break;
            case 'H':
                // 최고고도
                String bestHeightKey = "guild/" + partyId + "/bestHeight";
                List<String> topHeightUsers = getTopUsersFromSortedSet(bestHeightKey, 5);
                topHeightUsers.forEach(userId->{
                    userList.add(parseUserId(userId));
                });
                break;
            case 'D':
                // 최장거리
                String bestDistanceKey = "guild/" + partyId + "/bestDistance";
                List<String> topDistanceUsers = getTopUsersFromSortedSet(bestDistanceKey, 5);
                topDistanceUsers.forEach(userId->{
                    userList.add(parseUserId(userId));
                });
                break;
            default:
                break;
        }

        //Auth 서버한테 요청
        PartyMemberInfoRequest requestDto=PartyMemberInfoRequest.builder()
                .userIdList(userList)
                .build();
        ResponseEntity<FeignResponseDto<PartyMemberInfoResponse>> userInfos = userFeign.getPartyMemberInfo(requestDto);
        if(!userInfos.getStatusCode().is2xxSuccessful()) {
            log.error("[guild->auth] 랭킹 유저 정보를 불러오는 데 실패했습니다/userId {}",userInfos);
            throw new FeignClientException("[guild->auth] 랭킹 유저 정보를 불러오는 데 실패했습니다");
        }
        PartyMemberInfoResponse resp = userInfos.getBody().getData();
        return resp;
    }
    private List<String> getTopUsersFromSortedSet(String key, int topN) {
        Set<String> topUsers = redisTemplate.opsForZSet().reverseRange(key, 0, topN - 1);
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
