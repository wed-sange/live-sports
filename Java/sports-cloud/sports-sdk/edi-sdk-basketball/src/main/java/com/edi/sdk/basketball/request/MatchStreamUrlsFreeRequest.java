package com.edi.sdk.basketball.request;

import com.edi.sdk.core.EdiRequest;

import lombok.Data;

/**
 2023年5月26日 上午10:31:07
 */
@Data
public class MatchStreamUrlsFreeRequest implements EdiRequest {
	private static final String PATH = "/api/v5/basketball/match/stream/urls_free";

	@Override
	public String getPath() {
		return PATH;
	}
}
