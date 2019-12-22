package com.cloud.common.util;

import java.util.Arrays;
import java.util.List;

/**
 * @author zhangyl
 * @Date 2019/12/22
 */
public class FileUtil {
    /**
     * 支持的文件类型
     */
    public static final List<String> ALLOW_TYPES = Arrays.asList("image/jpg", "image/jpeg", "image/png", "image/gif");

    /**
     * 校验文件类型是否是被允许的
     *
     * @param postfix
     * @return
     */
    public static boolean allowUpload(String postfix) {
        return ALLOW_TYPES.contains(postfix);
    }
}
