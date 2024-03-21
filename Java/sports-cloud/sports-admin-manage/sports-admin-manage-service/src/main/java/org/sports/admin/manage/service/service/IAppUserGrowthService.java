package org.sports.admin.manage.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.sports.admin.manage.dao.entity.AppUserGrowthDO;
import org.sports.admin.manage.service.vo.AppUserGrowthVO;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * APP用户成长值信息 服务类
 * </p>
 *

 * @since 2023-07-26
 */
public interface IAppUserGrowthService extends IService<AppUserGrowthDO> {

    /**
     * 批量添加用户成长值 一次尽量不要超过100条
     * @param userGrowthList
     */
    void addUserGrowthBatch(List<AppUserGrowthVO> userGrowthList);

    /**
     * 单次添加用户成长值
     * @param appUserGrowthVO
     */
    void addUserGrowthSingle(AppUserGrowthVO appUserGrowthVO);

    /**
     * 获取播聊天互动上次处理截止时间
     * @return
     */
    LocalDateTime getLastDealEndTimeByLiveChat();

}
