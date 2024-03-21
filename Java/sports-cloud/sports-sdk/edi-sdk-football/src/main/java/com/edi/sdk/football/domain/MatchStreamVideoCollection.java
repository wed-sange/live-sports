package com.edi.sdk.football.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 集锦录像地址
 */
@Data
public class MatchStreamVideoCollection {
    /**
     * 图片
     */
    private String cover;
    /**
     * 时长-秒（s）
     */
    private Integer duration;
    /**
     * web直播地址
     */
    @JsonProperty(value = "pc_link")
    private String pcLink;
    /**
     * wap直播地址
     */
    @JsonProperty(value = "mobile_link")
    private String mobileLink;
    /**
     * 类型，1-集锦、2-录像
     */
    private Integer type;
    /**
     * 名称
     */
    private String title;
}