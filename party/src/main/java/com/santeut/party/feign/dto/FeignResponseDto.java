package com.santeut.party.feign.dto;

import lombok.Data;

@Data
public class FeignResponseDto<T> {
  public int status;
  public T data;
}
