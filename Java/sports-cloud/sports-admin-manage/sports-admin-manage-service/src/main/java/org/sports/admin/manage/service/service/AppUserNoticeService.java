package org.sports.admin.manage.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.sports.admin.manage.dao.entity.AppUserNoticeDO;
import org.sports.admin.manage.service.vo.AppUserNoticeVO;
import org.sports.springboot.starter.convention.page.PageRequest;
import org.sports.springboot.starter.convention.page.PageResponse;

/**
 * APP用户通知服务层入口

 */
public interface AppUserNoticeService extends IService<AppUserNoticeDO> {

    /**
     * 根据用户ID 获取当前系统通知信息
     * @param pageRequest
     * @param userId
     * @return
     */
    PageResponse<AppUserNoticeVO>  getNoticeInfosByUserId(PageRequest pageRequest, Long userId);

    /**
     * 根据用户ID 获取最新一条数据
     * @param userId
     * @return
     */
    AppUserNoticeDO getLastInfoByUserId(Long userId);

    /**
     * 根据用户ID 统计未读数量
     * @param userId
     * @return
     */
    Long countNoRead(Long userId);

    /**
     * 根据用户ID 所有通知已读
     * @param userId
     */
    void readAllNoticeInfo(Long userId);

    void delNoticeByUserId(Long userId);
}
