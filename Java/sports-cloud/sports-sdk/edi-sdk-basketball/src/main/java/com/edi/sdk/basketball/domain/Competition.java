package com.edi.sdk.basketball.domain;


import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 赛事
 * 
 2023年5月22日 下午5:05:49
 */
@Data
public class Competition {
	/**
	 * 赛事id
	 */
	private Integer id; 
	/**
	 * 分类id
	 */
	@JsonProperty(value = "category_id")
	@Schema(description = "分类id")
	private Integer categoryId; 
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
	 * 赛事logo
	 */
	private String logo; 
	/**
	 * 赛事类型，0-未知、1-联赛、2-杯赛
	 */
	private Integer type;
	/**
	 *  更新时间
	 */
	@JsonProperty(value = "updated_at")
	@Schema(description = "更新时间")
	private Integer updatedAt;
}
