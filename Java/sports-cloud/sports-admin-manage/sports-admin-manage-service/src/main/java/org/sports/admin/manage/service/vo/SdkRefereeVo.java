package org.sports.admin.manage.service.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.sports.admin.manage.dao.enums.MatchType;

/**
 * 教练
 */
@Data
public class SdkRefereeVo {
    /**
     * 年龄
     */
    private Integer age;
    /**
     * 生日（0-未知）
     */
    private Integer birthday;
    /**
     * 裁判id
     */
    private Integer id;
    /**
     * 英文名称
     */
    @JsonProperty(value = "name_en")
    @Schema(description = "英文名称")
    private String nameEn;
    /**
     * 中文名称
     */
    @JsonProperty(value = "name_zh")
    @Schema(description = "中文名称")
    private String nameZh;
    /**
     * 粤语名称
     */
    @JsonProperty(value = "name_zht")
    @Schema(description = "粤语名称")
    private String nameZht;
    /**
     * 更新时间
     */
    @JsonProperty(value = "updated_at")
    @Schema(description = "更新时间")
    private Integer updatedAt;

    @Schema(description = "比赛类型：1：足球；2：篮球")
    private MatchType matchType;
}