package com.edi.sdk.basketball.request;

import com.edi.sdk.core.EdiRequest;

import lombok.Data;

/**

 * 2023年5月23日 下午1:53:21
 */
@Data
public class MatchTrendDetailRequest implements EdiRequest {
	private static final String PATH = "/api/v5/basketball/match/trend/detail";
	
	/**
	 * 比赛id
	 */
	private Integer id;
	
	@Override
	public String getPath() {
		return PATH;
	}

}
