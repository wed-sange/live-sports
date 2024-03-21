package com.edi.sdk.football.request;


import com.edi.sdk.core.EdiRequest;
import lombok.Data;

/**

 */
@Data
public class BdSfResultRequest implements EdiRequest {
	private static final String PATH = "/api/v2/sports/bd/sf_result";

	@Override
	public String getPath() {
		return PATH;
	}
}
