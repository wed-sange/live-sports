package org.sports.admin.manage.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.sports.admin.manage.dao.entity.AppNoticeConfigDO;
import org.sports.admin.manage.service.vo.AppNoticeConfigVO;

import java.util.List;

/**
 * APP用户通知设置服务层入口

 */
public interface AppNoticeConfigService extends IService<AppNoticeConfigDO> {

    /**
     * 根据用户ID获取通知设置
     * @param userId
     * @return
     */
    AppNoticeConfigVO getConfigInfo(Long userId);



    /**
     * 批量用户ID获取通知设置
     * @param userIds
     * @return
     */
    List<AppNoticeConfigDO> getNoticeConfig(List<Long> userIds);

}
