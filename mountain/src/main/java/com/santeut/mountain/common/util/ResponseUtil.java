package com.santeut.mountain.common.util;

import com.santeut.mountain.common.response.BasicResponse;
import com.santeut.mountain.common.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseUtil {

  public static ResponseEntity<BasicResponse> buildBasicResponse(HttpStatus status, Object data) {
    BasicResponse basicResponse = new BasicResponse(status.value(), data);
    return new ResponseEntity<>(basicResponse, status);
  }

  public static ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status,
      String errorName, String message) {
    ErrorResponse errorResponse = new ErrorResponse(status.value(), errorName, message);
    return new ResponseEntity<>(errorResponse, status);
  }

}
