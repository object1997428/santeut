package com.santeut.common.repository;

import com.santeut.common.entity.CommentEntity;
import com.santeut.common.entity.LikeEntity;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<LikeEntity, Integer> {

    // 좋아요 개수 조회
    int countByLikeReferenceIdAndLikeReferenceType(@NotNull int likeReferenceId, @NotNull Character likeReferenceType);

    Optional<LikeEntity> findByLikeReferenceIdAndLikeReferenceType(Integer referenceId, Character referenceType);

    // 좋아요 이미 눌렀는지 체크
    int countByUserIdAndLikeReferenceIdAndLikeReferenceType(int userId, Integer likeReferenceId, Character likeReferenceType);

    //
//    @Query("UPDATE LikeEntity l SET l.isDeleted = true")
//    void deleteLikeDirectly(int likeReferenceId, char likeReferenceType, int userId);

    void deleteByLikeReferenceIdAndLikeReferenceTypeAndUserId(Integer likeReferenceId, Character likeReferenceType, Integer userId);
}
