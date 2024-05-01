package com.santeut.common.repository;

import com.santeut.common.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {
    Optional<List<CommentEntity>> findAllByCommentReferenceIdAndCommentReferenceType(Integer commentReferenceId, Character commentReferenceType);
}
