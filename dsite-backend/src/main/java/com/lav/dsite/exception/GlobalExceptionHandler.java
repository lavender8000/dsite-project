package com.lav.dsite.exception;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.lav.dsite.common.Result;
import com.lav.dsite.enums.ResponseStatus;
import com.lav.dsite.utils.LogManager;
import com.lav.dsite.utils.ResultHandler;

import io.lettuce.core.RedisException;
import jakarta.persistence.PersistenceException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private HttpServletRequest request;

    // (自定義異常)
    // DsiteService 異常 - 操作 Service層 異常
    @ExceptionHandler(DsiteServiceException.class)
    public ResponseEntity<Result<Object>> handleDsiteServiceException(DsiteServiceException e) {
        LogManager.exceptionWithUrl(e, request);
        return ResultHandler.getResponseEntity(Result.error(e.getResponseStatus()));
    }

    // HttpRequest異常 - 未攜帶Header
    // HttpRequest異常 - 未攜帶Body
    // HttpRequest異常 - 不支援的HttpMediaType
    // HttpRequest異常 - 不支援的HttpRequestMethod
    @ExceptionHandler({
        MissingRequestHeaderException.class,
        HttpMessageNotReadableException.class,
        HttpMediaTypeNotSupportedException.class,
        HttpRequestMethodNotSupportedException.class
    })
    public ResponseEntity<Result<Object>> handleInvalidRequestException(Exception e) {
        LogManager.exceptionWithUrl(e, request);
        return ResultHandler.getResponseEntity(Result.error(ResponseStatus.INVALID_REQUEST));
    }

    // 無效參數異常 - 非法參數
    // 無效參數異常 - 不符合參數規則(@Valid)
    // 無效參數異常 - 類型錯誤
    @ExceptionHandler({
        IllegalArgumentException.class, 
        MethodArgumentNotValidException.class,
        MethodArgumentTypeMismatchException.class
    })
    public ResponseEntity<Result<Object>> handleInvalidArgumentException(Exception e) {
        LogManager.exceptionWithUrl(e, request);
        return ResultHandler.getResponseEntity(Result.error(ResponseStatus.INVALID_ARGUMENT));
    }

    // 持久化異常
    @ExceptionHandler(PersistenceException.class)
    public ResponseEntity<Result<Object>> handlePersistenceException(PersistenceException e) {
        LogManager.exceptionWithUrl(e, request);
        return ResultHandler.getResponseEntity(Result.error(ResponseStatus.DATABASE_ERROR));
    }

    // Redis異常
    @ExceptionHandler(RedisException.class)
    public ResponseEntity<Result<Object>> handleRedisException(RedisException e) {
        LogManager.exceptionWithUrl(e, request);
        return ResultHandler.getResponseEntity(Result.error(ResponseStatus.REDIS_ERROR));
    }

    // IO異常
    @ExceptionHandler(IOException.class)
    public ResponseEntity<Result<Object>> handleIOException(IOException e) {
        LogManager.exceptionWithUrl(e, request);
        return ResultHandler.getResponseEntity(Result.error(ResponseStatus.IO_ERROR));
    }

    // 其他異常
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result<Object>> handleException(Exception e) {
        LogManager.exceptionWithUrl(e, request);
        LogManager.exception(e, e);
        return ResultHandler.getResponseEntity(Result.error(ResponseStatus.INTERNAL_SERVER_ERROR));
    }

}
