package com.santeut.auth.dto.request;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class UpdateProfileImageRequest {

    MultipartFile userProfile;
}
