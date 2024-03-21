package com.edi.sdk.football.request;

import com.edi.sdk.core.EdiRequest;
import com.edi.sdk.core.RequestMethod;
import lombok.Data;

/**
 2023年5月31日 上午10:53:07
 * 获取国家队FIFA男子排名
 * 返回国家队FIFA男子排名数据，可根据pub_time更新时间查询历史排名数据
 *
 * 数据很少变动，根据‘数据更新’接口变动更新，或建议请求频次：1天/次
 */
@Data
public class RankingFifaMenRequest implements EdiRequest {
	private static final String PATH = "/api/v5/football/ranking/fifa/men";

	/**
	 * 更新时间，默认为最新一期（时间戳格式）
	 */
	private Integer pub_time;

	@Override
	public RequestMethod getMethod() {
		return RequestMethod.GET;
	}

	@Override
	public String getPath() {
		return PATH;
	}
}
