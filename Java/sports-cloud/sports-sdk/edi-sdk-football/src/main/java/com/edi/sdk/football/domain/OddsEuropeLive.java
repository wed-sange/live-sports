package com.edi.sdk.football.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 实时百欧指数数据
 *
 * 欧赔指数字段说明
 * example：[2686532,9,[1578296189,1.57,3.9,6,0],0]
 * Enum:
 * Array[4]
 * 0:"比赛id - int"
 * 1:"指数公司id，详见百欧公司列表 - int"
 * 2:Array[5]
 * 0:"变化时间 - int"
 * 1:"胜 - float"
 * 2:"平 - float"
 * 3:"负 - float"
 * 4:"是否封盘：1-封盘，0-未封盘 - int"
 * 3:"是否修正数据。用于清除错误数据：1-删除旧数据 0-新增数据 - int"
 *
 */
@Data
public class OddsEuropeLive extends ArrayList<List<Object>>{

}