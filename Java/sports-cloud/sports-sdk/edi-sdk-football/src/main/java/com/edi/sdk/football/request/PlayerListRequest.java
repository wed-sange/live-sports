package com.edi.sdk.football.request;

import com.edi.sdk.core.EdiRequest;

import lombok.Data;

/**
 2023年5月26日 上午10:31:07
 */
@Data
public class PlayerListRequest implements EdiRequest {
	private static final String PATH = "/api/v5/football/player/list";
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
