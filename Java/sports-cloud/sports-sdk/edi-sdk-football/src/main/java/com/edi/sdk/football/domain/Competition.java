package com.edi.sdk.football.domain;

import java.util.List;

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
	 * 卫冕冠军 字段说明（为空不存在）
	 */
	@JsonProperty(value = "title_holder")
	@Schema(description = "卫冕冠军 字段说明（为空不存在）<br/>example：[10181, 6]", allowableValues = { "球队id - int", "赛事冠军次数 - int" })
	private List<Integer> titleHolder;
	/**
	 * 夺冠最多球队 字段说明（为空不存在）
	 */
	@JsonProperty(value = "most_titles")
	@Schema(description = "夺冠最多球队 字段说明（为空不存在）<br/>example：[[10135], 20]", allowableValues = { "$$[\"球队id - int\"]$$","赛事冠军次数 - int" })
	private List<Object> mostTitles;
	/**
	 * 晋级淘汰球队 字段说明（为空不存在）
	 */
	@Schema(description = "晋级淘汰球队 字段说明（为空不存在）<br/>example：[[10021], [10411]]", allowableValues = {
			"$$[\"球队id（低级赛事升级球队列表，没有为空） - int\"]$$", "$$[\"球队id（高级赛事降级球队列表，没有为空） - int\"]$$" })
	private List<List<Integer>> newcomers;
	/**
	 * 赛事层级 字段说明（为空不存在）
	 */
	@Schema(description = "赛事层级 字段说明（为空不存在）<br/>example：[[], [83]]", allowableValues = {"[\"高一级赛事id，没有为空 - int\"]","[\"低一级赛事id，没有为空 - int\"]"})
	private List<List<Integer>> divisions;
	/**
	 * 东道主（为空不存在）
	 */
	private Host host;
	/**
	 * 主颜色，可能不存在
	 */
	@JsonProperty(value = "primary_color")
	@Schema(description = "主颜色，可能不存在")
	private String primaryColor;
	/**
	 * 次颜色，可能不存在
	 */
	@JsonProperty(value = "secondary_color")
	@Schema(description = "次颜色，可能不存在")
	private String secondaryColor;
	/**
	 * 更新时间
	 */
	@JsonProperty(value = "updated_at")
	@Schema(description = "更新时间")
	private Integer updatedAt;

	@Data
	public static class Host {
		/**
		 * 国家
		 */
		private String country;
		/**
		 * 城市
		 */
		private String city;
	}
}
