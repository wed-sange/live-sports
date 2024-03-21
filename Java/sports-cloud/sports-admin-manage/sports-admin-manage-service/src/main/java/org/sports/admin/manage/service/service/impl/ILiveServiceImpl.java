package org.sports.admin.manage.service.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.sports.admin.manage.dao.entity.LiveDO;
import org.sports.admin.manage.dao.entity.LiveOpeninfoDO;
import org.sports.admin.manage.dao.entity.LiveUserDO;
import org.sports.admin.manage.dao.enums.CycleType;
import org.sports.admin.manage.dao.enums.LiveStatus;
import org.sports.admin.manage.dao.enums.MatchType;
import org.sports.admin.manage.dao.mapper.LiveMapper;
import org.sports.admin.manage.dao.repository.IHotMatchDao;
import org.sports.admin.manage.dao.repository.ILiveDao;
import org.sports.admin.manage.dao.repository.ILiveUserDao;
import org.sports.admin.manage.dao.req.*;
import org.sports.admin.manage.service.converter.LiveConvert;
import org.sports.admin.manage.service.service.*;
import org.sports.admin.manage.service.vo.*;
import org.sports.springboot.starter.common.toolkit.BeanUtil;
import org.sports.springboot.starter.convention.exception.ServiceException;
import org.sports.springboot.starter.convention.page.PageResponse;
import org.sports.springboot.starter.database.mybatisplus.PageUtil;
import org.sports.springboot.starter.distributedid.SnowflakeIdUtil;
import org.sports.springboot.starter.satoken.util.SotokenUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ILiveServiceImpl extends ServiceImpl<LiveMapper, LiveDO> implements ILiveService {
    @Resource
    private  ILiveDao liveDao;
    @Resource
    private  ILiveHeatConfigService configService;
    @Resource
    private  IHotMatchDao hotMatchDao;
    @Resource
    private  ILiveUserDao liveUserDao;
    @Resource
    private  ILiveOpeninfoService liveOpeninfoService;
    @Resource
    private  IChatGroupUserService chatGroupUserService;
    @Resource
    private  SdkService sdkService;
    @Resource
    private IPushMessageService pushMessageService;



    private final LiveMapper liveMapper;

    @Override
    @Transactional
    public LiveDO liveOpen(LiveOpenRequest request) {
        Long userId = SotokenUtil.getCheckUserId();
        LiveUserDO liveUserInfo = liveUserDao.getLiveUserInfo(userId);

        LiveOpeninfoDO liveOpeninfoDO = liveOpeninfoService.getById(request.getOpenInfoId());
        if (liveOpeninfoDO == null) {
            throw new ServiceException("请先编辑开播信息");
        }else{
            //配置当前此开播信息是用户正在使用的开播信息
            liveOpeninfoService.setUsedOpenInfo(request.getOpenInfoId(), userId);
        }
        //获取比赛信息
        SdkMatchVo match = sdkService.getMatchDetailById(request.getMatchId(), request.getMatchType());
        if (Objects.isNull(match)) {
            throw new ServiceException("该比赛不存在");
        }
        SdkCompetitionVo competition = sdkService.findCompetitionById(match.getCompetitionId(), request.getMatchType());
        SdkTeamVo away = sdkService.findTeamById(match.getAwayTeamId(), request.getMatchType());
        SdkTeamVo home = sdkService.findTeamById(match.getHomeTeamId(), request.getMatchType());
        if (Objects.isNull(competition) || Objects.isNull(away) || Objects.isNull(home)) {
            throw new ServiceException("该比赛异常");
        }
        LiveDO liveDO = LiveConvert.INSTANCE.liveOpenRequestToDo(request);
        //组装信息
        liveDO.setId(SnowflakeIdUtil.nextId());
        liveDO.setUserId(userId);
        liveDO.setNickName(liveUserInfo.getNickName());
        liveDO.setUserLogo(liveUserInfo.getHead());
        liveDO.setHotValue(0L);
        liveDO.setTitlePage(liveOpeninfoDO.getTitlePage());
        liveDO.setNotice(liveOpeninfoDO.getNotice());
        liveDO.setFirstMessage(liveOpeninfoDO.getFirstMessage());
        liveDO.setOpenTime(LocalDateTime.now(ZoneOffset.UTC));
        liveDO.setLiveStatus(LiveStatus.LIVING);
        liveDO.setMatchTime(match.getMatchTime());
        //主队
        liveDO.setHomeTeamName(home.getShortNameZh());
        liveDO.setHomeTeamLogo(home.getLogo());
        //客队
        liveDO.setAwayTeamName(away.getShortNameZh());
        liveDO.setAwayTeamLogo(away.getLogo());

        //赛事
        liveDO.setCompetitionId(competition.getId());
        liveDO.setCompetitionName(competition.getShortNameZh());

        LiveHeatConfigVO liveHeatConfig = configService.getLiveHeatConfig();

        if (Objects.nonNull(liveHeatConfig)) {
            //基础热度值
            liveDO.setHotValue((long) (liveHeatConfig.getBaseHeat()));
            //比赛纯净播放地址
            liveDO.setSourceUrl(liveHeatConfig.getStreamingAddress());
            if (hotMatchDao.exist(match.getCompetitionId(), request.getMatchType())) {
                //热门赛事基础热度
                liveDO.setHotValue((long) liveHeatConfig.getBaseHeatHot());
            }
        }
        if (Objects.nonNull(liveDao.selectByUserId(userId, LiveStatus.LIVING))) {
            //当前比赛、用户已开播
            throw new ServiceException("已有直播");
        }
        liveDao.insert(liveDO);
        // 发送消息 开播
        pushMessageService.startLive(liveDO);
        return liveDO;

    }

    @Override
    public void liveClose(Long userId) {
        LiveDO liveDO = liveDao.selectByUserId(userId, LiveStatus.LIVING);
        if (Objects.nonNull(liveDO)) {
            liveDO.setCloseTime(LocalDateTime.now(ZoneOffset.UTC));
            liveDO.setLiveStatus(LiveStatus.CLOSED);
            liveDao.updateById(liveDO);
            // 发送消息 关播
            pushMessageService.closeLive(liveDO.getId(), liveDO.getUserId(), liveDO.getMatchId(), liveDO.getMatchType());
        }
    }

    @Override
    public void liveCloseByAdmin(Long id) {
        LiveDO liveDO = liveDao.selectById(id);
        if (Objects.nonNull(liveDO)) {
            liveDO.setCloseTime(LocalDateTime.now(ZoneOffset.UTC));
            liveDO.setLiveStatus(LiveStatus.CLOSED);
            liveDao.updateById(liveDO);
            // 发送消息 关播
            pushMessageService.closeLive(liveDO.getId(), liveDO.getUserId(), liveDO.getMatchId(), liveDO.getMatchType());

        }
    }

    @Override
    public void updateAddress(Long userId, LiveUpdateAddressRequest request) {
        LiveDO liveDO = liveDao.selectByUserId(userId, LiveStatus.LIVING);
        if (Objects.nonNull(liveDO)) {
            liveDO.setPlayUrl(request.getPlayUrl());
            liveDao.updateById(liveDO);
            pushMessageService.updateLiveUrl(liveDO);
        }
    }

    @Override
    public List<LiveDO> getAllLiveing(List<Long> liveUserIds) {
        return liveDao.getAllLiveing(liveUserIds);
    }

    @Override
    public List<LiveVo> getLivingByUserIds(List<Long> userIds) {
        return BeanUtil.convert(liveDao.getLivingByUserIds(userIds), LiveVo.class);
    }

    @Override
    public List<LiveVo> search(Integer matchId, MatchType matchType, LiveStatus liveStatus) {
        return BeanUtil.convert(liveDao.search(matchId, matchType, liveStatus), LiveVo.class);
    }

    @Override
    public void updateLiveHotValueBatch(List<LiveDO> liveDoList) {
        this.updateBatchById(liveDoList, 200);
    }

    @Override
    public List<LiveVo> findByIds(Collection<Long> ids) {
        return BeanUtil.convert(liveDao.selectByIds(ids), LiveVo.class);
    }

    @Override
    public List<LiveVo> findLatestByUserIds(Collection<Long> userIds) {
        if (CollUtil.isEmpty(userIds)) {
            return Collections.emptyList();
        }
        return BeanUtil.convert(liveMapper.findLatestByUserIds(userIds), LiveVo.class);
    }


    @Override
    public Table<MatchType, Integer, List<LiveIdTypeVo>> getByIdsAndType(List<LiveIdTypeRequest> requests) {
        Map<MatchType, Map<Integer, List<LiveDO>>> search = liveDao.search(requests);
        Table<MatchType, Integer, List<LiveIdTypeVo>> result = HashBasedTable.create();
        search.forEach((matchType, values) ->
                values.forEach((matchId, lives) ->
                        result.put(matchType, matchId, BeanUtil.convert(lives, LiveIdTypeVo.class))));
        return result;
    }

    @Override
    public PageResponse<LiveVo> getLivePage(LivePageRequest pageRequest) {
        //先判断主播昵称模糊查询
        List<Long> userIdList = null;
        if (Strings.isNotBlank(pageRequest.getNickName())) {
            LiveUserDO liveUserDO = new LiveUserDO();
            liveUserDO.setNickName(pageRequest.getNickName());
            userIdList = liveUserDao.getLiveUserFilter(liveUserDO);
            //假如为空 直接返回
            if (CollectionUtils.isEmpty(userIdList)) {
                PageResponse<LiveVo> liveVoPage = new PageResponse<>();
                liveVoPage.setTotal(0L);
                liveVoPage.setRecords(new ArrayList<>());
                return liveVoPage;
            }
        }
        PageResponse<LiveDO> liveDoPage = liveDao.getLivePage(pageRequest, userIdList);
        PageResponse<LiveVo> liveVOPage = liveDoPage.convert(each -> BeanUtil.convert(each, LiveVo.class));
        //结果处理
        List<LiveVo> liveList = liveVOPage.getRecords();
        if (!CollectionUtils.isEmpty(liveList)) {
            //直播中在线直播用户人数
            if (LiveStatus.LIVING.equals(pageRequest.getLiveStatus())) {
                List<String> liveIds = liveList.stream().map(i -> i.getId().toString()).collect(Collectors.toList());
                Map<String, Integer> collect = chatGroupUserService.getLiveUserByGroupIds(liveIds);
                liveList.forEach(item -> {
                    item.setOnlineUser(collect.getOrDefault(item.getId().toString(), 0));
                });
            }
            //用户信息封装
            List<Long> userIds = liveList.stream().map(LiveVo::getUserId).distinct().collect(Collectors.toList());
            List<LiveUserDO> liveUserList = liveUserDao.getLiveUserList(userIds);
            Map<Long, LiveUserDO> liveUserMap = liveUserList.stream().collect(Collectors.toMap(LiveUserDO::getId, i -> i));
            for (LiveVo liveVo : liveList) {
                if (liveUserMap.containsKey(liveVo.getUserId())) {
                    liveVo.setNickName(liveUserMap.get(liveVo.getUserId()).getNickName());
                    liveVo.setUserLogo(liveUserMap.get(liveVo.getUserId()).getHead());
                }
            }
        }
        return liveVOPage;
    }

    @Override
    public LiveStatisticsVO getLiveStatistics(Long userId, Integer type) {
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;
        if (CycleType.CURRENT_MONTH.getCode().equals(type)) {
            endTime = LocalDateTime.now(ZoneOffset.UTC);
            startTime = endTime.withDayOfMonth(1);
        } else if (CycleType.NEARLY_THREE_MONTH.getCode().equals(type)) {
            endTime = LocalDateTime.now(ZoneOffset.UTC);
            startTime = endTime.minusMonths(3);
        }
        long count = liveDao.getLiveTimes(userId, startTime, endTime);
        long hours = liveDao.getLiveHours(userId, startTime, endTime);
        long maxLiveUser = chatGroupUserService.getMaxLiveUser(userId, startTime, endTime);
        long totalLiveUser = chatGroupUserService.getTotalLiveUser(userId, startTime, endTime);
        return LiveStatisticsVO.builder().totalLiveCount(count).totalLiveHours(hours).maxLiveUserCount(maxLiveUser).totalLiveUsers(totalLiveUser).build();
    }

    @Override
    public PageResponse<HomeLivingVo> living(LiveSearchRequest request) {
        Page<LiveDO> search = liveDao.search(request, LiveStatus.LIVING);
        return PageUtil.convert(search, HomeLivingVo.class);
    }

    @Override
    public List<LiveVo> getHotMatchLive(MatchType matchType, LiveStatus liveStatus, Collection<Integer> competitionIds, Integer top) {
        return BeanUtil.convert(liveDao.selectFirstOpenLive(matchType, liveStatus, competitionIds, top), LiveVo.class);
    }

    @Override
    public HomeLivingInfoVo livingInfo(Long id) {
        LiveDO liveDO = liveDao.selectById(id);
        Assert.notNull(liveDO, "该直播不存在");
        HomeLivingInfoVo convert = BeanUtil.convert(liveDO, HomeLivingInfoVo.class);
        //直播源
        List<LiveDO> infos = liveDao.selectByMatchId(liveDO.getMatchId(), liveDO.getMatchType(), LiveStatus.LIVING, Collections.singleton(id));
        convert.setInfos(BeanUtil.convert(infos, HomeLivingInfoVo.class));
        convert.setFirstMessage(handleFirstMessage(convert.getNickName(),convert.getFirstMessage()));
        convert.setNotice(handleNotice(convert.getNotice()));
        return convert;
    }

    private String handleNotice(String notice) {
        if(Strings.isBlank(notice)){
            return null;
        }
        notice = notice.replaceAll("<a ", "<a style='color:#94999F;' ");
        return notice;
    }

    public  String handleFirstMessage(String nickName, String firstMessage) {
        if(Strings.isBlank(firstMessage)){
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
    public List<LiveVo> selectByMatchId(Integer matchId, MatchType matchType, LiveStatus liveStatus, Collection<Long> exceptId) {
        return BeanUtil.convert(liveDao.selectByMatchId(matchId, matchType, liveStatus, exceptId), LiveVo.class);
    }

}
