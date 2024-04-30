package com.santeut.auth.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ImageUtil {

    private String originName;
    private String storedName;
    private String accessUrl;

    public String extractExtension(String originName){

        int index = originName.lastIndexOf('.');
        return originName.substring(index, originName.length());
    }

    public String getFileName(String originName){
        return UUID.randomUUID()+"."+extractExtension(originName);
    }

    public String saveImage(MultipartFile multipartFile){

        String originalName = multipartFile.getOriginalFilename();
        Imag
    }
}
