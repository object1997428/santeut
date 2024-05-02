package com.santeut.common.repository;

import com.santeut.common.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {
    Optional<List<CommentEntity>> findAllByCommentReferenceIdAndCommentReferenceType(Integer commentReferenceId, Character commentReferenceType);

    // 댓글 수정 jpa
    @Modifying
    @Transactional
    @Query("UPDATE CommentEntity c SET c.commentContent = :commentContent WHERE c.id=:commentId")
    void updateCommentDirectly(@Param("commentId") Integer commentId, @Param("commentContent") String commentContent);

    // 댓글 수정 jpa
    @Modifying
    @Transactional
    @Query("UPDATE CommentEntity c SET c.isDeleted = true , c.deletedAt = :deletedAt WHERE c.id= :commentId")
    void deleteCommentDirectly(@Param("commentId") Integer commentId, @Param("deletedAt") LocalDateTime deletedAt);

    int countByCommentReferenceIdAndCommentReferenceType(Integer commentReferenceId, Character commentReferenceType);
}
