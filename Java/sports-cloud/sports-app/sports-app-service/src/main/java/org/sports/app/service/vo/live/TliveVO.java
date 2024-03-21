package org.sports.app.service.vo.live;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 文字直播VO

 */
@Data
public class TliveVO  implements Serializable {
   @Schema(description = "事件内容")
    private String data;

   @Schema(description = "是否重要事件，1-是、0-否")
    private Integer main;

   @Schema(description = "事件发生方，0-中立、1-主队、2-客队")
    private Integer position;

   @Schema(description = "事件时间(分钟)")
    private String time;

   @Schema(description = "类型，详见状态码->技术统计")
    private Integer type;
}