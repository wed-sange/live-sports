package com.edi.sdk.football.request;

import com.alibaba.fastjson2.annotation.JSONField;
import com.edi.commons.model.dto.SearchDto;
import com.edi.sdk.core.EdiRequest;
import com.edi.sdk.core.RequestMethod;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 按比赛时间、赛事类型、状态 获取比赛列表
 */
@Data
public class MatchScheduleSearchRequest extends SearchDto implements EdiRequest {

    private static final String PATH = "/api/v5/football/match/schedule/list";

    /**
     * 开始时间
     */
    @JSONField(name = "start_match_time", format = "yyyyMMddHHmmss")
    private LocalDateTime startMatchTime;
    /**
     * 结束时间
     */
    @JSONField(name = "end_match_time", format = "yyyyMMddHHmmss")
    private LocalDateTime endMatchTime;

    /**
     * 赛事ID's
     */
    @JSONField(name = "competition_ids")
    private List<Integer> competitionIds;

    /**
     * 比赛状态
     */
    private List<Integer> status;


    @Override
    public String getPath() {
        return PATH;
    }

    @Override
    public RequestMethod getMethod() {
        return RequestMethod.GET;
    }
}
