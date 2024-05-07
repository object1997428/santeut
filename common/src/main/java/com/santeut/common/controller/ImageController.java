package com.santeut.common.controller;

import com.santeut.common.common.response.BasicResponse;
import com.santeut.common.common.util.ResponseUtil;
import com.santeut.common.entity.ImageEntity;
import com.santeut.common.repository.ImageRepository;
import com.santeut.common.service.ImageService;
import com.santeut.common.service.LikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/image")
@Slf4j
@RestController
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    // 게시글 이미지 저장 (CREATE)
    @PostMapping("/upload/{referenceId}/{referenceType}")
    public ResponseEntity<BasicResponse> saveImageUrl(@PathVariable Integer referenceId, @PathVariable Character referenceType,@RequestBody Map<String, String> imageUrl) {
        imageService.savePostImages(referenceId, referenceType, imageUrl.get("imageUrl"));
        return ResponseUtil.buildBasicResponse(HttpStatus.OK, "성공적으로 이미지가 저장되었습니다.");
    }

    // 게시글 이미지들 불러오기 (READ)
    @GetMapping("/{referenceId}/{referenceType}")
    public ResponseEntity<BasicResponse> getImages(@PathVariable Integer referenceId, @PathVariable Character referenceType) {
        return ResponseUtil.buildBasicResponse(HttpStatus.OK, imageService.getPostImages(referenceId, referenceType));
    }
}
