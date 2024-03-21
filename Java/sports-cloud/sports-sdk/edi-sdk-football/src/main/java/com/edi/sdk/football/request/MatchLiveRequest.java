package com.edi.sdk.football.request;

import com.edi.sdk.core.EdiRequest;
import lombok.Data;

/**
 * @ClassName MatchLiveRequest
 * @Description 获取实时统计数据
 
 * @Date 2023/6/29
 * @Version 1.0
 **/
@Data
public class MatchLiveRequest implements EdiRequest {
    private static final String PATH = "/api/v5/football/match/live";

    @Override
    public String getPath() {
        return PATH;
    }
}
