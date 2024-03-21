package com.edi.sdk.football.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.List;

/**
 *
 * 单场比赛百欧指数数据
 *
 * 指数公司id，详见百欧公司列表
 * 欧赔指数字段说明
 * example：[1578296189,1.57,3.9,6,0]
 * Enum:
 * Array[5]
 * 0:"变化时间 - int"
 * 1:"主胜 - float"
 * 2:"和局 - float"
 * 3:"客胜 - float"
 * 4:"是否封盘：1-封盘、0-未封盘 - int"
 */
@Data
public class OddsEuropeHistory extends HashMap<String,List<Object>> {

}