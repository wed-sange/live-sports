package com.edi.sdk.football.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 球员转会列表
 */
@Data
public class PlayerTransfer {

    /**
     * 球员id
     */
    private Integer id;
    /**
     * 球员转会列表
     */
    private List<Transfer> transfer;

    /**
     * 更新时间
     */
    @JsonProperty(value = "updated_at")
    @Schema(description = "赛季id")
    private Integer updatedAt;

    @Data
    public static class Transfer {
        /**
         * 转入球队id
         */
        @JsonProperty(value = "to_team_id")
        @Schema(description = "转入球队id")
        private Integer toTeamId;
        /**
         * 转会费用
         */
        @JsonProperty(value = "transfer_fee")
        @Schema(description = "转会费用")
        private Integer transferFee;
        /**
         * 转入球队名称
         */
        @JsonProperty(value = "to_team_name")
        @Schema(description = "转入球队名称")
        private String toTeamName;
        /**
         * 转会时间
         */
        @JsonProperty(value = "transfer_time")
        @Schema(description = "转会时间")
        private Integer transferTime;
        /**
         * 转出球队id
         */
        @JsonProperty(value = "from_team_id")
        @Schema(description = "转出球队id")
        private Integer fromTeamId;
        /**
         * 转出球队名称
         */
        @JsonProperty(value = "from_team_name")
        @Schema(description = "转出球队名称")
        private String fromTeamName;
        /**
         * 转会类型，1-租借、2-租借结束、3-转会、4-退役、5-选秀、6-已解约、7-已签约、8-未知
         */
        @JsonProperty(value = "transfer_type")
        @Schema(description = "转会类型，1-租借、2-租借结束、3-转会、4-退役、5-选秀、6-已解约、7-已签约、8-未知")
        private Integer transferType;
        /**
         * 转会描述（含单位）
         */
        @JsonProperty(value = "transfer_desc")
        @Schema(description = "转会描述（含单位）")
        private String transferDesc;
    }

}