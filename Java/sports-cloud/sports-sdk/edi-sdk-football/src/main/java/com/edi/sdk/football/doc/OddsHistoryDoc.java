package com.edi.sdk.football.doc;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;


/**
 * 获取单场比赛指数数据
 */
@Data
public class OddsHistoryDoc {

    @Schema(description = "integer")
    private String code;

    @Schema(description = "返回结果")
    private OddsHistory1Doc results;

    @Data
    public class OddsHistory1Doc {

        @Schema(name = "公司id", description = "公司id")
        private List<CompanyOddsHistory> id;

        @Data
        public class CompanyOddsHistory {
            @ArraySchema(
                    schema = @Schema(description = "<p>欧赔-指数数据字段说明</p>" +
                            "<p>PS：赔率为赢率+本金</p>" +
                            "<p>example：[1523242579,\"75\",4.75,3.0,1.83,1,0,\"0-0\"]</p>" +
                            "<p>Enum:</p>" +
                            "<p>Array[8]</p>",
                            type = "array",
                            allowableValues = {"变化时间 - int",
                                    "比赛进行时间，未开始为空 - string",
                                    "主胜 - float",
                                    "和局 - float",
                                    "客胜 - float",
                                    "比赛状态，详见状态码->比赛状态 - int",
                                    "比分，主队-客队 - string",
                                    "比分，主队-客队 - string"})
            )
            private List<Object> eu;

            @ArraySchema(
                    schema =
                    @Schema(description = "<p>大小球-指数数据字段说明</p>" +
                            "<p>PS：赔率为赢率，不包含本金</p>" +
                            "<p>example：[1523242579,\"75\",0.88,2.5,1.02,4,0,\"0-0\"]</p>" +
                            "<p>Enum:</p>" +
                            "<p>Array[8]</p>",
                            allowableValues = {"变化时间 - int",
                                    "比赛进行时间，未开始为空 - string",
                                    "大球 - float",
                                    "盘口 - float",
                                    "小球 - float",
                                    "比赛状态，详见状态码->比赛状态 - int",
                                    "是否封盘：0-否，1-是 - int",
                                    "比分，主队-客队 - string"})
            )
            private List<Object> bs;

            @ArraySchema(
                    schema =
                    @Schema(description = "<p>亚盘-指数数据字段说明（盘口 - 为正，主让客；为负，客让主）</p>" +
                            "<p>PS：赔率为赢率，不包含本金</p>" +
                            "<p>example：[1523242579,\"75\",1.06,0.75,0.86,4,0,\"0-0\"]</p>" +
                            "<p>Enum:</p>" +
                            "<p>Array[8]</p>",
                            allowableValues = {"变化时间 - int",
                                    "比赛进行时间，未开始为空 - string",
                                    "主胜 - float",
                                    "盘口 - float",
                                    "客胜 - float",
                                    "比赛状态，详见状态码->比赛状态 - int",
                                    "是否封盘：0-否，1-是 - int",
                                    "比分，主队-客队 - string"})
            )
            private List<Object> asia;

            @ArraySchema(
                    schema =
                    @Schema(description = "<p>角球-指数数据字段说明</p>" +
                            "<p>PS：赔率为赢率，不包含本金</p>" +
                            "<p>example：[1604652164,\"75\",1.2,7.5,0.65,4,0,\"0-4\"]</p>" +
                            "<p>Enum:</p>" +
                            "<p>Array[8]</p>",
                            allowableValues = {"变化时间 - int",
                                    "比赛进行时间，未开始为空 - string",
                                    "大   - float",
                                    "盘口 - float",
                                    "小   - float",
                                    "比赛状态，详见状态码->比赛状态 - int",
                                    "是否封盘：0-否，1-是 - int",
                                    "角球比，主队-客队 - string"})
            )
            private List<Object> cr;

        }

    }

}