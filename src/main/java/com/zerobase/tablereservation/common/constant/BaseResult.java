package com.zerobase.tablereservation.common.constant;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class BaseResult {
    private HttpStatus code;
    private String message;
}
