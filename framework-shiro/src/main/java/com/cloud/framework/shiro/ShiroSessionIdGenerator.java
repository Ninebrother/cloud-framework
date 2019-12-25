package com.cloud.framework.shiro;

import com.cloud.framework.shiro.constant.ShiroConstant;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;

import java.io.Serializable;

/**
 * @author zhangyl
 * @Date 2019/12/22
 */
public class ShiroSessionIdGenerator implements SessionIdGenerator {
    /**
     * 实现SessionId生成
     *
     * @Author Sans
     * @CreateTime 2019/6/11 11:54
     */
    @Override
    public Serializable generateId(Session session) {
        Serializable sessionId = new JavaUuidSessionIdGenerator().generateId(session);
        return String.format(ShiroConstant.REDIS_PREFIX_LOGIN, sessionId).replace("-","");
    }
}
