package com.santeut.community.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.santeut.community.common.exception.S3Exception;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageService {
    private final AmazonS3 s3;

    @Value("${cloud.aws.s3.bucketName}")
    private String bucketName;

        public String uploadImage(MultipartFile file) {
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        try {
            s3.putObject(new PutObjectRequest(bucketName, fileName, file.getInputStream(), null));
            return s3.getUrl(bucketName, fileName).toString();
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new S3Exception("S3 파일 업로드 실패");
        }
    }
}
