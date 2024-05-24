package com.santeut.guild.common.exception;

public class FeignClientException extends RuntimeException{
    public FeignClientException(String message) {
        super(message);
    }
}
