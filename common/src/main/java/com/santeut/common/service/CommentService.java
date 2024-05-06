package com.santeut.common.service;

import com.santeut.common.common.exception.AccessDeniedException;
import com.santeut.common.common.exception.DataNotFoundException;
import com.santeut.common.common.exception.FeignClientException;
import com.santeut.common.common.exception.RepositorySaveException;
import com.santeut.common.dto.request.UserInfoFeignRequestDto;
import com.santeut.common.dto.response.CommentListResponseDto;
import com.santeut.common.entity.CommentEntity;
import com.santeut.common.feign.UserInfoClient;
import com.santeut.common.feign.service.AuthServerService;
import com.santeut.common.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    //    private final UserInfoClient userInfoClient;
    private final AuthServerService authServerService;

    // 댓글 작성 (CREATE)
    public void createComment(int postId, char postType, String commentContent, int userId) {
        try {
            commentRepository.save(CommentEntity.builder()
                    .commentReferenceType(postType)
                    .commentReferenceId(postId)
                    .userId(userId) // open feign 사용
                    .commentContent(commentContent)
                    .build()
            );
        } catch (Exception e) {
            log.info(e.getMessage());
            throw new RepositorySaveException("댓글 저장중 에러 발생");
        }
    }

    // 댓글 목록 가져오기 (READ)
    public CommentListResponseDto getComments(Integer postId, Character postType) {

        // guild 서버에 길드 정보 가져오기 #1
        // 구현 필요

        // 댓글 리스트 가져오기 (spring jpa)
        List<CommentEntity> commentEntitiesList = commentRepository.findAllByCommentReferenceIdAndCommentReferenceType(postId, postType)
                .orElseThrow(() -> new DataNotFoundException("댓글이 없습니다."));
        // Entity를 Dto에 맞게 변환해서 반환
        return CommentListResponseDto.builder()
                .guildId(null) // #1 구현후 값 넣어주기
                .commentList(commentEntitiesList
                        .stream()
                        .map(comment ->
                                CommentListResponseDto.CommentInfo.builder()
                                        .commentContent(comment.getCommentContent())
                                        .createdAt(comment.getCreatedAt())
                                        .userNickname(authServerService.getNickname(comment.getUserId())) // open feign 사용
                                        .build()
                        ).collect(Collectors.toList())
                )
                .build();
    }

    public void updateComment(Integer commentId, String commentContent, int userId) {
        // 댓글을 쓴 사람의 userId 조회
        int commentUserId = commentRepository.findById(commentId).orElseThrow(() -> new RuntimeException("커맨트 정보 조회중 오류 발생 ")).getUserId();

        // 권한이 없다면 에러 처리
        if (userId != commentUserId) {
            throw new AccessDeniedException("권한이 없습니다.");
        }
        // 댓글 수정 로직 구현
        commentRepository.updateCommentDirectly(commentId, commentContent);
    }

    public void deleteComment(Integer commentId, int userId) {

        // 댓글을 쓴 사람의 userId 조회
        int commentUserId = commentRepository.findById(commentId).orElseThrow(() -> new RuntimeException("커맨트 정보 조회중 오류 발생 ")).getUserId();

        // 권한이 없다면 에러 처리
        if (userId != commentUserId) {
            throw new AccessDeniedException("권한이 없습니다.");
        }
        // 댓글 삭제 로직 구현
        commentRepository.deleteCommentDirectly(commentId, LocalDateTime.now());
    }

    public int getCommentCnt(Integer postId, Character postType) {
        return commentRepository.countByCommentReferenceIdAndCommentReferenceType(postId, postType);
    }
}