package com.edi.sdk.basketball.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.edi.sdk.core.EdiRequest;

import lombok.Data;

/**
 2023年5月23日 下午1:53:21
 */
@Data
public class MatchScheduleSeaSonRequest implements EdiRequest {
	private static final String PATH = "/api/v5/basketball/match/schedule/season";
	/**
	 * 赛季id
	 */
	@JsonProperty(value = "id")
	private Integer id;

	@Override
	public String getPath() {
		return PATH;
	}
}
