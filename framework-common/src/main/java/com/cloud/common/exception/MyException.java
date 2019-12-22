package com.cloud.common.exception;

import com.cloud.common.enums.StatusEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zhangyl
 * @Date 2019/12/22
 */
@Getter
@Setter
public class MyException extends RuntimeException {
    private static final long serialVersionUID = 1630481950686140764L;
    private String code;
    private String message;

    public MyException() {
    }

    public MyException(String message) {
        this.message = message;
    }

    public MyException(StatusEnum statusEnum) {
        this.code = statusEnum.getCode();
        this.message = statusEnum.getMessage();
    }
}
