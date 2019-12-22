package com.cloud.framework.core.shiro;

/**
 * @author zhangyl
 * @Date 2019/12/22
 */
public class ShiroConstant {
    public static final String SHIRO_CACHE_KEY = "shiro:cache:";
    public static final String SHIRO_SESSION_KEY = "shiro:session:";

    public static final int SHIRO_SESSION_EXPIRE = 1800;


    public static final String REDIS_PREFIX_LOGIN = "login_token_%s";
}
