package com.santeut.community.feign;

import com.santeut.community.common.config.FeignConfiguration;
import com.santeut.community.dto.response.UserInfoFeignRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@FeignClient(name = "LikeCommentCntClient", url="${common-service.url}", configuration = FeignConfiguration.class)
public interface LikeCommentCntClient {
    // 좋아요 수 가져오기
    @GetMapping("/like-cnt") // api 만들어지면 그걸로 바꿔야함
    Map<String, Integer> getLikeCnt();

    @GetMapping("/comment-cnt") // api 만들어지면 그걸로 바꿔야함
    Map<String, Integer> getCommentCnt();
}
