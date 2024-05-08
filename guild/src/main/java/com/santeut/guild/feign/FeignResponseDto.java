package com.santeut.guild.feign;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FeignResponseDto<T> {
    private String status;
    private T data;
}