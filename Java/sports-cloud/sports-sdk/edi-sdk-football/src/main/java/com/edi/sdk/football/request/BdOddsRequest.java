package com.edi.sdk.football.request;


import com.edi.sdk.core.EdiRequest;
import lombok.Data;

/**

 */
@Data
public class BdOddsRequest implements EdiRequest {
	private static final String PATH = "/api/v2/sports/bd/odds";

	@Override
	public String getPath() {
		return PATH;
	}
}
