package com.edi.sdk.basketball.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * 比赛集锦录像

 * 2023年5月31日 下午2:07:53
 */
@Data
public class VideoCollection {
	/**
	 * 类型，1-集锦、2-录像
	 */
	private Integer type;
	/**
	 * 名称
	 */
	private String title;
	/**
	 * wap直播地址
	 */
	@JsonProperty(value = "mobile_link")
	private String mobileLink;
	/**
	 * web直播地址
	 */
	@JsonProperty(value = "pc_link")
	private String pcLink;
	/**
	 * 图片
	 */
	private String cover;
	/**
	 * 时长-秒（s）
	 */
	private Integer duration;
}
