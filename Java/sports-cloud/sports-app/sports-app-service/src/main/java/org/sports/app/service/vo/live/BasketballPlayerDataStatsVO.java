package org.sports.app.service.vo.live;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 篮球球员数据统计数据VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasketballPlayerDataStatsVO implements Serializable {

    @Schema(description = "在场持续时间")
    private Integer time;

    @Schema(description = "命中次数 - 投篮次数")
    private String hitAndShot;

    @Schema(description = "三分球投篮命中次数 - 三分投篮次数")
    private String hit3AndShot3;

    @Schema(description = "罚球命中次数 - 罚球投篮次数")
    private String hitAndPenalty;

    @Schema(description = "进攻篮板")
    private Integer offensiveRebound;

    @Schema(description = "防守篮板")
    private Integer defensiveRebound;

    @Schema(description = "总的篮板")
    private Integer rebound;

    @Schema(description = "助攻数")
    private Integer assists;

    @Schema(description = "抢断数")
    private Integer steals;

    @Schema(description = "盖帽数")
    private Integer blocks;

    @Schema(description = "失误次数")
    private Integer mistakes;

    @Schema(description = "个人犯规次数")
    private Integer fouls;

    @Schema(description = "+/-值")
    private Integer value;

    @Schema(description = "得分")
    private Integer score;

    @Schema(description = "是否出场(1-出场，0-没出场)")
    private Integer toPlay;

    @Schema(description = "是否在场上（0 - 在场上，1 - 没在场上）（用于赛中）")
    private Integer onField;

    @Schema(description = "是否是替补（1-替补，0-首发）")
    private Integer first;

    public BasketballPlayerDataStatsVO(List<String> list) {
        this.time = Integer.parseInt(list.get(0));
        this.hitAndShot = list.get(1);
        this.hit3AndShot3 = list.get(2);
        this.hitAndPenalty = list.get(3);
        this.offensiveRebound = Integer.parseInt(list.get(4));
        this.defensiveRebound = Integer.parseInt(list.get(5));
        this.rebound = Integer.parseInt(list.get(6));
        this.assists = Integer.parseInt(list.get(7));
        this.steals = Integer.parseInt(list.get(8));
        this.blocks = Integer.parseInt(list.get(9));
        this.mistakes = Integer.parseInt(list.get(10));
        this.fouls = Integer.parseInt(list.get(11));
        this.value = Integer.parseInt(list.get(12));
        this.score = Integer.parseInt(list.get(13));
        this.toPlay = Integer.parseInt(list.get(14));
        this.onField = Integer.parseInt(list.get(15));
        this.first = Integer.parseInt(list.get(16));
    }
}