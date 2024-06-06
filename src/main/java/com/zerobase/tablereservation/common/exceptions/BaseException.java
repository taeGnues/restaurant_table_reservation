package com.zerobase.tablereservation.common.exceptions;

import lombok.Getter;

// 기본적으로 사용할 CustomException이다.
@Getter
public class BaseException extends RuntimeException{
    private final ExceptionCode exceptionCode;

    public BaseException(ExceptionCode exceptionCode) {
        this.exceptionCode = exceptionCode;
    }

    public BaseException(String message, ExceptionCode exceptionCode) {
        super(message);
        this.exceptionCode = exceptionCode;
    }

    public BaseException(String message, Throwable cause, ExceptionCode exceptionCode) {
        super(message, cause);
        this.exceptionCode = exceptionCode;
    }

    public BaseException(Throwable cause, ExceptionCode exceptionCode) {
        super(cause);
        this.exceptionCode = exceptionCode;
    }

    public BaseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ExceptionCode exceptionCode) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.exceptionCode = exceptionCode;
    }
}
