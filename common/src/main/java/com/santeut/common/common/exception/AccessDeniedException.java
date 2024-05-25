package com.santeut.common.common.exception;

//필요한 에러 이렇게 만들어서 쓰세요
public class AccessDeniedException extends RuntimeException{
    public AccessDeniedException(String message) {
        super(message);
    }
}