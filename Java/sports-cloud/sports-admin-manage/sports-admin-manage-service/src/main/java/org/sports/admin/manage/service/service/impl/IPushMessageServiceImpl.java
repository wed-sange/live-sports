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


import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.redisson.api.RedissonClient;
import org.sports.admin.manage.dao.entity.AppNoticeConfigDO;
import org.sports.admin.manage.dao.entity.LiveDO;
import org.sports.admin.manage.dao.entity.MyFollowDO;
import org.sports.admin.manage.dao.entity.base.AppUserRegIdDO;
import org.sports.admin.manage.dao.enums.LiveStatus;
import org.sports.admin.manage.dao.enums.MatchType;
import org.sports.admin.manage.dao.enums.MessageType;
import org.sports.admin.manage.dao.enums.PushType;
import org.sports.admin.manage.dao.repository.IMyFollowDao;
import org.sports.admin.manage.dao.req.LiveIdTypeRequest;
import org.sports.admin.manage.service.constant.CacheConstant;
import org.sports.admin.manage.service.constant.RedisCacheConstant;
import org.sports.admin.manage.service.dto.*;
import org.sports.admin.manage.service.enums.YnEnum;
import org.sports.admin.manage.service.service.*;
import org.sports.admin.manage.service.vo.LiveIdTypeVo;
import org.sports.admin.manage.service.vo.LiveVo;
import org.sports.admin.manage.service.vo.SdkMatchVo;
import org.sports.admin.manage.service.vo.SdkTeamVo;
import org.sports.springboot.starter.cache.DistributedCache;
import org.sports.springboot.starter.cache.toolkit.CacheUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Service
@Slf4j
public class IPushMessageServiceImpl implements IPushMessageService {
    @Resource
    private MessageSender messageSender;

    @Resource
    private IMyFollowDao myFollowDao;
    @Resource
    private ILiveService liveService;

    @Resource
    private SdkService sdkService;

    @Resource
    private DistributedCache distributedCache;

    @Resource
    private IAppUserRegIdService appUserRegIdService;

    @Resource
    private AppNoticeConfigService appNoticeConfigService;

    @Resource
    private RedissonClient redissonClient;
    private final ExecutorService executor = Executors.newFixedThreadPool(10);

    @Override
    public void forbiddenAppUser(Long userId, String bizId, LocalDateTime createTime, Long noticeId, String title, String reason) {
        if (Objects.isNull(userId)) {
            return;
        }
        executor.execute(() -> messageSender.send(MessageType.FORBIDDEN_APP_USER, NoticeUserDTO.builder().noticeId(noticeId).bizId(bizId).userId(userId).title(title).reason(reason).createTime(createTime.toInstant(ZoneOffset.UTC).toEpochMilli()).build()));


    }

    @Override
    public void unforbiddenAppUser(Long userId, String bizId, LocalDateTime createTime, Long noticeId, String title, String reason) {
        if (Objects.isNull(userId)) {
            return;
        }
        executor.execute(() -> messageSender.send(MessageType.UNFORBIDDEN_APP_USER, NoticeUserDTO.builder().noticeId(noticeId).bizId(bizId).userId(userId).title(title).reason(reason).createTime(createTime.toInstant(ZoneOffset.UTC).toEpochMilli()).build()));

    }


