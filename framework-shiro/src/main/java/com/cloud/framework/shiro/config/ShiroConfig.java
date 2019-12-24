package com.cloud.framework.shiro.config;

import com.cloud.framework.shiro.ShiroRealm;
import com.cloud.framework.shiro.ShiroSessionIdGenerator;
import com.cloud.framework.shiro.ShiroSessionManager;
import com.cloud.framework.shiro.client.UserClient;
import com.cloud.framework.shiro.constant.ShiroConstant;
import com.cloud.framework.shiro.filter.AjaxPermissionsAuthorizationFilter;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.mgt.SessionsSecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCrypt;

import javax.servlet.Filter;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhangyl
 * @Date 2019/12/22
 */
@Configuration
@EnableFeignClients(basePackageClasses = {UserClient.class})
@ComponentScan("com.cloud.framework.shiro")
public class ShiroConfig {
    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private int port;
    @Value("${spring.redis.password}")
    private String password;
    @Value("${shiro.excludes}")
    private String shiroExcludes;

    /**
     * 开启Shiro-aop注解支持
     *
     * @Attention 使用代理方式所以需要开启代码支持
     * @Author Sans
     * @CreateTime 2019/6/12 8:38
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    /**
     * Shiro基础配置
     *
     * @Author Sans
     * @CreateTime 2019/6/12 8:42
     */
    @Bean
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();

        // 注意过滤器配置顺序不能颠倒
        // 配置过滤:不会被拦截的链接
        filterChainDefinitionMap.put("/swagger-ui.html", "anon");
        filterChainDefinitionMap.put("/webjars/**", "anon");
        filterChainDefinitionMap.put("/v2/**", "anon");
        filterChainDefinitionMap.put("/swagger-resources/**", "anon");
        if (StringUtils.isNotEmpty(shiroExcludes)) {
            List<String> excludes = Arrays.asList(shiroExcludes.split(","));
            for (String exclude : excludes) {
                filterChainDefinitionMap.put(exclude, "anon");
            }
        }
        Map<String, Filter> filterMap = new LinkedHashMap<>();
        filterMap.put("authc", new AjaxPermissionsAuthorizationFilter());
        shiroFilterFactoryBean.setFilters(filterMap);
        filterChainDefinitionMap.put("/**", "authc");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    /**
     * 安全管理器
     *
     * @Author Sans
     * @CreateTime 2019/6/12 10:34
     */
    @Bean
    public SessionsSecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 自定义Ssession管理
        securityManager.setSessionManager(sessionManager());
        // 自定义Cache实现
        securityManager.setCacheManager(cacheManager());
        // 自定义Realm验证
        securityManager.setRealm(shiroRealm());
        return securityManager;
    }

    /**
     * 身份验证器
     *
     * @Author Sans
     * @CreateTime 2019/6/12 10:37
     */
    @Bean
    public ShiroRealm shiroRealm() {
        ShiroRealm shiroRealm = new ShiroRealm();
        //配置BCrypt
        shiroRealm.setCredentialsMatcher((token, info) -> {
            UsernamePasswordToken userToken = (UsernamePasswordToken) token;
            //要验证的明文密码
            String plaintext = new String(userToken.getPassword());
            //数据库中的加密后的密文
            String hashed = info.getCredentials().toString();
            return BCrypt.checkpw(plaintext, hashed);
        });
        return shiroRealm;
    }

    /**
     * 配置Redis管理器
     *
     * @Attention 使用的是shiro-redis开源插件
     * @Author Sans
     * @CreateTime 2019/6/12 11:06
     */
    @Bean
    public RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(host);
        redisManager.setPort(port);
        if (StringUtils.isNotEmpty(password)) {
            redisManager.setPassword(password);
        }
        return redisManager;
    }

    /**
     * 配置Cache管理器
     * 用于往Redis存储权限和角色标识
     *
     * @Attention 使用的是shiro-redis开源插件
     * @Author Sans
     * @CreateTime 2019/6/12 12:37
     */
    @Bean
    public RedisCacheManager cacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        redisCacheManager.setKeyPrefix(ShiroConstant.SHIRO_CACHE_KEY);
        //redis中针对不同用户缓存(此处的id需要对应user实体中的id字段,用于唯一标识)
        redisCacheManager.setPrincipalIdFieldName("id");
        return redisCacheManager;
    }

    /**
     * SessionID生成器
     *
     * @return
     */
    @Bean
    public ShiroSessionIdGenerator sessionIdGenerator() {
        return new ShiroSessionIdGenerator();
    }

    /**
     * 配置RedisSessionDAO
     *
     * @Attention 使用的是shiro-redis开源插件
     * @Author Sans
     * @CreateTime 2019/6/12 13:44
     */
    @Bean
    public RedisSessionDAO redisSessionDAO() {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager());
        redisSessionDAO.setSessionIdGenerator(sessionIdGenerator());
        redisSessionDAO.setKeyPrefix(ShiroConstant.SHIRO_SESSION_KEY);
        redisSessionDAO.setExpire(ShiroConstant.SHIRO_SESSION_EXPIRE);
        return redisSessionDAO;
    }

    /**
     * 配置Session管理器
     *
     * @Author Sans
     * @CreateTime 2019/6/12 14:25
     */
    @Bean
    public SessionManager sessionManager() {
        ShiroSessionManager shiroSessionManager = new ShiroSessionManager();
        shiroSessionManager.setSessionDAO(redisSessionDAO());
        return shiroSessionManager;
    }

}
