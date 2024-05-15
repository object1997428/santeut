package com.santeut.common.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.santeut.common.common.exception.*;
import com.santeut.common.dto.request.AlarmRequestDto;
import com.santeut.common.dto.request.CommunityFeignDto;
import com.santeut.common.dto.request.GuildPostFeignDto;
import com.santeut.common.dto.request.UserInfoFeignRequestDto;
import com.santeut.common.dto.response.CommentListResponseDto;
import com.santeut.common.entity.CommentEntity;
import com.santeut.common.feign.CommunityClient;
import com.santeut.common.feign.GuildClient;
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
    private final AlarmService alarmService;
    private final CommunityClient communityClient;
    private final GuildClient guildClient;

    // 댓글 작성 (CREATE)
    @Transactional
    public void createComment(int postId, char postType, String commentContent, int userId) throws JsonProcessingException {
            CommentEntity comment = commentRepository.save(CommentEntity.builder()
                    .commentReferenceType(postType)
                    .commentReferenceId(postId)
                    .userId(userId) // open feign 사용
                    .commentContent(commentContent)
                    .build()
            );
            // 길드 게시판인지 커뮤니티 게시판인지에 따라 다른 서버 서비스 호출
            int postUserId = 0;
            String commentUserNickname = authServerService.getNickname(userId);
            if(postType == 'G') {
                postUserId = guildClient.getPostInfo(postId, userId).orElseThrow(() -> new FeignClientException("guild에서 길드게시글 요청 실패")).getData().getUserId();
            }else {
                postUserId = communityClient.getPostInfo(postId, postType, userId).orElseThrow(() -> new FeignClientException("community에 요청 실패")).getData().getUserId();
            }
            // 알람 만들 DTO 작성
            AlarmRequestDto alarmRequestDto = AlarmRequestDto.builder()
                    .userId(postUserId)
                    .referenceId(comment.getCommentReferenceId())
                    .referenceType(comment.getCommentReferenceType() + "C")
                    .alarmTitle("✉ 댓글 알림")
                    .alarmContent(commentUserNickname+"님이 댓글을 남기셨습니다.")
                    .build();

//             알람을 만들어주는 함수 호출
            alarmService.createAlarm(comment.getCommentReferenceId(), alarmRequestDto.getReferenceType(), alarmRequestDto);

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