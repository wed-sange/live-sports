package com.edi.sdk.basketball.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 教练

 * 2023年5月22日 下午5:06:50
 */
@Data
public class Coach {
	/**
	 * 教练id
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
	 * 教练logo
	 */
	private String logo;
	/**
	 * 执教球队id
	 */
	@JsonProperty(value = "team_id")
	@Schema(description = "执教球队id")
	private Integer teamId;
	/**
	 * 更新时间
	 */
	@JsonProperty(value = "updated_at")
	@Schema(description = "更新时间")
	private Integer updatedAt;
}
