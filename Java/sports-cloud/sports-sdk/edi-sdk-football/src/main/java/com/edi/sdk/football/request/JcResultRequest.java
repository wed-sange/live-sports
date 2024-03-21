package com.edi.sdk.football.request;


import com.edi.sdk.core.EdiRequest;
import lombok.Data;

/**

 */
@Data
public class JcResultRequest implements EdiRequest {
	private static final String PATH = "/api/v2/sports/jc/result";

	@Override
	public String getPath() {
		return PATH;
	}
}
