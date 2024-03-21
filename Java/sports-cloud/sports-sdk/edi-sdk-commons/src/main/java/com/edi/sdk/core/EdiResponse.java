package com.edi.sdk.core;

import lombok.Data;

/**
 *
 */
@Data
public class EdiResponse<T> {
    /**
     * 响应码
     */
    private Integer code;
    /**
     * 错误消息
     */
    private String err;
    /**
     * 响应消息
     */
    private String msg;
    /**
     * 查询情况
     */
    private EdiResponseQuery query;
    /**
     * 响应结果
     */
    private T results;

    /**
     * 体彩响应结果
     */
    private T data;
}
