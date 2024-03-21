package org.sports.admin.manage.dao.entity;

import lombok.Data;

/**
 * 需要推送给全部用户的消息内容
 */
@Data
public class PushToAllWsDto {
    // cmd码，固定为24
    private Integer cmd = 24;
    // code码，根据消息类型设置值，具体值设置参考与前端约定的文档 //可用枚举ImStatus
    private Integer code;
    // 消息体
    private String data;
}
