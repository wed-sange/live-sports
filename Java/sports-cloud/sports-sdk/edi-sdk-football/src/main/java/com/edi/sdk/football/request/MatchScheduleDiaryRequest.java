package com.edi.sdk.football.request;

import com.edi.sdk.core.EdiRequest;
import lombok.Data;

/**
 * @ClassName MatchScheduleDiaryRequest
 * @Description 获取赛程赛果列表-日期查询
 
 * @Date 2023/6/29
 * @Version 1.0
 **/
@Data
public class MatchScheduleDiaryRequest implements EdiRequest {
    private static final String PATH = "/api/v5/football/match/schedule/diary";

    /**
     * 查询日期，格式为yyyymmdd（20200101）
     */
    private String date;

    @Override
    public String getPath() {
        return PATH;
    }
}
