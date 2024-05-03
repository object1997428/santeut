package com.santeut.guild.common.exception;

import com.santeut.guild.common.response.ResponseCode;
import lombok.Getter;

@Getter
public class DataNotFoundException extends RuntimeException{

    int code;
    String message;

    public DataNotFoundException(String message) {
        super(message);
    }

    public DataNotFoundException(ResponseCode responseCode) {
        super(responseCode.getMessage());
        this.code = responseCode.getCode();
        this.message = responseCode.getMessage();
    }

    public DataNotFoundException(int code, String message){
        super(message);
        this.code = code;
        this.message = message;
    }

}
