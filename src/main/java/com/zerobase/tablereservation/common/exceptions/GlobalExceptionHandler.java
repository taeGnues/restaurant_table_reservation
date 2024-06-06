package com.zerobase.tablereservation.common.exceptions;

import com.zerobase.tablereservation.common.constant.BaseResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 해당 클래스는 발생하는 서버에서 발생하는 Exception들을 적절히 가공해
 * 일정한 양식으로 클라이언트 측에 넘겨준다.
 */

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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResult> handleValidationExceptions(MethodArgumentNotValidException ex) {
        var errorMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        var result = BaseResult.builder()
                .code(HttpStatus.BAD_REQUEST)
                .message(errorMessage)
                .build();

        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResult> ExceptionHandle(Exception e){
        var result = BaseResult.builder()
                .code(HttpStatus.BAD_REQUEST)
                .message("알 수 없는 오류가 발생했습니다.")
                .build();

        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

}
