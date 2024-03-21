package com.edi.sdk.basketball.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**

 * 2023年5月31日 下午1:58:24
 */
@Data
public class UrlsFree {
	/**
	 * 比赛id
	 */
	@JsonProperty(value = "match_id")
	@Schema(description = "比赛id")
	private Integer matchId;
	/**
	 * 比赛时间
	 */
	@JsonProperty(value = "match_time")
	@Schema(description = "比赛时间")
	private Integer matchTime;
	/**
	 * 赛事
	 */
	private String comp;
	/**
	 * 主队
	 */
	private String home;
	/**
	 * 客队
	 */
	private String away;
	/**
	 * wap直播地址，没有为空
	 */
	@JsonProperty(value = "mobile_link")
	@Schema(description = "wap直播地址，没有为空")
	private String mobileLink;
	/**
	 * web直播地址，没有为空
	 */
	@JsonProperty(value = "pc_link")
	@Schema(description = "web直播地址，没有为空")
	private String pcLink;
}
