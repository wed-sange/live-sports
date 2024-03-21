package org.sports.admin.manage.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.sports.admin.manage.dao.entity.AppNoticeConfigDO;
import org.sports.admin.manage.dao.mapper.AppNoticeConfigMapper;
import org.sports.admin.manage.service.enums.YnEnum;
import org.sports.admin.manage.service.service.AppNoticeConfigService;
import org.sports.admin.manage.service.vo.AppNoticeConfigVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

/**
 * APP用户通知设置服务层实现

 */
@Service
public class AppNoticeConfigServiceImpl extends ServiceImpl<AppNoticeConfigMapper, AppNoticeConfigDO> implements AppNoticeConfigService {
    @Override
    public AppNoticeConfigVO getConfigInfo(Long userId) {
        AppNoticeConfigDO appNoticeConfigDO = this.getOne(new LambdaUpdateWrapper<AppNoticeConfigDO>().eq(AppNoticeConfigDO::getUserId, userId));
        if(appNoticeConfigDO == null){
            appNoticeConfigDO = new AppNoticeConfigDO();
            appNoticeConfigDO.setUserId(userId);
            appNoticeConfigDO.setYnFollowMatch(YnEnum.ONE.getValue());
            appNoticeConfigDO.setYnLiveOpen(YnEnum.ONE.getValue());
            appNoticeConfigDO.setUpdateTime(LocalDateTime.now(ZoneOffset.UTC));
            this.save(appNoticeConfigDO);
        }
        AppNoticeConfigVO appNoticeConfigVO = new AppNoticeConfigVO();
        BeanUtils.copyProperties(appNoticeConfigDO, appNoticeConfigVO);
        return appNoticeConfigVO;
    }

    @Override
    public List<AppNoticeConfigDO> getNoticeConfig(List<Long> userIds) {
        return this.list(new LambdaUpdateWrapper<AppNoticeConfigDO>().in(AppNoticeConfigDO::getUserId, userIds));
    }
}
