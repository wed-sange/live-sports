package org.sports.admin.manage.service.service;

import org.sports.admin.manage.dao.entity.FeedbackDO;
import org.sports.admin.manage.dao.req.*;
import org.sports.admin.manage.service.vo.FeedbackDetailByAdminVO;
import org.sports.admin.manage.service.vo.FeedbackDetailByUserVO;
import org.sports.admin.manage.service.vo.FeedbackListByAdminVO;
import org.sports.admin.manage.service.vo.FeedbackListByUserVO;
import org.sports.springboot.starter.convention.page.PageRequest;
import org.sports.springboot.starter.convention.page.PageResponse;


public interface IFeedbackService {

    void submit(Long userId,String username,FeedbackSubmitRequest request);

    void ignore(Long adminId,String adminName,FeedbackIgnoreRequest request);

    void reply(Long adminId,String adminName,FeedbackReplyRequest request);

    PageResponse<FeedbackListByAdminVO> listByAdmin(FeedbackListByAdminPageRequest request);

    FeedbackDetailByUserVO detailByUser(Long userId,Long id);

    FeedbackDetailByAdminVO detailByAdmin(Long id);

    PageResponse<FeedbackListByUserVO> pageByUser(Long userId,FeedbackListByUserRequest request);

    /**
     * 根据用户ID 获取最新已处理反馈内容
     * @param userId
     * @return
     */
    FeedbackDO getLastDealByUserId(Long userId);

    /**
     * 根据用户查询未读反馈通知数量
     * @param userId
     * @return
     */
    Long countNoReadFeedback(Long userId);

    /**
     * 反馈通知分页查询
     * 所有未读 变为 已读
     * @param pageRequest
     * @param userId
     * @return
     */
    PageResponse<FeedbackListByUserVO> queryFeedbackByPage(PageRequest pageRequest, Long userId);

    /**
     * 根据用户ID 清除所有未读反馈
     * @param userId
     */
    void cancelAllUnreadMsgByUserId(Long userId);

    /**
     * 根据用户ID 删除所有反馈记录
     * @param userId
     */
    void delRecordsByUserId(Long userId);
}
