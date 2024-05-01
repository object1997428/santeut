package com.santeut.mountain.common.exception;

import com.santeut.mountain.common.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ApiExceptionController {

  // 리소스를 찾을 수 없음
  @ExceptionHandler(NotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ErrorResponse notFoundException(NotFoundException e) {
    return constructErrorResponse(e, HttpStatus.NOT_FOUND, "[mountain] not found exception");
  }

  private ErrorResponse constructErrorResponse(Exception e, HttpStatus status, String errorType) {
    log.error("[exceptionHandle] ex={}", e.getMessage(), e);
    return new ErrorResponse(status.value(), errorType, e.getMessage());
  }

}
