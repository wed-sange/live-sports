/**
 *
 */
package org.jim.core.packets;

import cn.hutool.core.bean.BeanUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 版本: [1.0]
 * 功能说明:
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User extends Message implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 用户id;
     */
    private String userId;
    /**
     * user nick
     */
    private String nick;
    /**
     * 用户头像
     */
    private String avatar;
    /**
     * 等级名称
     */
    private String lvName;

    /**
     * 等级
     */
    private Integer lvNum;

    /**
     * 用户身份：0-普通用户   1-主播   2-主播助理 3：运营
     */
    private Integer identity;

    /**
     * 在线状态(online、offline)
     */
    private String status = UserStatusType.OFFLINE.getStatus();
    /**
     * 个性签名;
     */
    private String sign;
    /**
     * 登录终端(APP端:1, LIVE端:2);
     */
    private String loginType;
    /**
     * 用户所属终端;(ws、tcp、http、android、ios等)
     */
    private String terminal;
    /**
     * 好友列表;
     */
    private List<Group> friends;
    /**
     * 群组列表;
     */
    private List<Group> groups;

    /**
     * 是否禁言
     */
    private boolean mute;


    @Override
    public User clone() {
        User cloneUser = new User();
        BeanUtil.copyProperties(this, cloneUser, "friends", "groups");
        return cloneUser;
    }

}
