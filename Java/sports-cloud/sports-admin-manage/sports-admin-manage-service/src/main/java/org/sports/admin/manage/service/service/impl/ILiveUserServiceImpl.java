/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.sports.admin.manage.service.service.impl;


import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.redisson.api.RedissonClient;
import org.sports.admin.manage.dao.entity.AppAnchorMuteUserDO;
import org.sports.admin.manage.dao.entity.AppPinnedUserDO;
import org.sports.admin.manage.dao.entity.AppUserNoticeDO;
import org.sports.admin.manage.dao.entity.LiveUserDO;
import org.sports.admin.manage.dao.enums.AppUserNoticeTypeEnum;
import org.sports.admin.manage.dao.enums.CancelType;
import org.sports.admin.manage.dao.enums.IdentityType;
import org.sports.admin.manage.dao.mapper.LiveUserMapper;
import org.sports.admin.manage.dao.repository.ILiveUserDao;
import org.sports.admin.manage.dao.repository.IMyFollowDao;
import org.sports.admin.manage.dao.req.*;
import org.sports.admin.manage.service.constant.CacheConstant;
import org.sports.admin.manage.service.constant.RedisCacheConstant;
import org.sports.admin.manage.service.constant.UserConstant;
import org.sports.admin.manage.service.converter.LiveUserConvert;
import org.sports.admin.manage.service.enums.YnEnum;
import org.sports.admin.manage.service.service.*;
import org.sports.admin.manage.service.vo.LiveOpeninfoVO;
import org.sports.admin.manage.service.vo.LiveUserNameVO;
import org.sports.admin.manage.service.vo.LiveUserOpenVO;
import org.sports.admin.manage.service.vo.LiveUserVO;
import org.sports.springboot.starter.base.exception.ErrorCode;
import org.sports.springboot.starter.base.exception.enums.ServiceErrorCodeRange;
import org.sports.springboot.starter.base.utils.RandomUtils;
import org.sports.springboot.starter.base.utils.ValidationUtils;
import org.sports.springboot.starter.cache.DistributedCache;
import org.sports.springboot.starter.cache.toolkit.CacheUtil;
import org.sports.springboot.starter.convention.exception.ServiceException;
import org.sports.springboot.starter.convention.page.PageResponse;
import org.sports.springboot.starter.database.mybatisplus.PageUtil;
import org.sports.springboot.starter.distributedid.SnowflakeIdUtil;
import org.sports.springboot.starter.satoken.enums.UserChannelEnum;
import org.sports.springboot.starter.satoken.util.SotokenUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Service
@Slf4j
public class ILiveUserServiceImpl extends ServiceImpl<LiveUserMapper, LiveUserDO> implements ILiveUserService {

    @Resource
    private ILiveUserDao liveUserDao;
    @Resource
    private IMyFollowDao myFollowDao;
    @Resource
    private IPushMessageService pushMessageService;
    @Resource
    private ILiveOpeninfoService liveOpeninfoService;
    @Resource
    private RedissonClient redissonClient;
    @Resource
    private DistributedCache distributedCache;

    @Resource
    private IAppAnchorMuteUserService appAnchorMuteUserService;

    @Resource
    private AppUserNoticeService appUserNoticeService;

    @Resource
    private IAppPinnedUserService appPinnedUserService;

    @Override
    public PageResponse<LiveUserVO> getLiveOperateUserPage(LiveUserPageRequest pageRequest) {
        LambdaQueryWrapper<LiveUserDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LiveUserDO::getYnCancel, CancelType.UN_CANCELLED.getCode());
        queryWrapper.eq(Objects.nonNull(pageRequest.getIdentityType()), LiveUserDO::getIdentityType, pageRequest.getIdentityType());
        queryWrapper.eq(Objects.equals(pageRequest.getYnForbidden(), YnEnum.ZERO.getValue()), LiveUserDO::getYnForbidden, pageRequest.getYnForbidden());
        queryWrapper.gt(Objects.equals(pageRequest.getYnForbidden(), YnEnum.ONE.getValue()), LiveUserDO::getYnForbidden, YnEnum.ZERO.getValue());
        if (StringUtils.isNotEmpty(pageRequest.getNickName())) {
            queryWrapper.and(wrapper -> wrapper.like(StringUtils.isNotEmpty(pageRequest.getNickName()), LiveUserDO::getNickName, pageRequest.getNickName())
                    .or().like(StringUtils.isNotEmpty(pageRequest.getNickName()), LiveUserDO::getAccount, pageRequest.getNickName()));
        }

        queryWrapper.orderByDesc(LiveUserDO::getCreateTime).orderByDesc(LiveUserDO::getId);
        Page<LiveUserDO> liveUserDOPage = this.baseMapper.selectPage(new Page<>(pageRequest.getCurrent(), pageRequest.getSize()), queryWrapper);
        PageResponse<LiveUserVO> liveUserVOPage = PageUtil.convert(liveUserDOPage, LiveUserVO.class);

