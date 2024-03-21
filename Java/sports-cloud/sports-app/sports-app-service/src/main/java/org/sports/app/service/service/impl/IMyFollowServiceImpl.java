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

package org.sports.app.service.service.impl;


import cn.hutool.extra.pinyin.PinyinUtil;
import com.alicp.jetcache.anno.Cached;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.sports.admin.manage.dao.entity.MyFollowDO;
import org.sports.admin.manage.dao.enums.MatchType;
import org.sports.admin.manage.dao.repository.IMyFollowDao;
import org.sports.admin.manage.dao.req.FriendsPageRequest;
import org.sports.admin.manage.service.constant.CacheConstant;
import org.sports.admin.manage.service.service.ILiveService;
import org.sports.admin.manage.service.service.ILiveUserService;
import org.sports.admin.manage.service.vo.LiveUserVO;
import org.sports.admin.manage.service.vo.LiveVo;
import org.sports.app.service.service.IMyFollowService;
import org.sports.app.service.vo.live.MyFollowAnchorVO;
import org.sports.springboot.starter.convention.page.PageRequest;
import org.sports.springboot.starter.convention.page.PageResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
@Slf4j
public class IMyFollowServiceImpl implements IMyFollowService {

    @Resource
    private IMyFollowDao myFollowDao;

    @Resource
    private ILiveUserService liveUserService;

    @Resource
    private ILiveService liveService;
    @Override
    public PageResponse<MyFollowAnchorVO> getMyFollowAnchorPage(Long userId, PageRequest pageRequest) {
        PageResponse<MyFollowDO> myFollowAnchorPage = myFollowDao.getMyFollowAnchorPage(userId, pageRequest);
        List<MyFollowDO> records = myFollowAnchorPage.getRecords();
        List<MyFollowAnchorVO> returnList = Lists.newArrayList();
        if(!CollectionUtils.isEmpty(records)){
            List<Long> anchorIds = records.stream().map(MyFollowDO::getBizId).distinct().collect(Collectors.toList());
            //批量查询主播信息
            List<LiveUserVO> liveUserList = liveUserService.getLiveUserList(anchorIds);
            Map<Long,LiveUserVO> liveUserMap = liveUserList.stream().collect(Collectors.toMap(LiveUserVO::getId, Function.identity()));
            List<LiveVo> livingByUserIds = liveService.getLivingByUserIds(anchorIds);
            Map<Long,LiveVo> livingMap = livingByUserIds.stream().collect(Collectors.toMap(LiveVo::getUserId, Function.identity()));
            records.forEach(item ->{
                MyFollowAnchorVO vo = new MyFollowAnchorVO();
                LiveUserVO liveUserVO = liveUserMap.get(item.getBizId());
                if(Objects.nonNull(liveUserVO)) {
                    vo.setAnchorId(liveUserVO.getId());
                    vo.setNickName(liveUserVO.getNickName());
                    vo.setHead(liveUserVO.getHead());
                }
                LiveVo liveVo = livingMap.get(item.getBizId());
                if(Objects.nonNull(liveVo)){
                    vo.setLiveId(liveVo.getId());
                    vo.setMatchId(liveVo.getMatchId());
                    vo.setMatchType(liveVo.getMatchType());
                }
                vo.setFans(getFansCount(item.getBizId()));
                returnList.add(vo);
            });
        }
        return PageResponse.<MyFollowAnchorVO>builder()
                .current(pageRequest.getCurrent())
                .size(pageRequest.getSize())
                .total(myFollowAnchorPage.getTotal())
                .records(returnList)
                .build();
    }



    @Override
    public void unfollowAnchor(Long userId, Long anchorId) {
        myFollowDao.unfollowAnchor(userId, anchorId);
    }

    @Override
    @Cached(name = CacheConstant.LIVE_USER_FANS_COUNT, key = "#anchorId", expire = 2, timeUnit = TimeUnit.MINUTES)
    public Long getFansCount(Long anchorId) {
        return  myFollowDao.getFansCount(anchorId);
    }

    @Override
    @Transactional
    public void followAnchor(Long userId, Long anchorId) {
        myFollowDao.unfollowAnchor(userId, anchorId);
        myFollowDao.followAnchor(userId, anchorId);
    }

