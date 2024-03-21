package org.sports.admin.manage.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.logging.log4j.util.Strings;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.sports.admin.manage.dao.entity.AppUserDO;
import org.sports.admin.manage.dao.entity.AppUserNoticeDO;
import org.sports.admin.manage.dao.enums.AppUserNoticeTypeEnum;
import org.sports.admin.manage.dao.mapper.AppUserMapper;
import org.sports.admin.manage.dao.req.UserPageRequest;
import org.sports.admin.manage.service.constant.CacheConstant;
import org.sports.admin.manage.service.constant.RedisCacheConstant;
import org.sports.admin.manage.service.enums.UserLvEnum;
import org.sports.admin.manage.service.enums.YnEnum;
import org.sports.admin.manage.service.service.AppUserNoticeService;
import org.sports.admin.manage.service.service.IAppUserService;
import org.sports.admin.manage.service.service.IPushMessageService;
import org.sports.admin.manage.service.vo.AppUserVO;
import org.sports.springboot.starter.convention.page.PageResponse;
import org.sports.springboot.starter.database.mybatisplus.PageUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * APP用户信息 服务实现类
 * </p>
 *

 * @since 2023-07-20
 */
@Service
public class IAppUserServiceImpl extends ServiceImpl<AppUserMapper, AppUserDO> implements IAppUserService {

