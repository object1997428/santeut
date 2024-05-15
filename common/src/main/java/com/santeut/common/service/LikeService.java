package com.santeut.common.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.santeut.common.common.exception.AccessDeniedException;
import com.santeut.common.common.exception.DataNotFoundException;
import com.santeut.common.common.exception.FeignClientException;
import com.santeut.common.common.exception.RepositorySaveException;
import com.santeut.common.dto.FCMCategory;
import com.santeut.common.dto.FCMRequestDto;
import com.santeut.common.dto.request.AlarmRequestDto;
import com.santeut.common.dto.response.CommentListResponseDto;
import com.santeut.common.entity.AlarmTokenEntity;
import com.santeut.common.entity.CommentEntity;
import com.santeut.common.entity.LikeEntity;
import com.santeut.common.feign.CommunityClient;
import com.santeut.common.feign.GuildClient;
import com.santeut.common.feign.service.AuthServerService;
import com.santeut.common.repository.AlarmTokenRepository;
import com.santeut.common.repository.CommentRepository;
import com.santeut.common.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final AuthServerService authServerService;
    private final AlarmTokenRepository alarmTokenRepository;
    private final GuildClient guildClient;
    private final CommunityClient communityClient;
    private final AlarmService alarmService;

    // 좋아요 개수 가져오기
    public int getLikeCnt(Integer postId, Character postType) {
        return likeRepository.countByLikeReferenceIdAndLikeReferenceType(postId, postType);
    }

    // 좋아요 누르기
    public void hitLike(Integer postId, Character postType, int userId) throws JsonProcessingException {

        if(likeRepository.countByUserIdAndLikeReferenceIdAndLikeReferenceType(userId,postId,postType) > 0) throw new AccessDeniedException("이미 좋아요를 눌렀습니다.");

        LikeEntity likeEntity = LikeEntity.builder()
                .likeReferenceId(postId)
                .likeReferenceType(postType)
                .userId(userId).build();
        likeRepository.save(likeEntity);

        // 길드 게시판인지 커뮤니티 게시판인지에 따라 다른 서버 서비스 호출
        int postUserId = 0;
        String commentUserNickname = authServerService.getNickname(userId);
        if(postType == 'G') {
            postUserId = guildClient.getPostInfo(postId, userId).orElseThrow(() -> new FeignClientException("guild에서 길드게시글 요청 실패")).getData().getUserId();
        }else {
            postUserId = communityClient.getPostInfo(postId, postType, userId).orElseThrow(() -> new FeignClientException("community에 요청 실패")).getData().getUserId();
        }

        // 알람 만들 DTO 작성
        AlarmRequestDto alarmRequestDto = AlarmRequestDto.builder()
                .userId(postUserId)
                .referenceId(likeEntity.getLikeReferenceId())
                .referenceType(likeEntity.getLikeReferenceType() + "L")
                .alarmTitle("👍 좋아요 알림")
                .alarmContent(commentUserNickname+"님이 좋아요를 누르셨습니다.")
                .build();

//             알람을 만들어주는 함수 호출
        alarmService.createAlarm(likeEntity.getLikeReferenceId(), alarmRequestDto.getReferenceType(), alarmRequestDto);
    }

    // 좋아요 했는지 체크해주기
    public boolean likePushed(Integer postId, Character postType, int userId) {
        return likeRepository.countByUserIdAndLikeReferenceIdAndLikeReferenceType(userId,postId, postType) > 0;
    }

    // 좋아요 취소하기
    public void cancleLike(Integer postId, Character postType, int userId) {
        likeRepository.deleteByLikeReferenceIdAndLikeReferenceTypeAndUserId(postId, postType, userId);
    }
}