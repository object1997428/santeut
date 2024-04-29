package com.santeut.community.dto.request;

import lombok.*;

@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostUpdateReqeustRequestDto {
    private String postTitle;
    private String postContent;
    private Character postType;
}
