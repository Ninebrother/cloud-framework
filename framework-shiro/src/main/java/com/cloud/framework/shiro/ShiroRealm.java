package com.cloud.framework.shiro;

import com.cloud.common.entity.LoginUser;
import com.cloud.common.enums.YNEnum;
import com.cloud.framework.shiro.client.UserClient;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

/**
 * @author zhangyl
 * @Date 2019/12/22
 * shiro权限匹配和账号密码匹配
 */
public class ShiroRealm extends AuthorizingRealm {

    public static final Logger logger = LoggerFactory.getLogger(ShiroRealm.class);

    @Autowired
    private UserClient userClient;

    /**
     * 权限信息认证(包括角色以及权限)是用户访问controller的时候才进行验证(redis存储的此处权限信息)
     * 触发检测用户权限时才会调用此方法，例如checkRole,checkPermission
     * 1、subject.hasRole(“admin”) 或 subject.isPermitted(“admin”)：自己去调用这个是否有什么角色或者是否有什么权限的时候；
     * 2、@RequiresRoles("admin") 或@RequiresPermissions("sys:role:add")：在方法上加注解的时候；
     *
     * @param principalCollection 身份信息
     * @return AuthorizationInfo 权限信息
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        logger.info("===============Shiro权限认证开始============ [ roles、permissions]==========");
        //获取用户ID
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        LoginUser loginUserDTO = (LoginUser) principalCollection.getPrimaryPrincipal();
        Long userId = loginUserDTO.getUserId();

        // 设置用户拥有的角色集合，比如“admin”
        Set<String> rolesSet = new HashSet<>(userClient.listRoleByUserId(userId).getData());
        // 设置用户拥有的权限集合，比如“sys:role:add,sys:user:add”
        Set<String> permsSet = new HashSet<>(userClient.listPermsByUserId(userId).getData());
        //将查到的权限和角色分别传入authorizationInfo中
        authorizationInfo.setStringPermissions(permsSet);
        authorizationInfo.setRoles(rolesSet);
        logger.info("===============Shiro权限认证成功==============");
        return authorizationInfo;
    }

    /**
     * 用户信息认证是在用户进行登录的时候进行验证(不存redis)
     * 也就是说验证用户输入的账号和密码是否正确，错误抛出异常
     * SecurityUtils.getSubject().login（）的时候调用；(即:登录验证)
     *
     * @Author Sans
     * @CreateTime 2019/6/12 12:36
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //获取用户的输入的账号.
        String loginName = (String) authenticationToken.getPrincipal();
        //通过username从数据库中查找 User对象，如果找到进行验证
        //实际项目中,这里可以根据实际情况做缓存,如果不做,Shiro自己也是有时间间隔机制,2分钟内不会重复执行该方法
        LoginUser user = userClient.getByUserName(loginName).getData();
        //判断账号是否存在
        if (user == null) {
            throw new AuthenticationException("用户不存在");
        }
        //判断账号是否被冻结
        if (user.getState() == YNEnum.NO.getValue()) {
            throw new LockedAccountException("账号已被锁定,请联系管理员!");
        }
        //进行验证
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                user,                                  //用户名
                user.getPassword(),                    //密码
                getName()
        );
        return authenticationInfo;
    }
}
