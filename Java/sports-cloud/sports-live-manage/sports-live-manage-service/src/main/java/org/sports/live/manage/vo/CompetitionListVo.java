package org.sports.live.manage.vo;

import lombok.Builder;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Builder
@Data
public class CompetitionListVo implements Comparable<CompetitionListVo> {
    /**
     * 赛事ID
     */
    private Integer competitionId;
    /**
     * 赛事
     */
    private String competitionName;
    /**
     * 赛事全称
     */
    private String fullCompetitionName;
    /**
     * 赛事 拼音首字母
     */
    private String shortName;

    @Override
    public int compareTo(@NotNull CompetitionListVo o) {
        return shortName.compareTo(o.shortName);
    }
}
