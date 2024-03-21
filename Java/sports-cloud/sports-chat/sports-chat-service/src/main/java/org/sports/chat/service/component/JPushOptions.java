package org.sports.chat.service.component;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @描述: JPushOptions
 * @版权: Copyright (c) 2023


 * @版本: 1.0
 * @创建日期: 2023/12/7
 * @创建时间: 16:00
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JPushOptions implements Serializable {

    /**
     * 离线消息保留时长(秒)
     */
    @JSONField(name = "time_to_live")
    private int timeToLive;

    /**
     * APNs 是否生产环境
     */
    private boolean apns_production;

    /**
     * 离线消息保留时长(秒)
     */
    @JSONField(name = "third_party_channel")
    private ThirdPartyChannel thirdPartyChannel;
}
