package com.edi.sdk.football.request;

import com.edi.sdk.core.EdiRequest;
import com.edi.sdk.core.RequestMethod;
import lombok.Data;

/**
 2023年5月31日 上午10:53:07
 * 获取单场比赛指数数据
 * 返回单场比赛所有指数公司的指数变化历史，从初盘到请求接口的时刻（根据‘指数更新数据’接口变动更新，作为补充使用）
 *
 * 范围：初盘、即时盘、滚球盘
 * 每家公司每种指数的第一个盘口，即为初盘；初盘到开球为即时盘；开球后为滚球盘
 *
 * 请求次数：120次/min
 *
 * 说明：
 * 指数变化数据都在实时指数接口获取，实时指数接口返回的是最近60s内的全量比赛的变动指数数据，需本地记录
 * 若指数数据获取有缺失或未获取到，再通过单场比赛指数接口进行查缺补漏
 */
@Data
public class OddsHistoryRequest implements EdiRequest {
	private static final String PATH = "/api/v5/football/odds/history";
	/**
	 * 比赛id
	 */
	private Integer id;

	@Override
	public RequestMethod getMethod() {
		return RequestMethod.GET;
	}

	@Override
	public String getPath() {
		return PATH;
	}
}
