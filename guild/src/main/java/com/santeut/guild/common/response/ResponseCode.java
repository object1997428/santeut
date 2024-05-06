package com.santeut.guild.common.response;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ResponseCode {

    SUCCESS(HttpStatus.OK.value(), "success"),

    // 유저
    EXISTS_USER_NICKNAME(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "존재하는 유저 닉네임"),
    EXISTS_USER_LOGIN_ID(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "존재하는 로그인 아이디"),
    NOT_MATCH_USER_LOGIN_ID(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "로그인 아이디가 일치하지 않음"),
    NOT_MATCH_USER_PASSWORD(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "패스워드가 일치하지 않음"),
    NOT_EXISTS_USER(HttpServletResponse.SC_NOT_FOUND, "존재하지 않는 유저"),
    NOT_EXISTS_USER_LEVEL(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "존재하지 않는 등급 정보"),

    // 길드
    EXISTS_GUILD_NAME(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "존재하는 길드명"),
    NOT_EXISTS_GUILD(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "존재하지 않는 길드"),
    NOT_MATCH_GUILD_LEADER(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "동호회장이 아닙니다.");

    private final int code;
    private final String message;
}
