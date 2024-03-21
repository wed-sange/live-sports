package com.edi.sdk.basketball.request;

import com.edi.sdk.core.EdiRequest;

import lombok.Data;

/**
 * 变动比赛列表
 * 
 2023年5月30日 下午2:12:11
 */
@Data
public class RecentMatchListRequest implements EdiRequest {
	private static final String PATH = "/api/v5/basketball/recent/match/list";

	/**
	 * 查询大于等于id的记录，根据id排序
	 */
	private Integer id;

	/**
	 * 返回数据最大数，默认为1000，最大为1000
	 */
	private Integer limit;

	/**
	 * 查询大于等于更新时间的记录(时间戳)，根据更新时间排序
	 */
	private Integer time;

	@Override
	public String getPath() {
		return PATH;
	}
}
