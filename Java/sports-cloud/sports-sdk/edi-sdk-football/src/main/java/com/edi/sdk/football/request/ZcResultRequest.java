package com.edi.sdk.football.request;


import com.edi.sdk.core.EdiRequest;
import lombok.Data;

/**

 */
@Data
public class ZcResultRequest implements EdiRequest {
	private static final String PATH = "/api/v2/sports/zc/result";

	@Override
	public String getPath() {
		return PATH;
	}

	/**
	 * 足彩类型，sfc：胜负彩/任九、bqc：半全场、jqc：进球彩，默认 sfc
	 */
	private String type;
}
