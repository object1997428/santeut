package com.santeut.common.service;

import com.santeut.common.common.exception.AccessDeniedException;
import com.santeut.common.common.exception.DataNotFoundException;
import com.santeut.common.common.exception.FeignClientException;
import com.santeut.common.common.exception.RepositorySaveException;
import com.santeut.common.dto.response.CommentListResponseDto;
import com.santeut.common.entity.CommentEntity;
import com.santeut.common.entity.LikeEntity;
import com.santeut.common.feign.service.AuthServerService;
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

    // 좋아요 개수 가져오기
    public int getLikeCnt(Integer postId, Character postType) {
        return likeRepository.countByLikeReferenceIdAndLikeReferenceType(postId, postType);
    }

    // 좋아요 누르기
    public void hitLike(Integer postId, Character postType, int userId) {

        if(likeRepository.countByUserIdAndLikeReferenceIdAndLikeReferenceType(userId,postId,postType) > 0) throw new AccessDeniedException("이미 좋아요를 눌렀습니다.");

        LikeEntity likeEntity = LikeEntity.builder()
                .likeReferenceId(postId)
                .likeReferenceType(postType)
                .userId(userId).build();
        likeRepository.save(likeEntity);
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