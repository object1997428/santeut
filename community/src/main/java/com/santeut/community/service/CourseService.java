package com.santeut.community.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.santeut.community.common.exception.FeignClientException;
import com.santeut.community.dto.request.PostCreateRequestDto;
import com.santeut.community.dto.response.CourseReadResponseDto;
import com.santeut.community.entity.PostEntity;
import com.santeut.community.feign.PartyClient;
import com.santeut.community.feign.dto.CommentListFeignDto;
import com.santeut.community.feign.dto.FeignPartyLatLngResponseDto;
import com.santeut.community.feign.service.AuthServerService;
import com.santeut.community.feign.service.CommonServerService;
import com.santeut.community.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CourseService {
    private final PostRepository postRepository;
    private final AuthServerService authServerService;
    private final CommonServerService commonServerService;
    private final PartyClient partyClient;

    ObjectMapper om = new ObjectMapper();

    public void createPost(PostCreateRequestDto postCreateRequestDto,int  userId) {

        // 게시글 DB에 저장
        PostEntity newPost = postRepository.save(PostEntity.builder()
                .userId(userId)
                .postType(postCreateRequestDto.getPostType())
                .postTitle(postCreateRequestDto.getPostTitle())
                .postContent(postCreateRequestDto.getPostContent())
                .userPartyId(postCreateRequestDto.getUserPartyId())
                .build()
        );
    }

    // 코스 게시글 조회 ( READ )
    public CourseReadResponseDto readPost(int postId, char postType,int partyUserId, int userId) {

        // party서버에서 등산 좌표 리스트 받아오기
        FeignPartyLatLngResponseDto feignPartyLatLngResponseDto = partyClient.getLatLngList(partyUserId, userId).orElseThrow(() -> new FeignClientException("Party에서 좌표 리스트 받아오기 실패함")).getData();

        // 게시판 정보 받아오기
        PostEntity postEntity = postRepository.findByIdAndPostType(postId, postType).orElseThrow((null));
        int  postIdToSend = postEntity.getId();
        int postUserIdToSend = postEntity.getUserId();
        char postTypeToSend =  postType;
        String postTitleToSend = postEntity.getPostTitle();
        String postContentToSend = postEntity.getPostContent();
        LocalDateTime createdAtToSend = postEntity.getCreatedAt();
        int partyUserIdToSend = partyUserId;
        String userNicknameToSend = authServerService.getNickname(postEntity.getUserId());

        // 소모임 정보 받아오기
//        HikingRecordFeignDto hikingRecord = partyClient.getHikingInfo(userId).orElseThrow(() -> new FeignClientException("party에서 등산 기록 조회 실패함")).getData();
//        String partyName = hikingRecord.getContent().
        // 그 외 정보 받아오기 ( 좋아요 수, 댓글 수, 조회수 , 작성자여부, 좋아요클릭여부 ..
        int commentCnt = commonServerService.getCommentCnt(postId, postType);
        int likeCnt = commonServerService.getLikeCnt(postId, postType);
        int hitCnt = postEntity.getHitCnt();
        boolean isLike = commonServerService.likePushed(postId, postType, userId);
        boolean isWriter = postEntity.getUserId() == userId;

        // 댓글 목록 받아오기
        List<CommentListFeignDto.Comment> commentList = commonServerService.getCommentList(postId, postType).getCommentList();

        return CourseReadResponseDto.builder()
                .postId(postIdToSend)
                .userPartyId(partyUserIdToSend)
                .userId(postUserIdToSend)
                .postTitle(postTitleToSend)
                .postContent(postContentToSend)
                .postType(postTypeToSend)
                .createdAt(createdAtToSend)
                .locationDataList(feignPartyLatLngResponseDto.getCourseList())
                .userNickname(userNicknameToSend)
                .likeCnt(likeCnt)
                .hitCnt(hitCnt)
                .isWriter(isWriter)
                .isLike(isLike)
                .commentList(commentList)
                .build();
    }
}
