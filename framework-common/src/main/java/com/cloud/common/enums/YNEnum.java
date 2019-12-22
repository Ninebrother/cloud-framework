package com.cloud.common.enums;

import lombok.Getter;

/**
 * @author zhangyl
 * @Date 2019/12/22
 */

@Getter
public enum YNEnum {
    YES(1, "是"),
    NO(0, "否"),;
    private int value;
    private String desc;

    YNEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
