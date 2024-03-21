package com.edi.sdk.football.request;

import com.edi.sdk.core.EdiRequest;
import lombok.Data;

/**
 * @ClassName MatchTrendDetailRequest
 * @Description 获取比赛趋势详情
 
 * @Date 2023/6/29
 * @Version 1.0
 **/
@Data
public class MatchTrendDetailRequest implements EdiRequest {
    private static final String PATH = "/api/v5/football/match/trend/detail";

    /**
     * 比赛id
     */
    private Integer id;

    @Override
    public String getPath() {
        return PATH;
    }
}
