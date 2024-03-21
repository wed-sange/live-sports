package com.edi.sdk.football.domain;

import java.util.List;

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
	 * 国家id
	 */
	@JsonProperty(value = "country_id")
	@Schema(description = "国家id")
	private Integer countryId;

	/**
	 * 国籍
	 */
	private String nationality;

	/**
	 * 球员logo(国家队，可判断球队是国家队时使用)
	 */
	@JsonProperty(value = "national_logo")
	@Schema(description = "球员logo(国家队，可判断球队是国家队时使用)")
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
	 * 合同截止时间
	 */
	@JsonProperty(value = "contract_until")
	@Schema(description = "合同截止时间")
	private Integer contractUntil;

	/**
	 * 惯用脚，0-未知、1-左脚、2-右脚、3-左右脚
	 */
	@JsonProperty(value = "preferred_foot")
	@Schema(description = "惯用脚，0-未知、1-左脚、2-右脚、3-左右脚")
	private Integer preferredFoot;

	/**
	 * 球员u系列
	 */
	private String suffix;

	/**
	 * 能力评分（没有字段不存在）
	 */
	@Schema(description = "能力评分（没有字段不存在）", allowableValues = { "类型 - int", "评分，满分100 - int", "平均分，满分100 - int" })
	private List<List<Integer>> ability;

	/**
	 * 技术特点字段说明（没有字段不存在）
	 */
	@Schema(description = "技术特点字段说明（没有字段不存在）：<br/>1-卸球<br/>2-罚点球<br/>3-任意球<br/>4-远射<br/>5-临门一脚<br/>6-传球<br/>7-组织进攻<br/>8-带球<br/>9-断球<br/>10-铲球<br/>11-稳定性<br/>12-过人<br/>13-长传<br/>14-控球<br/>15-空中对抗<br/>16-地面对抗<br/>17-失误倾向<br/>18-纪律性<br/>19-扑点球<br/>20-反应<br/>21-弃门参与进攻<br/>22-高球拦截<br/>23-处理球<br/>24-远距离射门<br/>25-站位<br/>26-高位紧逼<br/>27-远射扑救<br/>28-传中<br/>29-越位意识<br/>30-近射扑救<br/>31-专注度<br/>32-防守参与度<br/>33-关键传球<br/>34-头球<br/>35-定位球<br/>36-直传球<br/>37-反击<br/>38-一脚出球<br/>39-起高球<br/>40-造犯规<br/>41-内切<br/>42-拳击球<br/>43-解围<br/><br/>example：[[[11, 1]], [[7, 1]]]", allowableValues = {
			"$$[$$[\"优点：类型，没有为空 - int\",\"排名（世界范围，0-未知） - int\"]$$]$$",
			"$$[$$[\"缺点：类型，没有为空 - int\",\"排名（世界范围，0-未知） - int\"]$$]$$" })
	private List<List<List<Integer>>> characteristics;

	/**
	 * 擅长位置，F-前锋、M-中场、D-后卫、G-守门员、其他为未知
	 */
	private String position;
	
	/**
	 * 详细位置字段说明（没有字段不存在）
	 */
	@Schema(description = "详细位置字段说明（没有字段不存在）", allowableValues = { "主要位置 - string", "$$[\"次要位置列表 - string\"]$$"})
	private List<Object> positions;

	/**
	 * 更新时间
	 */
	private Integer updated_at;
}
