package com.edi.sdk.football.request;

import com.edi.sdk.core.EdiRequest;
import lombok.Data;

/**
 2023年5月27日 下午1:53:21
 */
@Data
public class MatchScheduleSeaSonRequest implements EdiRequest {
	private static final String PATH = "/api/v5/football/match/schedule/season";
	/**
	 * 赛季id
	 */
	private Integer id;

	@Override
	public String getPath() {
		return PATH;
	}
}
