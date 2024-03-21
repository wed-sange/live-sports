package com.edi.sdk.basketball.request;

import com.alibaba.fastjson2.annotation.JSONField;
import com.edi.sdk.core.EdiRequest;
import com.edi.sdk.core.RequestMethod;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class MatchTimeCountListRequest implements EdiRequest {
    /**
     * 开始时间
     */
    @JSONField( name = "start_match_time",  format = "yyyyMMddHHmmss")
    private LocalDateTime startMatchTime;
    /**
     * 结束时间
     */
    @JSONField( name = "end_match_time",  format = "yyyyMMddHHmmss")
    private LocalDateTime endMatchTime;

    @Override
    public String getPath() {
        return "/api/v5/basketball/schedule/count";
    }

    @Override
    public RequestMethod getMethod() {
        return RequestMethod.GET;
    }
}
