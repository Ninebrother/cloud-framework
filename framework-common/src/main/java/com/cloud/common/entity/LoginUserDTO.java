package com.cloud.common.entity;

import com.cloud.common.enums.YNEnum;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author zhangyl
 * @Date 2019/12/22
 */
@Getter
@Setter
public class LoginUserDTO implements Serializable {

    private static final long serialVersionUID = -6379483900498522294L;
    private Long userId;

    /**
     * 登录账号
     */
    private String userName;

    /**
     * 密码
     */
    private String password;

    /**
     * 状态(0-删除、禁用,1-正常) {@link YNEnum}
     */
    private Integer state;
}
