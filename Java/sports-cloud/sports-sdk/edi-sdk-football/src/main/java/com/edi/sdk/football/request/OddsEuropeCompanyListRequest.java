package com.edi.sdk.football.request;

import com.edi.sdk.core.EdiRequest;
import com.edi.sdk.core.RequestMethod;
import lombok.Data;

/**
 2023年5月31日 上午10:53:07
 * 获取百欧公司列表
 * 返回百欧公司列表数据
 *
 * 数据很少变动，建议请求频次：1天/次
 */
@Data
public class OddsEuropeCompanyListRequest implements EdiRequest {
	private static final String PATH = "/api/v5/football/odds/europe/company/list";

	@Override
	public RequestMethod getMethod() {
		return RequestMethod.GET;
	}

	@Override
	public String getPath() {
		return PATH;
	}
}
