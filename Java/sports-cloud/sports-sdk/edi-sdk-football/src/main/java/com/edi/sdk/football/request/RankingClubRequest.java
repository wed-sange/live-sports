package com.edi.sdk.football.request;

import com.edi.sdk.core.EdiRequest;
import com.edi.sdk.core.RequestMethod;
import lombok.Data;

/**
 2023年5月31日 上午10:53:07
 *
 * 获取俱乐部排名
 * 返回俱乐部排名数据
 *
 * 数据很少变动，根据‘数据更新’接口变动更新，或建议请求频次：1天/次
 */
@Data
public class RankingClubRequest implements EdiRequest {
	private static final String PATH = "/api/v5/football/ranking/club";


	@Override
	public RequestMethod getMethod() {
		return RequestMethod.GET;
	}

	@Override
	public String getPath() {
		return PATH;
	}
}
