package com.santeut.hiking.common.exception;

//필요한 에러 이렇게 만들어서 쓰세요
public class FeginFailerException extends RuntimeException{
    public FeginFailerException(String message) {
        super(message);
    }
}
