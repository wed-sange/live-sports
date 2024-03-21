package com.edi.sdk.basketball.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * 变动比赛
 * 
 2023年5月30日 下午2:35:40
 */
@Data
public class Match {
	/**
	 * 比赛id
	 */
	private Integer id;
	/**
	 * 赛事id
	 */
	@JsonProperty(value = "competition_id")
	private Integer competitionId;
	/**
	 * 主队id
	 */
	@JsonProperty(value = "home_team_id")
	private Integer homeTeamId;
	/**
	 * 客队id
	 */
	@JsonProperty(value = "away_team_id")
	private Integer awayTeamId;
	/**
	 * 类型id，1-常规赛、2-季后赛、3-季前赛、4-全明星、5-杯赛、6-附加赛、0-无
	 */
	private Integer kind;
	/**
	 * 比赛总节数(不包含加时)
	 */
	@JsonProperty(value = "period_count")
	private Integer periodCount;
	/**
	 * 比赛状态，详见状态码->比赛状态
	 */
	@JsonProperty(value = "status_id")
	private Integer statusId;
	/**
	 * 比赛时间
	 */
	@JsonProperty(value = "match_time")
	private Integer matchTime;
	/**
	 * 是否中立场，1-是、0-否
	 */
	private Integer neutral;
	/**
	 * 主队得分列表 <br />
	 * 比分字段说明 <br />
	 * 
	 * example：[0, 0, 0, 0, 0] <br />
	 * Enum: <br />
	 * Array[5] <br />
	 * 0:"第1节分数 - int" <br />
	 * 1:"第2节分数 - int" <br />
	 * 2:"第3节分数 - int" <br />
	 * 3:"第4节分数 - int" <br />
	 * 4:"加时分数 - int" <br />
	 * 
	 */
	@JsonProperty(value = "home_scores")
	private List<Object> homeScores;
	/**
	 * 客队得分列表 <br />
	 * 比分字段说明 <br />
	 * 
	 * example：[0, 0, 0, 0, 0] <br />
	 * Enum: <br />
	 * Array[5] <br />
	 * 0:"第1节分数 - int" <br />
	 * 1:"第2节分数 - int" <br />
	 * 2:"第3节分数 - int" <br />
	 * 3:"第4节分数 - int" <br />
	 * 4:"加时分数 - int" <br />
	 * 
	 */
	@JsonProperty(value = "away_scores")
	private List<Object> awayScores;
	/**
	 * 加时赛比分字段说明（大于1个加时才有该字段，每一位为1节加时比分）<br />
	 * example：[[8, 9, 6], [8, 9, 8]] <br />
	 * Enum: <br />
	 * Array[2] <br />
	 * 0:Array[3] <br />
	 * 0:"主队第1节加时分数 - int" <br />
	 * 1:"主队第2节加时分数 - int" <br />
	 * 2:"主队第n节加时分数 - int" <br />
	 * 1:Array[3] <br />
	 * 0:"客队第1节加时分数 - int" <br />
	 * 1:"客队第2节加时分数 - int" <br />
	 * 2:"客队第n节加时分数 - int" <br />
	 */
	@JsonProperty(value = "over_time_scores")
	private List<List<Integer>> overTimeScores;
	/**
	 * 主队排名
	 */
	@JsonProperty(value = "home_position")
	private String homePosition;
	/**
	 * 客队排名
	 */
	@JsonProperty(value = "away_position")
	private String awayPosition;
	/**
	 * 动画、情报 字段
	 */
	private Coverage coverage;
	/**
	 * 场馆id
	 */
	@JsonProperty(value = "venue_id")
	private Integer venueId;
	/**
	 * 赛季id
	 */
	@JsonProperty(value = "season_id")
	private Integer seasonId;
	/**
	 * 回合
	 */
	private Round round;
	/**
	 * 彩票期号、序号（lottery 体彩查询存在）
	 */
	private Lottery lottery;
	/**
	 * 更新时间
	 */
	@JsonProperty(value = "updated_at")
	private Integer updatedAt;

	@Data
	public static class Coverage {
		/**
		 * 是否有动画，1-是、0-否
		 */
		private Integer mlive;
		/**
		 * 是否有情报，1-是、0-否
		 */
		private Integer intelligence;
	}

	@Data
	public static class Round {
		/**
		 * 阶段id
		 */
		@JsonProperty(value = "stage_id")
		private Integer stageId;
		/**
		 * 第几组，1-A、2-B以此类推
		 */
		@JsonProperty(value = "group_num")
		private Integer groupNum;
		/**
		 * 第几轮
		 */
		@JsonProperty(value = "round_num")
		private Integer roundNum;
	}
	
	@Data
	public static class Lottery {
		/**
		 * 彩票期号（竞彩无期号，为空）
		 */
		private String issue;
		/**
		 * 序号
		 */
		@JsonProperty(value = "issue_num")
		private String issueNum;
	}
}
