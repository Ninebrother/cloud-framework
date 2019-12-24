package com.cloud.common.enums;

import lombok.Getter;

/**
 * @author zhangyl
 * @Date 2019/12/22
 */
@Getter
public enum StatusEnum {
    SUCCESS("200", "SUCCESS"),

    FAILED("500", "FAILED"),

    UN_AUTH("403", "没有权限，请联系管理员授权"),
    UN_LOGIN("404", "没有登录，请登录"),;

    private String code;
    private String message;


    StatusEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
