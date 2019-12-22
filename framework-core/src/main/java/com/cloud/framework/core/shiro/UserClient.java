package com.cloud.framework.core.shiro;

import com.cloud.common.entity.LoginUserDTO;
import com.cloud.common.entity.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author zhangyl
 * @Date 2019/12/22
 */
@Component
public class UserClient {

    @Value("user.url")
    private String userUrl;

    public static final String LIST_ROLE = "listRoleByUserId";

    public static final String LIST_PERMS = "listPermsByUSerId";

    public static final String GET_USER = "getOneByLoginName";
    @Autowired
    private RestTemplate restTemplate;

    /**
     * 获取用户角色
     *
     * @param userId
     * @return
     */
    public Set<String> listRoleByUSerId(Long userId) {
        String url = userUrl + LIST_ROLE;
        ResponseResult<List<String>> responseResult = restTemplate.getForObject(url, ResponseResult.class, userId);
        return new HashSet<>(responseResult.getData());
    }

    /**
     * 获取用户权限
     *
     * @param userId
     * @return
     */
    public Set<String> listPermsByUSerId(Long userId) {
        String url = userUrl + LIST_PERMS;
        ResponseResult<List<String>> responseResult = restTemplate.getForObject(url, ResponseResult.class, userId);
        return new HashSet<>(responseResult.getData());
    }

    /**
     * 根据登录名查询用户信息
     *
     * @param LoginName
     * @return
     */
    public LoginUserDTO getOneByLoginName(String LoginName) {
        String url = userUrl + GET_USER;
        ResponseResult<LoginUserDTO> responseResult = restTemplate.getForObject(url, ResponseResult.class, LoginName);
        return responseResult.getData();
    }
}
