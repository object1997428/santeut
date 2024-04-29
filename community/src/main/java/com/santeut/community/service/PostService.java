package com.santeut.community.service;

import com.santeut.community.common.exception.AccessDeniedException;
import com.santeut.community.common.exception.ZeroDataException;
import com.santeut.community.dto.request.PostCreateReqeustRequestDto;
import com.santeut.community.dto.request.PostUpdateReqeustRequestDto;
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
    public void createPost(PostCreateReqeustRequestDto postCreateReqeustRequestDto) {
        postRepository.save(PostEntity.builder()
                .userId(postCreateReqeustRequestDto.getUserId())
                .postType(postCreateReqeustRequestDto.getPostType())
                .postTitle(postCreateReqeustRequestDto.getPostTitle())
                .postContent(postCreateReqeustRequestDto.getPostContent())
                .userPartyId(postCreateReqeustRequestDto.getUserPartyId())
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
    public void updatePost(PostUpdateReqeustRequestDto postUpdateReqeustRequestDto, int postId, char postType) {
        // title, content 외의 부분은 그대로 저장을 해야 하므로 우선 게시글을 조회해서 PostEntity에 넣어줌
        PostEntity post = postRepository.findByIdAndPostType(postId, postType)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
        // 바뀐 제목과 본문을 저장해줌
        post.setPostTitle(postUpdateReqeustRequestDto.getPostTitle());
        post.setPostContent(postUpdateReqeustRequestDto.getPostContent());
        // jpa를 이용하여 실제로 저장함
        postRepository.save(post);
    }

    // 게시글 삭제 (DELETE)
    public void deletePost(int postId, char postType) {
        UserInfoFeignRequestDto userInfo = userInfoClient.getUserInfo();
        PostEntity page = postRepository.findByIdAndPostType(postId, postType).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
        if(page.getUserId() == userInfo.getUserId()) {
            postRepository.deleteById(postId);
        }else {
            throw new AccessDeniedException("삭제할 권한이 없습니다.");
        }
        postRepository.deleteById(postId);
    }
}


