package org.sports.admin.manage.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.sports.admin.manage.dao.entity.ChatMessageDO;
import org.sports.admin.manage.dao.entity.MessageSlidePageReqDto;
import org.sports.admin.manage.dao.req.*;
import org.sports.admin.manage.service.vo.ChatMessageVO2;
import org.sports.admin.manage.service.vo.LivingUserVO;
import org.sports.springboot.starter.convention.page.PageRequest;
import org.sports.springboot.starter.convention.page.PageResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 群组用户信息 服务类
 * </p>
 *

 * @since 2023-07-26
 */
public interface IChatMessageService extends IService<ChatMessageDO> {

    /**
     * 统计新用户在直播间发送聊天次数
     * @param endTime 当前统计时间
     * @return
     */
    Map<Long, Integer> countUserChatMsgNumByNew(LocalDateTime endTime);

    /**
     * 根据时间区间段 统计用户在直播间发送聊天次数
     * @param beginTime 可为空
     * @param endTime 不可为空
     * @return
     */
    Map<Long, Integer> countUserChatMsgNumByLeave(LocalDateTime beginTime, LocalDateTime endTime);

    /**
     * 消息列表：消息页面分页查询
     * @param pageRequest
     * @param userId
     * @return
     */
    PageResponse<ChatMessageVO> queryMsgByPage(AppUserMsgPageRequest pageRequest, Long userId);


    /**
     * 查询用户 指定聊天来源 未读数量
     * @param anchorIdList
     * @param userId
     * @return
     */
    Map<String, Integer> countNoReadMsg(List<String>  anchorIdList, Long userId);



    /**
     * 根据主播ID，删除当前用户聊天记录
     * @param userId
     * @param anchorId
     */
    void delRecordsByAnchorId(Long userId, String anchorId);

    /**
     * 根据用户ID 清除发送主播Id下所有红点
     * @param userId
     * @param anchorId
     */
    void cancelAllUnreadMsgByUserId(Long userId, String anchorId);

    /**
     * 根据用户ID 清除所有红点
     * @param userId
     */
    void cancelAllUnreadMsgByUserId(Long userId);

    /**
     * 聊天私聊信息分页查询
     */
    PageResponse<ChatMessageVO2> getPrivateMsgPage(ChatMessagePageRequest req);

    /**
     * 运营查看所有私聊主播信息
     * @param pageRequest
     */
    PageResponse<LiveChatMessageVO> queryPrivateLivePage(PageRequest pageRequest);

    /**
     * 根据主播id查看所有当前主播私聊信息
     * @param liveId
     */
    PageResponse<LiveChatMessageVO> queryLivePrivateById(Long liveId);

    /**
     * 运营查看所有当前在线主播信息
     */
    List<LivingUserVO> queryPublicLivePage(Long liveId);


    /**
     * 根据主播ID查询是否在线直播
     * 保持queryPublicLivePage返回结构一样
     */
    List<LivingUserVO> queryLiveOnlineById(Long liveId);

    /**
     * 根据主播ID查询所有私聊用户列表
     *
     * @param loginUserId
     * @param pageRequest
     */
    PageResponse<LiveChatMessageVO> queryPrivateUserByLivePage(Long loginUserId, UserByLivePageRequest pageRequest);

    List<ChatMessageDO> getHistoryMessage(MessageSlidePageReqDto messageSlidePageReqDto, Boolean aFalse);

    /**
     * 根据直播ids统计当前在线直播用户消息发送次数
     * @param liveIdList
     * @return
     */
    Map<String, Integer> getLiveMsgSendByGroupIds(List<String>  liveIdList);

    /**
     * 主播或者助理已读消息
     * @param readRequest
     */
    void anchorReadMessage(MessageReadRequest readRequest);


    List<ChatMessageDO> privateLatestMessage(MessageQueryRequest messageQueryRequest);

    ChatMessageDO privateEarliestUnread(MessageQueryRequest2 messageQueryRequest);
}
