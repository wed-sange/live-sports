package com.edi.sdk.football.request;


import com.edi.sdk.core.EdiRequest;
import lombok.Data;

/**

 */
@Data
public class ArchiveRequest implements EdiRequest {
	private static final String PATH = "/api/v5/football/archive";

	@Override
	public String getPath() {
		return PATH;
	}
}
