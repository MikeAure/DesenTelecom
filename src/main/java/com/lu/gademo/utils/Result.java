package com.lu.gademo.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    int code;
    String message;
    T data;

    public static <T> Result<T> success(T data) {
       return new Result<T>(200, "success", data);
    }

    public static Result<?> success() {
        return success(null);
    }

    public static <T> Result<T> error(T data) {
        return new Result<>(400, "error", data);
    }

    public static Result<?> error() {
        return error(null);
    }
}
