package com.lav.dsite.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.lav.dsite.common.Result;

public class ResultHandler {
    public static <T> ResponseEntity<Result<T>> getResponseEntity(Result<T> result) {
        
        MediaType mediaType = MediaType.APPLICATION_JSON;

        HttpStatus httpStatus;
        try {
            httpStatus = HttpStatus.valueOf(result.getStatus());
        } catch (IllegalArgumentException e) {
            // 處理狀態碼 異常
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            String msg = result.getMsg();
            result.setMsg(msg + ", Unhandled status code, returning internal server error");
        }

        return ResponseEntity.status(httpStatus).contentType(mediaType).body(result);
    }
}
