package com.edi.sdk.football.domain;

import lombok.Data;

import java.util.HashMap;
import java.util.List;

/**
 * 实时指数数据
 *
 * 指数公司id，详见状态码->指数公司ID
 * 指数数据字段说明（PS：亚盘盘口 - 为正，主让客；为负，客让主）
 * （PS：亚盘、大小球、角球 - 赔率为赢率，不包含本金；欧赔 - 赔率为赢率+本金）
 * example：[2713492,"asia",[1578294410,"2","0.68,-1.25,1.15,0",1],"0-0"]
 * Enum:
 * Array[4]
 * 0:"比赛id - int"
 * 1:"指数类型：asia-亚盘、eu-欧赔、bs-大小球、cr-角球 - string"
 * 2:Array[4]
 * 0:"变化时间 - int"
 * 1:"比赛进行时间，未开始为空 - string"
 * 2:"主胜/大球/大,和局/盘口,客胜/小球/小,是否封盘：1-封盘,0-未封盘 - string"
 * 3:"比赛状态，详见状态码->比赛状态 - int"
 * 3:"进球比分/角球比(cr)，主队-客队 - string"
 */
@Data
public class OddsLive {
    /**
     * id
     */
    private Integer id;

    /**
     * 实时指数数据
     */
    private HashMap<String,List<List<Object>>> oddMap;
}