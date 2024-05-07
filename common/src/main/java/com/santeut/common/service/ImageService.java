package com.santeut.common.service;

import com.santeut.common.common.exception.DataNotFoundException;
import com.santeut.common.entity.ImageEntity;
import com.santeut.common.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ImageService {
    private final ImageRepository imageRepository;

    // 게시판 이미지들을 테이블에 저장
    public void savePostImages(Integer referenceId, Character referenceType, String imageUrl) {
        imageRepository.save(ImageEntity.builder()
                .imageReferenceId(referenceId)
                .imageReferenceType(referenceType)
                .imageUrl(imageUrl)
                .build()
        );
    }

    // 게시글 이미지들 불러오기
    public List<String> getPostImages(Integer referenceId, Character referenceType) {
        List<String> images = new ArrayList<>();
        List<ImageEntity> imageEntities = imageRepository.findAllByImageReferenceIdAndImageReferenceType(referenceId, referenceType).orElse(null);

        // 이미지가 없으면 빈 배열을 반환
        if (imageEntities == null) {
            return images;
        }
        // 이미지가 존재하면 url을 꺼내 list에 담아 반환
        for (ImageEntity imageEntity : imageEntities ) {
            images.add(imageEntity.getImageUrl());
        }

        return images;
    }

}
