package com.edi.sdk.basketball.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.edi.sdk.core.EdiRequest;

import lombok.Data;

/**
 2023年5月23日 下午1:53:21
 */
@Data
public class MatchScheduleParamRequest implements EdiRequest {
	private static final String PATH = "/api/v5/basketball/match/schedule/param";
	/**
	 * 赛事id
	 */
	@JsonProperty(value = "competition_id")
	private Integer competitionId;
	/**
	 * 彩种类型，201-竞彩篮球、301-北单胜负过关
	 */
	@JsonProperty(value ="lottery_id")
	private Integer lotteryId;

	@Override
	public String getPath() {
		return PATH;
	}

}
