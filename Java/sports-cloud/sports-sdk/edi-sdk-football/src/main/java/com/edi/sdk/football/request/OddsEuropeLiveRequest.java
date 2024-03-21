package com.edi.sdk.football.request;

import com.edi.sdk.core.EdiRequest;
import com.edi.sdk.core.RequestMethod;
import lombok.Data;

/**
 2023年5月31日 上午10:53:07
 * 获取实时百欧指数数据
 * 返回实时变动的欧赔数据，无赔率变动的比赛不返回
 * 返回最近60秒内变化的欧赔数据，需要客户自身把变动数据记录下来
 *
 * 范围：初盘、即时盘
 * 每家公司每种指数的第一个盘口，即为初盘；初盘到开球为即时盘
 *
 * 建议请求频次：5秒/次
 */
@Data
public class OddsEuropeLiveRequest implements EdiRequest {
	private static final String PATH = "/api/v5/football/odds/europe/live";

	@Override
	public RequestMethod getMethod() {
		return RequestMethod.GET;
	}

	@Override
	public String getPath() {
		return PATH;
	}
}
