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
    NOT_FIND_USER(HttpStatus.NOT_FOUND,"알 수 없는 유저의 접근입니다."),
    DUPLICATE_USERNAME(HttpStatus.BAD_REQUEST,"중복되는 username이 존재합니다."),

    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "예약이 존재하지 않습니다."),
    RESERVATION_TOO_QUICK(HttpStatus.BAD_REQUEST, "최소 현재 시각에서 15분 이후부터 예약이 가능합니다."),
    RESERVATION_ALREADY_DONE(HttpStatus.BAD_REQUEST, "이미 등록된 예약이 존재합니다."),
    RESERVATION_TOO_LATE(HttpStatus.BAD_REQUEST, "예약 시간 10분 전까지 방문해야합니다."),

    REVIEW_NEEDED_VISIT(HttpStatus.BAD_REQUEST, "리뷰를 작성하려면 먼저 해당 매장에 방문해야합니다."),
    REVIEW_ALREADY_DONE(HttpStatus.BAD_REQUEST, "이미 작성된 리뷰가 존재합니다."),
    REVIEW_EMPTY(HttpStatus.BAD_REQUEST, "리뷰가 존재하지 않습니다."),


    /*
     * 500 : DB, Server 오류
     */
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
