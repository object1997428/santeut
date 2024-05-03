package com.santeut.community.dto.request;

import lombok.*;

@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostCreateRequestDto {

    private String postTitle;

    private String postContent;

    private Character postType;

    private Integer userPartyId;
}
