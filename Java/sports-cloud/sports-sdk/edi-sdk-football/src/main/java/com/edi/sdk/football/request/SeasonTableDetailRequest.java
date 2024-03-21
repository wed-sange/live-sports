package com.edi.sdk.football.request;

import com.edi.sdk.core.EdiRequest;
import lombok.Data;

/**
 2023年5月23日 下午1:53:21
 */
@Data
public class SeasonTableDetailRequest implements EdiRequest {
	private static final String PATH = "/api/v5/football/season/table/detail";
	/**
	 * 赛季id
	 */
	private Integer id;

	@Override
	public String getPath() {
		return PATH;
	}

}
