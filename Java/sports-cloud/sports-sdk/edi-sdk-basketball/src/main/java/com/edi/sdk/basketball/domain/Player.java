package com.edi.sdk.basketball.domain;


import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 球员
 * 
 2023年5月22日 下午5:06:31
 */
@Data
public class Player {
	/**
	 * 球员id
	 */
	private Integer id;
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
	 * 球员logo
	 */
	private String logo;
	/**
	 * 球员logo(国家队)
	 */
	@JsonProperty(value = "national_logo")
	@Schema(description = "球员logo(国家队)")
	private String nationalLogo;
	/**
	 * 出生日期（0-未知）
	 */
	private Integer birthday;
	/**
	 * 年龄
	 */
	private Integer age;
	/**
	 * 身高
	 */
	private Integer height;
	/**
	 * 体重
	 */
	private Integer weight;
	/**
	 * 选秀顺位
	 */
	private String drafted;
	/**
	 * 联盟球龄
	 */
	@JsonProperty(value = "league_career_age")
	@Schema(description = "联盟球龄")
	private Integer leagueCareerAge;
	/**
	 * 毕业学校
	 */
	private String school;
	/**
	 * 城市
	 */
	private String city;
	/**
	 * 年薪($)
	 */
	private Integer salary;
	/**
	 * 球衣号
	 */
	@JsonProperty(value = "shirt_number")
	@Schema(description = "球衣号")
	private Integer shirtNumber;
	/**
	 * 位置，C-中锋、SF-小前锋、PF-大前锋、SG-得分后卫、PG-组织后卫、F-前锋、G-后卫，其它都为未知
	 */
	private String position;
	/**
	 * 合同到期
	 */
	@JsonProperty(value = "contract_until")
	@Schema(description = "合同到期")
	private String contract_until;
	/**
	 * 更新时间
	 */
	@JsonProperty(value = "updated_at")
	@Schema(description = "更新时间")
	private Integer updatedAt;
}
