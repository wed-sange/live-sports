package org.sports.admin.manage.dao.req;

import lombok.Data;

import java.io.Serializable;

/**
 * 需要推送给全部用户的消息内容
 */
@Data
public class PushToAllWsDto implements Serializable {
    // cmd码，固定为24
    private Integer command;
    // code码，根据消息类型设置值，具体值设置参考与前端约定的文档
    private Integer code = Integer.valueOf(10000);
    // 消息内容
    private Object data;
}
