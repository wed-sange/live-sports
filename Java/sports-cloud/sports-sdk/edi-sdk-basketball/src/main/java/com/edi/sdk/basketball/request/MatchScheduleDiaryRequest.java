package com.edi.sdk.basketball.request;

import com.edi.sdk.core.EdiRequest;

import lombok.Data;

/**
 2023年5月23日 下午1:53:21
 */
@Data
public class MatchScheduleDiaryRequest implements EdiRequest {
	private static final String PATH = "/api/v5/basketball/match/schedule/diary";

	/**
	 * 查询日期，格式为yyyymmdd（20200101）
	 */
	private String date;

	@Override
	public String getPath() {
		return PATH;
	}
}
