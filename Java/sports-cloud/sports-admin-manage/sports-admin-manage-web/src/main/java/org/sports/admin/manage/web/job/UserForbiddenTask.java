package org.sports.admin.manage.web.job;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.sports.admin.manage.dao.entity.AppUserDO;
import org.sports.admin.manage.dao.entity.LiveUserDO;
import org.sports.admin.manage.dao.enums.ForbiddenType;
import org.sports.admin.manage.service.service.IAppUserService;
import org.sports.admin.manage.service.service.ILiveUserService;
import org.sports.admin.manage.service.vo.AppUserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Component
@Slf4j
public class UserForbiddenTask {

    @Resource
    private IAppUserService appUserService;
    @Resource
    private ILiveUserService liveUserService;
    @Resource
    private RedissonClient redissonClient;

    /**
     * APP用户禁言到期自动解禁
     * 每一分钟执行一次
     */
    @Scheduled(initialDelay = 10000, fixedRate = 1000 * 60)
    void dealAppUserFreeTask() {
        String key = "job:dealAppUserFreeTask";
        RLock lock = redissonClient.getLock(key);
        if(lock.tryLock()){
            try {
                LocalDateTime nowTime = LocalDateTime.now(ZoneOffset.UTC);
                List<AppUserDO> appUserDOList = appUserService.list(new LambdaQueryWrapper<AppUserDO>()
                        .eq(AppUserDO::getYnForbidden, ForbiddenType.COMMON_FORBIDDEN.getCode())
                        .le(AppUserDO::getForbiddenTime, nowTime));
                if (CollectionUtils.isEmpty(appUserDOList)) {
                    return;
                }
                AppUserVO appUserVO;
                for (AppUserDO appUserDO : appUserDOList) {
                    try {
                        appUserVO = new AppUserVO();
                        BeanUtils.copyProperties(appUserDO, appUserVO);
                        appUserService.unForbiddenUser(appUserVO);
                    } catch (Exception e) {
                        log.error("APP用户{}到期自动解禁失败", appUserDO.getId(), e);
                    }
                }
            }finally{
                lock.unlock();
            }
        }
    }


    /**
     * LIVE用户禁言到期自动解禁
     * 每5分钟执行一次
     */
    @Scheduled(initialDelay = 10000, fixedRate = 1000 * 60 * 5)
    void dealLiveUserFreeTask() {
        String key = "job:dealLiveUserFreeTask";
        RLock lock = redissonClient.getLock(key);
        if(lock.tryLock()){
            try {
                LocalDateTime nowTime = LocalDateTime.now(ZoneOffset.UTC);
                List<LiveUserDO> liveUserDOList = liveUserService.list(new LambdaQueryWrapper<LiveUserDO>()
                        .select(LiveUserDO::getId)
                        .eq(LiveUserDO::getYnForbidden, ForbiddenType.COMMON_FORBIDDEN.getCode())
                        .le(LiveUserDO::getForbiddenTime, nowTime));
                if (CollectionUtils.isEmpty(liveUserDOList)) {
                    return;
                }
                for (LiveUserDO liveUserDO : liveUserDOList) {
                    try {
                        liveUserService.unForbiddenLiveUser(liveUserDO.getId());
                    } catch (Exception e) {
                        log.error("直播用户{}到期自动解禁失败", liveUserDO.getId(), e);
                    }
                }
            }finally{
                lock.unlock();
            }
        }
    }
}
