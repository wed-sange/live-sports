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
public class BasketballTeamDataStatsVO implements Serializable {

    @Schema(description = "命中次数")
    private Integer hit;
    @Schema(description = "投篮次数")
    private Integer shot;
    @Schema(description = "二分球投篮命中次数")
    private Integer hit2;
    @Schema(description = "二分投篮次数")
    private Integer shot2;
    @Schema(description = "三分球投篮命中次数")
    private Integer hit3;
    @Schema(description = "三分投篮次数")
    private Integer shot3;

    @Schema(description = "罚球命中次数")
    private Integer penaltyHit;
    @Schema(description = "罚球投篮次数")
    private Integer penalty;

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

    @Schema(description = "得分")
    private Integer score;

    public BasketballTeamDataStatsVO(List<String> list) {
        String[] hitAndShotArray = list.get(1).split("-");
        this.hit = Integer.parseInt(hitAndShotArray[0]);
        this.shot = Integer.parseInt(hitAndShotArray[1]);
        String[] hit3AndShot3Array = list.get(2).split("-");
        this.hit3 = Integer.parseInt(hit3AndShot3Array[0]);
        this.shot3 = Integer.parseInt(hit3AndShot3Array[1]);
        this.hit2 = this.hit - this.hit3;
        this.shot2 = this.shot - this.shot3;
        String[] penaltyArray = list.get(3).split("-");
        this.penaltyHit = Integer.parseInt(penaltyArray[0]);
        this.penalty = Integer.parseInt(penaltyArray[1]);
        this.offensiveRebound = Integer.parseInt(list.get(4));
        this.defensiveRebound = Integer.parseInt(list.get(5));
        this.rebound = Integer.parseInt(list.get(6));
        this.assists = Integer.parseInt(list.get(7));
        this.steals = Integer.parseInt(list.get(8));
        this.blocks = Integer.parseInt(list.get(9));
        this.mistakes = Integer.parseInt(list.get(10));
        this.fouls = Integer.parseInt(list.get(11));
        this.score = Integer.parseInt(list.get(13));
    }
}