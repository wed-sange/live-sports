package org.sports.admin.manage.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.sports.admin.manage.dao.entity.ChatGroupUserDO;
import org.sports.admin.manage.dao.req.GroupUserPageRequest;
import org.sports.admin.manage.service.vo.ChatGroupUserVO;
import org.sports.springboot.starter.convention.page.PageResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 群组用户信息 服务类
 * </p>
 *

 * @since 2023-07-27
 */
public interface IChatGroupUserService extends IService<ChatGroupUserDO> {

    /**
     * 根据直播ids统计当前在线直播用户人数
     * @param liveIdList
     * @return
     */
    Map<String, Integer> getLiveUserByGroupIds(List<String>  liveIdList);

    /**
     * 统计开始时间结束时间主播直播单次最高用户数
     * @param userId
     * @param startTime
     * @param endTime
     * @return
     */
    long getMaxLiveUser(Long userId, LocalDateTime startTime, LocalDateTime endTime);
    /**
     * 统计开始时间结束时间主播直播总用户数
     * @param userId
     * @param startTime
     * @param endTime
     * @return
     */
    long getTotalLiveUser(Long userId, LocalDateTime startTime, LocalDateTime endTime);

    PageResponse<ChatGroupUserVO> getGroupUser(GroupUserPageRequest req);
}
