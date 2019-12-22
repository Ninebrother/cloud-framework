package com.cloud.common.entity;

import com.cloud.common.enums.StatusEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zhangyl
 * @Date 2019/12/22
 */
@Getter
@Setter
public class ResponseResult<T> {
    private String code;
    private String message;
    private T data;

    public ResponseResult() {

    }

    public ResponseResult message(String message) {
        this.message = message;
        return this;
    }

    public ResponseResult data(T t) {
        this.data = t;
        return this;
    }

    public static <T> ResponseResult<T> success() {
        ResponseResult<T> result = new ResponseResult<>();
        result.setCode(StatusEnum.SUCCESS.getCode());
        result.setMessage(StatusEnum.SUCCESS.getMessage());
        return result;
    }

    public static <T> ResponseResult<T> success(T t) {
        ResponseResult<T> result = new ResponseResult<>();
        result.setCode(StatusEnum.SUCCESS.getCode());
        result.setMessage(StatusEnum.SUCCESS.getMessage());
        result.setData(t);
        return result;
    }

    public static <T> ResponseResult<T> fail() {
        ResponseResult<T> result = new ResponseResult<>();
        result.setCode(StatusEnum.FAILED.getCode());
        result.setMessage(StatusEnum.FAILED.getMessage());
        return result;
    }
}