        List<LiveUserVO> liveUserList = liveUserVOPage.getRecords();
        //计算主播下面的助手数量
//        List<Long> liveUserIds = liveUserList.stream().filter(item -> IdentityType.ANCHOR.getCode().equals(item.getIdentityType())).map(LiveUserVO::getId).collect(Collectors.toList());
//        if (!CollectionUtils.isEmpty(liveUserIds)) {
//            List<Map<String, Object>> mapList = this.listMaps(new QueryWrapper<LiveUserDO>()
//                    .select("belong_live as belongLive, count(id) as helperCount")
//                    .eq("identity_type", IdentityType.ASSISTANT.getCode())
//                    .eq("yn_cancel", YnEnum.ZERO.getValue())
//                    .in("belong_live", liveUserIds)
//                    .groupBy("belong_live"));
//            Map<Long, Integer> helperMap = new HashMap<>();
//            if (!CollectionUtils.isEmpty(mapList)) {
//                for (Map<String, Object> map : mapList) {
//                    helperMap.put(Long.valueOf(String.valueOf(map.get("belongLive"))), Integer.valueOf(String.valueOf(map.get("helperCount"))));
//                }
//            }
//            for (LiveUserVO liveUserVO : liveUserList) {
//                if (helperMap.containsKey(liveUserVO.getId())) {
//                    liveUserVO.setAssistantCount(helperMap.get(liveUserVO.getId()));
//                } else {
//                    liveUserVO.setAssistantCount(0);
//                }
//            }
//        }
        //查找未注销的所有主播信息
        List<LiveUserVO> liveUserVOList = queryLiveUsers();
        //以id跟account组装成map
        Map<Long, String> liveUserMap = liveUserVOList.stream().collect(Collectors.toMap(LiveUserVO::getId, LiveUserVO::getNickName));

        // 根据绑定主播id展示主播信息
        liveUserList.forEach(item -> {
            if (item != null) {
                String relationIdsStr = item.getPossessLive();
                if (relationIdsStr != null && !relationIdsStr.isEmpty()) {
                    List<LiveUserNameVO> accountList = new ArrayList<>();
                    String[] liveUserIdsArray = relationIdsStr.split(",");
                    for (String liveUserIdStr : liveUserIdsArray) {
                        if (!StringUtils.isEmpty(liveUserIdStr)&&!"null".equals(liveUserIdStr)){
                            long liveUserId= Long.parseLong(liveUserIdStr);
                            if (liveUserMap.containsKey(liveUserId)) {
                                String userIdStr = liveUserMap.get(liveUserId);
                                LiveUserNameVO userNameVO = new LiveUserNameVO();
                                userNameVO.setId(liveUserId);
                                userNameVO.setAccount(userIdStr);
                                accountList.add(userNameVO);
                            }
                            item.setRelationLiveUsers(accountList);
                        }
                    }
                }
            }
        });