    @Override
    public void startLive(LiveDO liveDO) {
        if (Objects.isNull(liveDO)) {
            return;
        }
        executor.execute(() -> {
            LiveOperateDTO vo = new LiveOperateDTO();
            vo.setId(String.valueOf(liveDO.getId()));
            vo.setAnchorId(String.valueOf(liveDO.getUserId()));
            vo.setLiveStatus(LiveStatus.LIVING.getCode());
            vo.setMatchId(liveDO.getMatchId());
            vo.setMatchType(liveDO.getMatchType().getCode());
            vo.setCompetitionName(liveDO.getCompetitionName());
            vo.setHomeTeamName(liveDO.getHomeTeamName());
            vo.setAwayTeamName(liveDO.getAwayTeamName());
            vo.setPlayUrl(liveDO.getPlayUrl());
            vo.setHotValue(liveDO.getHotValue());
            vo.setNickName(liveDO.getNickName());
            vo.setUserLogo(liveDO.getUserLogo());
            vo.setCoverImg(liveDO.getTitlePage());
            List<LiveVo> search = liveService.search(null, null, LiveStatus.LIVING);
            for (int i = 0; i < search.size(); i++) {
                if (liveDO.getId().equals(search.get(i).getId())) {
                    vo.setRank(i + 1);
                    break;
                }
            }
            messageSender.send(MessageType.ANCHOR_OPEN, vo);
        });
        redissonClient.getMap(RedisCacheConstant.LIVEID_ANCHORID_MAP).put(String.valueOf(liveDO.getId()), String.valueOf(liveDO.getUserId()));
    }

    @Override
    public void kickOutUser(Long userId, Long liveId, LocalDateTime createTime, Long noticeId, String title, String reason) {
        if (Objects.isNull(userId)) {
            return;
        }
        executor.execute(() -> messageSender.send(MessageType.KICK_OUT_USER, NoticeUserDTO.builder().noticeId(noticeId).bizId(liveId.toString()).userId(userId).title(title).reason(reason).createTime(createTime.toInstant(ZoneOffset.UTC).toEpochMilli()).build()));

    }

    @Override
    public void closeLive(Long liveId, Long anchorId, Integer matchId, MatchType matchType) {
        if (Objects.isNull(liveId) || Objects.isNull(anchorId)) {
            return;
        }
        executor.execute(() -> {
            LiveOperateDTO vo = new LiveOperateDTO();
            vo.setId(String.valueOf(liveId));
            vo.setLiveStatus(LiveStatus.CLOSED.getCode());
            vo.setAnchorId(String.valueOf(anchorId));
            vo.setMatchType(matchType.getCode());
            vo.setMatchId(matchId);
            messageSender.send(MessageType.ANCHOR_CLOSE, vo);
        });
        redissonClient.getMap(RedisCacheConstant.LIVEID_ANCHORID_MAP).remove(String.valueOf(liveId));
    }

    @Override
    public void updateLiveUrl(LiveDO liveDO) {
        if (Objects.isNull(liveDO)) {
            return;
        }
        executor.execute(() -> {
            LiveOperateDTO vo = new LiveOperateDTO();
            vo.setId(String.valueOf(liveDO.getId()));
            vo.setAnchorId(String.valueOf(liveDO.getUserId()));
            vo.setMatchId(liveDO.getMatchId());
            vo.setMatchType(liveDO.getMatchType().getCode());
            vo.setPlayUrl(liveDO.getPlayUrl());
            messageSender.send(MessageType.ANCHOR_UPDATE_PLAY_URL, vo);
        });

    }

    @Override
    public void feedbackReply(Long feedbackId, Long userId, LocalDateTime createTime, Long noticeId, String title, String reason) {
        if (Objects.isNull(feedbackId) || Objects.isNull(userId)) {
            return;
        }
        executor.execute(() -> messageSender.send(MessageType.FEEDBACK_REPLY, NoticeUserDTO.builder().noticeId(noticeId).bizId(String.valueOf(feedbackId)).userId(userId).title(title).reason(reason).createTime(createTime.toInstant(ZoneOffset.UTC).toEpochMilli()).build()));
    }

    @Override
    public void pushUnreadMessageTotalCount(String anchorId, String userId, Integer identity) {
        if (Strings.isBlank(anchorId) && Strings.isBlank(userId)) {
            return;
        }
        if (Objects.isNull(identity)) {
            return;
        }
        executor.execute(() -> {
            UserDTO userDto = new UserDTO();
            userDto.setUserId(userId);
            userDto.setAnchorId(anchorId);
            userDto.setIdentity(identity);
            messageSender.send(MessageType.USER_UNREAD_MSG_COUNT, userDto);
        });
    }

