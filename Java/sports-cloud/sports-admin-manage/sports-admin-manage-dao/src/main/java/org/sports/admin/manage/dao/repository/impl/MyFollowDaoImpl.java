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

package org.sports.admin.manage.dao.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sports.admin.manage.dao.entity.MyFollowDO;
import org.sports.admin.manage.dao.enums.FollowType;
import org.sports.admin.manage.dao.enums.IsFocus;
import org.sports.admin.manage.dao.enums.IsFriend;
import org.sports.admin.manage.dao.enums.MatchType;
import org.sports.admin.manage.dao.mapper.MyFollowMapper;
import org.sports.admin.manage.dao.repository.IMyFollowDao;
import org.sports.springboot.starter.convention.page.PageRequest;
import org.sports.springboot.starter.convention.page.PageResponse;
import org.sports.springboot.starter.database.mybatisplus.PageUtil;
import org.sports.springboot.starter.distributedid.SnowflakeIdUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;

/**
 * @描述: 我的关注和订阅实现类
 * @版权: Copyright (c) 2023


 * @版本: 1.0
 * @创建日期: 2023/7/24
 * @创建时间: 10:11
 */
@Slf4j
@Service
@AllArgsConstructor
public class MyFollowDaoImpl implements IMyFollowDao {
    @Resource
    private MyFollowMapper myFollowMapper;

    @Override
    public PageResponse<MyFollowDO> getMyFollowAnchorPage(Long userId, PageRequest pageRequest) {
        LambdaQueryWrapper<MyFollowDO> queryWrapper = Wrappers.lambdaQuery(MyFollowDO.class)
                .eq(MyFollowDO::getFollowType, FollowType.ANCHOR.getCode()).eq(MyFollowDO::getUserId, userId).eq(MyFollowDO::getIsFocus, IsFocus.YES).orderByDesc(MyFollowDO::getCreateTime);
        Page<MyFollowDO> myFollowDOPage = myFollowMapper.selectPage(new Page<>(pageRequest.getCurrent(), pageRequest.getSize()), queryWrapper);
        return PageUtil.convert(myFollowDOPage, MyFollowDO.class);
    }

    @Override
    public List<MyFollowDO> getMyFollowMatchList(Long userId) {

        LambdaQueryWrapper<MyFollowDO> queryWrapper = Wrappers.lambdaQuery(MyFollowDO.class)
                .eq(MyFollowDO::getFollowType, FollowType.MATCH.getCode()).eq(MyFollowDO::getUserId, userId);
        return myFollowMapper.selectList(queryWrapper);
    }

    @Override
    public List<MyFollowDO> getFollowMatchList(List<Long> matchIds, MatchType matchType) {
        LambdaQueryWrapper<MyFollowDO> queryWrapper = Wrappers.lambdaQuery(MyFollowDO.class)
                .eq(MyFollowDO::getFollowType, FollowType.MATCH.getCode())
                .eq(MyFollowDO::getMatchType, matchType)
                .in(MyFollowDO::getBizId, matchIds);
        return myFollowMapper.selectList(queryWrapper);
    }

