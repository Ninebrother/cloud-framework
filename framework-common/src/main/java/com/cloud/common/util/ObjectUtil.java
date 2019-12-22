package com.cloud.common.util;


import org.springframework.beans.BeanUtils;

/**
 * @author zhangyl
 * @Date 2019/12/22
 */
public class ObjectUtil {
    public static <T> T copy(Object source, Class<T> target) {
        try {
            T t = target.newInstance();
            BeanUtils.copyProperties(source, t);
            return t;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
