package com.edi.sdk.football.request;

import com.edi.sdk.core.EdiRequest;
import com.edi.sdk.core.RequestMethod;
import lombok.Data;

/**
 2023年5月31日 上午10:53:07
 * 获取实时指数数据
 * 返回实时变动的赔率数据，无赔率变动的比赛不返回
 * 只返回最近60秒变化的赔率数据，需要客户自身把变动数据记录下来
 *
 * 范围：初盘、即时盘、滚球盘
 * 每家公司每种指数的第一个盘口，即为初盘；初盘到开球为即时盘；开球后为滚球盘
 *
 * 建议请求频次：3秒/次
 */
@Data
public class OddsLiveRequest implements EdiRequest {
	private static final String PATH = "/api/v5/football/odds/live";

	@Override
	public RequestMethod getMethod() {
		return RequestMethod.GET;
	}

	@Override
	public String getPath() {
		return PATH;
	}
}
