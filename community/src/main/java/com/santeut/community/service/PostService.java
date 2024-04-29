package com.santeut.community.service;

import com.santeut.community.common.exception.ZeroDataException;
import com.santeut.community.dto.request.PostReqeustRequestDto;
import com.santeut.community.dto.response.PostListResponseDto;
import com.santeut.community.dto.response.PostReadResponseDto;
import com.santeut.community.dto.response.UserInfoFeignRequestDto;
import com.santeut.community.entity.PostEntity;
import com.santeut.community.feign.LikeCommentCntClient;
import com.santeut.community.feign.UserInfoClient;
import com.santeut.community.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserInfoClient userInfoClient;
    private final LikeCommentCntClient likeCommentCntClient;


    // 게시글 목록을 불러오는 Service
    public PostListResponseDto getPosts(char postType) {
        PostListResponseDto postListResponseDto = new PostListResponseDto(postRepository.findAllByPostType(postType)
                .orElseThrow(() -> new ZeroDataException("데이터를 찾지 못했습니다."))
                .stream()
                .map(post -> {
                            // 닉네임 받아오기 위한 feign 함수
                            UserInfoFeignRequestDto userInfoFeignRequestDto = userInfoClient.getUserInfo();
                            // 좋아요 수 가져오기 위한 feign 함수
                            int likeCnt = likeCommentCntClient.getLikeCnt().get("likeCnt");
                            // 댓글 수 가져오기 위한 feign 함수
                            int commentCnt = likeCommentCntClient.getCommentCnt().get("commentCnt");
                            return PostListResponseDto.PostInfo.builder()
                                    .postId(post.getId())
                                    .postTitle(post.getPostTitle())
                                    .postContent(post.getPostContent())
                                    .likeCnt(likeCnt)
                                    .commentCnt(commentCnt)
                                    .createdAt(post.getCreatedAt())
                                    .postType(post.getPostType())
                                    .userNickname(userInfoFeignRequestDto.getUserNickname())
                                    .build();
                        }
                )
                .collect(Collectors.toList()));

        return postListResponseDto;
    }

    // 게시글 작성 (CREATE)
    public void createPost(PostReqeustRequestDto postReqeustRequestDto) {
        postRepository.save(PostEntity.builder()
                .userId(postReqeustRequestDto.getUserId())
                .postType(postReqeustRequestDto.getPostType())
                .postTitle(postReqeustRequestDto.getPostTitle())
                .postContent(postReqeustRequestDto.getPostContent())
                .userPartyId(postReqeustRequestDto.getUserPartyId())
                .build()
        );
    }

    // 게시글 읽기 (READ)
    public PostReadResponseDto readPost(int postId, char postType) {
        return postRepository.findByIdAndPostType(postId, postType)
                .map(post -> {
                    // PostEntity가 존재하면 사용자 정보 가져오기
                    return userInfoClient.getUserInfo(post.getUserId())
                            .map(userInfo -> {
                                // UserInfo가 존재하면 PostReadResponseDto 생성 및 반환
                                return PostReadResponseDto.builder()
                                        .postId(post.getId())
                                        .postType(post.getPostType())
                                        .postTitle(post.getPostTitle())
                                        .postContent(post.getPostContent())
                                        .nickName(userInfo.getUserNickname())
                                        .build();
                            })
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found for given post")); // UserInfo가 없는 경우 예외 처리
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found")); // PostEntity가 없는 경우 예외 처리

    }
    // 게시글 수정 (UPDATE)

    // 게시글 삭제 (DELETE)
}


