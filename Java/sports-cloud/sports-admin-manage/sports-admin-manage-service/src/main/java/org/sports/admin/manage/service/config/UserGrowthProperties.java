package org.sports.admin.manage.service.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 用户成长值配置
 */
@Data
@Component
public class UserGrowthProperties {

    /** 直播公聊每次互动获得积分 */
    @Value("${user.growth.liveValue:}")
    private Integer liveValue;

    /** 观看直播每10分钟获得积分 */
    @Value("${user.growth.watchValue:}")
    private Integer watchValue;

}
