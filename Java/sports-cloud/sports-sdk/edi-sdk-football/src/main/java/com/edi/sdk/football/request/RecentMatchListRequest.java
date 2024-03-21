package com.edi.sdk.football.request;

import com.edi.sdk.core.EdiRequest;
import lombok.Data;

/**
 * @ClassName RecentMatchListRequest
 * @Description 获取变动比赛列表
 
 * @Date 2023/6/29
 * @Version 1.0
 **/
@Data
public class RecentMatchListRequest implements EdiRequest {
    private static final String PATH = "/api/v5/football/recent/match/list";
    /**
     * 查询大于等于id的记录，根据id排序
     */
    private Integer id;

    /**
     * 查询大于等于更新时间的记录(时间戳)，根据更新时间排序
     */
    private Integer time;

    /**
     * 返回数据最大数，默认为1000，最大为1000
     */
    private Integer limit;

    @Override
    public String getPath() {
        return PATH;
    }
}
