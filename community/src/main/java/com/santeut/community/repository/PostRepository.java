package com.santeut.community.repository;

import com.santeut.community.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<PostEntity, Integer> {
        Optional<List<PostEntity>> findAllByPostType(char postType);
        Optional<PostEntity> findByIdAndPostType(int postId, char postType);
}