    @Resource
    private RedissonClient redissonClient;
    @Resource
    private IPushMessageService pushMessageService;
    @Resource
    private AppUserNoticeService appUserNoticeService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AppUserDO getByTel(String tel){
        return this.getOne(new LambdaQueryWrapper<AppUserDO>().eq(AppUserDO::getTel, tel));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AppUserDO getByEmail(String email){
        return this.getOne(new LambdaQueryWrapper<AppUserDO>().eq(AppUserDO::getEmail, email));
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<AppUserDO> getByNickName(String nickName){
        return this.list(new LambdaQueryWrapper<AppUserDO>().eq(AppUserDO::getName, nickName));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PageResponse<AppUserVO> getAppUserPage(UserPageRequest pageRequest){
        LambdaQueryWrapper queryWrapper= new LambdaQueryWrapper<AppUserDO>()
                .eq(AppUserDO::getYnCancel, YnEnum.ZERO.getValue())
                .eq(pageRequest.getId() != null, AppUserDO::getId, pageRequest.getId())
                .eq(pageRequest.getYnForbidden() != null && pageRequest.getYnForbidden() == 0, AppUserDO::getYnForbidden, pageRequest.getYnForbidden())
                .gt(pageRequest.getYnForbidden() != null && pageRequest.getYnForbidden() == 1, AppUserDO::getYnForbidden, 0)
                .like(Strings.isNotBlank(pageRequest.getName()), AppUserDO::getName, pageRequest.getName())
                .orderByDesc(AppUserDO::getCreateTime);

        Page<AppUserDO> page = this.baseMapper.selectPage(new Page<>(pageRequest.getCurrent(), pageRequest.getSize()), queryWrapper);
        PageResponse<AppUserVO> backResponse = PageUtil.convert(page, AppUserVO.class);
        List<AppUserVO> appUserVOList = backResponse.getRecords();
        LocalDateTime nowTime = LocalDateTime.now(ZoneOffset.UTC);
        for (AppUserVO appUserVO : appUserVOList) {
            //普通禁言计算
            if(1 == appUserVO.getYnForbidden()){
                int leaveDay = (int)(Duration.between(nowTime, appUserVO.getForbiddenTime()).getSeconds()/60/60/24)+1;
                appUserVO.setLeaveDay(Math.max(leaveDay, 0));
            }
        }
        return backResponse;
    }

    /**
     * 用户自动解禁
     * @param appUserVO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unForbiddenUser(AppUserVO appUserVO){
        LocalDateTime nowTime = LocalDateTime.now(ZoneOffset.UTC);
        //第一步 更新用户信息
        this.update(new LambdaUpdateWrapper<AppUserDO>()
                .eq(AppUserDO::getId, appUserVO.getId())
                .set(AppUserDO::getYnForbidden, YnEnum.ZERO.getValue())
                .set(AppUserDO::getForbiddenDay, YnEnum.ZERO.getValue())
                .set(AppUserDO::getForbiddenTime, null)
                .set(AppUserDO::getForbiddenDescp, null)
                .set(AppUserDO::getUpdateTime, nowTime));
        appUserVO.setYnForbidden(0);
        appUserVO.setForbiddenDay(0);
        appUserVO.setForbiddenTime(null);
        appUserVO.setForbiddenDescp(null);
        //第二步 记录系统通知
        AppUserNoticeDO appUserNoticeDO = new AppUserNoticeDO();
        appUserNoticeDO.setType(AppUserNoticeTypeEnum.UNFORBIDDEN.getCode());
        appUserNoticeDO.setUserId(appUserVO.getId());
        appUserNoticeDO.setTitle("系统解除禁言");
        appUserNoticeDO.setReadFlag(false);
        appUserNoticeDO.setCreateTime(nowTime);
        appUserNoticeService.save(appUserNoticeDO);
        //第三步 通知前端
        pushMessageService.unforbiddenAppUser(appUserVO.getId(), null, nowTime, appUserNoticeDO.getId(), appUserNoticeDO.getTitle(), null);
        //第四步 更新禁言缓存
        redissonClient.getSet(RedisCacheConstant.APP_USER_FORBIDDEN).remove(appUserVO.getId().toString());
        //第五步 删除用户缓存
        redissonClient.getBucket(CacheConstant.APP_USERVO + appUserVO.getId()).delete();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void forbiddenUser(AppUserDO appUserDO){
        LocalDateTime nowTime = LocalDateTime.now(ZoneOffset.UTC);
        //第一步 更新用户信息
        this.update(new LambdaUpdateWrapper<AppUserDO>()
                .eq(AppUserDO::getId, appUserDO.getId())
                .set(AppUserDO::getYnForbidden, appUserDO.getYnForbidden())
                .set(AppUserDO::getForbiddenDay, appUserDO.getForbiddenDay())
                .set(AppUserDO::getForbiddenTime, appUserDO.getForbiddenTime())
                .set(AppUserDO::getForbiddenDescp, appUserDO.getForbiddenDescp())
                .set(AppUserDO::getUpdateTime, nowTime));
        //第二步 记录系统通知
        AppUserNoticeDO appUserNoticeDO = new AppUserNoticeDO();
        appUserNoticeDO.setType(AppUserNoticeTypeEnum.FORBIDDEN.getCode());
        appUserNoticeDO.setUserId(appUserDO.getId());
        appUserNoticeDO.setTitle("系统禁言");
        appUserNoticeDO.setNotice(appUserDO.getForbiddenDescp());
        appUserNoticeDO.setReadFlag(false);
        appUserNoticeDO.setCreateTime(nowTime);
        appUserNoticeService.save(appUserNoticeDO);
        //第三步 通知前端
        pushMessageService.forbiddenAppUser(appUserDO.getId(), null, nowTime, appUserNoticeDO.getId(), appUserNoticeDO.getTitle(), appUserNoticeDO.getNotice());
        //第四步 更新禁言缓存
        redissonClient.getSet(RedisCacheConstant.APP_USER_FORBIDDEN).add(String.valueOf(appUserDO.getId()));
        //第五步 删除用户缓存
        redissonClient.getBucket(CacheConstant.APP_USERVO + appUserDO.getId()).delete();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addUserGrowthValueBatch(Map<Long, Integer> userGrowthMap){
        //批量和单个修改用户成长值互斥
        RLock lk = redissonClient.getLock(RedisCacheConstant.ADD_USER_GROWTH_VALUE_LOCK);
        lk.lock();
        try {
            List<AppUserDO> userDoList = this.listByIds(userGrowthMap.keySet());
            for (AppUserDO userDO : userDoList) {
                Integer growthValue = userDO.getGrowthValue() == null ? userGrowthMap.get(userDO.getId()) : userDO.getGrowthValue() +  userGrowthMap.get(userDO.getId());
                userDO.setGrowthValue(growthValue);
                dealUserGrowth(userDO);
            }
            this.updateBatchById(userDoList, 200);
            for (Long userId : userGrowthMap.keySet()) {
                refreshCacheAppUser(userId);
            }
        }finally {
            lk.unlock();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addUserGrowthValue(Long userId, Integer growthValue){
        //批量和单个修改用户成长值互斥
        RLock lk = redissonClient.getLock(RedisCacheConstant.ADD_USER_GROWTH_VALUE_LOCK);
        lk.lock();
        try{
            AppUserDO userDO = this.getById(userId);
            if(userDO != null){
                userDO.setGrowthValue(userDO.getGrowthValue() == null ? growthValue : userDO.getGrowthValue() +  growthValue);
                dealUserGrowth(userDO);
                this.updateById(userDO);
                refreshCacheAppUser(userId);
            }
        }finally {
            lk.unlock();
        }

    }

    /**
     * 用户成长等级处理
     * @param userDO
     */
    private void dealUserGrowth(AppUserDO userDO){
        UserLvEnum lvEnum = UserLvEnum.getLvEnumByScore(userDO.getGrowthValue());
        userDO.setLvNum(lvEnum.getLv());
        userDO.setLvName(lvEnum.getName());
        userDO.setGrowthValueNext(UserLvEnum.getNextLvEnumScore(lvEnum.getLv()));
        userDO.setUpdateTime(LocalDateTime.now(ZoneOffset.UTC));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public  AppUserVO getCacheAppUserById(Long id){
        Object appUser = redissonClient.getBucket(CacheConstant.APP_USERVO + id).get();
        if(appUser != null){
            return (AppUserVO)appUser;
        }
        return refreshCacheAppUser(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AppUserVO refreshCacheAppUser(Long id){
        AppUserDO appUserDO = this.getById(id);
        if(appUserDO == null){
            return null;
        }
        if(Objects.equals(appUserDO.getYnCancel(), YnEnum.ONE.getValue())){
            redissonClient.getBucket(CacheConstant.APP_USERVO + id).delete();
            return null;
        }
        dealForbiddenObsolete(appUserDO);
        AppUserVO appUserVO = new AppUserVO();
        BeanUtils.copyProperties(appUserDO, appUserVO);
        redissonClient.getBucket(CacheConstant.APP_USERVO + id).set(appUserVO, 30, TimeUnit.MINUTES);
        return appUserVO;
    }


    /**
     * 处理app用户禁言是否到期
     * @param appUserDO
     */
    private void dealForbiddenObsolete(AppUserDO appUserDO){
        if(appUserDO != null && appUserDO.getYnForbidden() == 1 && appUserDO.getForbiddenTime().isBefore(LocalDateTime.now(ZoneOffset.UTC))){
            AppUserVO appUserVO = new AppUserVO();
            appUserVO.setId(appUserDO.getId());
            unForbiddenUser(appUserVO);
            appUserDO.setYnForbidden(0);
            appUserDO.setForbiddenDay(0);
            appUserDO.setForbiddenTime(null);
            appUserDO.setForbiddenDescp(null);
        }
    }

}
