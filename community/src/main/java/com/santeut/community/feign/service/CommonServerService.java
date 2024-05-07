package com.santeut.community.feign.service;

import com.santeut.community.common.exception.FeignClientException;
import com.santeut.community.feign.dto.CommentListFeignDto;
import com.santeut.community.feign.CommonClient;
import feign.Feign;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommonServerService {
    private final CommonClient commonClient;

    public int getLikeCnt(Integer postId, Character postType) {
        return commonClient.getLikeCnt(postId, postType).orElseThrow(() ->new FeignClientException("common 서버에서 좋아요 개수 가져오기 실패함")).getData().get("likeCnt");
    }

    // 좋아요 눌렀는지 여부 가져오기
    public boolean likePushed(Integer postId, Character postType, Integer userId) {
        return commonClient.likePushed(postId, postType, userId).orElseThrow(()->new FeignClientException("common서버에서 좋아요 여부 가져오기 실패함")).getData().get("likePushed");
    }

    public int getCommentCnt(Integer postId, Character postType) {
        return commonClient.getCommentCnt(postId, postType).orElseThrow(() ->new FeignClientException("common 서버에서 댓글 개수 가져오기 실패함")).getData().get("commentCnt");
    }

    public CommentListFeignDto getCommentList(int postId, char postType) {
        return commonClient.getCommentList(postId, postType).orElseThrow(() -> new FeignClientException("common 서버에서 댓글 목록 가져오기 실패함")).getData();
    }

    public void saveImageUrl(Integer postId, Character postType, String imageUrl) {
        Map<String, String> imageUrlJson = new HashMap<>();
        imageUrlJson.put("imageUrl", imageUrl);
        commonClient.saveImageUrl(postId, postType, imageUrlJson);
    }

    public List<String> getImages(Integer postId, Character postType) {
        return commonClient.getImages(postId, postType).orElseThrow(()->new FeignClientException("common 서버에서 이미지 목록 가져오기 실패함")).getData();
    }
}
