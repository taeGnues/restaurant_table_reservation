package com.zerobase.tablereservation.common.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ExceptionCode {
    /*
     * 400 : Request, Response 오류
     */
    WRONG_TYPE_ROLE(HttpStatus.BAD_REQUEST, "잘못된 타입의 role를 입력했습니다."),
    WRONG_PASSWORD(HttpStatus.BAD_REQUEST, "잘못된 비밀번호를 입력했습니다."),
    CUSTOMER_NOT_FOUND_USERNAME(HttpStatus.NOT_FOUND, "해당 username에 해당하는 고객을 찾을 수 없습니다."),


    MANAGER_NOT_FOUND_USERNAME(HttpStatus.NOT_FOUND, "해당 username에 해당하는 점장을 찾을 수 없습니다."),
    MANAGER_EMPTY_RESTAURANT(HttpStatus.NOT_FOUND, "해당 유저가 등록한 식당이 없습니다."),
    MANAGER_ALREADY_REGISTERED(HttpStatus.BAD_REQUEST, "이미 등록된 식당이 존재합니다."),

    RESTAURANT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 식당은 존재하지 않습니다."),
    NOT_FIND_USER(HttpStatus.NOT_FOUND,"일치하는 유저가 없습니다."),
    DUPLICATE_USERNAME(HttpStatus.BAD_REQUEST,"중복되는 username이 존재합니다."),


    /*
     * 500 : DB, Server 오류
     */
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
