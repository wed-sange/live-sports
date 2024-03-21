package com.edi.sdk.football.request;

import com.edi.sdk.core.EdiRequest;
import lombok.Data;

/**
 * @ClassName MatchStreamVideoCollectionRequest
 * @Description 获取版权比赛集锦录像地址
 
 * @Date 2023/6/29
 * @Version 1.0
 **/
@Data
public class MatchStreamVideoCollectionRequest implements EdiRequest {
    private static final String PATH = "/api/v5/football/match/stream/video_collection";

    /**
     * 比赛id，在‘版权比赛直播地址’接口获取
     */
    private Integer id;

    @Override
    public String getPath() {
        return PATH;
    }
}
