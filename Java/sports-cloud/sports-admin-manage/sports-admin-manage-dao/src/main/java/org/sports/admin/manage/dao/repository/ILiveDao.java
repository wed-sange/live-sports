package org.sports.admin.manage.dao.repository;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.sports.admin.manage.dao.entity.LiveDO;
import org.sports.admin.manage.dao.enums.LiveStatus;
import org.sports.admin.manage.dao.enums.MatchType;
import org.sports.admin.manage.dao.req.LiveIdTypeRequest;
import org.sports.admin.manage.dao.req.LivePageRequest;
import org.sports.admin.manage.dao.req.LiveSearchRequest;
import org.sports.springboot.starter.convention.page.PageResponse;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface ILiveDao {
    void insert(LiveDO liveDO);

    void delete(Long id);

    LiveDO selectById(Long id);

    List<LiveDO> selectByIds(Collection<Long> ids);

    LiveDO selectByUserId(Long userId, Long id);

    LiveDO selectByUserId(Long userId, LiveStatus liveStatus);

    LiveDO selectByUserId(Long userId, MatchType matchType, LiveStatus liveStatus);

    List<LiveDO> topHot(MatchType matchType, LiveStatus liveStatus, Collection<Integer> competitionIds, Integer limit);

    List<LiveDO> selectFirstOpenLive(MatchType matchType, LiveStatus liveStatus, Collection<Integer> competitionIds, Integer limit);

    LiveDO selectByUserId(Long userId, Integer matchId, MatchType matchType, LiveStatus liveStatus);

    Map<Integer, List<LiveDO>> selectByUserId(Long userId, Collection<Integer> matchIds, MatchType matchType, LiveStatus liveStatus);

    List<LiveDO> selectByUserId(Long userId);

    void updateById(LiveDO liveDO);

    Page<LiveDO> search(LiveSearchRequest request, LiveStatus liveStatus);

    /**
     * 根据比赛ID查询正在直播的比赛
     *
     * @param matchId    比赛 ID
     * @param matchType  比赛类型
     * @param liveStatus 直播状态
     * @return 正在直播的比赛
     */
    List<LiveDO> search(Integer matchId, MatchType matchType, LiveStatus liveStatus);

    Map<MatchType, Map<Integer, List<LiveDO>>> search(List<LiveIdTypeRequest> requests);

    /**
     * 更新热度值
     */
    void saveHotValue(Long id, Long hotValue);

    /**
     * 获取所有正在直播中的live
     *
     * @return
     */
    List<LiveDO> getAllLiveing(List<Long> liveUserIds);


    /**
     * 根据状态、主播昵称、类型分页查询直播列表
     *
     * @param pageRequest
     * @param userIdList 满足昵称过滤用户
     * @return
     */
    PageResponse<LiveDO> getLivePage(LivePageRequest pageRequest, List<Long> userIdList);

    /**
     * 查询主播在一段时间内的直播次数
     *
     * @param userId
     * @param startTime
     * @param endTime
     * @return
     */
    long getLiveTimes(Long userId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 查询主播在一段时间的直播时长（小时）
     *
     * @param userId
     * @param startTime
     * @param endTime
     * @return
     */
    long getLiveHours(Long userId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 根据直播用户ID批量查询正在直播的比赛
     * @param userIds
     * @return
     */
    List<LiveDO> getLivingByUserIds(List<Long> userIds);

    List<LiveDO> selectByMatchId(Integer matchId, MatchType matchType, LiveStatus liveStatus, Collection<Long> exceptId);
}
