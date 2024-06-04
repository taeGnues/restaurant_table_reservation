package com.zerobase.tablereservation.common.exceptions;

import com.zerobase.tablereservation.common.constant.BaseResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<BaseResult> BaseExceptionHandle(BaseException e){
        var result = BaseResult.builder()
                .code(e.getExceptionCode().getHttpStatus())
                .message(e.getExceptionCode().getMessage())
                .build();

        return new ResponseEntity<>(result, e.getExceptionCode().getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResult> ExceptionHandle(Exception e){
        var result = BaseResult.builder()
                .code(HttpStatus.BAD_REQUEST)
                .message(e.getMessage()) // test용도로 추후 수정 필요함.
//                .message("알 수 없는 오류가 발생했습니다.")
                .build();

        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

}
