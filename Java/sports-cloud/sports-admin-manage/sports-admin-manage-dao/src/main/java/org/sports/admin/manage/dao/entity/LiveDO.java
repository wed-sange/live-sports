package org.sports.admin.manage.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import org.sports.admin.manage.dao.enums.LiveStatus;
import org.sports.admin.manage.dao.enums.MatchType;
import org.sports.springboot.starter.database.mybatisplus.MysqlBaseDO;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_live")
public class LiveDO extends MysqlBaseDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long id;
    /**
     * USER_ID
     */
    private Long userId;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 头像
     */
    private String userLogo;
    /**
     * 封面
     */
    private String titlePage;
    /**
     * 公告
     */
    private String notice;
    /**
     * 首条消息
     */
    private String firstMessage;
    /**
     * 赛事ID
     */
    private Integer competitionId;
    /**
     * 赛事名称
     */
    private String competitionName;
    /**
     * 比赛ID
     */
    private Integer matchId;
    /**
     * 比赛类型
     */
    private MatchType matchType;
    /**
     * 比赛时间
     */
    private Integer matchTime;
    /**
     * 主队
     */
    private String homeTeamName;
    /**
     * 主队LOGO
     */
    private String homeTeamLogo;
    /**
     * 客队
     */
    private String awayTeamName;
    /**
     * 客队LOGO
     */
    private String awayTeamLogo;
    /**
     * 来源地址
     */
    private String sourceUrl;

    /**
     * 播放地址
     */
    private String playUrl;

    /**
     * 开播时间
     */
    private LocalDateTime openTime;
    /**
     * 关播时间
     */
    private LocalDateTime closeTime;
    /**
     * 直播状态
     */
    private LiveStatus liveStatus;

    /**
     * 总热度
     */
    private Long hotValue;

    /**
     * 初始热度
     */
    private Long hotInitValue = 0L;

    /**
     * 间隔30分钟对应次数
     */
    private Integer hotTimeNo = 0;

    /**
     * 间隔30分钟已获取热度值
     */
    private Long hotTimeValue = 0L;
}
