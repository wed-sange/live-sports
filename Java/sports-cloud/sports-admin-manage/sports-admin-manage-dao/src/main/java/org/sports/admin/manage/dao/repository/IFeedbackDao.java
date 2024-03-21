package org.sports.admin.manage.dao.repository;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.sports.admin.manage.dao.entity.FeedbackDO;
import org.sports.admin.manage.dao.req.FeedbackListByAdminPageRequest;
import org.sports.admin.manage.dao.req.FeedbackListByUserRequest;
import org.sports.springboot.starter.convention.page.PageRequest;
import org.sports.springboot.starter.convention.page.PageResponse;


public interface IFeedbackDao {
    Page<FeedbackDO> page(Long userId, FeedbackListByUserRequest pageRequest);

    PageResponse<FeedbackDO> page(FeedbackListByAdminPageRequest pageRequest);

    FeedbackDO findById(Long id);

    void insert(FeedbackDO bean);

    void updateById(FeedbackDO bean);

    void delete(FeedbackDO bean);

    void deleteById(Long id);

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
     * 根据用户当天反馈数量
     * @param userId 用户ID
     * @return 数量
     */
    Long countFeedbackToday(Long userId);

    /**
     * 反馈通知分页查询
     * 所有未读 变为 已读
     * @param pageRequest
     * @param userId
     * @return
     */
    PageResponse<FeedbackDO> queryFeedbackByPage(PageRequest pageRequest, Long userId);

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
