package com.edi.sdk.basketball.request;

import com.alibaba.fastjson2.annotation.JSONField;
import com.edi.sdk.core.EdiRequest;
import com.edi.sdk.core.RequestMethod;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class SearchByTimeCompetitionRequest implements EdiRequest {

    /**
     * 开始时间
     */
    @NotNull(message = "开始时间不能为空！")
    @JSONField(name = "start_match_time", format = "yyyyMMddHHmmss")
    @Schema(description = "开始时间", example = "yyyyMMddHHmmss")
    private LocalDateTime startMatchTime;
    /**
     * 结束时间
     */
    @NotNull(message = "结束时间不能为空！")
    @JSONField(name = "end_match_time", format = "yyyyMMddHHmmss")
    @Schema(description = "结束时间", example = "yyyyMMddHHmmss")
    private LocalDateTime endMatchTime;

    @Override
    public String getPath() {
        return "/api/v5/basketball/competition/search/by/time";
    }

    @Override
    public RequestMethod getMethod() {
        return RequestMethod.GET;
    }
}
