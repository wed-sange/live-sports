package org.sports.admin.manage.dao.repository.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.sports.admin.manage.dao.entity.LiveDO;
import org.sports.admin.manage.dao.enums.LiveStatus;
import org.sports.admin.manage.dao.enums.MatchType;
import org.sports.admin.manage.dao.mapper.LiveMapper;
import org.sports.admin.manage.dao.repository.ILiveDao;
import org.sports.admin.manage.dao.req.LiveIdTypeRequest;
import org.sports.admin.manage.dao.req.LivePageRequest;
import org.sports.admin.manage.dao.req.LiveSearchRequest;
import org.sports.springboot.starter.convention.page.PageResponse;
import org.sports.springboot.starter.database.mybatisplus.PageUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ILiveDaoImpl implements ILiveDao {
    @Resource
    private LiveMapper mapper;

    /**
     * 这里出现一个主播同时开播的情况，开播前需要关闭正在开播的。
     *
     * @param liveDO
     */
    @Override
    @Transactional
    public void insert(LiveDO liveDO) {
        mapper.update(null, new LambdaUpdateWrapper<LiveDO>().eq(LiveDO::getUserId, liveDO.getUserId()).eq(LiveDO::getLiveStatus, LiveStatus.LIVING)
                .set(LiveDO::getLiveStatus, LiveStatus.CLOSED));
        mapper.insert(liveDO);
    }

    @Override
    public void delete(Long id) {
        mapper.deleteById(id);
    }

    @Override
    public LiveDO selectById(Long id) {
        return mapper.selectById(id);
    }

    @Override
    public List<LiveDO> selectByIds(Collection<Long> ids) {
        return mapper.selectBatchIds(ids);
    }

    @Override
    public LiveDO selectByUserId(Long userId, Long id) {
        return mapper.selectOne(Wrappers.lambdaQuery(LiveDO.class)
                .eq(Objects.nonNull(id), LiveDO::getId, id)
                .eq(Objects.nonNull(userId), LiveDO::getUserId, userId)
        );
    }

    @Override
    public LiveDO selectByUserId(Long userId, LiveStatus liveStatus) {
        return this.selectByUserId(userId, null, liveStatus);
    }

    @Override
    public LiveDO selectByUserId(Long userId, MatchType matchType, LiveStatus liveStatus) {
        return mapper.selectOne(Wrappers.lambdaQuery(LiveDO.class)
                .eq(Objects.nonNull(userId), LiveDO::getUserId, userId)
                .eq(Objects.nonNull(matchType), LiveDO::getMatchType, matchType)
                .eq(Objects.nonNull(liveStatus), LiveDO::getLiveStatus, liveStatus)
        );
    }

    @Override
    public List<LiveDO> topHot(MatchType matchType, LiveStatus liveStatus, Collection<Integer> competitionIds, Integer limit) {
        List<LiveDO> liveDOS = mapper.selectTopHot(matchType, competitionIds, liveStatus, limit);

        Map<String, Object> distinct = new HashMap<>();
        //有可能需要去重
        // SET sql_mode=(SELECT REPLACE(@@sql_mode,'ONLY_FULL_GROUP_BY',''));
        return liveDOS.stream().filter(item -> {
            String key = item.getMatchId() + "-" + item.getHotValue() + "-" + item.getMatchType();
            if (distinct.containsKey(key)) {
                return false;
            } else {
                distinct.put(key, null);
            }
            return true;
        }).collect(Collectors.toList());
    }

    @Override
    public List<LiveDO> selectFirstOpenLive(MatchType matchType, LiveStatus liveStatus, Collection<Integer> competitionIds, Integer limit) {
        List<LiveDO> liveDOS = mapper.selectFirstOpenLive(matchType, competitionIds, liveStatus, limit);

        Map<String, Object> distinct = new HashMap<>();
        //有可能需要去重
        // SET sql_mode=(SELECT REPLACE(@@sql_mode,'ONLY_FULL_GROUP_BY',''));
        return liveDOS.stream().filter(item -> {
            String key = item.getMatchId() + "-" + item.getHotValue() + "-" + item.getMatchType();
            if (distinct.containsKey(key)) {
                return false;
            } else {
                distinct.put(key, null);
            }
            return true;
        }).collect(Collectors.toList());
    }

    @Override
    public LiveDO selectByUserId(Long userId, Integer matchId, MatchType matchType, LiveStatus liveStatus) {
        return mapper.selectOne(Wrappers.lambdaQuery(LiveDO.class)
                .eq(Objects.nonNull(userId), LiveDO::getUserId, userId)
                .eq(Objects.nonNull(matchId), LiveDO::getMatchId, matchId)
                .eq(Objects.nonNull(matchType), LiveDO::getMatchType, matchType)
                .eq(Objects.nonNull(liveStatus), LiveDO::getLiveStatus, liveStatus)
        );
    }

    @Override
    public Map<Integer, List<LiveDO>> selectByUserId(Long userId, Collection<Integer> matchIds, MatchType matchType, LiveStatus liveStatus) {
        return mapper.selectList(Wrappers.lambdaQuery(LiveDO.class)
                .eq(LiveDO::getUserId, userId)
                .in(!CollectionUtils.isEmpty(matchIds), LiveDO::getMatchId, matchIds)
                .eq(Objects.nonNull(matchType), LiveDO::getMatchType, matchType)
                .eq(LiveDO::getLiveStatus, liveStatus)
        ).stream().collect(Collectors.groupingBy(LiveDO::getMatchId));
    }

    @Override
    public List<LiveDO> selectByUserId(Long userId) {
        return mapper.selectList(Wrappers.lambdaQuery(LiveDO.class)
                .eq(LiveDO::getUserId, userId)
        );
    }

    @Override
    public void updateById(LiveDO liveDO) {
        mapper.updateById(liveDO);
    }

    @Override
    public Page<LiveDO> search(LiveSearchRequest request, LiveStatus liveStatus) {
        //获取热度排名
        return mapper.selectPage(new Page<>(request.getCurrent(), request.getSize()), Wrappers.lambdaQuery(LiveDO.class)
                .eq(Objects.nonNull(request.getMatchType()), LiveDO::getMatchType, request.getMatchType())
                .eq(Objects.nonNull(liveStatus), LiveDO::getLiveStatus, liveStatus)
                //指定比赛ID的直播列表
                .eq(Objects.nonNull(request.getMatchId()), LiveDO::getMatchId, request.getMatchId())
                //排除的直播
                .ne(Objects.nonNull(request.getExceptId()), LiveDO::getId, request.getExceptId())
                .and(Objects.nonNull(request.getName()), liveDOLambdaQueryWrapper -> liveDOLambdaQueryWrapper.in(CollectionUtil.isNotEmpty(request.getAnchorIds()), LiveDO::getUserId, request.getAnchorIds())
                        .or(liveDOLambdaQueryWrapper1 -> liveDOLambdaQueryWrapper1.like(LiveDO::getHomeTeamName, request.getName()))
                        .or(liveDOLambdaQueryWrapper2 -> liveDOLambdaQueryWrapper2.like(LiveDO::getAwayTeamName, request.getName())))
                //热度排名
                .orderByDesc(LiveDO::getHotValue)
        );
    }

    @Override
    public List<LiveDO> search(Integer matchId, MatchType matchType, LiveStatus liveStatus) {

        return mapper.selectList(Wrappers.lambdaQuery(LiveDO.class)
                .eq(Objects.nonNull(matchId), LiveDO::getMatchId, matchId)
                .eq(Objects.nonNull(matchType), LiveDO::getMatchType, matchType)
                .eq(LiveDO::getLiveStatus, liveStatus).orderByDesc(LiveDO::getHotValue)
        );
    }

    @Override
    public Map<MatchType, Map<Integer, List<LiveDO>>> search(List<LiveIdTypeRequest> requests) {
        Map<MatchType, List<Integer>> collect = requests.stream().collect(Collectors.groupingBy(LiveIdTypeRequest::getMatchType,
                Collectors.mapping(LiveIdTypeRequest::getMatchId, Collectors.toList())));
        Map<MatchType, Map<Integer, List<LiveDO>>> result = new HashMap<>();
        collect.forEach((matchType, matchIds) -> {
            List<LiveDO> liveDOS = mapper.selectList(Wrappers.lambdaQuery(LiveDO.class)
                    .in(LiveDO::getMatchId, matchIds)
                    .eq(LiveDO::getMatchType, matchType)
                    .eq(LiveDO::getLiveStatus, LiveStatus.LIVING));

            result.put(matchType, liveDOS.stream().collect(Collectors.groupingBy(LiveDO::getMatchId)));

        });
        return result;
    }

    @Override
    public void saveHotValue(Long id, Long hotValue) {
        mapper.update(null, new LambdaUpdateWrapper<LiveDO>()
                .eq(LiveDO::getId, id)
                .set(LiveDO::getHotValue, hotValue));
    }

    @Override
    public List<LiveDO> getAllLiveing(List<Long> liveUserIds) {
        return mapper.selectList(new LambdaQueryWrapper<LiveDO>()
                .eq(LiveDO::getLiveStatus, LiveStatus.LIVING)
                .in(CollectionUtil.isNotEmpty(liveUserIds), LiveDO::getUserId, liveUserIds)
                .orderByDesc(LiveDO::getOpenTime));
    }

    /**
     * 这里查询状态处理逻辑：
     * 进行中：直接根据表状态查询
     * 未开始的：需要查询的是用户最近一次已结束的直播内容
     *
     * @param pageRequest
     * @return
     */
    @Override
    public PageResponse<LiveDO> getLivePage(LivePageRequest pageRequest, List<Long> userIdList) {
        if (LiveStatus.NOT_START.equals(pageRequest.getLiveStatus())) {
            //查询直播用户最新一条
            LambdaQueryWrapper<LiveDO> queryLatestWrapper = new QueryWrapper<LiveDO>().select("max(id) as id,user_id,max(create_time) as create_time").lambda()
                    .notInSql(LiveDO::getUserId, "SELECT DISTINCT user_id FROM t_live WHERE live_status = 2")
                    .groupBy(LiveDO::getUserId).orderByDesc(LiveDO::getCreateTime);

            Page<LiveDO> notStartPage = mapper.selectPage(new Page<>(pageRequest.getCurrent(), pageRequest.getSize()), queryLatestWrapper);
            List<Long> ids = notStartPage.getRecords().stream().map(LiveDO::getId).collect(Collectors.toList());
            List<LiveDO> liveDOS = Lists.newArrayList();
            if (!CollectionUtils.isEmpty(ids)) {
                LambdaQueryWrapper<LiveDO> queryWrapper = Wrappers.lambdaQuery(LiveDO.class)
                        .in(LiveDO::getId, ids);
                liveDOS = mapper.selectList(queryWrapper);
            }
            PageResponse newsDOPage = PageResponse.builder()
                    .current(pageRequest.getCurrent())
                    .size(pageRequest.getSize())
                    .total(notStartPage.getTotal())
                    .build();
            newsDOPage.setRecords(liveDOS);
            return newsDOPage;

        } else {
            LambdaQueryWrapper<LiveDO> queryWrapper = Wrappers.lambdaQuery(LiveDO.class)
                    .eq(Objects.nonNull(pageRequest.getLiveStatus()), LiveDO::getLiveStatus, pageRequest.getLiveStatus())
                    .in(Strings.isNotBlank(pageRequest.getNickName()), LiveDO::getUserId, userIdList)
                    .eq(Objects.nonNull(pageRequest.getMatchType()), LiveDO::getMatchType, pageRequest.getMatchType())
                    .orderByAsc(LiveDO::getCreateTime);
            Page<LiveDO> newsDOPage = mapper.selectPage(new Page<>(pageRequest.getCurrent(), pageRequest.getSize()), queryWrapper);
            return PageUtil.convert(newsDOPage, LiveDO.class);
        }


    }

    @Override
    public long getLiveTimes(Long userId, LocalDateTime startTime, LocalDateTime endTime) {
        LambdaQueryWrapper<LiveDO> queryWrapper = Wrappers.lambdaQuery(LiveDO.class)
                .eq(LiveDO::getUserId, userId);
        if (Objects.nonNull(startTime) && Objects.nonNull(endTime)) {
            queryWrapper.between(LiveDO::getOpenTime, startTime, endTime);
        }
        return mapper.selectCount(queryWrapper).longValue();

    }

    @Override
    public long getLiveHours(Long userId, LocalDateTime startTime, LocalDateTime endTime) {
        LambdaQueryWrapper<LiveDO> queryWrapper = Wrappers.lambdaQuery(LiveDO.class)
                .eq(LiveDO::getUserId, userId);
        if (Objects.nonNull(startTime) && Objects.nonNull(endTime)) {
            queryWrapper.between(LiveDO::getOpenTime, startTime, endTime);
        }
        List<LiveDO> list = mapper.selectList(queryWrapper);

        long totalHours = 0;
        for (LiveDO entity : list) {
            if (Objects.isNull(entity.getOpenTime())) {
                continue;
            }
            LocalDateTime start = entity.getOpenTime();
            LocalDateTime end = entity.getCloseTime();
            if (Objects.isNull(end)) {
                end = LocalDateTime.now(ZoneOffset.UTC);
            }
            Duration duration = Duration.between(start, end);
            totalHours += duration.toHours();
        }
        return totalHours;
    }

    @Override
    public List<LiveDO> getLivingByUserIds(List<Long> userIds) {
        return mapper.selectList(new LambdaQueryWrapper<LiveDO>()
                .eq(LiveDO::getLiveStatus, LiveStatus.LIVING)
                .in(LiveDO::getUserId, userIds));
    }

    @Override
    public List<LiveDO> selectByMatchId(Integer matchId, MatchType matchType, LiveStatus liveStatus, Collection<Long> exceptId) {
        return mapper.selectList(Wrappers.lambdaQuery(LiveDO.class)
                .eq(Objects.nonNull(matchId), LiveDO::getMatchId, matchId)
                .eq(Objects.nonNull(matchType), LiveDO::getMatchType, matchType)
                .eq(Objects.nonNull(liveStatus), LiveDO::getLiveStatus, liveStatus)
                .notIn(!CollectionUtils.isEmpty(exceptId), LiveDO::getId, exceptId)
        );
    }
}
