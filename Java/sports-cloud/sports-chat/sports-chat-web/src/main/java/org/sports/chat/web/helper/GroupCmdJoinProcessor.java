package org.sports.chat.web.helper;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.jim.core.ImChannelContext;
import org.jim.core.packets.*;
import org.jim.server.processor.group.GroupCmdProcessor;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.sports.admin.manage.dao.entity.AppUserDO;
import org.sports.admin.manage.dao.entity.ChatGroupUserDO;
import org.sports.admin.manage.dao.entity.LiveUserDO;
import org.sports.admin.manage.dao.mapper.AppUserMapper;
import org.sports.admin.manage.dao.mapper.ChatGroupUserMapper;
import org.sports.admin.manage.dao.mapper.LiveUserMapper;
import org.sports.admin.manage.service.constant.RedisCacheConstant;
import org.sports.admin.manage.service.enums.YnEnum;
import org.sports.chat.service.constant.CacheConstant;
import org.sports.chat.service.constant.RedissonPublishQueueConstant;
import org.sports.chat.service.entity.GroupDO;
import org.sports.chat.service.mapper.ChatGroupMapper;
import org.sports.chat.service.util.LRUCache;
import org.sports.springboot.starter.cache.DistributedCache;
import org.sports.springboot.starter.cache.toolkit.CacheUtil;
import org.sports.springboot.starter.common.enums.DelEnum;
import org.sports.springboot.starter.satoken.enums.UserChannelEnum;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
public class GroupCmdJoinProcessor implements GroupCmdProcessor {

    private final ChatGroupUserMapper groupUserMapper = SpringUtil.getBean(ChatGroupUserMapper.class);
    private final LiveUserMapper liveUserMapper = SpringUtil.getBean(LiveUserMapper.class);
    private final AppUserMapper appUserMapper = SpringUtil.getBean(AppUserMapper.class);
    private final RedissonClient redissonClient = SpringUtil.getBean(RedissonClient.class);

    private final ChatGroupMapper chatGroupMapper = SpringUtil.getBean(ChatGroupMapper.class);
    private final RedisTemplate redisTemplate = SpringUtil.getBean("redisTemplate", RedisTemplate.class);

    private final DistributedCache distributedCache = SpringUtil.getBean(DistributedCache.class);
    // 缓存，采用userId + _ + groupId的方式 存值。如：1321_100;
    private static final LRUCache<String, ChatGroupUserDO> USER_LRU_CACHE = new LRUCache<>(100000);

    @Override
    public JoinGroupRespBody join(Group joinGroup, ImChannelContext imChannelContext) {
        if (StringUtils.isBlank(joinGroup.getUserId()) || "undefined".equals(joinGroup.getUserId())) {
            return JoinGroupRespBody.success();
        }

        String groupId = joinGroup.getGroupId();
        String userId = joinGroup.getUserId();
        /**
         * 查看用户被踢出直播间时间是否过期
         */
        //通过直播间ID查询主播ID
        String anchorId = (String) redissonClient.getMap(RedisCacheConstant.LIVEID_ANCHORID_MAP).get(groupId);
        if (Strings.isNotBlank(anchorId)) {
            String cacheKey = CacheUtil.buildKey(org.sports.admin.manage.service.constant.CacheConstant.KICK_OUT_USER_KEY, userId, anchorId);
            if (Objects.nonNull(distributedCache.get(cacheKey))) {
                return JoinGroupRespBody.failed();
            }
        }

        // 存在未创建群聊的情况，这里要先校验群聊是否存在,不存在则要先创建群聊
        RSet<Object> set = redissonClient.getSet(CacheConstant.CHAT_GROUPIDS);
        if (!set.contains(groupId)) {
            GroupDO groupDO = new GroupDO();
            groupDO.setGroupId(groupId);
            try {
                // 数据如果偶发不一致直接捕捉报错不再添加
                chatGroupMapper.insert(groupDO);
            } catch (Exception ignored) {
            }
            // 使用set确保幂等性，重复添加不影响
            set.add(groupId);
        }
        // 看用户是否加入过群聊：Y-直接修改状态 N-创建一个新的
        // 先走缓存查看一波
        LocalDateTime now = LocalDateTime.now(Clock.systemUTC());
        ChatGroupUserDO userDO = USER_LRU_CACHE.get(userId + "_" + groupId);
        LambdaQueryWrapper<ChatGroupUserDO> wrapper = Wrappers.lambdaQuery(ChatGroupUserDO.class).eq(ChatGroupUserDO::getUserId, userId).eq(ChatGroupUserDO::getGroupId, groupId).eq(ChatGroupUserDO::getDelFlag, DelEnum.NORMAL.code());
        // 缓存中有，直接缓存
        if (ObjectUtils.isNotNull(userDO)) {
            userDO.setUpdateTime(now);
            userDO.setCreateTime(now);
            userDO.setStatus(UserStatusType.ONLINE.getNumber());
            userDO.setLeaveStatus(YnEnum.ZERO.getValue());
            groupUserMapper.update(userDO, wrapper);
        } else {
            // 缓存没有，库中查询
            userDO = groupUserMapper.selectOne(wrapper);
            if (ObjectUtils.isNull(userDO)) {
                // 库中没有，需要创建
                ChatGroupUserDO aDo = new ChatGroupUserDO();
                aDo.setStatus(UserStatusType.ONLINE.getNumber());
                aDo.setGroupId(groupId);
                aDo.setUserId(userId);
                // 获取头像及昵称等缓存数据
                Object o = redisTemplate.opsForValue().get("user:" + userId + ":info");
                if (o != null) {
                    User user = JSONObject.parseObject(JSONObject.toJSONString(o), User.class);
                    aDo.setNick(user.getNick());
                    aDo.setAvatar(user.getAvatar());
                } else {
                    String loginType = joinGroup.getLoginType();
                    // 直播用户
                    if (UserChannelEnum.LIVE.getFlag().equals(loginType)) {
                        LiveUserDO liveUserDO = liveUserMapper.selectById(userId);
                        if (Objects.nonNull(liveUserDO)) {
                            aDo.setNick(liveUserDO.getNickName());
                            aDo.setAvatar(liveUserDO.getHead());
                        }
                    } else if (UserChannelEnum.APP.getFlag().equals(loginType)) {
                        // APP用户
                        AppUserDO appUserDO = appUserMapper.selectById(userId);
                        if (Objects.nonNull(appUserDO)) {
                            aDo.setNick(appUserDO.getName());
                            aDo.setAvatar(appUserDO.getHead());
                        }
                    }
                }
                try {
                    groupUserMapper.insert(aDo);
                } catch (Exception e) {
                    log.error("插入数据库异常，数据结构体：{}", JSON.toJSONString(aDo), e);
                }
                userDO = aDo;
            } else {
                // 库中有，直接修改并且缓存
                userDO.setStatus(UserStatusType.ONLINE.getNumber());
                userDO.setUpdateTime(now);
                userDO.setLeaveStatus(YnEnum.ZERO.getValue());
                groupUserMapper.update(userDO, wrapper);
            }
            userDO.setUpdateTime(now);
        }
        // 更新缓存
        USER_LRU_CACHE.put(userId, userDO);
        return JoinGroupRespBody.success();
    }

