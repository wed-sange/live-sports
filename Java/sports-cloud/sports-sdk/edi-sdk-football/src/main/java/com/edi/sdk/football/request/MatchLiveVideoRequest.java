package com.edi.sdk.football.request;

import com.edi.sdk.core.EdiRequest;
import lombok.Data;

/**
 * 查询比赛直播线路查询
 */
@Data
public class MatchLiveVideoRequest implements EdiRequest {
    private static final String PATH = "/api/v5/football/live-video/list";

    @Override
    public String getPath() {
        return PATH;
    }
}
