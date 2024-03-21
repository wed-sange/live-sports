package com.edi.sdk.football.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 赛事赛制
 * 
 2023年5月22日 下午5:05:49
 */
@Data
public class CompetitionRule {
	/**
	 * 赛制id
	 */
	private Integer id;
	/**
	 * 赛事id
	 */
	@JsonProperty(value = "competition_id")
	@Schema(description = "赛事id")
	private Integer competitionId;
	/**
	 * 赛季id列表
	 */
	@JsonProperty(value = "season_ids")
	@Schema(description = "赛季id列表<br/>example：[8021]", allowableValues = {"赛季id - int"})
	private List<Integer> seasonIds;
	/**
	 * 赛制说明
	 */
	private String text;
	/**
	 * 更新时间
	 */
	@JsonProperty(value = "updated_at")
	@Schema(description = "更新时间")
	private Integer updatedAt;
}
