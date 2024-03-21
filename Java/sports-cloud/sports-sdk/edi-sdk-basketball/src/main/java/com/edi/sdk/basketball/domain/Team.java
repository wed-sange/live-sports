package com.edi.sdk.basketball.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 球队

 * 2023年5月22日 下午5:06:10
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
	 * 赛区id，1-大西洋赛区、2-中部赛区、3-东南赛区、4-太平洋赛区、5-西北赛区、6-西南赛区、7-A组赛区、8-B组赛区、9-C组赛区、10-D组赛区（1~6:NBA 7~10:CBA）、0-无
	 */
	@JsonProperty(value = "conference_id")
	@Schema(description = "赛区id，1-大西洋赛区、2-中部赛区、3-东南赛区、4-太平洋赛区、5-西北赛区、6-西南赛区、7-A组赛区、8-B组赛区、9-C组赛区、10-D组赛区（1~6:NBA 7~10:CBA）、0-无")
	private Integer conferenceId;
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
	 * 是否国家队，1-是、0-否
	 */
	private Integer national;
	/**
	 * 场馆id
	 */
	@JsonProperty(value = "venue_id")
	@Schema(description = "场馆id")
	private Integer venueId;
	/**
	 * 更新时间
	 */
	@JsonProperty(value = "updated_at")
	@Schema(description = "更新时间")
	private Integer updatedAt;
}
