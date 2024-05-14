package com.santeut.common.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FCMRequestDto {

    public String type; //PUSH, POPUP
    public String title;
    public String content;
    public String category;

    public static FCMRequestDto of(String type,String title, String content, FCMCategory category) {
        return FCMRequestDto.builder()
                .type(type)
                .title(title)
                .content(content)
                .category(category.code)
                .build();
    }

}