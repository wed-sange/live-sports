package org.sports.admin.manage.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.sports.admin.manage.dao.entity.AppUserLiveShareDO;

import java.util.List;
import java.util.Map;

/**
 * app用户直播分享记录服务入口

 */
public interface IAppUserLiveShareService extends IService<AppUserLiveShareDO> {

    /**
     * 根据直播ids统计当前直播分享次数
     * @param liveIdList
     * @return
     */
    Map<String, Integer> getLiveShareByGroupIds(List<String> liveIdList);

}
