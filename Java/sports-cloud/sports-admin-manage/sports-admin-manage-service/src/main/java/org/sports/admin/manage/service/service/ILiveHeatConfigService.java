package org.sports.admin.manage.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.sports.admin.manage.dao.entity.LiveHeatConfigDO;
import org.sports.admin.manage.service.vo.LiveHeatConfigVO;

/**
 * <p>
 * 直播间热度管理 服务类
 * </p>
 *

 * @since 2023-07-26
 */
public interface ILiveHeatConfigService extends IService<LiveHeatConfigDO> {

    /**
     * 获取直播间热度配置信息 缓存5分钟
     * @return
     */
    LiveHeatConfigVO getLiveHeatConfig();

    /**
     * 修改直播间热度配置信息
     * @param liveHeatConfigVO
     */
    void updateLiveHeatConfig(LiveHeatConfigVO liveHeatConfigVO);

}
