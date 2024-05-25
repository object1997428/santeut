package com.santeut.common.repository;

import com.santeut.common.entity.LikeEntity;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<LikeEntity, Integer> {

    // 좋아요 개수 조회
    int countByLikeReferenceIdAndLikeReferenceType(@NotNull int likeReferenceId, @NotNull Character likeReferenceType);

    // 좋아요 이미 눌렀는지 체크
    int countByUserIdAndLikeReferenceIdAndLikeReferenceType(int userId, Integer likeReferenceId, Character likeReferenceType);

    // 좋아요 취소(삭제)
    void deleteByLikeReferenceIdAndLikeReferenceTypeAndUserId(Integer likeReferenceId, Character likeReferenceType, Integer userId);
}
