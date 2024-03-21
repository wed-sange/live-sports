package org.sports.chat.service.util;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.sports.admin.manage.dao.entity.LiveUserDO;
import org.sports.admin.manage.dao.enums.IdentityType;
import org.sports.admin.manage.dao.mapper.LiveUserMapper;
import org.sports.admin.manage.service.constant.RedisCacheConstant;
import org.sports.admin.manage.service.enums.YnEnum;
import org.sports.chat.service.constant.CacheConstant;
import org.sports.springboot.starter.satoken.enums.LiveIdentityTypeEnum;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class LiveGroupCache implements InitializingBean {

//    public static final Map<String, List<String>> ANCHORSEND_IDS = new HashMap<>();
//    public static final Set<String> OPERATOR_IDS = new HashSet<>();
//    private static final ReentrantReadWriteLock LOCK = new ReentrantReadWriteLock();
//    public static final ReentrantReadWriteLock.ReadLock READ_LOCK = LOCK.readLock();
//    public static final ReentrantReadWriteLock.WriteLock WRITE_LOCK = LOCK.writeLock();

    @Resource
    private LiveUserMapper liveUserMapper;

    @Resource
    private RedissonClient redissonClient;

    @Override
    public void afterPropertiesSet() throws Exception {
        refreshCache();
    }

    /**
     * 刷新关联关系：  主播 1->n 助手；   助理1->n 主播 ; 助手 1->1 主播
     */
    public void refreshCache() {
        RReadWriteLock readWriteLock = redissonClient.getReadWriteLock(CacheConstant.CHAT_LIVE_GROUP_USER_SESSION);
        RLock rLock = readWriteLock.writeLock();
        rLock.lock(10, TimeUnit.SECONDS);
        try {
            /**
             * 设置主播对应的助手关系和主播关联的超级助理关系
             */
            List<String> anchorIds = liveUserMapper.getUserIdsByIdentityType(Integer.valueOf(LiveIdentityTypeEnum.ANCHOR.getValue()));
            anchorIds.forEach(anchorId -> {
                /**
                 * 主播对应的助理
                 */
                List<LiveUserDO> liveUserDOS = liveUserMapper.selectList(Wrappers.lambdaQuery(LiveUserDO.class)
                        .eq(LiveUserDO::getIdentityType, IdentityType.OPERATOR.getCode()).like(LiveUserDO::getPossessLive, anchorId));
                List<String> operateIds = Lists.newArrayList();
                operateIds.add(anchorId);//当前主播也加上
                if (CollectionUtils.isNotEmpty(liveUserDOS)) {
                    /**
                     * 需要剔除掉正在封禁和已经注销的助理
                     */
                    operateIds.addAll(liveUserDOS.stream().filter(item -> !YnEnum.ONE.getValue().equals(item.getYnForbidden()) && !YnEnum.ONE.getValue().equals(item.getYnCancel())).map(item -> String.valueOf(item.getId())).collect(Collectors.toList()));

                }
                redissonClient.getBucket(CacheConstant.CHAT_LIVE_GROUP_USER_SESSION + CacheConstant.ANCHOR + anchorId).set(operateIds);
            });
            /**
             * 刷新助理缓存
             */
            List<LiveUserDO> liveUserDOS = liveUserMapper.selectList(Wrappers.lambdaQuery(LiveUserDO.class)
                    .eq(LiveUserDO::getIdentityType, IdentityType.OPERATOR.getCode()));
            /**
             * 设置已经封禁的和已经注销的助理
             */
            liveUserDOS.forEach(item -> {
                if (YnEnum.ZERO.getValue().equals(item.getYnForbidden()) && YnEnum.ZERO.getValue().equals(item.getYnCancel())) {//未封禁或者未注销
                    redissonClient.getSet(RedisCacheConstant.LIVE_USER_FORBIDDEN).remove(String.valueOf(item.getId()));
                } else {
                    redissonClient.getSet(RedisCacheConstant.LIVE_USER_FORBIDDEN).add(String.valueOf(item.getId()));
                }
            });

        } finally {
            rLock.unlock();
        }
    }
}
