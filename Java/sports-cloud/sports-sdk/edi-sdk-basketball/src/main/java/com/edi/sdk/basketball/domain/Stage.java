package com.edi.sdk.basketball.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 赛季

 * 2023年5月22日 下午5:08:10
 */
@Data
public class Stage {
	/**
	 * 阶段id
	 */
	private Integer id; 
	/**
	 * 赛季id
	 */
	@JsonProperty(value = "season_id")
	@Schema(description = "赛季id")
	private Integer seasonId;
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
	 * 比赛模式，1-积分赛、2-淘汰赛
	 */
	private Integer mode;
	/**
	 * 总组数
	 */
	@JsonProperty(value = "group_count")
	@Schema(description = "总组数")
	private Integer groupCount;
	/**
	 * 总轮数
	 */
	@JsonProperty(value = "round_count")
	@Schema(description = "总轮数")
	private Integer roundCount;
	/**
	 * 排序，阶段的先后顺序
	 */
	private Integer order;	
	/**
	 * 更新时间
	 */
	@JsonProperty(value = "updated_at")
	@Schema(description = "更新时间")
	private Integer updatedAt;
}
