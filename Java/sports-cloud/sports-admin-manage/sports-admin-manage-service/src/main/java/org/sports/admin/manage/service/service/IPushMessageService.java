package org.sports.admin.manage.service.service;

import org.sports.admin.manage.dao.entity.LiveDO;
import org.sports.admin.manage.dao.enums.MatchType;
import org.sports.admin.manage.service.dto.MatchScoreDTO;
import org.sports.admin.manage.service.vo.SdkMatchVo;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 推送消息接口
 */
public interface IPushMessageService {
    /**
     * 禁言App用户
     * @param userId 用户ID
     * @param createTime 禁言时间
     * @param noticeId 通知ID
     * @param title 通知标题
     * @param reason 通知内容（原因）
     */
    void forbiddenAppUser(Long userId,String bizId, LocalDateTime createTime, Long noticeId, String title, String reason);

    /**
     * 解禁App用户
     * @param userId 用户ID
     * @param bizId 主播ID
     * @param createTime 解禁时间
     * @param noticeId 通知ID
     * @param title 通知标题
     * @param reason 通知内容（原因）
     */
    void unforbiddenAppUser(Long userId,String bizId, LocalDateTime createTime, Long noticeId, String title, String reason);

    /**
     * 关播
     *
     * @param liveId
     * @param anchorId
     * @param matchId
     * @param matchType
     */
    void closeLive(Long liveId, Long anchorId, Integer matchId, MatchType matchType);

    /**
     * 更新直播间播放地址
     * @param liveDO
     */
    void updateLiveUrl(LiveDO liveDO);

    /**
     * 反馈通知明细
     * @param feedbackId 反馈ID
     * @param userId 用户ID
     * @param createTime 反馈回答时间
     * @param noticeId 通知ID
     * @param title 通知标题
     * @param reason 通知内容（原因）
     */
    void feedbackReply(Long feedbackId,Long userId, LocalDateTime createTime, Long noticeId, String title, String reason);

    /**
     * 推送用户或者主播未读消息总数
     * @param anchorId
     * @param userId
     * @param identity
     */
    void pushUnreadMessageTotalCount(String anchorId, String userId, Integer identity);

    /**
     * 推送比赛实时分数
     * @param matchScoreList
     */
    void pushMatchRealScore(List<MatchScoreDTO> matchScoreList);

    /**
     * 推送主播更新
     */
    void pushLiveUserRefresh();

    /**
     * 推送新闻更新
     */
    void pushNewsRefresh();

    /**
     * 推送已完成比赛
     * @param finishedMatch
     * @param matchType
     */
    void pushFinishedMatch(List<MatchScoreDTO> finishedMatch, MatchType matchType);
    /**
     * 推送5分钟之内将要开赛的比赛
     * @param filterMatchList
     * @param matchType
     */
    void pushUpcomingMatch(List<SdkMatchVo> filterMatchList, MatchType matchType);

    /**
     * 开播
     * @param liveDO
     */
    void startLive(LiveDO liveDO);

    /**
     * 用户被踢出直播间
     * @param userId
     * @param liveId
     * @param createTime
     * @param noticeId
     * @param title
     * @param reason
     */
    void kickOutUser(Long userId, Long liveId ,LocalDateTime createTime, Long noticeId, String title, String reason);
}
