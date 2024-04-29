package com.santeut.auth.common.exception;

import com.santeut.auth.common.response.ResponseCode;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException{

    private final int code;
    private final String message;

    public CustomException(ResponseCode responseHeader) {
        super(responseHeader.getMessage());
        this.code = responseHeader.getCode();
        this.message = responseHeader.getMessage();
    }

    public CustomException(int status, String message){
        super(message);
        this.code = status;
        this.message = message;
    }
}