    @Override
    public OutGroupRespBody out(Group outGroup, ImChannelContext imChannelContext) {
        if (StringUtils.isBlank(outGroup.getUserId()) || "undefined".equals(outGroup.getUserId())) {
            return OutGroupRespBody.success();
        }
        String groupId = outGroup.getGroupId();
        String userId = outGroup.getUserId();
        // 看用户是否加入过群聊：Y-直接修改状态 N-创建一个新的
        // 先走缓存查看一波
        ChatGroupUserDO userDO = USER_LRU_CACHE.get(userId + "_" + groupId);
        LocalDateTime startTime;
        LocalDateTime now = LocalDateTime.now(Clock.systemUTC());
        LambdaQueryWrapper<ChatGroupUserDO> wrapper = Wrappers.lambdaQuery(ChatGroupUserDO.class).eq(ChatGroupUserDO::getUserId, userId).eq(ChatGroupUserDO::getGroupId, groupId).eq(ChatGroupUserDO::getDelFlag, DelEnum.NORMAL.code());
        // 缓存中有，直接缓存
        if (ObjectUtils.isNotNull(userDO)) {
            userDO.setStatus(UserStatusType.OFFLINE.getNumber());
            startTime = userDO.getUpdateTime();
            userDO.setUpdateTime(now);
            userDO.setLeaveStatus(YnEnum.ONE.getValue());
            groupUserMapper.update(userDO, wrapper);
        } else {
            // 缓存没有，库中查询
            userDO = groupUserMapper.selectOne(wrapper);
            if (ObjectUtils.isNull(userDO)) {
                // 库中没有，业务有问题，没加入直播间的用户无法退出直播间
                return OutGroupRespBody.failed();
            } else {
                // 库中有，直接修改并且缓存
                userDO.setStatus(UserStatusType.OFFLINE.getNumber());
                startTime = userDO.getUpdateTime();
                userDO.setUpdateTime(now);
                userDO.setLeaveStatus(YnEnum.ONE.getValue());
                groupUserMapper.update(userDO, wrapper);
            }
        }
        // 更新缓存
        USER_LRU_CACHE.put(userId, userDO);
        // 将用户单次观看时长传入Redis中，以便其他模块计算经验值
        Duration between = Duration.between(startTime, now);
        long seconds = between.toSeconds();
        if (seconds >= 10 * 60) {
            RBlockingQueue<String> blockingQueue = redissonClient.getBlockingQueue(RedissonPublishQueueConstant.UserWatchTimePer);
            blockingQueue.putAsync(userId + "," + seconds);
        }
        return OutGroupRespBody.success();
    }

    @Override
    public void process(ImChannelContext imChannelContext, Message message) {

    }
}
