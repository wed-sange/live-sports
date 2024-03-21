package com.edi.sdk.basketball.request;

import com.edi.sdk.core.EdiRequest;

import lombok.Data;

/**

 * 2023年5月23日 下午1:53:21
 */
@Data
public class MatchStreamVideoCollectionRequest implements EdiRequest {
	private static final String PATH = "/api/v5/basketball/match/stream/video_collection";
	
	/**
	 * 比赛id
	 */
	private Integer id;
	
	@Override
	public String getPath() {
		return PATH;
	}

}
