package com.santeut.community.feign.service;

import com.santeut.community.common.exception.FeignClientException;
import com.santeut.community.dto.response.CommentListResponseDto;
import com.santeut.community.feign.CommonClient;
import feign.Feign;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommonServerService {
    private final CommonClient commonClient;

    public int getLikeCnt(Integer postId, Character postType) {
        Map<String, Integer> result =  commonClient.getLikeCnt(postId, postType).orElseThrow(() ->new FeignClientException("common 서버에서 좋아요 개수 가져오기 실패함"));
        return result.get("likeCnt");
    }

    public int getCommentCnt(Integer postId, Character postType) {
        Map<String, Integer> result =  commonClient.getCommentCnt(postId, postType).orElseThrow(() ->new FeignClientException("common 서버에서 댓글 개수 가져오기 실패함"));
        return result.get("commentCnt");
    }

    public CommentListResponseDto getCommentList(int postId, char postType) {
        return commonClient.getCommentList(postId, postType).orElseThrow(() -> new FeignClientException("common 서버에서 댓글 목록 가져오기 실패함"));
    }
}