        LocalDateTime nowTime = LocalDateTime.now(ZoneOffset.UTC);
        List<LiveUserVO> removeList = new ArrayList<>();
        for (LiveUserVO liveUserVO : liveUserList) {
            //普通禁言计算
            if (1 == liveUserVO.getYnForbidden()) {
                //假如到期 则解封
                if (nowTime.isAfter(liveUserVO.getForbiddenTime())) {
                    this.update(new LambdaUpdateWrapper<LiveUserDO>()
                            .eq(LiveUserDO::getId, liveUserVO.getId())
                            .set(LiveUserDO::getYnForbidden, 0)
                            .set(LiveUserDO::getForbiddenDay, 0)
                            .set(LiveUserDO::getForbiddenTime, null)
                            .set(LiveUserDO::getForbiddenDescp, null));
                    liveUserVO.setYnForbidden(0);
                    liveUserVO.setForbiddenDay(0);
                    liveUserVO.setForbiddenTime(null);
                    liveUserVO.setForbiddenDescp(null);
                    removeList.add(liveUserVO);
                } else {
                    liveUserVO.setLeaveDay((int) (Duration.between(nowTime, liveUserVO.getForbiddenTime()).getSeconds() / 60 / 60 / 24) + 1);
                }
            }
        }
        if (removeList.size() > 0) {
            liveUserList.removeAll(removeList);
        }
        return liveUserVOPage;
    }


    @Override
    public List<LiveUserVO> queryHelpersByLiveId(Long liveId) {
        List<LiveUserDO> liveUserDOList = this.list(new LambdaQueryWrapper<LiveUserDO>()
                .eq(LiveUserDO::getBelongLive, liveId)
                .eq(LiveUserDO::getIdentityType, IdentityType.ASSISTANT.getCode())
                .eq(LiveUserDO::getYnCancel, YnEnum.ZERO.getValue())
                .orderByDesc(LiveUserDO::getCreateTime));
        return LiveUserConvert.INSTANCE.convertToListVo(liveUserDOList);
    }

    @Override
    public List<LiveUserVO> getLiveUserList(List<Long> ids) {
        //优先从redis里面获取  没有或者差的 从数据库获取
        Map<Long, String> liveUserMap = redissonClient.getMap(RedisCacheConstant.LIVE_ANCHOR_INFO_CACHE);
        List<Long> dealIdList; // 需要从数据库里面查询的ids
        List<LiveUserVO> backList = new ArrayList<>(); //返回主播信息
        if (CollectionUtils.isEmpty(liveUserMap)) {
            dealIdList = ids;
        } else {
            dealIdList = new ArrayList<>();
            for (Long id : ids) {
                if (liveUserMap.containsKey(id)) {
                    backList.add(JSONObject.parseObject(liveUserMap.get(id), LiveUserVO.class));
                } else {
                    dealIdList.add(id);
                }
            }
            if (dealIdList.size() == 0) {
                return backList.stream().distinct().collect(Collectors.toList());
            }
        }
        List<LiveUserVO> dealUserList = LiveUserConvert.INSTANCE.convertToListVo(liveUserDao.getLiveUserList(dealIdList));
        backList.addAll(dealUserList);
        dealUserList.forEach(liveUserVO -> {
            if (IdentityType.ANCHOR.getCode().equals(liveUserVO.getIdentityType())) {
                redissonClient.getMap(RedisCacheConstant.LIVE_ANCHOR_INFO_CACHE).put(liveUserVO.getId(), JSONObject.toJSONString(liveUserVO));
            }
        });
        return backList.stream().distinct().collect(Collectors.toList());
    }

    @Override
    public LiveUserVO getLiveUserInfo(Long id) {
        LiveUserDO liveUserInfo = liveUserDao.getLiveUserInfo(id);
        if (Objects.isNull(liveUserInfo)) {
            return null;
        }
        LiveUserVO liveUserVO = LiveUserConvert.INSTANCE.convertToVo(liveUserInfo);
        if (YnEnum.ONE.getValue().equals(liveUserInfo.getSetOpenInfo())) {
            liveUserVO.setSetOpenInfo(true);
        } else {
            liveUserVO.setSetOpenInfo(false);
        }
        //查询开播信息
        LiveOpeninfoVO liveOpeninfoVO = liveOpeninfoService.getUsedOpenInfoByLiveUserId(id);
        if (Objects.nonNull(liveOpeninfoVO)) {
            liveUserVO.setNotice(liveOpeninfoVO.getNotice());
        }
        return liveUserVO;
    }

    @Override
    public void createLiveUser(LiveUserAddRequest request) {
        if (!UserConstant.INIT_PASSWD.equals(request.getPasswd()) && !ValidationUtils.isPasswd(request.getPasswd())) {
            throw new ServiceException("密码8-16位，包含字母大小组组合+数字");
        }
        //校验账号是否存在
        LiveUserDO oldLiveUserDo = liveUserDao.getByAccount(request.getAccount());
        if (oldLiveUserDo != null) {
            if (YnEnum.ONE.getValue().equals(oldLiveUserDo.getYnCancel())) {
                throw new ServiceException("该账号已注销");
            }
            throw new ServiceException("该账号已存在");
        }
        //助手账号特殊校验
        if (IdentityType.ASSISTANT.getCode().equals(request.getIdentityType())) {
            oldLiveUserDo = liveUserDao.getLiveUserInfo(request.getBelongLive());
            if (oldLiveUserDo == null) {
                throw new ServiceException("主播账号不存在");
            }
            if (YnEnum.ONE.getValue().equals(oldLiveUserDo.getYnCancel())) {
                throw new ServiceException("主播账号已注销");
            }
        }
        LiveUserDO liveUserDO = new LiveUserDO();
        liveUserDO.setId(SnowflakeIdUtil.nextId());
        liveUserDO.setAccount(request.getAccount());
        liveUserDO.setPasswd(DigestUtil.md5Hex(request.getPasswd()));
        liveUserDO.setNickName(RandomUtils.buildNickName());
        liveUserDO.setIdentityType(request.getIdentityType());
        if (request.getBelongLive() != null) {
            liveUserDO.setBelongLive(request.getBelongLive());
        }
        liveUserDO.setRemarks(request.getRemarks());
        liveUserDO.setYnForbidden(YnEnum.ZERO.getValue());
        liveUserDO.setYnCancel(YnEnum.ZERO.getValue());
        liveUserDO.setCreateTime(LocalDateTime.now(ZoneOffset.UTC));
        liveUserDO.setCreator(SotokenUtil.getBGUserName());
        liveUserDO.setDelFlag(YnEnum.ZERO.getValue());
        this.save(liveUserDO);
        //主播缓存刷新
        if (IdentityType.ANCHOR.getCode().equals(request.getIdentityType())) {
            LiveUserVO liveUserVO = LiveUserConvert.INSTANCE.convertToVo(liveUserDO);
            redissonClient.getMap(RedisCacheConstant.LIVE_ANCHOR_INFO_CACHE).put(liveUserVO.getId(), JSONObject.toJSONString(liveUserVO));
        }
        //直播端用户缓存更新提醒
        pushMessageService.pushLiveUserRefresh();
    }

    @Override
    public void operateBindingLiveUser(OperateAddLiveUserRequest request) {
        //查询助理信息
        LiveUserDO liveUserDO = liveUserDao.getLiveUserInfo(request.getId());
        //list转string
        if (!CollectionUtils.isEmpty(request.getLiveUserId())) {
            liveUserDO.setPossessLive(request.getLiveUserId().stream().map(String::valueOf).collect(Collectors.joining(",")));
        }else {
            liveUserDO.setPossessLive(null);
        }
        if (Objects.nonNull(request.getIdentityType())) {
            liveUserDO.setIdentityType(request.getIdentityType());
        }
        liveUserDO.setRemarks(request.getRemarks());

        liveUserDao.updateLiveUser(liveUserDO);
        //直播端用户缓存更新提醒
        pushMessageService.pushLiveUserRefresh();

    }

    @Override
    public List<LiveUserVO> queryLiveUserByOperate(Long id) {
        //查询助理信息
        LiveUserDO liveUserDO = liveUserDao.getLiveUserInfo(id);
        //数据库历史数据，运营账号下面没有分配主播
        if (Strings.isBlank(liveUserDO.getPossessLive())) {
            return null;
        }
        // 将逗号分隔的数字列表转换为Long类型的列表
        List<Long> possessLiveList = Arrays.stream(liveUserDO.getPossessLive().split(","))
                .map(Long::valueOf)
                .collect(Collectors.toList());
        List<LiveUserVO> liveUserDOList = getLiveUserList(possessLiveList);
        return liveUserDOList;
    }

    @Override
    public List<LiveUserVO> queryLiveUsers() {
        //获取所有未注销的主播列表
        List<LiveUserDO> liveUserDOList = this.list(new LambdaQueryWrapper<LiveUserDO>()
                .eq(LiveUserDO::getIdentityType, IdentityType.ANCHOR.getCode())
                .eq(LiveUserDO::getYnCancel, YnEnum.ZERO.getValue()));
        return LiveUserConvert.INSTANCE.convertToListVo(liveUserDOList);
    }

    @Override
    public void updateLiveUser(LiveUserUpdateRequest request) {
        this.update(new LambdaUpdateWrapper<LiveUserDO>()
                .eq(LiveUserDO::getId, request.getId())
                .set(LiveUserDO::getRemarks, request.getRemarks()));
        //主播缓存刷新
        LiveUserVO liveUserVO = LiveUserConvert.INSTANCE.convertToVo(liveUserDao.getLiveUserInfo(request.getId()));
        if (IdentityType.ANCHOR.getCode().equals(liveUserVO.getIdentityType())) {
            redissonClient.getMap(RedisCacheConstant.LIVE_ANCHOR_INFO_CACHE).put(liveUserVO.getId(), JSONObject.toJSONString(liveUserVO));
        }
    }

    @Override
    public void cancelLiveUser(Long id) {
        /**
         * 假如注销主播 则一同注销该主播下所有助手
         */
        LiveUserDO liveUserDO = this.getById(id);
        List<Long> userIdList = getLiveUserAssistantList(liveUserDO);
        this.update(new LambdaUpdateWrapper<LiveUserDO>()
                .in(LiveUserDO::getId, userIdList)
                .set(LiveUserDO::getYnCancel, CancelType.CANCELLED.getCode())
                .set(LiveUserDO::getUpdateTime, LocalDateTime.now(ZoneOffset.UTC)));
        SotokenUtil.kickoutBatch(userIdList, UserChannelEnum.LIVE, null);
        //主播缓存刷新
        LiveUserVO liveUserVO = LiveUserConvert.INSTANCE.convertToVo(liveUserDao.getLiveUserInfo(id));
        if (IdentityType.ANCHOR.getCode().equals(liveUserVO.getIdentityType())) {
            redissonClient.getMap(RedisCacheConstant.LIVE_ANCHOR_INFO_CACHE).remove(id);
        }
        //直播端用户缓存更新提醒
        pushMessageService.pushLiveUserRefresh();
    }

    /**
     * 获取用户下 所有助手id
     *
     * @param liveUserDO
     * @return
     */
    private List<Long> getLiveUserAssistantList(LiveUserDO liveUserDO) {
        List<Long> userIdList = new ArrayList<>();
        userIdList.add(liveUserDO.getId());
        if (Objects.equals(liveUserDO.getIdentityType(), IdentityType.ANCHOR.getCode())) {
            List<LiveUserDO> liveUserDOList = this.list(new LambdaQueryWrapper<LiveUserDO>().select(LiveUserDO::getId).eq(LiveUserDO::getBelongLive, liveUserDO.getId()));
            if (!CollectionUtils.isEmpty(liveUserDOList)) {
                List<Long> uidList = liveUserDOList.stream().map(LiveUserDO::getId).collect(Collectors.toList());
                userIdList.addAll(uidList);
            }
        }
        return userIdList;
    }

    @Override
    public void forbiddenLiveUser(UserForbiddenRequest forbiddenRequest) {
        /**
         * 假如封禁主播 则一同强制退出该主播下所有助手
         */
        LiveUserDO liveUserDO = this.getById(forbiddenRequest.getId());
        if (IdentityType.ASSISTANT.getCode().equals(liveUserDO.getIdentityType())) {
            throw new ServiceException("助手不允许封禁");
        }
        LocalDateTime nowTime = LocalDateTime.now(ZoneOffset.UTC);
        LocalDateTime forbiddenTime = null;
        if (forbiddenRequest.getYnForbidden() == 1) {
            forbiddenTime = nowTime.plusDays(forbiddenRequest.getForbiddenDay());
        }
        this.update(new LambdaUpdateWrapper<LiveUserDO>()
                .eq(LiveUserDO::getId, forbiddenRequest.getId())
                .set(LiveUserDO::getYnForbidden, forbiddenRequest.getYnForbidden())
                .set(LiveUserDO::getForbiddenDay, forbiddenRequest.getForbiddenDay())
                .set(LiveUserDO::getForbiddenTime, forbiddenTime)
                .set(LiveUserDO::getForbiddenDescp, forbiddenRequest.getForbiddenDescp())
                .set(LiveUserDO::getUpdateTime, nowTime));
        List<Long> userIdList = getLiveUserAssistantList(liveUserDO);
        SotokenUtil.logoutBatch(userIdList, UserChannelEnum.LIVE, null);
        //直播端用户缓存更新提醒
        pushMessageService.pushLiveUserRefresh();
    }

    @Override
    public void unForbiddenLiveUser(Long id) {
        LocalDateTime nowTime = LocalDateTime.now(ZoneOffset.UTC);
        this.update(new LambdaUpdateWrapper<LiveUserDO>()
                .eq(LiveUserDO::getId, id)
                .set(LiveUserDO::getYnForbidden, YnEnum.ZERO.getValue())
                .set(LiveUserDO::getForbiddenDay, 0)
                .set(LiveUserDO::getForbiddenTime, nowTime)
                .set(LiveUserDO::getForbiddenDescp, "")
                .set(LiveUserDO::getUpdateTime, nowTime));
        //直播端用户缓存更新提醒
        pushMessageService.pushLiveUserRefresh();
    }

    @Override
    public void resetPasswd(Long id) {
        this.update(new LambdaUpdateWrapper<LiveUserDO>()
                .eq(LiveUserDO::getId, id)
                .set(LiveUserDO::getPasswd, DigestUtil.md5Hex(UserConstant.INIT_PASSWD))
                .set(LiveUserDO::getUpdateTime, LocalDateTime.now(ZoneOffset.UTC)));
        SotokenUtil.logout(id, UserChannelEnum.LIVE, null);
    }

    @Override
    public LiveUserDO getByAccount(String account) {
        return liveUserDao.getByAccount(account);
    }

    @Override
    public LiveUserOpenVO getLiveUserOpenInfo(Long loginUserId, Long anchorId) {
        LiveUserOpenVO liveUserOpenVO = LiveUserConvert.INSTANCE.convertToOpenVo(liveUserDao.getLiveUserInfo(anchorId));
        //查询开播信息
        LiveOpeninfoVO liveOpeninfoVO = liveOpeninfoService.getUsedOpenInfoByLiveUserId(anchorId);
        if (liveOpeninfoVO != null) {
            liveUserOpenVO.setTitlePage(liveOpeninfoVO.getTitlePage());
            liveUserOpenVO.setNotice(handleNotice(liveOpeninfoVO.getNotice()));
            liveUserOpenVO.setFirstMessage(handleFirstMessage(liveUserOpenVO.getNickName(), liveOpeninfoVO.getFirstMessage()));
        }
        //如果当前用户登录需要查询是否关注状态
        if (Objects.nonNull(liveUserOpenVO) && Objects.nonNull(loginUserId)) {
            liveUserOpenVO.setFocus(myFollowDao.isFocusAnchor(loginUserId, anchorId));
        }
        if (Objects.nonNull(liveUserOpenVO)) {
            liveUserOpenVO.setFansCount(myFollowDao.getFansCount(anchorId));
        }
        return liveUserOpenVO;
    }

    private String handleNotice(String notice) {
        if (Strings.isBlank(notice)) {
            return null;
        }
        notice = notice.replaceAll("<a ", "<a style='color:#94999F;' ");
        return notice;
    }

    public static String handleFirstMessage(String nickName, String firstMessage) {
        if (Strings.isBlank(firstMessage)) {
            return null;
        }
        String anchorStr = "<span style='color: #34A853;font-weight:500;margin-right:5px;flex-shrink:0;'>"
                + nickName + ":" + "</span>";

        Pattern pattern = Pattern.compile("<([\\s\\S]*?)>");
        Matcher matcher = pattern.matcher(firstMessage);

        if (matcher.find()) {
            String replaceStr = matcher.group(1);
            if (replaceStr != null) {
                firstMessage = firstMessage.replaceFirst("<" + replaceStr + ">", "<" + replaceStr + ">" + anchorStr);
            }
        } else {
            firstMessage = "<div>" + anchorStr + firstMessage + "</div>";
        }
        firstMessage = firstMessage.replaceAll("<a ", "<a style='color:#94999F;' ");
        return firstMessage;
    }

    @Override
    public List<LiveUserVO> getLiveUserListByNickName(String nickName) {
        List<LiveUserDO> liveUserDOList = this.list(new LambdaQueryWrapper<LiveUserDO>()
                .like(LiveUserDO::getNickName, nickName));
        return LiveUserConvert.INSTANCE.convertToListVo(liveUserDOList);
    }

    @Override
    public ErrorCode checkLiveUser(LiveUserDO liveUser) {
        //判断是否删除
        if (Objects.equals(YnEnum.ONE.getValue(), liveUser.getYnCancel())) {
            return ServiceErrorCodeRange.ACCOUNT_CANCELD;
        }
        //判断是否封禁
        ErrorCode errorCode = checkLiveUserForbidden(liveUser);
        if (!Objects.isNull(errorCode)) {
            return errorCode;
        }
        //假如是助手 判断所属主播是否有效
        if (IdentityType.ASSISTANT.getCode().equals(liveUser.getIdentityType())) {
            LiveUserDO belongLiveUserDo = liveUserDao.getLiveUserInfo(liveUser.getBelongLive());
            if (belongLiveUserDo == null || YnEnum.ONE.getValue().equals(belongLiveUserDo.getYnCancel())) {
                return ServiceErrorCodeRange.ACCOUNT_CANCELD;
            }
            return checkLiveUserForbidden(belongLiveUserDo);
        }
        return null;
    }

    @Override
    public void setOpenInfoFlag(Long userId) {
        LiveUserDO liveUserInfo = liveUserDao.getLiveUserInfo(userId);
        if (Objects.nonNull(liveUserInfo) && YnEnum.ZERO.getValue().equals(liveUserInfo.getSetOpenInfo())) {
            this.update(new LambdaUpdateWrapper<LiveUserDO>()
                    .eq(LiveUserDO::getId, userId)
                    .set(LiveUserDO::getSetOpenInfo, YnEnum.ONE.getValue())
                    .set(LiveUserDO::getUpdateTime, LocalDateTime.now(ZoneOffset.UTC)));
        }
    }

    @Override
    public void kickOutUser(KickOutUserRequest kick) {
        LiveUserDO liveUserInfo = liveUserDao.getLiveUserInfo(kick.getAnchorId());
        if (Objects.isNull(liveUserInfo)) {
            throw new ServiceException("踢出失败：踢出主播不存在！");
        }
        LocalDateTime nowTime = LocalDateTime.now(ZoneOffset.UTC);
        String cacheKey = CacheUtil.buildKey(CacheConstant.KICK_OUT_USER_KEY, String.valueOf(kick.getUserId()), String.valueOf(kick.getAnchorId()));
        if (IdentityType.ANCHOR.getCode().equals(liveUserInfo.getIdentityType()) || IdentityType.OPERATOR.getCode().equals(liveUserInfo.getIdentityType())) {
            distributedCache.put(cacheKey, String.valueOf(kick.getUserId()), 15, TimeUnit.MINUTES);
        } else {
            throw new ServiceException("您没有权限踢出用户！");
        }
        //第二步 记录系统通知
        AppUserNoticeDO appUserNoticeDO = new AppUserNoticeDO();
        appUserNoticeDO.setType(AppUserNoticeTypeEnum.KICK_OUT.getCode());
        appUserNoticeDO.setUserId(kick.getUserId());
        appUserNoticeDO.setTitle("踢出提醒");
        appUserNoticeDO.setNotice("您已被主播【" + liveUserInfo.getNickName() + "】踢出直播间");
        appUserNoticeDO.setReadFlag(false);
        appUserNoticeDO.setCreateTime(nowTime);
        appUserNoticeService.save(appUserNoticeDO);
        pushMessageService.kickOutUser(kick.getUserId(), kick.getLiveId(), nowTime, appUserNoticeDO.getId(), appUserNoticeDO.getTitle(), appUserNoticeDO.getNotice());
    }

    @Override
    @Transactional
    public void forbiddenAppUser(MuteUserRequest muteUserRequest) {
        LiveUserDO liveUserInfo = liveUserDao.getLiveUserInfo(muteUserRequest.getAnchorId());
        if (Objects.isNull(liveUserInfo)) {
            throw new ServiceException("禁言失败：禁言主播不存在！");
        }
        LocalDateTime nowTime = LocalDateTime.now(ZoneOffset.UTC);
        /**
         * 保持幂等性
         */

        if (appAnchorMuteUserService.count(Wrappers.lambdaQuery(AppAnchorMuteUserDO.class)
                .eq(AppAnchorMuteUserDO::getAnchorId, muteUserRequest.getAnchorId())
                .eq(AppAnchorMuteUserDO::getUserId, muteUserRequest.getUserId())) > 0) {
            return;
        }
        AppAnchorMuteUserDO bean = new AppAnchorMuteUserDO();
        bean.setId(SnowflakeIdUtil.nextId());
        bean.setAnchorId(muteUserRequest.getAnchorId());
        bean.setUserId(muteUserRequest.getUserId());
        bean.setCreateTime(nowTime);
        bean.setUpdateTime(nowTime);
        appAnchorMuteUserService.save(bean);
        //第二步 记录系统通知
        AppUserNoticeDO appUserNoticeDO = new AppUserNoticeDO();
        appUserNoticeDO.setType(AppUserNoticeTypeEnum.FORBIDDEN.getCode());
        appUserNoticeDO.setUserId(muteUserRequest.getUserId());
        appUserNoticeDO.setTitle("主播禁言");
        appUserNoticeDO.setNotice("您的账号已被主播【" + liveUserInfo.getNickName() + "】禁言");
        appUserNoticeDO.setReadFlag(false);
        appUserNoticeDO.setCreateTime(nowTime);
        appUserNoticeService.save(appUserNoticeDO);
        //第三步 通知前端
        pushMessageService.forbiddenAppUser(muteUserRequest.getUserId(), String.valueOf(muteUserRequest.getAnchorId()), nowTime, appUserNoticeDO.getId(), appUserNoticeDO.getTitle(), appUserNoticeDO.getNotice());
        //第四步 更新禁言缓存
        redissonClient.getSet(CacheUtil.buildKey(RedisCacheConstant.APP_ANCHOR_USER_FORBIDDEN, String.valueOf(muteUserRequest.getAnchorId()))).add(String.valueOf(muteUserRequest.getUserId()));

    }

    @Override
    @Transactional
    public void unforbiddenAppUser(MuteUserRequest muteUserRequest) {
        LiveUserDO liveUserInfo = liveUserDao.getLiveUserInfo(muteUserRequest.getAnchorId());
        if (Objects.isNull(liveUserInfo)) {
            throw new ServiceException("禁言失败：禁言主播不存在！");
        }
        LocalDateTime nowTime = LocalDateTime.now(ZoneOffset.UTC);
        appAnchorMuteUserService.remove(Wrappers.lambdaQuery(AppAnchorMuteUserDO.class)
                .eq(AppAnchorMuteUserDO::getAnchorId, muteUserRequest.getAnchorId())
                .eq(AppAnchorMuteUserDO::getUserId, muteUserRequest.getUserId()));
        //第二步 记录系统通知
        AppUserNoticeDO appUserNoticeDO = new AppUserNoticeDO();
        appUserNoticeDO.setType(AppUserNoticeTypeEnum.FORBIDDEN.getCode());
        appUserNoticeDO.setUserId(muteUserRequest.getUserId());
        appUserNoticeDO.setTitle("主播解禁");
        appUserNoticeDO.setNotice("您的账号已被主播【" + liveUserInfo.getNickName() + "】解除禁言");
        appUserNoticeDO.setReadFlag(false);
        appUserNoticeDO.setCreateTime(nowTime);
        appUserNoticeService.save(appUserNoticeDO);
        pushMessageService.unforbiddenAppUser(muteUserRequest.getUserId(), String.valueOf(muteUserRequest.getAnchorId()), nowTime, appUserNoticeDO.getId(), appUserNoticeDO.getTitle(), appUserNoticeDO.getNotice());
        //第四步 更新禁言缓存
        redissonClient.getSet(CacheUtil.buildKey(RedisCacheConstant.APP_ANCHOR_USER_FORBIDDEN, String.valueOf(muteUserRequest.getAnchorId()))).remove(String.valueOf(muteUserRequest.getUserId()));
    }

    @Override
    public void createOperateUser(OpLiveUserAddRequest request) {
        if (!UserConstant.INIT_PASSWD.equals(request.getPasswd()) && !ValidationUtils.isPasswd(request.getPasswd())) {
            throw new ServiceException("密码8-16位，包含字母大小组组合+数字");
        }
        //校验账号是否存在
        LiveUserDO oldLiveUserDo = liveUserDao.getByAccount(request.getAccount());
        if (oldLiveUserDo != null) {
            if (YnEnum.ONE.getValue().equals(oldLiveUserDo.getYnCancel())) {
                throw new ServiceException("该账号已注销");
            }
            throw new ServiceException("该账号已存在");
        }
        LiveUserDO liveUserDO = new LiveUserDO();
        liveUserDO.setId(SnowflakeIdUtil.nextId());
        liveUserDO.setAccount(request.getAccount());
        liveUserDO.setPasswd(DigestUtil.md5Hex(request.getPasswd()));
        liveUserDO.setNickName(RandomUtils.buildNickName());
        liveUserDO.setIdentityType(request.getIdentityType());
        if (!CollectionUtils.isEmpty(request.getLiveUserId())) {
            liveUserDO.setPossessLive(request.getLiveUserId().stream().map(String::valueOf).collect(Collectors.joining(",")));
        }
        liveUserDO.setRemarks(request.getRemarks());
        liveUserDO.setYnForbidden(YnEnum.ZERO.getValue());
        liveUserDO.setYnCancel(YnEnum.ZERO.getValue());
        liveUserDO.setCreateTime(LocalDateTime.now(ZoneOffset.UTC));
        liveUserDO.setCreator(SotokenUtil.getBGUserName());
        liveUserDO.setDelFlag(YnEnum.ZERO.getValue());
        this.save(liveUserDO);
        //主播缓存刷新
        if (IdentityType.ANCHOR.getCode().equals(request.getIdentityType())) {
            LiveUserVO liveUserVO = LiveUserConvert.INSTANCE.convertToVo(liveUserDO);
            redissonClient.getMap(RedisCacheConstant.LIVE_ANCHOR_INFO_CACHE).put(liveUserVO.getId(), JSONObject.toJSONString(liveUserVO));
        }
        //直播端用户缓存更新提醒
        pushMessageService.pushLiveUserRefresh();
    }

    @Override
    public void setTop(TopUserRequest topUserRequest) {
        Integer type = IdentityType.ANCHOR.getCode();
        if(Objects.nonNull(topUserRequest.getUserId())){
            type = IdentityType.APP_USER.getCode();
        }
        AppPinnedUserDO one = appPinnedUserService.getOne(Wrappers.lambdaQuery(AppPinnedUserDO.class)
                .eq(AppPinnedUserDO::getOperateId, SotokenUtil.getCheckUserId())
                .eq(AppPinnedUserDO::getAnchorId, topUserRequest.getAnchorId())
                .eq(AppPinnedUserDO::getType, type)
                .eq(Objects.nonNull(topUserRequest.getUserId()), AppPinnedUserDO::getUserId, topUserRequest.getUserId()));
        if (Objects.nonNull(one)) {
            one.setCreateTime(LocalDateTime.now(ZoneOffset.UTC));
            appPinnedUserService.updateById(one);
            return;
        }
        AppPinnedUserDO bean = new AppPinnedUserDO();
        bean.setType(type);
        bean.setOperateId(SotokenUtil.getCheckUserId());
        bean.setAnchorId(topUserRequest.getAnchorId());
        bean.setUserId(topUserRequest.getUserId());
        appPinnedUserService.save(bean);
    }

    @Override
    public void untop(TopUserRequest topUserRequest) {
        Integer type = IdentityType.ANCHOR.getCode();
        if(Objects.nonNull(topUserRequest.getUserId())){
            type = IdentityType.APP_USER.getCode();
        }
        AppPinnedUserDO one = appPinnedUserService.getOne(Wrappers.lambdaQuery(AppPinnedUserDO.class)
                .eq(AppPinnedUserDO::getOperateId, SotokenUtil.getCheckUserId())
                .eq(AppPinnedUserDO::getAnchorId, topUserRequest.getAnchorId())
                .eq(AppPinnedUserDO::getType, type)
                .eq(Objects.nonNull(topUserRequest.getUserId()),AppPinnedUserDO::getUserId, topUserRequest.getUserId()));
        if (Objects.nonNull(one)) {
            appPinnedUserService.removeById(one.getId());
        }
    }

    /**
     * 判断live用户是否被封禁
     *
     * @param liveUser
     * @return
     */
    private ErrorCode checkLiveUserForbidden(LiveUserDO liveUser) {
        if (liveUser.getYnForbidden() == 1) {
            LocalDateTime nowTime = LocalDateTime.now(ZoneOffset.UTC);
            //假如到期 则解封
            if (nowTime.isAfter(liveUser.getForbiddenTime())) {
                this.update(new LambdaUpdateWrapper<LiveUserDO>()
                        .eq(LiveUserDO::getId, liveUser.getId())
                        .set(LiveUserDO::getYnForbidden, 0)
                        .set(LiveUserDO::getForbiddenDay, 0)
                        .set(LiveUserDO::getForbiddenTime, null)
                        .set(LiveUserDO::getForbiddenDescp, null));
                liveUser.setYnForbidden(0);
                liveUser.setForbiddenDay(0);
                liveUser.setForbiddenTime(null);
                liveUser.setForbiddenDescp(null);
                return null;
            } else {
                return ServiceErrorCodeRange.ACCOUNT_DISABLED;
            }
        } else if (liveUser.getYnForbidden() == 2) {
            return ServiceErrorCodeRange.ACCOUNT_DISABLED;
        }
        return null;
    }
}
