package com.edi.sdk.football.request;

import com.alibaba.fastjson2.annotation.JSONField;
import com.edi.sdk.core.EdiRequest;
import lombok.Data;

/**
 * @ClassName MatchScheduleParamRequest
 * @Description 获取赛程赛果列表-维度查询
 
 * @Date 2023/6/29
 * @Version 1.0
 **/
@Data
public class MatchScheduleParamRequest implements EdiRequest {
    private static final String PATH = "/api/v5/football/match/schedule/param";

    /**
     * 赛事id
     */
    @JSONField(name = "competition_id")
    private Integer competitionId;

    /**
     * 彩种类型，101-竞彩足球、301-北单胜负过关、404-北单让球胜平负、401-14场胜负
     */
    @JSONField(name = "lottery_id")
    private Integer lotteryId;

    @Override
    public String getPath() {
        return PATH;
    }
}
