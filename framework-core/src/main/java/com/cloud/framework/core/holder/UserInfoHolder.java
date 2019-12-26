package com.cloud.framework.core.holder;

import com.cloud.common.entity.GlobalContants;
import com.cloud.common.entity.LoginUser;
import com.cloud.common.enums.StatusEnum;
import com.cloud.common.exception.MyException;
import com.cloud.framework.core.redis.IRedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zhangyl
 * @Date 2019/12/25
 * 相当登陆用户信息
 */
public class UserInfoHolder {

    public static final Logger logger = LoggerFactory.getLogger(UserInfoHolder.class);

    /**
     * 获取相当登陆用户信息
     *
     * @return
     */
    public static LoginUser get() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        String token = request.getHeader(GlobalContants.X_ACCESS_TOKEN);
        if (token == null) {
            throw new MyException(StatusEnum.UN_LOGIN);
        }
        IRedisService redisService = SpringContextHolder.getBean(IRedisService.class);
        LoginUser loginUser = redisService.get(token, LoginUser.class);
        if (loginUser == null) {
            logger.error(StatusEnum.UN_LOGIN.getMessage());
            throw new MyException(StatusEnum.UN_LOGIN);
        }
        return loginUser;
    }
}
