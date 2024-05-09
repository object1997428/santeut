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
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/image")
@Slf4j
@RestController
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    // 게시글 이미지 저장 (CREATE)
    @PostMapping("/save/{referenceId}/{referenceType}")
    public ResponseEntity<BasicResponse> uploadFile(@PathVariable int referenceId, @PathVariable char referenceType, @RequestPart List<MultipartFile> images) {
        List<String> imageUrlList = new ArrayList<>();

        // S3에 저장
        for(MultipartFile image : images) {
            imageUrlList.add(imageService.uploadImage(image));
        }
        Map<String, List<String>> res = new HashMap<>();
        // DB에 이미지 URL 저장
        for(String imageUrl : imageUrlList) {
            imageService.savePostImages(referenceId, referenceType, imageUrl);
        }
        return ResponseUtil.buildBasicResponse(HttpStatus.OK, "성공적으로 이미지가 저장 됨");
    }

    // 게시글 이미지들 불러오기 (READ)
    @GetMapping("/{referenceId}/{referenceType}")
    public ResponseEntity<BasicResponse> getImages(@PathVariable Integer referenceId, @PathVariable Character referenceType) {
        return ResponseUtil.buildBasicResponse(HttpStatus.OK, imageService.getPostImages(referenceId, referenceType));
    }

}
