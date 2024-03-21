package org.sports.admin.manage.service.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 直播间热度管理
 * </p>
 *

 * @since 2023-07-26
 */
@Data
@Schema(description = "直播间热度管理")
public class LiveHeatConfigVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "基础热度值（非热门）")
    @NotNull(message = "{question.notBlank}")
    private Integer baseHeat;

    @Schema(description = "基础热度值（热门）")
    @NotNull(message = "{question.notBlank}")
    private Integer baseHeatHot;

//    @Schema(description = "30分钟增加值上限")
//    @NotNull(message = "{question.notBlank}")
//    private Integer thirtyAddUpper;
//
//    @Schema(description = "30分钟增加值下限")
//    @NotNull(message = "{question.notBlank}")
//    private Integer thirtyAddLower;

    @Schema(description = "人数系数值")
    @NotNull(message = "{question.notBlank}")
    private Integer peopleNumberRatio;

    @Schema(description = "直播间消息发送系数")
    @NotNull(message = "{question.notBlank}")
    private Integer msgSendRatio;

    @Schema(description = "直播间分享系数")
    @NotNull(message = "{question.notBlank}")
    private Integer shareRatio;

    @Schema(description = "推流地址")
    private String streamingAddress;

}
