package org.sports.admin.manage.service.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "sports.message")
public class SportsMessageProperties {
    /**
     * 消息发送队列
     */
    private String queue = "sports:message:queue";
}