    @Override
    public void pushMatchRealScore(List<MatchScoreDTO> matchScoreList) {
        if (Objects.isNull(matchScoreList)) {
            return;
        }
        executor.execute(() -> messageSender.send(MessageType.MATCH_SCORE, matchScoreList));
    }

    @Override
    public void pushLiveUserRefresh() {
        executor.execute(() -> messageSender.send(MessageType.LIVE_USER_CACHE_REFRESH, new Object()));
    }

    @Override
    public void pushNewsRefresh() {
        executor.execute(() -> messageSender.send(MessageType.NEWS_REFRESH, new Object()));
    }

    @Override
    public void pushFinishedMatch(List<MatchScoreDTO> finishedMatch, MatchType matchType) {

        if (CollectionUtils.isEmpty(finishedMatch)) {
            return;
        }
        executor.execute(() -> {
            List<Long> matchIds = finishedMatch.stream().map(item -> Long.valueOf(item.getMatchId())).collect(Collectors.toList());
            List<MyFollowDO> followMatchList = myFollowDao.getFollowMatchList(matchIds, matchType);
            if (!CollectionUtils.isEmpty(followMatchList)) {
                Map<Long, List<MyFollowDO>> followMatchMap = followMatchList.stream().collect(Collectors.groupingBy(MyFollowDO::getBizId, Collectors.toList()));
                List<Long> followMatchIds = followMatchList.stream().map(MyFollowDO::getBizId).collect(Collectors.toList()); //有关注的比赛ID列表
                List<MatchScoreDTO> filterMatchList = finishedMatch.stream().filter(item -> followMatchIds.contains(Long.valueOf(item.getMatchId()))).collect(Collectors.toList());
                List<LiveIdTypeRequest> requestList = Lists.newArrayList();
                filterMatchList.forEach(item -> requestList.add(LiveIdTypeRequest.builder()
                        .matchType(matchType)
                        .matchId(item.getMatchId())
                        .build()));
                Table<MatchType, Integer, List<LiveIdTypeVo>> byIdsAndType = liveService.getByIdsAndType(requestList);
                List<MatchPushDTO> matchScoreDTOList = Lists.newArrayList();
                for (MatchScoreDTO match : filterMatchList) {
                    MatchPushDTO matchPushDTO = new MatchPushDTO();
                    matchPushDTO.setTitle("完赛通知");
                    matchPushDTO.setMatchId(match.getMatchId());
                    matchPushDTO.setMatchType(match.getMatchType().getCode());
                    matchPushDTO.setPushType(PushType.MATCH_FINISHED.getCode());
                    //获取比赛信息
                    SdkMatchVo sdkMatchVo = sdkService.getMatchDetailById(match.getMatchId(), matchType);
                    SdkTeamVo away = sdkService.findTeamById(sdkMatchVo.getAwayTeamId(), matchType);
                    SdkTeamVo home = sdkService.findTeamById(sdkMatchVo.getHomeTeamId(), matchType);
                    //没有取到主队或者客队不推送，这里会出现调用接口失败的情况
                    if (Objects.isNull(away) || Objects.isNull(home)) {
                        log.error("推送消息时调用接口查询队伍信息失败，推送比赛：{},比赛类型:{}", match.getMatchId(), match.getMatchType());
                        continue;
                    }
                    matchPushDTO.setAlert(MessageFormat.format("你关注的比赛：{0} VS {1} 已结束，比分 {2}", home.getShortNameZh(), away.getShortNameZh(), match.getRegularTimeCore()));
                    List<LiveIdTypeVo> liveIdTypeVos = byIdsAndType.get(match.getMatchType(), match.getMatchId());
                    String liveId = match.getMatchType().getCode() + "_" + match.getMatchId();
                    matchPushDTO.setLiveId(liveId);
                    matchPushDTO.setPureFlow(true);
                    if (!CollectionUtils.isEmpty(liveIdTypeVos)) {
                        LiveIdTypeVo liveIdTypeVo = liveIdTypeVos.stream().max(Comparator.comparing(LiveIdTypeVo::getHotValue)).orElse(null);
                        if (Objects.nonNull(liveIdTypeVo)) {
                            matchPushDTO.setLiveId(String.valueOf(liveIdTypeVo.getId()));
                            matchPushDTO.setPureFlow(false);
                            matchPushDTO.setAnchorId(String.valueOf(liveIdTypeVo.getUserId()));
                        }
                    }
                    matchPushDTO.setRegIds(filterUserNoticeOpen(followMatchMap.get(Long.valueOf(match.getMatchId())).stream().map(item -> item.getUserId()).collect(Collectors.toList()), MessageType.FINISHED_MATCH, match.getMatchId(), match.getMatchType().getCode()));
                    if (CollectionUtils.isEmpty(matchPushDTO.getRegIds())) {
                        continue;
                    }
                    matchScoreDTOList.add(matchPushDTO);
                }
                if (matchScoreDTOList.size() > 0) {
                    messageSender.send(MessageType.FINISHED_MATCH, matchScoreDTOList);
                }
            }
        });
    }

