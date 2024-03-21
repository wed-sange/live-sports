package org.sports.admin.manage.web.job;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.sports.admin.manage.service.constant.CacheConstant;
import org.sports.admin.manage.service.enums.UserGrowthTypeEnum;
import org.sports.admin.manage.service.rycore.utils.StringUtils;
import org.sports.admin.manage.service.service.IAppUserGrowthService;
import org.sports.admin.manage.service.service.IChatMessageService;
import org.sports.admin.manage.service.vo.AppUserGrowthVO;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class UserGrowthTask {

    @Resource
    private IAppUserGrowthService appUserGrowthService;
    @Resource
    private IChatMessageService chatMessageService;
    @Resource
    private RedissonClient redissonClient;

    /**
     * 处理用户观看直播 添加对应成长值
     * 每一分钟执行一次
     */
    @Scheduled(initialDelay = 10000, fixedRate = 1000 * 60)
    void dealWatchChatTask() {
        String key = "job:dealWatchChatTask";
        RLock lock = redissonClient.getLock(key);
        if (lock.tryLock()) {
            try {
                boolean isDeal = true;
                RBlockingQueue<String> blockingQueue = redissonClient.getBlockingQueue(CacheConstant.UserWatchTimePer);
                while (isDeal) {
                    String jsonStr = blockingQueue.poll();
                    if (Strings.isEmpty(jsonStr)) {
                        isDeal = false;
                    } else {
                        try {
                            String[] strs = jsonStr.split(",");
                            if (strs.length == 2) {
                                AppUserGrowthVO appUserGrowthVO = new AppUserGrowthVO();
                                appUserGrowthVO.setType(UserGrowthTypeEnum.WATCH_LIVE.getType());
                                String userId = strs[0];
                                if (StringUtils.isNumeric(userId)) {
                                    appUserGrowthVO.setUserId(Long.parseLong(userId));
                                    int eventNumber = Integer.parseInt(strs[1]) / 10 / 60;
                                    if (eventNumber > 0) {
                                        appUserGrowthVO.setEventNumber(eventNumber);
                                        appUserGrowthService.addUserGrowthSingle(appUserGrowthVO);
                                    }
                                }
                            }
                        } catch (Exception e) {
                            log.error("处理用户观看直播获取成长值消费异常;{}", jsonStr, e);
                            blockingQueue.putAsync(jsonStr);
                        }
                    }
                }
            } finally {
                lock.unlock();
            }
        }
    }

    /**
     * 批量处理用户在直播聊天互动 每互动一次 添加对应成长值
     * 每一个小时执行一次
     */
    @Scheduled(initialDelay = 10000, fixedRate = 1000 * 60 * 60)
    void dealLiveChatTask() {
        String key = "job:dealLiveChatTask";
        RLock lock = redissonClient.getLock(key);
        if (lock.tryLock()) {
            try {
                //第一步 获取上一次最后处理时间
                LocalDateTime lastEndTime = appUserGrowthService.getLastDealEndTimeByLiveChat();
                LocalDateTime nowTime = LocalDateTime.now(ZoneOffset.UTC);
                if (lastEndTime != null) {
                    //第二步 继续用户处理【上一次处理时间到本次处理时间】
                    Map<Long, Integer> numCountMap = chatMessageService.countUserChatMsgNumByLeave(lastEndTime, nowTime);
                    if (!CollectionUtils.isEmpty(numCountMap)) {
                        log.info("【批量处理用户在直播聊天互动开始】当前处理数量：" + numCountMap.size());
                        dealUserChatCount(numCountMap, lastEndTime, nowTime);
                        log.info("【批量处理用户在直播聊天互动开始】当前处理结束");
                    }
                }
                //第三步  处理从未处理过的用户【即新用户】
                Map<Long, Integer> numCountMap = chatMessageService.countUserChatMsgNumByNew(nowTime);
                if (!CollectionUtils.isEmpty(numCountMap)) {
                    log.info("【批量处理用户在直播聊天互动开始】历史异常处理数量：" + numCountMap.size());
                    dealUserChatCount(numCountMap, null, nowTime);
                    log.info("【批量处理用户在直播聊天互动开始】历史异常处理结束");
                }

            } finally {
                lock.unlock();
            }
        }
    }

    /**
     * 用户成长值信息组装处理
     *
     * @param numCountMap
     * @param beginTime
     * @param endTime
     */
    private void dealUserChatCount(Map<Long, Integer> numCountMap, LocalDateTime beginTime, LocalDateTime endTime) {
        List<AppUserGrowthVO> userGrowthList = new ArrayList<>();
        AppUserGrowthVO userGrowthVO;
        for (Long userId : numCountMap.keySet()) {
            userGrowthVO = new AppUserGrowthVO();
            userGrowthVO.setType(UserGrowthTypeEnum.LIVE_CHAT.getType());
            userGrowthVO.setUserId(userId);
            userGrowthVO.setEventNumber(numCountMap.get(userId));
            userGrowthVO.setBeginTime(beginTime);
            userGrowthVO.setEndTime(endTime);
            userGrowthList.add(userGrowthVO);
        }
        appUserGrowthService.addUserGrowthBatch(userGrowthList);
    }
}
