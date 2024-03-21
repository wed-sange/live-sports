package com.edi.sdk.basketball.request;


import com.edi.sdk.core.EdiRequest;

import lombok.Data;

/**

 */
@Data
public class ArchiveRequest implements EdiRequest {
	private static final String PATH = "/api/v5/basketball/archive";

	@Override
	public String getPath() {
		return PATH;
	}
}
