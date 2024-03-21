package org.sports.admin.manage.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.sports.admin.manage.dao.entity.LiveOpeninfoDO;
import org.sports.admin.manage.service.vo.LiveOpeninfoVO;

import java.util.List;

/**
 * 直播开播信息服务入口
 *

 * @since 2023-07-28
 */
public interface ILiveOpeninfoService extends IService<LiveOpeninfoDO> {

    /**
     * 根据直播用户ID查询使用中的开播信息配置
     * @param liveUserId
     * @return
     */
    LiveOpeninfoVO getUsedOpenInfoByLiveUserId(Long liveUserId);

    /**
     * 根据主播ID和开播信息~ID查询开播明细
     * @param openInfoId
     * @param anchorId
     * @return
     */
    LiveOpeninfoVO getOpenInfoById(Long openInfoId, Long anchorId);

    /**
     * 根据主播ID查询已配置几条开播信息
     * @param anchorId
     * @return
     */
    long countByAnchorId(Long anchorId);

    void delOpenInfoById(Long openInfoId, Long anchorId);

    List<LiveOpeninfoVO> getOpenInfoListByAnchorId(Long anchorId);

    void setUsedOpenInfo(Long openInfoId, Long anchorId);

    LiveOpeninfoVO getUsedOpenInfo(Long anchorId);

}
