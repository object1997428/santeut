package com.santeut.common.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FCMRequestDto {

    public String title;
    public String content;
    public String category;

    public static FCMRequestDto of(String title, String content, FCMCategory category) {
        return FCMRequestDto.builder()
                .title(title)
                .content(content)
                .category(category.code)
                .build();
    }

}