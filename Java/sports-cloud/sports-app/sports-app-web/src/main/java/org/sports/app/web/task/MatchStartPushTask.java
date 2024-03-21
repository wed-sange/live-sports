package org.sports.app.web.task;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.sports.admin.manage.dao.enums.MatchType;
import org.sports.admin.manage.service.enums.BasketballStatusType;
import org.sports.admin.manage.service.service.IPushMessageService;
import org.sports.admin.manage.service.service.SdkService;
import org.sports.admin.manage.service.vo.SdkMatchVo;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 直播详情信息同步任务
 */

@Slf4j
@Component
public class MatchStartPushTask {
    @Resource
    private SdkService sdkService;

    private final ExecutorService executor = Executors.newFixedThreadPool(2);

    @Resource
    private RedissonClient redissonClient;
    @Resource
    private IPushMessageService pushMessageService;
    /**
     * 将要进行的比赛推送，每5分钟处理一次
     */
     @Scheduled(cron = "0 1/5 * * * ? ")
    public void pushStartMatch() {
        RLock lock = redissonClient.getLock("sports:start-match-push:sync:lock");
        boolean res = false;
        try {
            res = lock.tryLock( 1,5, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(res) {
            log.info("推送将要开始比赛任务开始");
            List<SdkMatchVo> basketballList = null;
            List<SdkMatchVo> footballList = null;
            Future<List<SdkMatchVo>> bastketballFuture = executor.submit(() -> sdkService.getMatchList(null, LocalDate.now(ZoneOffset.UTC), LocalDate.now(ZoneOffset.UTC), MatchType.BASKETBALL, BasketballStatusType.getUpcomingStatus()));
            Future<List<SdkMatchVo>> footballFuture = executor.submit(() -> sdkService.getMatchList(null, LocalDate.now(ZoneOffset.UTC), LocalDate.now(ZoneOffset.UTC), MatchType.FOOTBALL, BasketballStatusType.getUpcomingStatus()));
            try {
                basketballList = bastketballFuture.get(5, TimeUnit.SECONDS);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
            try {
                footballList = footballFuture.get(5, TimeUnit.SECONDS);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
            if (!CollectionUtils.isEmpty(basketballList)) {
                //取5分钟之内要开赛的比赛
                List<SdkMatchVo> filterMatchList = basketballList.stream().filter(item -> (item.getMatchTime() - System.currentTimeMillis() / 1000) < 300).collect(Collectors.toList());
                if(!CollectionUtils.isEmpty(filterMatchList)){
                    pushMessageService.pushUpcomingMatch(filterMatchList, MatchType.BASKETBALL);
                }
            }
            if (!CollectionUtils.isEmpty(footballList)) {
                //取5分钟之内要开赛的比赛
                List<SdkMatchVo> filterMatchList = footballList.stream().filter(item -> (item.getMatchTime() - System.currentTimeMillis() / 1000) < 300).collect(Collectors.toList());
                if(!CollectionUtils.isEmpty(filterMatchList)){
                    pushMessageService.pushUpcomingMatch(filterMatchList, MatchType.FOOTBALL);
                }
            }
            log.info("推送将要开始比赛任务结束");
        } else {
            log.info("推送将要开始比赛任务其他设备执行");
        }
    }



}
