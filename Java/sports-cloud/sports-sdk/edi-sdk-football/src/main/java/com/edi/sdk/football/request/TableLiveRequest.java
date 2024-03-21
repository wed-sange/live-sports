package com.edi.sdk.football.request;

import com.edi.sdk.core.EdiRequest;

/**
 * @ClassName TableLiveRequest
 * @Description 获取实时积分榜数据
 
 * @Date 2023/6/29
 * @Version 1.0
 **/
public class TableLiveRequest implements EdiRequest {
    private static final String PATH = "/api/v5/football/table/live";

    @Override
    public String getPath() {
        return PATH;
    }
}
