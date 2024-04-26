package com.santeut.community.service;

import com.santeut.community.common.exception.ZeroDataException;
import com.santeut.community.dto.request.PostRequestDto;
import com.santeut.community.dto.response.PostResponseDto;
import com.santeut.community.entity.PostEntity;
import com.santeut.community.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    // 게시글 리스트 조회
    public PostResponseDto getPosts(char postType) {
        return new PostResponseDto(postRepository.findAllByPostType(postType)
                .orElseThrow(() -> new ZeroDataException("데이터를 찾지 못했습니다."))
                .stream()
                .map(post -> /*new PostResponseDto.PostInfo(
                        post.getId(),
                        post.getPostTitle(),
                        post.getPostContent(),
                        post.getCreatedAt()*/
                        PostResponseDto.PostInfo.builder()
                                .postId(post.getId())
                                .userId(post.getUserId())
                                .postTitle(post.getPostTitle())
                                .postContent(post.getPostContent())
                                .createdAt(post.getCreatedAt())
                                .postType(post.getPostType())
                                .likeCnt(null)
                                .commentCnt(null)
                                .hit(null)
                                .build()
                )
                .collect(Collectors.toList()));
    }
    // 게시글 작성
    public void createPost(PostRequestDto postRequestDto) {
        postRepository.save(PostEntity.builder()
                .userId(postRequestDto.getUserId())
                .postType(postRequestDto.getPostType())
                .postTitle(postRequestDto.getPostTitle())
                .postContent(postRequestDto.getPostContent())
                .userPartyId(postRequestDto.getUserPartyId())
                .build()
        );
    }
}