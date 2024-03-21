package com.edi.sdk.football.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 球队
 */
@Data
public class Team {
	/**
	 * 球队id
	 */
	private Integer id;
	/**
	 * 赛事id
	 */
	@JsonProperty(value = "competition_id")
	@Schema(description = "赛事id")
	private Integer competitionId;
	/**
	 * 国家id
	 */
	@JsonProperty(value = "country_id")
	@Schema(description = "国家id")
	private Integer countryId;
	/**
	 * 中文名称
	 */
	@JsonProperty(value = "name_zh")
	@Schema(description = "中文名称")
	private String nameZh;
	/**
	 * 粤语名称
	 */
	@JsonProperty(value = "name_zht")
	@Schema(description = "粤语名称")
	private String nameZht;
	/**
	 * 英文名称
	 */
	@JsonProperty(value = "name_en")
	@Schema(description = "英文名称")
	private String nameEn;
	/**
	 * 中文简称
	 */
	@JsonProperty(value = "short_name_zh")
	@Schema(description = "中文简称")
	private String shortNameZh;
	/**
	 * 粤语简称
	 */
	@JsonProperty(value = "short_name_zht")
	@Schema(description = "粤语简称")
	private String shortNameZht;
	/**
	 * 英文简称
	 */
	@JsonProperty(value = "short_name_en")
	@Schema(description = "英文简称")
	private String shortNameEn;
	/**
	 * 球队logo
	 */
	private String logo;
	/**
	 * 国家队logo（为国家队时存在）
	 */
	@JsonProperty(value = "country_logo")
	@Schema(description = "国家队logo（为国家队时存在）")
	private String countryLogo;
	/**
	 * 是否国家队，1-是、0-否
	 */
	private Integer national;
	/**
	 * 成立时间
	 */
	@JsonProperty(value = "foundation_time")
	@Schema(description = "成立时间")
	private Integer foundationTime;
	/**
	 * 球队官网
	 */
	private String website;
	/**
	 * 场馆id
	 */
	@JsonProperty(value = "venue_id")
	@Schema(description = "场馆id")
	private Integer venueId;
	/**
	 * 市值
	 */
	@JsonProperty(value = "market_value")
	@Schema(description = "市值")
	private Integer marketValue;
	/**
	 * 市值单位
	 */
	@JsonProperty(value = "market_value_currency")
	@Schema(description = "市值单位")
	private String marketValueCurrency;
	/**
	 * 总球员数，-1表示没有该字段数据
	 */
	@JsonProperty(value = "total_players")
	@Schema(description = "总球员数，-1表示没有该字段数据")
	private Integer totalPlayers;
	/**
	 * 非本土球员数，-1表示没有该字段数据
	 */
	@JsonProperty(value = "foreign_players")
	@Schema(description = "非本土球员数，-1表示没有该字段数据")
	private Integer foreignPlayers;
	/**
	 * 国家队球员数，-1表示没有该字段数据
	 */
	@JsonProperty(value = "national_players")
	@Schema(description = "国家队球员数，-1表示没有该字段数据")
	private Integer nationalPlayers;
	/**
	 * 更新时间
	 */
	@JsonProperty(value = "updated_at")
	@Schema(description = "更新时间")
	private Integer updatedAt;
}