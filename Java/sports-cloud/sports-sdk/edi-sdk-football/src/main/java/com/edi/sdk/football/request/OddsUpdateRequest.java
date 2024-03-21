package com.edi.sdk.football.request;

import com.edi.sdk.core.EdiRequest;
import com.edi.sdk.core.RequestMethod;
import lombok.Data;

/**
 2023年5月31日 上午10:53:07
 * 获取指数更新数据
 * 返回最近60秒内指数有更新修复的全量比赛以及关联的指数公司与更新时间（倒序）
 * 建议请求频次：3秒/次
 *
 * 说明：
 * 根据该接口获取到的比赛id与指数公司，通过‘单场比赛指数数据’接口获取并修复对应的公司指数数据
 */
@Data
public class OddsUpdateRequest implements EdiRequest {
	private static final String PATH = "/api/v5/football/odds/update";

	@Override
	public RequestMethod getMethod() {
		return RequestMethod.GET;
	}

	@Override
	public String getPath() {
		return PATH;
	}
}
