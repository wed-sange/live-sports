package org.sports.app.service.service;

import org.sports.admin.manage.dao.req.AppUserMsgPageRequest;
import org.sports.admin.manage.dao.req.ChatMessageVO;
import org.sports.springboot.starter.convention.page.PageResponse;

/**
 * APP用户消息处理入口
 */
public interface IMessageService {

    /**
     * 消息列表：消息页面分页查询
     * @param pageRequest
     * @param userId
     * @return
     */
    PageResponse<ChatMessageVO> queryMsgByPage(AppUserMsgPageRequest pageRequest, Long userId);

    /**
     * 根据主播ID，删除聊天记录
     * @param anchorId 主播ID
     * @param userId
     */
    void delRecordsByAnchorId(String anchorId, Long userId);

    /**
     * 清除消息红点提示
     * @param anchorId 主播ID
     * @param userId
     */
    void cancelUnread(String anchorId, Long userId);

    /**
     * 清除所有消息红点提示
     * @param userId
     */
    void cancelAllUnread(Long userId);

}