    @Override
    @Transactional
    public void unfollowMatch(Long userId, Long matchId, MatchType matchType) {
        myFollowDao.unfollowMatch(userId, matchId, matchType);
    }

    @Override
    @Transactional
    public void followMatch(Long userId, Long matchId, MatchType matchType) {
        myFollowDao.unfollowMatch(userId, matchId ,matchType);
        myFollowDao.followMatch(userId, matchId, matchType);
    }

    @Override
    public boolean isFocusMatch(Long userId, Integer matchId, MatchType matchType) {
        return myFollowDao.isFocusMatch(userId,matchId,matchType);
    }

    @Override
    public boolean isFocusAnchor(Long userId, Long anchorId) {
        return myFollowDao.isFocusAnchor(userId,anchorId);
    }

    @Override
    public List<MyFollowDO> getMyFollowMatchList(Long userId) {
        return myFollowDao.getMyFollowMatchList(userId);
    }

    @Override
    @Transactional
    public void unfriendAnchor(Long userId, Long anchorId) {
        myFollowDao.unfriendAnchor(userId, anchorId);
    }

    @Override
    public PageResponse<MyFollowAnchorVO> getMyFriendsPage(Long userId, FriendsPageRequest pageRequest) {
        List<MyFollowAnchorVO> returnList = Lists.newArrayList();
        List<MyFollowDO> records = Lists.newArrayList();
        Long total;
        if(Objects.nonNull(pageRequest.getUserName())){
            List<LiveUserVO> liveUserList = liveUserService.getLiveUserListByNickName(pageRequest.getUserName());
            if(CollectionUtils.isEmpty(liveUserList)){
                total = 0L;
            }else {
                List<Long> liveUserIds = liveUserList.stream().map(LiveUserVO::getId).collect(Collectors.toList());
                records =  myFollowDao.getMyFriendsList(userId, liveUserIds);
                total = (long) records.size();
            }
        }else {
            PageResponse<MyFollowDO> myFollowAnchorPage = myFollowDao.getMyFriendsPage(userId, pageRequest);
            records = myFollowAnchorPage.getRecords();
            total = myFollowAnchorPage.getTotal();
        }
        if (!CollectionUtils.isEmpty(records)) {
            List<Long> anchorIds = records.stream().map(MyFollowDO::getBizId).distinct().collect(Collectors.toList());
            //批量查询主播信息
            List<LiveUserVO> liveUserList = liveUserService.getLiveUserList(anchorIds);
            Map<Long, LiveUserVO> liveUserMap = liveUserList.stream().collect(Collectors.toMap(LiveUserVO::getId, Function.identity()));
            records.forEach(item -> {
                MyFollowAnchorVO vo = new MyFollowAnchorVO();
                LiveUserVO liveUserVO = liveUserMap.get(item.getBizId());
                if (Objects.nonNull(liveUserVO)) {
                    vo.setAnchorId(liveUserVO.getId());
                    vo.setNickName(liveUserVO.getNickName());
                    vo.setHead(liveUserVO.getHead());
                    vo.setShortName(PinyinUtil.getFirstLetter(liveUserVO.getNickName(), "").substring(0, 1).toUpperCase());
                    returnList.add(vo);
                }
            });
        }
        returnList.forEach(item->{
            String words = item.getShortName();
            if(Strings.isNotBlank(words)) {
                char firstChar = words.charAt(0);
                if(firstChar < 'A' || firstChar > 'Z'){
                    item.setShortName("#");
                }
            }
        });
        returnList.sort((s1, s2) -> {
            if (s1.getShortName().equals("#")) {
                return 1;  // 将 "#" 排在后面
            } else if (s2.getShortName().equals("#")) {
                return -1;  // 将 "#" 排在后面
            } else {
                return s1.getShortName().compareTo(s2.getShortName());  // 使用默认的自然顺序排序其他字符串
            }
        });
        return PageResponse.<MyFollowAnchorVO>builder()
                .current(pageRequest.getCurrent())
                .size(pageRequest.getSize())
                .records(returnList)
                .total(total)
                .build();
    }

    public static void main(String[] args) {
        char firstChar ='Z';
        if(firstChar < 'A' || firstChar > 'Z'){
            System.out.println("args = #");
        }
    }

}
