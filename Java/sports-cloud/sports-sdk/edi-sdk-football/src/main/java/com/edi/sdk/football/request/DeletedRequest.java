package com.edi.sdk.football.request;

import com.edi.sdk.core.EdiRequest;

/**
 * @ClassName DeletedRequest
 * @Description 获取删除数据
 
 * @Date 2023/6/29
 * @Version 1.0
 **/
public class DeletedRequest implements EdiRequest {
    private static final String PATH = "/api/v5/football/deleted";

    @Override
    public String getPath() {
        return PATH;
    }
}
