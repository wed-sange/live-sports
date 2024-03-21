package com.edi.sdk.football.request;

import com.edi.sdk.core.EdiRequest;
import lombok.Data;

/**
 *获取单场比赛必发数据
 * 返回单场比赛必发数据的变化历史，从初始到请求接口的时刻
 * 请求次数：120次/min
 *
 * 说明：
 * 必发变化数据在实时必发接口获取，实时必发接口返回的是最近60s内的全量比赛的变动必发数据，需本地记录
 * 若必发数据获取有缺失或未获取到，再通过单场比赛必发接口进行查缺补漏
 */
@Data
public class OddsBfHistoryRequest implements EdiRequest {
    private static final String PATH = "/api/v5/football/odds/bf/history";

    /**
     * 比赛id
     */
    private Integer id;

    @Override
    public String getPath() {
        return PATH;
    }
}
