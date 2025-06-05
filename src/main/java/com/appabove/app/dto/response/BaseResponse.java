package com.appabove.app.dto.response;

public class BaseResponse<T> {
    private int code;
    private String message;
    private T data;

    public BaseResponse() {
    }

    public BaseResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public BaseResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> BaseResponse<T> success(String message, T data) {
        return new BaseResponse<>(0, message, data);
    }

    public static <T> BaseResponse<T> success(String message) {
        return new BaseResponse<>(0, message);
    }

    public static <T> BaseResponse<T> error(String message) {
        return new BaseResponse<>(1, message);
    }

    // getters & setters

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

