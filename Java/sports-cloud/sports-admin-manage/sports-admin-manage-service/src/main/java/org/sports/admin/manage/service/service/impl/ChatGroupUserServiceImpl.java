package org.sports.admin.manage.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.redisson.api.RedissonClient;
import org.sports.admin.manage.dao.entity.AppAnchorMuteUserDO;
import org.sports.admin.manage.dao.entity.AppUserDO;
import org.sports.admin.manage.dao.entity.ChatGroupUserDO;
import org.sports.admin.manage.dao.entity.LiveDO;
import org.sports.admin.manage.dao.mapper.ChatGroupUserMapper;
import org.sports.admin.manage.dao.req.GroupUserPageRequest;
import org.sports.admin.manage.service.constant.RedisCacheConstant;
import org.sports.admin.manage.service.enums.UserLvEnum;
import org.sports.admin.manage.service.enums.YnEnum;
import org.sports.admin.manage.service.service.IAppAnchorMuteUserService;
import org.sports.admin.manage.service.service.IAppUserService;
import org.sports.admin.manage.service.service.IChatGroupUserService;
import org.sports.admin.manage.service.service.ILiveService;
import org.sports.admin.manage.service.vo.ChatGroupUserVO;
import org.sports.springboot.starter.cache.toolkit.CacheUtil;
import org.sports.springboot.starter.convention.exception.ServiceException;
import org.sports.springboot.starter.convention.page.PageResponse;
import org.sports.springboot.starter.database.mybatisplus.PageUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 群组用户信息 服务实现类
 * </p>
 *

 * @since 2023-07-27
 */
@Service
public class ChatGroupUserServiceImpl extends ServiceImpl<ChatGroupUserMapper, ChatGroupUserDO> implements IChatGroupUserService {
    @Resource
    private IAppAnchorMuteUserService appAnchorMuteUserService;

    @Resource
    private ILiveService liveService;

    @Resource
    private IAppUserService appUserService;

    @Resource
    private RedissonClient redissonClient;

    @Override
    public Map<String, Integer> getLiveUserByGroupIds(List<String> groupIdList) {
        List<Map<String, Object>> mapList = this.listMaps(new QueryWrapper<ChatGroupUserDO>()
                .select("group_id as groupId, count(group_id) as userCount")
                .in("group_id", groupIdList)
                .eq("status", YnEnum.ZERO.getValue())
                .eq("del_flag", YnEnum.ZERO.getValue())
                .groupBy("group_id"));
        Map<String, Integer> backMap = new HashMap<>();
        if (CollectionUtils.isEmpty(mapList)) {
            return backMap;
        }
        for (Map<String, Object> map : mapList) {
            backMap.put(String.valueOf(map.get("groupId")), Integer.valueOf(String.valueOf(map.get("userCount"))));
        }
        return backMap;
    }

    @Override
    public long getMaxLiveUser(Long userId, LocalDateTime startTime, LocalDateTime endTime) {
        Long maxUserCount = this.getBaseMapper().getMaxUserCount(userId, startTime, endTime);
        return Objects.isNull(maxUserCount) ? 0 : maxUserCount.longValue();
    }

    @Override
    public long getTotalLiveUser(Long userId, LocalDateTime startTime, LocalDateTime endTime) {
        Long totalLiveUserCount = this.getBaseMapper().getTotalLiveUserCount(userId, startTime, endTime);
        return Objects.isNull(totalLiveUserCount) ? 0 : totalLiveUserCount.longValue();
    }

    @Override
    public PageResponse<ChatGroupUserVO> getGroupUser(GroupUserPageRequest req) {

        if (Objects.isNull(req.getGroupId())) {
            throw new ServiceException("参数错误");
        }
        LiveDO liveDO = liveService.getById(req.getGroupId());
        if (Objects.isNull(liveDO)) {
            throw new ServiceException("参数错误");
        }
        List<String> appUserIds = new ArrayList<>();
        if (Objects.nonNull(req.getIsMute())) {
            List<AppAnchorMuteUserDO> appAnchorMuteUserDOS = appAnchorMuteUserService.queryMuteUsersByAnchorId(liveDO.getUserId());
            if (CollectionUtils.isEmpty(appAnchorMuteUserDOS)) {
                appUserIds = appAnchorMuteUserDOS.stream().map(item -> String.valueOf(item.getUserId())).collect(Collectors.toList());
            }
        }
        LambdaQueryWrapper<ChatGroupUserDO> queryWrapper = Wrappers.lambdaQuery(ChatGroupUserDO.class)
                .eq(ChatGroupUserDO::getGroupId, req.getGroupId())
                .isNotNull(ChatGroupUserDO::getNick)
                .eq(ChatGroupUserDO::getLeaveStatus, YnEnum.ZERO.getValue())
                .like(Objects.nonNull(req.getKeywords()), ChatGroupUserDO::getNick, req.getKeywords());
        if (!CollectionUtils.isEmpty(appUserIds)) {
            if (req.getIsMute()) {
                queryWrapper.in(ChatGroupUserDO::getUserId, appUserIds);
            } else {
                queryWrapper.notIn(ChatGroupUserDO::getUserId, appUserIds);
            }
        }
        queryWrapper.orderByDesc(ChatGroupUserDO::getCreateTime);
        Page<ChatGroupUserDO> liveUserDOPage = this.baseMapper.selectPage(new Page<>(req.getCurrent(), req.getSize()), queryWrapper);
        PageResponse<ChatGroupUserVO> liveUserVOPage = PageUtil.convert(liveUserDOPage, ChatGroupUserVO.class);
        //补充用户基本和是否封禁
        if(!CollectionUtils.isEmpty(liveUserVOPage.getRecords())) {
            List<AppUserDO> appUserDOS = appUserService.listByIds(liveUserVOPage.getRecords().stream().map(item -> Long.valueOf(item.getUserId())).collect(Collectors.toList()));
            Map<Long, AppUserDO> appUserMap = appUserDOS.stream().collect(Collectors.toMap(AppUserDO::getId, Function.identity()));
            liveUserVOPage.getRecords().forEach(item -> {
                AppUserDO appUserDO = appUserMap.get(Long.valueOf(item.getUserId()));
                if(Objects.isNull(appUserDO)) {
                    item.setLevel(UserLvEnum.ONE.getLv());
                }else {
                    item.setLevel(appUserDO.getLvNum());
                    item.setNick(appUserDO.getName());
                    item.setAvatar(appUserDO.getHead());
                }
                item.setMute(redissonClient.getSet(CacheUtil.buildKey(RedisCacheConstant.APP_ANCHOR_USER_FORBIDDEN, String.valueOf(liveDO.getUserId()))).contains(item.getUserId()));
            });
        }
        return liveUserVOPage;
    }

}
