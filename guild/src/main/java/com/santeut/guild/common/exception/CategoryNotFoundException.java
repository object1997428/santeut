package com.santeut.guild.common.exception;

import com.santeut.guild.common.response.ResponseCode;
import lombok.Getter;

@Getter
public class CategoryNotFoundException extends RuntimeException{

    int code;
    String message;

    public CategoryNotFoundException(String message) {
        super(message);
    }

    public CategoryNotFoundException(ResponseCode responseCode) {
        super(responseCode.getMessage());
        this.code = responseCode.getCode();
        this.message = responseCode.getMessage();
    }

    public CategoryNotFoundException(int code, String message){
        super(message);
        this.code = code;
        this.message = message;
    }

}
