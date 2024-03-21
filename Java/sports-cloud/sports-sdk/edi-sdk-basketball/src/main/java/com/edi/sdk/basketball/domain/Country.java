package com.edi.sdk.basketball.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 国家

 * 2023年5月22日 下午5:05:04
 */
@Data
public class Country {
	/**
	 * 国家Id
	 */
	private Integer id;
	/**
	 * 分类id
	 */
	@JsonProperty(value = "category_id")
	@Schema(description = "分类id")
	private Integer categoryId;
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
	 * 国家logo
	 */
	private String logo;
	/**
	 * 更新时间
	 */
	@JsonProperty(value = "updated_at")
	@Schema(description = "更新时间")
	private Integer updatedAt;
}
