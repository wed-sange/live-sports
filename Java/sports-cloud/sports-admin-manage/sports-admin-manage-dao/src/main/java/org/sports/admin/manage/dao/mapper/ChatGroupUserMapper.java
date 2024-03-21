package org.sports.admin.manage.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.sports.admin.manage.dao.entity.ChatGroupUserDO;

import java.time.LocalDateTime;

/**
 * <p>
 * 群组用户信息 Mapper 接口
 * </p>
 *

 * @since 2023-07-27
 */
@Mapper
public interface ChatGroupUserMapper extends BaseMapper<ChatGroupUserDO> {
    @Select("SELECT group_id FROM t_chat_group_user WHERE user_id = #{userId} " +
            "AND create_time = (SELECT MAX(create_time) FROM " +
            "t_chat_group_user WHERE user_id = #{userId})")
    String getLiverGroupId(String userId);
    @Select("<script>" +
            " SELECT max(user_count) from " +
            " (SELECT count(user_id) user_count FROM t_chat_group_user WHERE group_id in " +
            " (SELECT group_id FROM t_chat_group_user where user_id = #{userId} " +
            " <if test='start != null and end != null '> AND create_time &gt;=  #{start} AND create_time &lt;= #{end}</if>" +
            " ) GROUP BY group_id) as sub_table"+
            " </script>")
    Long getMaxUserCount(@Param("userId") Long userId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Select("<script>" +
            " SELECT count(user_id) FROM t_chat_group_user WHERE group_id in " +
            " (SELECT group_id FROM t_chat_group_user where user_id = #{userId} " +
            " <if test='start != null and end != null '> AND create_time &gt;=  #{start} AND create_time &lt;= #{end}</if> )" +
            " </script>")
    Long getTotalLiveUserCount(@Param("userId") Long userId, @Param("start") LocalDateTime startTime, @Param("end") LocalDateTime endTime);


}
