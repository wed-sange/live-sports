package com.edi.sdk.football.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MatchTimeCountList {
	/**
	 * 时间（天）
	 */
	@JsonProperty("time")
	private String _id;
	/**
	 * 天比赛数量
	 */
	private Integer num;
}
