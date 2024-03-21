package org.sports.admin.manage.dao.enums;

import lombok.Getter;

/**
 * 聊天信息类型 1:公聊、2私聊
 */
@Getter
public enum ChatMsgTypeEnum {

    PUBLIC(1),PRIVATE(2);

    private Integer value;

    ChatMsgTypeEnum(Integer value){
        this.value = value;
    }

}
