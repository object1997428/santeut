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

}
