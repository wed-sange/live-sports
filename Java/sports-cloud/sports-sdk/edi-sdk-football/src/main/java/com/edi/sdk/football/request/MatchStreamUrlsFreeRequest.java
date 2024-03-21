package com.edi.sdk.football.request;

import com.edi.sdk.core.EdiRequest;

/**
 * @ClassName MatchStreamUrlsFreeRequest
 * @Description 获取版权比赛直播地址
 
 * @Date 2023/6/29
 * @Version 1.0
 **/
public class MatchStreamUrlsFreeRequest implements EdiRequest {
    private static final String PATH = "/api/v5/football/match/stream/urls_free";

    @Override
    public String getPath() {
        return PATH;
    }
}
