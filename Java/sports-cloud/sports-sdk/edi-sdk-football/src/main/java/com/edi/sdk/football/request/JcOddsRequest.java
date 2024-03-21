package com.edi.sdk.football.request;


import com.edi.sdk.core.EdiRequest;
import lombok.Data;

/**

 */
@Data
public class JcOddsRequest implements EdiRequest {
	private static final String PATH = "/api/v2/sports/jc/odds";

	@Override
	public String getPath() {
		return PATH;
	}
}
