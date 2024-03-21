package com.edi.sdk.basketball.domain;


import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 数据更新
 * 
 2023年5月22日 下午5:08:59
 */
@Data
public class DataUpdate  {
	/**
	 * 比赛id（集锦录像 字段存在）
	 */
	@JsonProperty(value = "match_id")
	@Schema(description = "比赛id（集锦录像 字段存在）")
	private Integer matchId;
	/**
	 * 赛季id（对阵图、积分榜、赛季统计 字段存在）
	 */
	@JsonProperty(value = "season_id")
	@Schema(description = "赛季id（对阵图、积分榜、赛季统计 字段存在）")
	private Integer seasonId;
	/**
	 * 赛事id（积分榜、赛季统计 字段存在）
	 */
	@JsonProperty(value = "competition_id")
	@Schema(description = "赛事id（积分榜、赛季统计 字段存在）")
	private Integer competitionId;	
	/**
	 * 排名发布时间（fiba men、fiba women 字段存在）
	 */
	@JsonProperty(value = "pub_time")
	@Schema(description = "排名发布时间（fiba men、fiba women 字段存在）")
	private Integer pubTime;		
	/**
	 * 更新时间
	 */
	@JsonProperty(value = "update_time")
	@Schema(description = "更新时间")
	private Integer update_time;
}