    @Override
    public List<MyFollowDO> getMyFollowAnchorList(Long userId) {
        LambdaQueryWrapper<MyFollowDO> queryWrapper = Wrappers.lambdaQuery(MyFollowDO.class)
                .eq(MyFollowDO::getFollowType, FollowType.ANCHOR.getCode()).eq(MyFollowDO::getUserId, userId).eq(MyFollowDO::getIsFocus, IsFocus.YES);
        return myFollowMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional
    public void unfollowAnchor(Long userId, Long anchorId) {
        MyFollowDO myFollowDO = myFollowMapper.selectOne(Wrappers.lambdaQuery(MyFollowDO.class).eq(MyFollowDO::getUserId, userId).eq(MyFollowDO::getBizId, anchorId).eq(MyFollowDO::getFollowType, FollowType.ANCHOR.getCode()));
        if(Objects.nonNull(myFollowDO)) {
            myFollowDO.setIsFocus(IsFocus.NO.getCode());
            myFollowMapper.updateById(myFollowDO);
        }
    }

    @Override
    @Transactional
    public void followAnchor(Long userId, Long anchorId) {
        MyFollowDO myFollowDO = myFollowMapper.selectOne(Wrappers.lambdaQuery(MyFollowDO.class).eq(MyFollowDO::getUserId, userId).eq(MyFollowDO::getBizId, anchorId).eq(MyFollowDO::getFollowType, FollowType.ANCHOR.getCode()));
        if(Objects.nonNull(myFollowDO)) {
            myFollowDO.setIsFocus(IsFocus.YES.getCode());
            myFollowDO.setIsFriend(IsFriend.YES.getCode());
            myFollowDO.setCreateTime(LocalDateTime.now(ZoneOffset.UTC));
            myFollowDO.setUpdateTime(LocalDateTime.now(ZoneOffset.UTC));
            myFollowMapper.updateById(myFollowDO);
        }else {
            myFollowMapper.insert(MyFollowDO.builder().id(SnowflakeIdUtil.nextId()).userId(userId).bizId(anchorId).isFocus(IsFocus.YES.getCode()).isFriend(IsFriend.YES.getCode()).followType(FollowType.ANCHOR.getCode()).build());
        }
    }

    @Override
    @Transactional
    public void unfollowMatch(Long userId, Long matchId, MatchType matchType) {
        myFollowMapper.delete(Wrappers.lambdaQuery(MyFollowDO.class).eq(MyFollowDO::getUserId, userId).eq(MyFollowDO::getMatchType, matchType).eq(MyFollowDO::getBizId, matchId).eq(MyFollowDO::getFollowType, FollowType.MATCH.getCode()));
    }

    @Override
    @Transactional
    public void followMatch(Long userId, Long matchId, MatchType matchType) {
        myFollowMapper.insert(MyFollowDO.builder().id(SnowflakeIdUtil.nextId()).userId(userId).bizId(matchId).matchType(matchType).followType(FollowType.MATCH.getCode()).build());
    }

    @Override
    public long getFansCount(Long anchorId) {
        return myFollowMapper.selectCount(Wrappers.lambdaQuery(MyFollowDO.class)
                .eq(MyFollowDO::getFollowType, FollowType.ANCHOR.getCode()).eq(MyFollowDO::getBizId, anchorId).eq(MyFollowDO::getIsFocus, IsFocus.YES.getCode()));
    }

    @Override
    public List<MyFollowDO> getFansList(Long anchorId) {
        return myFollowMapper.selectList(Wrappers.lambdaQuery(MyFollowDO.class)
                .eq(MyFollowDO::getFollowType, FollowType.ANCHOR.getCode()).eq(MyFollowDO::getBizId, anchorId).eq(MyFollowDO::getIsFocus, IsFocus.YES.getCode()));
    }

    @Override
    @Transactional
    public boolean isFocusMatch(Long userId, Integer matchId, MatchType matchType) {
        return myFollowMapper.selectCount(Wrappers.lambdaQuery(MyFollowDO.class).eq(MyFollowDO::getUserId, userId).eq(MyFollowDO::getMatchType, matchType).eq(MyFollowDO::getBizId, matchId).eq(MyFollowDO::getFollowType, FollowType.MATCH.getCode())) > 0;
    }

    @Override
    @Transactional
    public boolean isFocusAnchor(Long userId, Long anchorId) {
        return myFollowMapper.selectCount(Wrappers.lambdaQuery(MyFollowDO.class).eq(MyFollowDO::getUserId, userId).eq(MyFollowDO::getBizId, anchorId).eq(MyFollowDO::getIsFocus, IsFocus.YES.getCode()).eq(MyFollowDO::getFollowType, FollowType.ANCHOR.getCode())) > 0;
    }

    @Override
    @Transactional
    public void unfriendAnchor(Long userId, Long anchorId) {
        MyFollowDO myFollowDO = myFollowMapper.selectOne(Wrappers.lambdaQuery(MyFollowDO.class).eq(MyFollowDO::getUserId, userId).eq(MyFollowDO::getBizId, anchorId).eq(MyFollowDO::getFollowType, FollowType.ANCHOR.getCode()));
        if(Objects.nonNull(myFollowDO)) {
            myFollowDO.setIsFriend(IsFriend.NO.getCode());
            myFollowMapper.updateById(myFollowDO);
        }
    }

    @Override
    public PageResponse<MyFollowDO> getMyFriendsPage(Long userId, PageRequest pageRequest) {
        LambdaQueryWrapper<MyFollowDO> queryWrapper = Wrappers.lambdaQuery(MyFollowDO.class)
                .eq(MyFollowDO::getFollowType, FollowType.ANCHOR.getCode()).eq(MyFollowDO::getUserId, userId).eq(MyFollowDO::getIsFriend, IsFriend.YES).orderByDesc(MyFollowDO::getCreateTime);
        Page<MyFollowDO> myFollowDOPage = myFollowMapper.selectPage(new Page<>(pageRequest.getCurrent(), pageRequest.getSize()), queryWrapper);
        return PageUtil.convert(myFollowDOPage, MyFollowDO.class);
    }

    @Override
    public List<MyFollowDO> getMyFriendsList(Long userId, List<Long> liveUserIds) {
        LambdaQueryWrapper<MyFollowDO> queryWrapper = Wrappers.lambdaQuery(MyFollowDO.class)
                .eq(MyFollowDO::getFollowType, FollowType.ANCHOR.getCode()).eq(MyFollowDO::getUserId, userId).eq(MyFollowDO::getIsFriend, IsFriend.YES).in(MyFollowDO::getBizId, liveUserIds);
        return myFollowMapper.selectList(queryWrapper);
    }
}
