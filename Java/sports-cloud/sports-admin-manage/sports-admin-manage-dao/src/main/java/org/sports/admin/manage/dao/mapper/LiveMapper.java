package org.sports.admin.manage.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.sports.admin.manage.dao.entity.LiveDO;
import org.sports.admin.manage.dao.enums.LiveStatus;
import org.sports.admin.manage.dao.enums.MatchType;
import org.springframework.lang.NonNull;

import java.util.Collection;
import java.util.List;

@Mapper
public interface LiveMapper extends BaseMapper<LiveDO> {
    @Select("<script>"
            + "SELECT ls.user_id, ls.stream_id, ls.stream_time "
            + "FROM live_stream ls "
            + "INNER JOIN ( "
            + "    SELECT user_id, MAX(stream_time) as max_stream_time "
            + "    FROM live_stream "
            + "    GROUP BY user_id "
            + ") grouped_ls ON ls.user_id = grouped_ls.user_id AND ls.stream_time = grouped_ls.max_stream_time "
            + "LIMIT #{page.offset}, #{page.size}"
            + "</script>")
    Page<LiveDO> selectLatestLivePage(@Param("page") Page<?> page);


    @Select("<script>" +
            "SELECT DISTINCT tl.*\n" +
            "    FROM \n" +
            "        (\n" +
            "         SELECT match_id,match_type, MAX(hot_value) AS max_hot_value\n" +
            "         FROM t_live\n" +
            "    <where>\n" +
            "        <if test='matchType != null'>\n" +
            "            AND match_type = #{matchType.code}\n" +
            "        </if>\n" +
            "        <if test='competitionIds != null and competitionIds.size() > 0'>\n" +
            "            AND competition_id in\n" +
            "            <foreach item='item' index='index' collection='competitionIds' open='(' separator=',' close=')'>\n" +
            "                #{item}\n" +
            "            </foreach>\n" +
            "        </if>\n" +
            "        <if test='liveStatus != null'>\n" +
            "            AND live_status = #{liveStatus.code}\n" +
            "        </if>\n" +
            "    </where>\n" +
            "         GROUP BY match_id, match_type \n" +
            "     limit " +
            "    <if test='limit != null'>\n" +
            "         #{limit}\n" +
            "    </if> " +
            "    <if test='limit == null'>\n" +
            "         5\n" +
            "    </if> " +
            "        ) AS max_values\n" +
            "    JOIN t_live tl \n" +
            "    ON tl.match_id = max_values.match_id AND tl.hot_value = max_values.max_hot_value AND tl.match_type = max_values.match_type\n" +
            "    ORDER BY tl.match_time ASC,tl.hot_value DESC, tl.id ASC\n" +
            "</script>")
    List<LiveDO> selectTopHot(@Param("matchType") MatchType matchType,
                              @Param("competitionIds") Collection<Integer> competitionIds,
                              @Param("liveStatus") LiveStatus liveStatus,
                              @Param("limit") Integer limit);

    @Select("<script>" +
            "SELECT DISTINCT tl.*\n" +
            "    FROM \n" +
            "        (\n" +
            "         SELECT match_id,match_type, MIN(open_time) AS min_open_time \n" +
            "         FROM t_live\n" +
            "    <where>\n" +
            "        <if test='matchType != null'>\n" +
            "            AND match_type = #{matchType.code}\n" +
            "        </if>\n" +
            "        <if test='competitionIds != null and competitionIds.size() > 0'>\n" +
            "            AND competition_id in\n" +
            "            <foreach item='item' index='index' collection='competitionIds' open='(' separator=',' close=')'>\n" +
            "                #{item}\n" +
            "            </foreach>\n" +
            "        </if>\n" +
            "        <if test='liveStatus != null'>\n" +
            "            AND live_status = #{liveStatus.code}\n" +
            "        </if>\n" +
            "    </where>\n" +
            "         GROUP BY match_id, match_type \n" +
            "     limit " +
            "    <if test='limit != null'>\n" +
            "         #{limit}\n" +
            "    </if> " +
            "    <if test='limit == null'>\n" +
            "         5\n" +
            "    </if> " +
            "        ) AS min_values\n" +
            "    JOIN t_live tl \n" +
            "    ON tl.match_id = min_values.match_id AND tl.open_time = min_values.min_open_time AND tl.match_type = min_values.match_type\n" +
            "    ORDER BY tl.match_time ASC,tl.hot_value DESC, tl.id ASC\n" +
            "</script>")
        //查找最新开始直播的比赛
    List<LiveDO> selectFirstOpenLive(@Param("matchType") MatchType matchType,
                                     @Param("competitionIds") Collection<Integer> competitionIds,
                                     @Param("liveStatus") LiveStatus liveStatus,
                                     @Param("limit") Integer limit);

    @Select("<script>\n" +
            "select \n" +
            "c.nick_name as nick_name,\n" +
            "c.head as user_logo,\n" +
            "b.*\n" +
            "from (select user_id,max(open_time) as open_time from \n" +
            "t_live\n" +
            "where user_id in " +
            "            <foreach item='item' index='index' collection='userIds' open='(' separator=',' close=')'>" +
            "                #{item}" +
            "            </foreach>\n" +
            "and del_flag = 0\n" +
            "GROUP BY user_id) as a left JOIN t_live as b\n" +
            "on a.user_id = b.user_id and a.open_time = b.open_time\n" +
            "left JOIN t_live_user as c on a.user_id = c.id \n" +
            "</script>")
    List<LiveDO> findLatestByUserIds(@NonNull @Param("userIds") Collection<Long> userIds);
}
