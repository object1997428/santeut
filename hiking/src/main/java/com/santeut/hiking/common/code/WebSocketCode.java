package com.santeut.hiking.common.code;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum WebSocketCode {
    SUCCESS("200","성공"),
    FAIL("201","불가능"),
    CHECK("300", "연결 체크")
    ;

    private final String code;
    private final String message;

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    WebSocketCode(final String code, final String message){
        this.code = code;
        this.message = message;
    }

    public static WebSocketCode codeOf(String code){
        return Arrays.stream(values())
                .filter(value -> value.getCode().equals(code))
                .findFirst()
                .orElse(null);
    }
}
