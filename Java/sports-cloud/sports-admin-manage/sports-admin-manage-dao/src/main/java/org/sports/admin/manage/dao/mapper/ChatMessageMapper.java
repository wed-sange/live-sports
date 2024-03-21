package org.sports.admin.manage.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.sports.admin.manage.dao.entity.ChatMessageDO;

import java.util.List;

/**
 * <p>
 * 群组用户信息 Mapper 接口
 * </p>
 *

 * @since 2023-07-26
 */
@Mapper
public interface ChatMessageMapper extends BaseMapper<ChatMessageDO> {


    /**
     * APP用户：消息页面分页查询
     *
     * @param page
     * @param toId
     * @return
     */
    @Select("<script>"
            + "select cm2.* from ("
            + " select DISTINCT cm1.id,cm1.create_time,cm1.anchor_id,cm1.content,cm1.msg_type from t_chat_message cm1"
            + " where cm1.chat_type = 2 and cm1.del_flag = 0 and cm1.user_del = 0  and (cm1.to_id = #{toId} or cm1.from_id = #{toId}) order by cm1.create_time desc"
            + " ) cm2 GROUP BY cm2.anchor_id order by cm2.create_time desc"
            + "</script>")
    Page<ChatMessageDO> queryMsgByPage1(@Param("page") Page<ChatMessageDO> page, @Param("toId") Long toId);

    /**
     * APP用户：消息页面分页查询[过滤name]
     *
     * @param page
     * @param toId
     * @param userName
     * @return
     */
    @Select("<script>"
            + "select cm2.* from ("
            + " select DISTINCT cm1.id,cm1.create_time,cm1.anchor_id,cm1.content,cm1.msg_type from t_chat_message cm1"
            + " where cm1.chat_type = 2 and cm1.del_flag = 0 and cm1.user_del = 0 and (cm1.to_id = #{toId} or cm1.from_id = #{toId})"
            + " and exists (select 1 from t_live_user lu where lu.id = cm1.anchor_id and lu.nick_name like concat('%',#{userName},'%')"
            + " )order by cm1.create_time desc"
            + " ) cm2 GROUP BY cm2.anchor_id order by cm2.create_time desc"
            + "</script>")
    Page<ChatMessageDO> queryMsgByPage2(@Param("page") Page<ChatMessageDO> page, @Param("toId") Long toId, @Param("userName") String userName);


    /**
     * 私聊主播分页查询, 置顶的主播排列最上面
     *
     * @param page
     * @return
     */
    @Select("<script>"
            + "SELECT cm2.* FROM ("
            + "  SELECT DISTINCT cm1.id, cm1.create_time, cm1.anchor_id, cm1.msg_type "
            + "  FROM t_chat_message cm1 "
            + "  WHERE cm1.chat_type = 2 "
            + "    AND cm1.del_flag = 0 "
            + "    AND cm1.anchor_del = 0 "
            + "    AND cm1.anchor_id IN "
            + "<foreach item='item' index='index' collection='anchorIds' open='(' separator=',' close=')'>"
            + "#{item}"
            + "</foreach>"
            + "  ORDER BY cm1.create_time DESC"
            + ") cm2 "
            + "GROUP BY cm2.anchor_id "
            + "ORDER BY "
            + "    <if test='anchorTopList != null and anchorTopList.size() > 0'>"
            + "  CASE WHEN cm2.anchor_id IN "
            + "<foreach item='item' index='index' collection='anchorTopList' open='(' separator=',' close=')'>"
            + "#{item}"
            + "</foreach>"
            + " THEN 0 ELSE 1 END, "
            + "    </if>"
            + "cm2.create_time DESC "
            + "</script>")
    Page<ChatMessageDO> queryPrivateLivePage(@Param("page") Page<ChatMessageDO> page, @Param("anchorIds") List<String> anchorIds, @Param("anchorTopList") List<String> anchorTopList);

    /**
     * 根据主播ID查询所有私聊用户列表
     *
     * @param page
     * @param anchorId
     * @return
     */
    @Select("<script>"
            + "SELECT cm3.* FROM ("
            + "  SELECT cm2.* FROM ("
            + "    SELECT DISTINCT cm1.id, cm1.create_time, cm1.from_id, cm1.content, cm1.msg_type, cm1.avatar, cm1.nick "
            + "    FROM t_chat_message cm1 "
            + "    WHERE cm1.anchor_id = #{anchorId} "
            + "      AND cm1.chat_type = 2 "
            + "      AND cm1.del_flag = 0 "
            + "    ORDER BY cm1.create_time DESC"
            + "  ) cm2 "
            + "  GROUP BY cm2.from_id "
            + "  ORDER BY "
            + "    <if test='userIds != null and userIds.size() > 0'>"
            + "      CASE WHEN cm2.from_id IN "
            + "<foreach item='item' index='index' collection='userIds' open='(' separator=',' close=')'>"
            + "#{item}"
            + "</foreach>"
            + " THEN 0 ELSE 1 END,"
            + "    </if>"
            + "    cm2.create_time DESC "
            + ") cm3 "
            + "WHERE NOT EXISTS (SELECT 1 FROM t_live_user u WHERE u.id = cm3.from_id) "
            + "</script>")
    Page<ChatMessageDO> queryPrivateUserByLivePage(@Param("page") Page<ChatMessageDO> page, @Param("anchorId") Long anchorId, @Param("userIds") List<String> userIds);

}
