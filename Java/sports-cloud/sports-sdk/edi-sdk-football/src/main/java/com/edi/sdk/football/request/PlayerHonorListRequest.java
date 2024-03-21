package com.edi.sdk.football.request;

import com.edi.sdk.core.EdiRequest;
import com.edi.sdk.core.RequestMethod;
import lombok.Data;

/**
 2023年5月31日 上午10:53:07
 * 获取球员荣誉列表
 * 返回全量球员荣誉数据，可根据时间戳增量获取新增或变动的数据
 *
 * 1、首次全量更新，根据参数id获取全量数据
 * 2、后续增量更新，根据参数time增量获取变动数据（建议请求频次：1min/次）
 */
@Data
public class PlayerHonorListRequest implements EdiRequest {
	private static final String PATH = "/api/v5/football/player/honor/list";
	/**
	 * 查询大于等于id的记录，根据id排序
	 */
	private Integer id;

	/**
	 * 返回数据最大数，默认为100，最大为100
	 */
	private Integer limit = 100;

	/**
	 * 查询大于等于更新时间的记录(时间戳)，根据更新时间排序
	 */
	private Integer time;

	@Override
	public RequestMethod getMethod() {
		return RequestMethod.GET;
	}

	@Override
	public String getPath() {
		return PATH;
	}
}
