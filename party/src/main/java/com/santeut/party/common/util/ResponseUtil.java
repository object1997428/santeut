package com.santeut.party.common.util;



import com.santeut.party.common.response.BasicResponse;
import com.santeut.party.common.response.ErrorResponse;
import com.santeut.party.common.response.PagingResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class ResponseUtil {

    public static ResponseEntity<BasicResponse> buildBasicResponse(HttpStatus status, Object data) {
        BasicResponse basicResponse = new BasicResponse(status.value(), data);
        return new ResponseEntity<>(basicResponse, status);
    }

    public static ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status, String errorName, String message) {
        ErrorResponse errorResponse = new ErrorResponse(status.value(), errorName, message);
        return new ResponseEntity<>(errorResponse, status);
    }

    public static ResponseEntity<PagingResponse> buildPagingResponse(
            HttpStatus status, PagingResponse pagingResponse) {
        return new ResponseEntity<>(pagingResponse, status);
    }

    public static ResponseEntity<PagingResponse> buildPagingResponse(
            HttpStatus status,
            List<?> content,
            boolean isFirst,
            boolean isLast,
            int page,
            int totalPage,
            Long totalElements,
            int size,
            boolean sorted,
            boolean asc,
            boolean filtered) {

        PagingResponse pagingResponse = new PagingResponse(
                status.value(),
                content,
                isFirst,
                isLast,
                page,
                totalPage,
                totalElements,
                size,
                sorted,
                asc,
                filtered
        );
        return new ResponseEntity<>(pagingResponse, status);
    }
}
