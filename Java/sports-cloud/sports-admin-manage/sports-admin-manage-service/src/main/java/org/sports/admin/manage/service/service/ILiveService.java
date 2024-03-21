package org.sports.admin.manage.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.google.common.collect.Table;
import org.sports.admin.manage.dao.entity.LiveDO;
import org.sports.admin.manage.dao.enums.LiveStatus;
import org.sports.admin.manage.dao.enums.MatchType;
import org.sports.admin.manage.dao.req.*;
import org.sports.admin.manage.service.vo.*;
import org.sports.springboot.starter.convention.page.PageResponse;

import java.util.Collection;
import java.util.List;

public interface ILiveService extends IService<LiveDO> {

    LiveDO liveOpen(LiveOpenRequest request);

    void liveClose(Long userId);

    void liveCloseByAdmin(Long id);

    void updateAddress(Long checkUserId, LiveUpdateAddressRequest request);

    /**
     * 获取正在直播中的live
     *
     * @return
     */
    List<LiveDO> getAllLiveing(List<Long> liveUserIds);

    /**
     * 根据主播ID批量查询正在直播的直播间
     *
     * @param userIds
     * @return
     */
    List<LiveVo> getLivingByUserIds(List<Long> userIds);


    List<LiveVo> search(Integer matchId, MatchType matchType, LiveStatus liveStatus);
    /**
     * 批量更新live热度值
     *
     * @param liveDoList
     */
    void updateLiveHotValueBatch(List<LiveDO> liveDoList);

    List<LiveVo> findByIds(Collection<Long> ids);

    /**
     * 根据用户ID查询最新的直播信息
     *
     * @param userIds 用户ID
     * @return 最新的直播信息
     */
    List<LiveVo> findLatestByUserIds(Collection<Long> userIds);

    /**
     * 根据比赛ID和比赛类型批量查询 直播信息
     */
    Table<MatchType, Integer, List<LiveIdTypeVo>> getByIdsAndType(List<LiveIdTypeRequest> requests);

    PageResponse<LiveVo> getLivePage(LivePageRequest req);

    /**
     * 根据主播和统计周期类型进行统计
     *
     * @param userId
     * @param type
     * @return
     */
    LiveStatisticsVO getLiveStatistics(Long userId, Integer type);

    PageResponse<HomeLivingVo> living(LiveSearchRequest request);

    /**
     * 根据条件查询正在直播并且直播热度第一的热门比赛
     *
     * @param matchType
     * @param liveStatus
     * @param competitionIds
     * @param top
     * @return
     */
    List<LiveVo> getHotMatchLive(MatchType matchType, LiveStatus liveStatus, Collection<Integer> competitionIds, Integer top);

    /**
     * 根据直播ID查询直播间详情
     *
     * @param id 直播间ID
     * @return 详情
     */
    HomeLivingInfoVo livingInfo(Long id);

    List<LiveVo> selectByMatchId(Integer matchId, MatchType matchType, LiveStatus liveStatus, Collection<Long> exceptId);
}
