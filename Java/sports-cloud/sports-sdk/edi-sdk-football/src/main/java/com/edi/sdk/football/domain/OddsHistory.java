package com.edi.sdk.football.domain;

import lombok.Data;

import java.util.HashMap;
import java.util.List;

/**
 * 指数公司id，详见状态码->指数公司ID
 */
@Data
public class OddsHistory extends HashMap<String, OddsHistory.CompanyOddsHistory> {

    @Data
    public static class CompanyOddsHistory {
        /**
         * <p>欧赔-指数数据字段说明</p>
         *
         * <p>PS：赔率为赢率+本金</p>
         * <p>example：[1523242579,"75",4.75,3.0,1.83,1,0,"0-0"]</p>
         * <p>Enum:</p>
         * <p>Array[8]</p>
         * <p>0:"变化时间 - int"</p>
         * <p>1:"比赛进行时间，未开始为空 - string"</p>
         * <p>2:"主胜 - float"</p>
         * <p>3:"和局 - float"</p>
         * <p>4:"客胜 - float"</p>
         * <p>5:"比赛状态，详见状态码->比赛状态 - int"</p>
         * <p>6:"是否封盘：0-否，1-是 - int"</p>
         * <p>7:"比分，主队-客队 - string"</p>
         */
        private List<Object> eu;

        /**
         * <p>大小球-指数数据字段说明</p>
         *
         * <p>PS：赔率为赢率，不包含本金</p>
         * <p>example：[1523242579,"75",0.88,2.5,1.02,4,0,"0-0"]</p>
         * <p>Enum:</p>
         * <p>Array[8]</p>
         * <p>0:"变化时间 - int"</p>
         * <p>1:"比赛进行时间，未开始为空 - string"</p>
         * <p>2:"大球 - float"</p>
         * <p>3:"盘口 - float"</p>
         * <p>4:"小球 - float"</p>
         * <p>5:"比赛状态，详见状态码->比赛状态 - int"</p>
         * <p>6:"是否封盘：0-否，1-是 - int"</p>
         * <p>7:"比分，主队-客队 - string"</p>
         */
        private List<Object> bs;


        /**
         * <p>亚盘-指数数据字段说明（盘口 - 为正，主让客；为负，客让主）</p>
         * <p>PS：赔率为赢率，不包含本金</p>
         * <p>example：[1523242579,"75",1.06,0.75,0.86,4,0,"0-0"]</p>
         * <p>Enum:</p>
         * <p>Array[8]</p>
         * <p>0:"变化时间 - int"</p>
         * <p>1:"比赛进行时间，未开始为空 - string"</p>
         * <p>2:"主胜 - float"</p>
         * <p>3:"盘口 - float"</p>
         * <p>4:"客胜 - float"</p>
         * <p>5:"比赛状态，详见状态码->比赛状态 - int"</p>
         * <p>6:"是否封盘：0-否，1-是 - int"</p>
         * <p>7:"比分，主队-客队 - string"</p>
         */
        private List<Object> asia;


        /**
         * <p>角球-指数数据字段说明</p>
         *
         * <p>PS：赔率为赢率，不包含本金</p>
         * <p>example：[1604652164,"75",1.2,7.5,0.65,4,0,"0-4"]</p>
         * <p>Enum:</p>
         * <p>Array[8]</p>
         * <p>0:"变化时间 - int"</p>
         * <p>1:"比赛进行时间，未开始为空 - string"</p>
         * <p>2:"大   - float"</p>
         * <p>3:"盘口 - float"</p>
         * <p>4:"小   - float"</p>
         * <p>5:"比赛状态，详见状态码->比赛状态 - int"</p>
         * <p>6:"是否封盘：0-否，1-是 - int"</p>
         * <p>7:"角球比，主队-客队 - string"</p>
         */
        private List<Object> cr;

    }
}