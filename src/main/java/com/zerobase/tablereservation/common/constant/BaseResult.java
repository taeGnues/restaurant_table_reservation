package com.zerobase.tablereservation.common.constant;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
/*
컨트롤러에 대한 에러 응답을 이 양식으로 전달한다.
 */
@Data
@Builder
public class BaseResult {
    private HttpStatus code;
    private String message;
}
