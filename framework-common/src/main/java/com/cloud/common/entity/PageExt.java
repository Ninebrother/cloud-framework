package com.cloud.common.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author zhangyl
 * @Date 2019/12/22
 */
@Getter
@Setter
public class PageExt implements Serializable {
    private static final long serialVersionUID = 2573128842797352560L;
    /**
     *第几页
     */
    private Integer pageNum;
    /**
     *每页记录数
     */
    private Integer pageSize;
}
