package org.sports.chat.service.component;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @描述: 极光推送-通知消息体封装
 * @版权: Copyright (c) 2023


 * @版本: 1.0
 * @创建日期: 2023/12/07
 * @创建时间: 17:37
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JPushRequest implements Serializable {

    /**
     * 发送业务方
     */
    private String from;

    /**
     * 发送目标
     */
    private Map<String, List<String>> to;

    /**
     * 请求ID
     */
    @JSONField(name = "request_id")
    private String requestId;

    /**
     * 请求ID
     */
    @JSONField(name = "custom_args")
    private String customArgs;

    /**
     * 请求体
     */
    private JPushBody body;



}
