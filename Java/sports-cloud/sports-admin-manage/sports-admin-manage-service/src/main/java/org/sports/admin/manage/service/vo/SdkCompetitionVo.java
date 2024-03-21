package org.sports.admin.manage.service.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.sports.admin.manage.dao.enums.MatchType;

import java.io.Serializable;
import java.util.List;

/**
 * 赛事
 *
 2023年5月22日 下午5:05:49
 */
@Data
public class SdkCompetitionVo implements Serializable {
    @Schema(description = "赛事id")
    private Integer id;
    @Schema(description = "分类id")
    private Integer categoryId;
    @Schema(description = "国家id")
    private Integer countryId;
    @Schema(description = "中文名称")
    private String nameZh;
    @Schema(description = "粤语名称")
    private String nameZht;
    @Schema(description = "英文名称")
    private String nameEn;
    @Schema(description = "中文简称")
    private String shortNameZh;
    @Schema(description = "粤语简称")
    private String shortNameZht;
    @Schema(description = "英文简称")
    private String shortNameEn;
    @Schema(description = "赛事logo")
    private String logo;
    @Schema(description = "赛事类型，0-未知、1-联赛、2-杯赛")
    private Integer type;
    @Schema(description = "卫冕冠军 字段说明（为空不存在）")
    private List<Integer> titleHolder;
    @Schema(description = "夺冠最多球队 字段说明（为空不存在）")
    private List<Object> mostTitles;
    @Schema(description = "晋级淘汰球队 字段说明（为空不存在）")
    private List<List<Integer>> newcomers;
    @Schema(description = "赛事层级 字段说明（为空不存在）")
    private List<List<Integer>> divisions;
    @Schema(description = "东道主（为空不存在）")
    private Host host;
    @Schema(description = "主颜色，可能不存在")
    private String primaryColor;
    @Schema(description = "次颜色，可能不存在")
    private String secondaryColor;
    @Schema(description = "更新时间")
    private Integer updatedAt;

    @Data
    public static class Host implements Serializable {
        /**
         * 国家
         */
        private String country;
        /**
         * 城市
         */
        private String city;
    }
    @Schema(description = "比赛类型：1：足球；2：篮球")
    private MatchType matchType;
}
