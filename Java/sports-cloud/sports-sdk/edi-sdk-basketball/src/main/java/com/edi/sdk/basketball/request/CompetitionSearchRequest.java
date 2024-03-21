package com.edi.sdk.basketball.request;

import com.edi.sdk.core.EdiRequest;
import lombok.Data;

/**
 * 按比赛时间、赛事类型、状态 获取比赛列表
 */
@Data
public class CompetitionSearchRequest implements EdiRequest {

    private static final String PATH = "/api/v5/basketball/competition/search/list";

    /**
     * 赛事名称
     */
    private String name;


    @Override
    public String getPath() {
        return PATH;
    }

}
