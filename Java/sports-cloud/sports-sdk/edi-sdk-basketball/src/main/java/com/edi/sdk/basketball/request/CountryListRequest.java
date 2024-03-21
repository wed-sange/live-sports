package com.edi.sdk.basketball.request;

import com.edi.sdk.core.EdiRequest;

/**
 2023年5月23日 下午1:53:21
 */
public class CountryListRequest implements EdiRequest {
	private static final String PATH = "/api/v5/basketball/country/list";

	@Override
	public String getPath() {
		return PATH;
	}

}
