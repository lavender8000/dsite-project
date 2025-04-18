package com.lav.dsite.common;

import com.lav.dsite.enums.ResponseStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {

    private int status;

    private ResponseStatus responseStatus;

    // 描述訊息
    private String msg;

    // 返回的資料
    private T data;

    public Result(int status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public Result(ResponseStatus responseStatus, T data) {
        this.status = responseStatus.getCode();
        this.msg = responseStatus.getMessage();
        this.data = data;
        this.responseStatus = responseStatus;
    }

    public static <T> Result<T> success() {
        return new Result<>(ResponseStatus.SUCCESS, null);
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(ResponseStatus.SUCCESS, data);
    }

    public static <T> Result<T> created() {
        return new Result<>(ResponseStatus.CREATED, null);
    }

    public static <T> Result<T> created(T data) {
        return new Result<>(ResponseStatus.CREATED, data);
    }

    public static <T> Result<T> error(ResponseStatus responseStatus) {
        return new Result<>(responseStatus, null);
    }
}
