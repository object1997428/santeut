package com.santeut.community.dto.request;

import lombok.*;

@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostUpdateRequestDto {
    private String postTitle;
    private String postContent;
}
