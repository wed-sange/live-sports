package org.sports.app.web.task;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.sports.app.service.service.IBasketballLiveDetailService;
import org.sports.app.service.service.IFootballLiveDetailService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 直播详情信息同步任务
 */

@Slf4j
@Component
public class LiveDetailSyncTask {
    @Resource
    private IFootballLiveDetailService footballLiveDetailService;
    @Resource
    private IBasketballLiveDetailService basketballLiveDetailService;
    @Resource
    private RedissonClient redissonClient;


    /**
     * 同步足球比赛线路，暂定每十分钟分钟更新
     */
    @Scheduled(cron = "0 */5 * * * ?")
    public void syncFootballVideoList() {
        RLock lock = redissonClient.getLock("sports:football:sync:video:lock");
        boolean res = false;
        try {
            res = lock.tryLock( 1,10, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (res) {
            log.info("同步足球比赛线路开始");
            footballLiveDetailService.loadMatchVideoLine();
            log.info("同步足球比赛线路结束");
        } else {
            log.debug("同步足球比赛线路其他实例已同步");
        }
    }

    /**
     * 同步足球实时数据，暂定每分钟更新
     */
    @Scheduled(cron = "0/30 * * * * ?")
    public void syncFootballData() {
        RLock lock = redissonClient.getLock("sports:football:sync:lock");
        boolean res = false;
        try {
            res = lock.tryLock( 1,30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (res) {
            log.info("同步足球比赛实时统计数据开始");
            footballLiveDetailService.loadMatchStatsCache();
            log.info("同步足球比赛实时统计数据结束");
        } else {
            log.debug("足球比赛其他实例已同步");
        }
    }


    /**
     * 同步篮球实时数据，暂定每分钟更新
     */
    @Scheduled(cron = "0/10 * * * * ?")
    public void syncBasketballData() {
        RLock lock = redissonClient.getLock("sports:basketball:sync:lock");
        boolean res = false;
        try {
            res = lock.tryLock( 1,10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.debug("篮球比赛其他实例已同步");
        }
        if (res) {
            log.info("同步篮球比赛实时统计数据开始");
            basketballLiveDetailService.loadMatchStatsCache();
            log.info("同步篮球比赛实时统计数据结束");
        } else {
            log.debug("篮球比赛其他实例已同步");
        }


    }
}
