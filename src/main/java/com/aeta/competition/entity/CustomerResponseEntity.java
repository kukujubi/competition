package com.aeta.competition.entity;

import com.aeta.competition.util.ResultCode;

public class CustomerResponseEntity<T> {
    private Integer code;
    private String message;
    private T data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
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

    private CustomerResponseEntity() {
    }

    public static <T> CustomerResponseEntity<T> getResponse(ResultCode code, String message) {
        return getResponse(code, message, null);
    }

    public static <T> CustomerResponseEntity<T> getResponse(T data) {
        return getResponse(ResultCode.SUCCESS, null, data);
    }

    public static <T> CustomerResponseEntity<T> getResponse(ResultCode code, String message, T data) {
        CustomerResponseEntity<T> responseEntity = new CustomerResponseEntity<T>();
        responseEntity.setCode(code.getCode());
        if (message != null && !message.equals("")) {
            responseEntity.setMessage(code.getDescription()+"["+message+"]");
        } else {
            responseEntity.setMessage(code.getDescription());
        }
        responseEntity.setData(data);
        return responseEntity;
    }
}