    @Override
    public void pushUpcomingMatch(List<SdkMatchVo> matchList, MatchType matchType) {
        if (CollectionUtils.isEmpty(matchList)) {
            return;
        }
        executor.execute(() -> {
            List<Long> matchIds = matchList.stream().map(item -> Long.valueOf(item.getId())).collect(Collectors.toList());
            List<MyFollowDO> followMatchList = myFollowDao.getFollowMatchList(matchIds, matchType);
            Map<Long, List<MyFollowDO>> followMatchMap = followMatchList.stream().collect(Collectors.groupingBy(MyFollowDO::getBizId, Collectors.toList()));
            if (!CollectionUtils.isEmpty(followMatchList)) {
                List<Long> followMatchIds = followMatchList.stream().map(MyFollowDO::getBizId).collect(Collectors.toList()); //有关注的比赛ID列表
                List<SdkMatchVo> filterMatchList = matchList.stream().filter(item -> followMatchIds.contains(Long.valueOf(item.getId()))).collect(Collectors.toList());
                List<LiveIdTypeRequest> requestList = Lists.newArrayList();
                filterMatchList.forEach(item -> requestList.add(LiveIdTypeRequest.builder()
                        .matchType(matchType)
                        .matchId(item.getId())
                        .build()));
                Table<MatchType, Integer, List<LiveIdTypeVo>> byIdsAndType = liveService.getByIdsAndType(requestList);
                List<MatchPushDTO> matchScoreDTOList = Lists.newArrayList();
                for (SdkMatchVo match : filterMatchList) {
                    MatchPushDTO matchPushDTO = new MatchPushDTO();
                    matchPushDTO.setTitle("开赛通知");
                    matchPushDTO.setMatchId(match.getId());
                    matchPushDTO.setMatchType(match.getMatchType().getCode());
                    matchPushDTO.setPushType(PushType.MATCH_START.getCode());
                    //获取比赛信息
                    SdkTeamVo away = sdkService.findTeamById(match.getAwayTeamId(), matchType);
                    SdkTeamVo home = sdkService.findTeamById(match.getHomeTeamId(), matchType);
                    //没有取到主队或者客队不推送，这里会出现调用接口失败的情况
                    if (Objects.isNull(away) || Objects.isNull(home)) {
                        log.error("推送消息时调用接口查询队伍信息失败，推送比赛：{},比赛类型:{}", match.getId(), match.getMatchType());
                        continue;
                    }
                    matchPushDTO.setAlert(MessageFormat.format("你关注的比赛：{0} VS {1}即将开始", home.getShortNameZh(), away.getShortNameZh()));
                    List<LiveIdTypeVo> liveIdTypeVos = byIdsAndType.get(match.getMatchType(), match.getId());
                    String liveId = match.getMatchType().getCode() + "_" + match.getId();
                    matchPushDTO.setLiveId(liveId);
                    matchPushDTO.setPureFlow(true);
                    if (!CollectionUtils.isEmpty(liveIdTypeVos)) {
                        LiveIdTypeVo liveIdTypeVo = liveIdTypeVos.stream().max(Comparator.comparing(LiveIdTypeVo::getHotValue)).orElse(null);
                        if (Objects.nonNull(liveIdTypeVo)) {
                            matchPushDTO.setLiveId(String.valueOf(liveIdTypeVo.getId()));
                            matchPushDTO.setPureFlow(false);
                            matchPushDTO.setAnchorId(String.valueOf(liveIdTypeVo.getUserId()));
                        }
                    }
                    matchPushDTO.setRegIds(filterUserNoticeOpen(followMatchMap.get(Long.valueOf(match.getId())).stream().map(item -> item.getUserId()).collect(Collectors.toList()), MessageType.START_MATCH, match.getId(), match.getMatchType().getCode()));
                    if (CollectionUtils.isEmpty(matchPushDTO.getRegIds())) {
                        continue;
                    }
                    matchScoreDTOList.add(matchPushDTO);
                }
                if (matchScoreDTOList.size() > 0) {
                    messageSender.send(MessageType.START_MATCH, matchScoreDTOList);
                }
            }
        });
    }

