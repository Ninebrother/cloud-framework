package com.cloud.framework.shiro.client;

import com.cloud.common.entity.LoginUser;
import com.cloud.common.entity.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @author zhangyl
 * @Date 2019/12/24
 */
@FeignClient(value = "user-service")
public interface UserClient {
    /**
     * 获取用户角色
     *
     * @param userId
     * @return
     */
    @GetMapping("role/listRoleByUserId/{userId}")
    ResponseResult<List<String>> listRoleByUserId(@PathVariable("userId") Long userId);
    /**
     *  获取用户权限
     *
     * @param userId
     * @return
     */
    @GetMapping("menu/listPermsByUserId/{userId}")
    ResponseResult<List<String>> listPermsByUserId(@PathVariable("userId") Long userId);

    /**
     * 获取用户角色信息
     *
     * @param userName
     * @return
     */
    @GetMapping("user/getByUserName/{userName}")
    ResponseResult<LoginUser> getByUserName(@PathVariable("userName") String userName);
}
