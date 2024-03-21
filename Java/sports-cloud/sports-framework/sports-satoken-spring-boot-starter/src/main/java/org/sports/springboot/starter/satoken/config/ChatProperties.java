package org.sports.springboot.starter.satoken.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * google支付相关配置
 */
@Data
@Component
public class ChatProperties {

    /** 渠道 */
    @Value("${sotoken.channel:}")
    private String channel;

    /** 白名单 */
    @Value("${sotoken.white:}")
    private String whiteUrls;

    /** admin超管角色 */
    @Value("${sotoken.adminSuper:}")
    private String adminSuperUrls;

    /** admin通用角色 */
    @Value("${sotoken.adminCommon:}")
    private String adminCommonUrls;

    /** live直播角色 */
    @Value("${sotoken.liveAnchor:}")
    private String liveAnchorUrls;

    /** live助手角色 */
    @Value("${sotoken.liveHelper:}")
    private String liveHelperUrls;

    /** live运营角色 */
    @Value("${sotoken.liveOperate:}")
    private String liveOperateUrls;

}
