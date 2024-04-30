package com.santeut.mountain.common.exception;

import com.santeut.mountain.common.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ApiExceptionController {

  
  private ErrorResponse constructErrorResponse(Exception e, HttpStatus status, String errorType) {
    log.error("[exceptionHandle] ex={}", e.getMessage(), e);
    return new ErrorResponse(status.value(), errorType, e.getMessage());
  }

}
