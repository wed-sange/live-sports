package com.edi.sdk.football.request;

import com.edi.sdk.core.EdiRequest;
import lombok.Data;

/**
 * 获取实时必发数据
 * 返回实时变动的必发数据，无变动的比赛不返回
 * 返回最近60秒变化的必发数据，需要客户自身把变动数据记录下来
 * <p>
 * 建议请求频次：3秒/次
 */
@Data
public class OddsBfLiveRequest implements EdiRequest {
    private static final String PATH = "/api/v5/football/odds/bf/live";

    @Override
    public String getPath() {
        return PATH;
    }
}
