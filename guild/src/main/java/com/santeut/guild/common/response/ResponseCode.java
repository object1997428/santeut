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
    EXISTS_GUILD_NAME(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "존재하는 동호회명"),
    NOT_EXISTS_GUILD(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "존재하지 않는 동호회"),
    NOT_MATCH_GUILD_LEADER(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "동호회장이 아닙니다."),

    // 지역
    WRONG_REGION_ID(HttpServletResponse.SC_BAD_REQUEST, "잘못된 지역 ID"),
    WRONG_REGION_NAME(HttpServletResponse.SC_BAD_REQUEST, "잘못된 지역명"),

    // 길드 유저
    NOT_EXISTS_GUILD_USER(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "존재하지 않는 내 동호회"),

    // 길드 요청
    ALREADY_REQUEST(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "이미 동호회 요청 중입니다."),
    ALREADY_APPROVE(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "이미 가입된 동호회입니다.");

    private final int code;
    private final String message;
}
