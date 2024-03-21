package org.sports.chat.service.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
/**
 * 校验是否登录的返回实体
 */
public class CheckLoginDto {

    private String userId;

    /**
     * 登录端口，APP端：app  LIVE端：live
     */
    private String loginType;

    private Boolean pass;
}
