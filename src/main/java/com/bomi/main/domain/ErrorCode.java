package com.bomi.main.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    MEMBER_NOT_EXIST(HttpStatus.NOT_ACCEPTABLE, "ACCOUNT-000", "회원가입하지 않은 이용자입니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "ACCOUNT-001", "존재하지 않는 회원입니다."),
    MEMBER_NOT_MATCH(HttpStatus.FORBIDDEN, "ACCOUNT-002", "권한이 없는 정보 요청입니다.")
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

}
