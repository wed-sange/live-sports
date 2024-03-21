package com.edi.sdk.football.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 赛事
 */
@Data
public class MatchVideo {

	/**
	 * 赛事id
	 */
	@JsonProperty(value = "comp_id")
	@Schema(description = "赛事id")
	private Integer comId;
	/**
	 * 赛事名称
	 */
	@JsonProperty(value = "comp")
	@Schema(description = "赛事名称")
	private String comp;
	/**
	 * 比赛状态
	 */
	@JsonProperty(value = "match_status")
	@Schema(description = "比赛状态")
	private Integer matchStatus;

	/**
	 * 比赛时间
	 */
	@JsonProperty(value = "match_time")
	@Schema(description = "比赛状态")
	private Integer matchTime;
	/**
	 * 比赛Id
	 */
	@JsonProperty(value = "match_id")
	@Schema(description = "比赛Id")
	private Integer matchId;
	/**
	 * 版权
	 */
	@JsonProperty(value = "copyright")
	@Schema(description = "版权")
	private String copyright;
	/**
	 * 运动id 1-足球，2-篮球
	 */
	@JsonProperty(value = "sport_id")
	@Schema(description = "运动id 1-足球，2-篮球")
	private String sportId;
	/**
	 * 主队名称
	 */
	@JsonProperty(value = "home")
	@Schema(description = "主队名称")
	private String home;
	/**
	 * 客队名称
	 */
	@JsonProperty(value = "客队名称")
	@Schema(description = "粤语简称")
	private String away;

	/**
	 *普清地址
	 */
	@JsonProperty(value = "pushurl1")
	@Schema(description = "普清地址")
	private PushUrl pushurl1;
	/**
	 *中文高清地址
	 */
	@JsonProperty(value = "pushurl2")
	@Schema(description = "中文高清地址")
	private PushUrl pushurl2;
	/**
	 *英文高清地址
	 */
	@JsonProperty(value = "pushurl3")
	@Schema(description = "英文高清地址")
	private PushUrl pushurl3;
	/**
	 *高清中文
	 */
	@JsonProperty(value = "pushurl4")
	@Schema(description = "高清中文")
	private PushUrl pushurl4;

	@Data
	public static class PushUrl {
		/**
		 * RTMP
		 */
		private String rtmpUrl;
		/**
		 * M3U8
		 */
		private String m3u8Url;
		/**
		 * flv
		 */
		private String flvUrl;
	}
}
