package org.sports.springboot.starter.edisdk.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "edi.basketball")
public class EdiBasketballProperties {
    /**
     * 用户名
     */
    private String user = "admin";
    /**
     * 密钥
     */
    private String secret = "d405dc6c8a2c8b511016cc5c1743eb33";
    /**
     * 接口地址
     */
    private String baseUrl = "http://192.168.101.14:8081";
}
