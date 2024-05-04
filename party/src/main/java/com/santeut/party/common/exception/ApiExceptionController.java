package com.santeut.party.common.exception;



import com.santeut.party.common.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.NoHandlerFoundException;


@Slf4j
@RestControllerAdvice
public class ApiExceptionController {

    // 소모임이 존재하지 않음
    @ExceptionHandler(DataNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleDataNotFoundException(DataNotFoundException e) {
        return constructErrorResponse(e,HttpStatus.NOT_FOUND,"DataNotFoundException");
    }

    // 이미 가입한 사용자
    @ExceptionHandler(AlreadyJoinedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleAlreadyJoinedException(AlreadyJoinedException e) {
        return constructErrorResponse(e,HttpStatus.CONFLICT,"AlreadyJoinedException");
    }

    // 잘못된 인수값 전달
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse illegalExHandle(IllegalArgumentException e) {
        return constructErrorResponse(e,HttpStatus.BAD_REQUEST,"IllegalArgumentException");
    }

    // 부적절한 객체상태오류
    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse illegalExHandle(IllegalStateException e) {
        return constructErrorResponse(e,HttpStatus.BAD_REQUEST, "IllegalStateException");
    }

    // 잘못된 요청 데이터
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationExceptions(MethodArgumentNotValidException e) {
        return constructErrorResponse(e,HttpStatus.BAD_REQUEST, "handleValidationExceptions");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ResponseStatusException.class)
    public ErrorResponse responseStatusException(ResponseStatusException e) {
        return constructErrorResponse(e,HttpStatus.BAD_REQUEST, "ResponseStatusException");
    }

    // 리소스를 찾을 수 없음
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNoHandlerFoundException(NoHandlerFoundException e) {
        return constructErrorResponse(e,HttpStatus.NOT_FOUND, "handleNoHandlerFoundException");
    }

    @ExceptionHandler(DataNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleDataNotFoundException(DataNotFoundException e) {
        return constructErrorResponse(e,HttpStatus.NOT_FOUND, "handleDataNotFoundException");
    }

    // 권한없음(접근거부)
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleAccessDeniedException(AccessDeniedException e) {
        return constructErrorResponse(e,HttpStatus.FORBIDDEN, "handleAccessDeniedException");
    }

    //만료된 파티에 접근할 때 발생하는 오류
    @ExceptionHandler(PartyExpiredException.class)
    @ResponseStatus(HttpStatus.GONE)
    public ErrorResponse handlePartyExpiredException(PartyExpiredException e) {
        return constructErrorResponse(e,HttpStatus.GONE, "handlePartyExpiredException");
    }

    //시작할 수 없는 파티에 접근할 때 발생하는 오류
    @ExceptionHandler(PartyNotStartedException.class)
    @ResponseStatus(HttpStatus.GONE)
    public ErrorResponse handlePartyNotStartedException(PartyNotStartedException e) {
        return constructErrorResponse(e,HttpStatus.GONE, "handlePartyNotStartedException");
    }

    //데이터 정합성이 맞지 않음
    @ExceptionHandler(DataMismatchException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleDataMissMatchException(DataMismatchException e) {
        return constructErrorResponse(e,HttpStatus.GONE, "handleDataMissMatchException");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(Exception e) {
        return constructErrorResponse(e,HttpStatus.GONE, "handleException");
    }

    private ErrorResponse constructErrorResponse(Exception e, HttpStatus status, String errorType) {
        log.error("[Party Server][exceptionHandle] ex={}", e.getMessage(), e);
        return new ErrorResponse(status.value(), errorType, e.getMessage());
    }
}
