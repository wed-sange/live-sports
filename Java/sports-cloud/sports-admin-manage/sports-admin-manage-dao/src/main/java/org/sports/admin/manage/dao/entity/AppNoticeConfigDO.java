package org.sports.admin.manage.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * APP用户通知设置实体类

 */
@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_app_notice_cofing")
public class AppNoticeConfigDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 是否开启关注比赛通知 1是 0否
     */
    private Integer ynFollowMatch;
    /**
     * 是否开启主播开播通知 1是 0否
     */
    private Integer ynLiveOpen;
    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

}
