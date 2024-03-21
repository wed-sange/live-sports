package org.sports.admin.manage.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import org.sports.admin.manage.dao.enums.MatchType;
import org.sports.springboot.starter.database.mybatisplus.MysqlBaseDO;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_admin_hot_match")
public class HotMatchDO extends MysqlBaseDO {
    /**
     * ID
     */
    private Long id;

    /**
     * 赛事ID
     */
    private Integer competitionId;

    /**
     * 名称
     */
    private String competitionName;

    /**
     * 全称
     */
    private String fullCompetitionName;

    /**
     * 类型 BASKETBALL FOOTBALL
     */
    private MatchType matchType;
}