    /**
     * 用户开启通知的并且5分钟之内未推送过的
     *
     * @param userIds
     * @return
     */
    private List<String> filterUserNoticeOpen(List<Long> userIds, MessageType messageType, Integer matchId, Integer matchType) {
        if (CollectionUtils.isEmpty(userIds)) {
            return null;
        }
        List<AppNoticeConfigDO> noticeConfig = appNoticeConfigService.getNoticeConfig(userIds);
        //查询出不开启通知的用户
        List<Long> notOpenUserIds = noticeConfig.stream().filter(item -> YnEnum.ZERO.getValue().equals(item.getYnFollowMatch())).map(item -> item.getUserId()).collect(Collectors.toList());
        userIds.removeAll(notOpenUserIds);
        //是否绑定极光推送regId
        List<AppUserRegIdDO> regIdList = appUserRegIdService.getUserRegIds(userIds);
        if (CollectionUtils.isEmpty(regIdList)) {
            return null;
        }
        List<String> userRegIds = regIdList.stream().map(AppUserRegIdDO::getRegId).collect(Collectors.toList());
        List<String> filterRegIds = Lists.newArrayList();
        //5分钟之类同种类型的消息已通知过就不通知了
        userRegIds.forEach(regId -> {
            String pushKey = null;
            String pushMatchKey = null;
            if (MessageType.FINISHED_MATCH.equals(messageType)) {
                pushKey = CacheUtil.buildKey(CacheConstant.PUSH_USER_FINISHED_MATCH, regId);
                pushMatchKey = CacheUtil.buildKey(CacheConstant.PUSH_USER_FINISHED_MATCH, regId, matchId.toString(), matchType.toString());
            } else {
                pushKey = CacheUtil.buildKey(CacheConstant.PUSH_USER_START_MATCH, regId);
                pushMatchKey = CacheUtil.buildKey(CacheConstant.PUSH_USER_START_MATCH, regId, matchId.toString(), matchType.toString());
            }
            if (Objects.isNull(distributedCache.get(pushKey)) && Objects.isNull(distributedCache.get(pushMatchKey))) {
                filterRegIds.add(regId);
                //todo 测试修改推送时间间隔，正式需要修改回来
                distributedCache.put(pushKey, true, 1, TimeUnit.MINUTES);
                distributedCache.put(pushMatchKey, true, 1, TimeUnit.DAYS);
            }
        });
        if (CollectionUtils.isEmpty(filterRegIds)) {
            return null;
        }
        return filterRegIds;

    }

}
