package com.santeut.community.dto.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PostCreateRequestDto {

    private String postTitle;

    private String postContent;

    private Character postType;

    private Integer userPartyId;

    private List<String> images;
}
