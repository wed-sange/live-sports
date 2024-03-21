package org.sports.admin.manage.service.enums;

import lombok.Getter;

/**
 * 用户成长值类型(1直播聊天互动 2观看直播)
 */
@Getter
public enum UserGrowthTypeEnum {
    LIVE_CHAT(1),WATCH_LIVE(2);

    private Integer type;

    UserGrowthTypeEnum(Integer type){
        this.type = type;
    }
}
