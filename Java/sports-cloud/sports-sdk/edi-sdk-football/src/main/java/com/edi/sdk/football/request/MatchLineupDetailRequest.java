package com.edi.sdk.football.request;

import com.edi.sdk.core.EdiRequest;
import lombok.Data;

/**
 * @ClassName MatchLineupDetailRequest
 * @Description 获取比赛阵容详情
 
 * @Date 2023/6/29
 * @Version 1.0
 **/
@Data
public class MatchLineupDetailRequest implements EdiRequest {
    private static final String PATH = "/api/v5/football/match/lineup/detail";

    /**
     * 比赛id
     */
    private Integer id;

    @Override
    public String getPath() {
        return PATH;
    }
}
