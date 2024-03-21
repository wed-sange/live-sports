package org.sports.admin.manage.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 赛事
 */
@Data
public class MatchVideoDTO {

	/**
	 * 赛事id
	 */
	@Schema(description = "赛事id")
	private Integer comId;
	/**
	 * 赛事名称
	 */
	@Schema(description = "赛事名称")
	private String comp;
	/**
	 * 比赛状态
	 */
	@Schema(description = "比赛状态")
	private Integer matchStatus;

	/**
	 * 比赛时间
	 */
	@Schema(description = "比赛状态")
	private Integer matchTime;
	/**
	 * 比赛Id
	 */
	@Schema(description = "比赛Id")
	private Integer matchId;
	/**
	 * 版权
	 */
	@Schema(description = "版权")
	private String copyright;
	/**
	 * 运动id 1-足球，2-篮球
	 */
	@Schema(description = "运动id 1-足球，2-篮球")
	private String sportId;
	/**
	 * 主队名称
	 */
	@Schema(description = "主队名称")
	private String home;
	/**
	 * 客队名称
	 */
	@Schema(description = "粤语简称")
	private String away;

	/**
	 *普清地址
	 */
	@Schema(description = "普清地址")
	private PushUrl pushurl1;
	/**
	 *中文高清地址
	 */
	@Schema(description = "中文高清地址")
	private PushUrl pushurl2;
	/**
	 *英文高清地址
	 */
	@Schema(description = "英文高清地址")
	private PushUrl pushurl3;
	/**
	 *高清中文
	 */
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
