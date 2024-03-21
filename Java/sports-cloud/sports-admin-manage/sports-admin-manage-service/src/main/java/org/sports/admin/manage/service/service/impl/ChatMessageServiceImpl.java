package org.sports.admin.manage.service.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.exceptions.ValidateException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.sports.admin.manage.dao.entity.*;
import org.sports.admin.manage.dao.enums.ChatMsgTypeEnum;
import org.sports.admin.manage.dao.enums.ChatType;
import org.sports.admin.manage.dao.enums.IdentityType;
import org.sports.admin.manage.dao.enums.LiveStatus;
import org.sports.admin.manage.dao.mapper.ChatMessageMapper;
import org.sports.admin.manage.dao.req.*;
import org.sports.admin.manage.service.enums.UserGrowthTypeEnum;
import org.sports.admin.manage.service.enums.YnEnum;
import org.sports.admin.manage.service.service.*;
import org.sports.admin.manage.service.vo.*;
import org.sports.springboot.starter.common.enums.DelEnum;
import org.sports.springboot.starter.convention.page.PageRequest;
import org.sports.springboot.starter.convention.page.PageResponse;
import org.sports.springboot.starter.database.mybatisplus.PageUtil;
import org.sports.springboot.starter.satoken.enums.UserChannelEnum;
import org.sports.springboot.starter.satoken.util.SotokenUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 群组用户信息 服务实现类
 * </p>
 *

 * @since 2023-07-26
 */
@Service
public class ChatMessageServiceImpl extends ServiceImpl<ChatMessageMapper, ChatMessageDO> implements IChatMessageService {

    @Resource
    private IAppUserService appUserService;
    @Resource
    private ChatMessageMapper chatMessageMapper;
    @Resource
    private ILiveUserService liveUserService;
    @Resource
    private ILiveService liveService;

    @Resource
    private IAppPinnedUserService appPinnedUserService;

    @Resource
    private ILiveOpeninfoService liveOpeninfoService;

    @Override
    public Map<Long, Integer> countUserChatMsgNumByNew(LocalDateTime endTime) {
        List<Map<String, Object>> mapList = this.listMaps(new QueryWrapper<ChatMessageDO>()
                .select("from_id as fromId,count(id) as chatMsgNum")
                .groupBy("from_id")
                .eq("identity_type", 0) //0 APP用户发送
                .eq("chat_type", ChatType.CHAT_TYPE_PUBLIC.getNumber())
                .notExists("select 1 from t_app_user_growth aug where aug.type = " + UserGrowthTypeEnum.LIVE_CHAT.getType() + " and aug.user_id = t_chat_message.from_id")
                .le("create_time", endTime));
        return dealCountUserChatMsgNum(mapList);
    }

    @Override
    public Map<Long, Integer> countUserChatMsgNumByLeave(LocalDateTime beginTime, LocalDateTime endTime) {
        List<Map<String, Object>> mapList = this.listMaps(new QueryWrapper<ChatMessageDO>()
                .select("from_id as fromId,count(id) as chatMsgNum")
                .groupBy("from_id")
                .eq("identity_type", 0) //0 APP用户发送
                .eq("chat_type", ChatType.CHAT_TYPE_PUBLIC.getNumber())
                .exists("select 1 from t_app_user_growth aug where aug.type = " + UserGrowthTypeEnum.LIVE_CHAT.getType() + " and aug.user_id = t_chat_message.from_id")
                .gt("create_time", beginTime)
                .le("create_time", endTime));
        return dealCountUserChatMsgNum(mapList);
    }

    /**
     * 用户消息互动统计结果处理
     *
     * @param mapList
     * @return
     */
    private Map<Long, Integer> dealCountUserChatMsgNum(List<Map<String, Object>> mapList) {
        if (CollectionUtils.isEmpty(mapList)) {
            return null;
        }
        Map<Long, Integer> backMap = new HashMap<>();
        for (Map<String, Object> map : mapList) {
            backMap.put(Long.valueOf(String.valueOf(map.get("fromId"))), Integer.valueOf(String.valueOf(map.get("chatMsgNum"))));
        }
        return backMap;
    }

    @Override
    public PageResponse<ChatMessageVO> queryMsgByPage(AppUserMsgPageRequest pageRequest, Long userId) {
        Page<ChatMessageDO> page;
        if (Strings.isEmpty(pageRequest.getUserName())) {
            page = this.baseMapper.queryMsgByPage1(new Page<>(pageRequest.getCurrent(), pageRequest.getSize()), userId);
        } else {
            page = this.baseMapper.queryMsgByPage2(new Page<>(pageRequest.getCurrent(), pageRequest.getSize()), userId, pageRequest.getUserName().trim());
        }
        return PageUtil.convert(page, ChatMessageVO.class);
    }

    @Override
    public Map<String, Integer> countNoReadMsg(List<String> anchorIdList, Long userId) {
        List<Map<String, Object>> mapList = this.listMaps(new QueryWrapper<ChatMessageDO>()
                .select("anchor_id as anchorId, count(1) as noreadSum")
                .eq("to_id", userId)
                .eq("chat_type", ChatMsgTypeEnum.PRIVATE.getValue()) //1:公聊、2私聊
                .eq("readable", YnEnum.ZERO.getValue()) // 是否已读：0 未读 1 已读
                .ne("user_del", YnEnum.ONE.getValue())
                .in("anchor_id", anchorIdList)
                .groupBy("anchor_id"));
        Map<String, Integer> backMap = new HashMap<>();
        if (CollectionUtils.isEmpty(mapList)) {
            return backMap;
        }
        for (Map<String, Object> map : mapList) {
            backMap.put(String.valueOf(map.get("anchorId")), Integer.valueOf(String.valueOf(map.get("noreadSum"))));
        }
        return backMap;
    }


    @Override
    public void delRecordsByAnchorId(Long userId, String anchorId) {
        this.update(new LambdaUpdateWrapper<ChatMessageDO>()
                .and(i -> i.eq(ChatMessageDO::getToId, userId.toString()).or().eq(ChatMessageDO::getFromId, userId.toString()))
                .eq(ChatMessageDO::getAnchorId, anchorId)
                .eq(ChatMessageDO::getChatType, ChatMsgTypeEnum.PRIVATE.getValue())
                .set(ChatMessageDO::getUserDel, YnEnum.ONE.getValue())
                .set(ChatMessageDO::getUpdateTime, LocalDateTime.now(ZoneOffset.UTC)));
    }

    @Override
    public void cancelAllUnreadMsgByUserId(Long userId, String anchorId) {
        this.update(new LambdaUpdateWrapper<ChatMessageDO>()
                .eq(ChatMessageDO::getToId, userId)
                .eq(ChatMessageDO::getAnchorId, anchorId)
                .eq(ChatMessageDO::getChatType, ChatMsgTypeEnum.PRIVATE.getValue())
                .eq(ChatMessageDO::getReadable, YnEnum.ZERO.getValue())
                .set(ChatMessageDO::getReadable, YnEnum.ONE.getValue())
                .set(ChatMessageDO::getUpdateTime, LocalDateTime.now(ZoneOffset.UTC)));
    }

    @Override
    public void cancelAllUnreadMsgByUserId(Long userId) {
        this.update(new LambdaUpdateWrapper<ChatMessageDO>()
                .eq(ChatMessageDO::getToId, userId)
                .eq(ChatMessageDO::getChatType, ChatMsgTypeEnum.PRIVATE.getValue())
                .eq(ChatMessageDO::getReadable, YnEnum.ZERO.getValue())
                .set(ChatMessageDO::getReadable, YnEnum.ONE.getValue())
                .set(ChatMessageDO::getUpdateTime, LocalDateTime.now(ZoneOffset.UTC)));
    }

    @Override
    public PageResponse<ChatMessageVO2> getPrivateMsgPage(ChatMessagePageRequest req) {
        LambdaQueryWrapper queryWrapper = new LambdaQueryWrapper<ChatMessageDO>()
                .eq(ChatMessageDO::getChatType, ChatMsgTypeEnum.PRIVATE.getValue())
                .like(Strings.isNotBlank(req.getNick()), ChatMessageDO::getNick, req.getNick())
                .like(Strings.isNotBlank(req.getContent()), ChatMessageDO::getContent, req.getContent())
                .eq(req.getIdentityType() != null, ChatMessageDO::getIdentityType, req.getIdentityType())
                .orderByDesc(ChatMessageDO::getCreateTime, ChatMessageDO::getId);

        Page<ChatMessageDO> page = this.baseMapper.selectPage(new Page<>(req.getCurrent(), req.getSize()), queryWrapper);
        PageResponse<ChatMessageVO2> chatMessageVO2Page = PageUtil.convert(page, ChatMessageVO2.class);
        List<String> toUserIdList = chatMessageVO2Page.getRecords().stream().map(ChatMessageVO2::getToId).distinct().collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(toUserIdList)) {
            List<AppUserDO> appUserDOList = appUserService.list(new LambdaQueryWrapper<AppUserDO>()
                    .select(AppUserDO::getId, AppUserDO::getName)
                    .in(AppUserDO::getId, toUserIdList));
            Map<Long, String> appUserMap = appUserDOList.stream().collect(Collectors.toMap(AppUserDO::getId, AppUserDO::getName));
            for (ChatMessageVO2 record : chatMessageVO2Page.getRecords()) {
                if (appUserMap.containsKey(Long.valueOf(record.getToId()))) {
                    record.setToName(appUserMap.get(Long.valueOf(record.getToId())));
                }
            }
        }

        return chatMessageVO2Page;
    }

    @Override
    public PageResponse<LiveChatMessageVO> queryPrivateLivePage(PageRequest pageRequest) {
        Long loginUserId = SotokenUtil.getCheckUserId();
        List<LiveUserVO> anchorUserVOS = liveUserService.queryLiveUserByOperate(loginUserId);
        if(CollectionUtils.isEmpty(anchorUserVOS)){
            return null;
        }
        /**
         * 查询运营设置置顶操作
         */
        List<AppPinnedUserDO> appPinnedUserDOS = appPinnedUserService.queryAnchorSetTopList(loginUserId);
        List<String> anchorTopList = new ArrayList<>();
        Map<Long, AppPinnedUserDO> appPinnedMap = new HashMap<>();
        if(CollectionUtil.isNotEmpty(appPinnedUserDOS)){
            anchorTopList.addAll(appPinnedUserDOS.stream().map(item-> String.valueOf(item.getAnchorId())).collect(Collectors.toList()));
            appPinnedMap = appPinnedUserDOS.stream().collect(Collectors.toMap(AppPinnedUserDO::getAnchorId, Function.identity()));
        }
        Page<ChatMessageDO> page = this.baseMapper.queryPrivateLivePage(new Page<>(pageRequest.getCurrent(), pageRequest.getSize()),anchorUserVOS.stream().map(item-> String.valueOf(item.getId())).collect(Collectors.toList()),anchorTopList);
        PageResponse<LiveChatMessageVO> pageResponse = PageUtil.convert(page, LiveChatMessageVO.class);
        if (!CollectionUtils.isEmpty(pageResponse.getRecords())) {
            List<LiveChatMessageVO> messageVOList = pageResponse.getRecords();
            //主播信息查询
            List<String> anchorIdStrList = messageVOList.stream().map(ChatMessageVO::getAnchorId).distinct().collect(Collectors.toList());
            List<Long> anchorIdList = anchorIdStrList.stream().map(i -> Long.valueOf(i)).collect(Collectors.toList());
            List<LiveUserVO> liveUserVoList = liveUserService.getLiveUserList(anchorIdList);
            Map<Long, LiveUserVO> liveUserVoMap = liveUserVoList.stream().collect(Collectors.toMap(LiveUserVO::getId, i -> i));

            //查询当前直播中的live
            List<LiveDO> liveDoList = liveService.getAllLiveing(anchorIdList);
            Map<Long, LiveDO> liveDOMap = liveDoList.stream().collect(Collectors.toMap(LiveDO::getUserId, Function.identity()));
            //未读红点查询
            List<Map<String, Object>> mapList = this.listMaps(new QueryWrapper<ChatMessageDO>()
                    .select("anchor_id as anchorId, count(1) as noreadSum")
                    .in("to_id", anchorIdList)
                    .in("anchor_id", anchorIdList)
                    .eq("chat_type", ChatMsgTypeEnum.PRIVATE.getValue()) //1:公聊、2私聊
                    .eq("readable", YnEnum.ZERO.getValue()) // 是否已读：0 未读 1 已读
                    .eq("anchor_del", YnEnum.ZERO.getValue())
                    .groupBy("anchor_id"));
            Map<String, Integer> noReadMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(mapList)) {
                for (Map<String, Object> map : mapList) {
                    noReadMap.put(String.valueOf(map.get("anchorId")), Integer.valueOf(String.valueOf(map.get("noreadSum"))));
                }
            }
            //主播信息/未读红点处理
            Long anchorId;
            for (LiveChatMessageVO record : messageVOList) {
                anchorId = Long.valueOf(record.getAnchorId());
                if (liveUserVoMap.containsKey(anchorId)) {
                    record.setNick(liveUserVoMap.get(anchorId).getNickName());
                    record.setAvatar(liveUserVoMap.get(anchorId).getHead());
                }
                record.setNoReadSum(noReadMap.getOrDefault(record.getAnchorId(), 0));
                LiveDO liveDO = liveDOMap.get(Long.valueOf(record.getAnchorId()));
                if (Objects.isNull(liveDO)) {
                    record.setLiveStatus(LiveStatus.NOT_START);
                } else {
                    record.setLiveStatus(LiveStatus.LIVING);
                }
                AppPinnedUserDO appPinnedUserDO = appPinnedMap.get(Long.valueOf(record.getAnchorId()));
                if(Objects.nonNull(appPinnedUserDO)){
                    record.setSetTop(true);
                    record.setSetTopTime(appPinnedUserDO.getCreateTime());
                }else {
                    record.setSetTop(false);
                }
            }
            //排序，倒序排列 置顶时间
            List<LiveChatMessageVO> resultList = Lists.newArrayList();
            List<LiveChatMessageVO> topList = messageVOList.stream().filter(item-> Objects.nonNull(item.getSetTopTime())).collect(Collectors.toList());
            topList.sort(Comparator.comparing(LiveChatMessageVO::getSetTopTime).reversed());
            messageVOList.removeAll(topList);
            resultList.addAll(topList);
            resultList.addAll(messageVOList);
            pageResponse.setRecords(resultList);
        }
        return pageResponse;
    }

    @Override
    public PageResponse<LiveChatMessageVO> queryLivePrivateById(Long liveId) {
        PageResponse<LiveChatMessageVO> pageResponse = new PageResponse<>(1, 10, 0L);
        List<ChatMessageDO> messageDOList = this.list(new LambdaQueryWrapper<ChatMessageDO>()
                .eq(ChatMessageDO::getAnchorId, liveId)
                .eq(ChatMessageDO::getChatType, ChatMsgTypeEnum.PRIVATE.getValue())
                .eq(ChatMessageDO::getAnchorDel, DelEnum.NORMAL.code())
                .orderByDesc(ChatMessageDO::getCreateTime)
                .last("LIMIT 1"));
        if (!CollectionUtils.isEmpty(messageDOList)) {
            pageResponse.setTotal(1L);
            LiveChatMessageVO chatMessageVO = new LiveChatMessageVO();
            chatMessageVO.setId(messageDOList.get(0).getId());
            chatMessageVO.setCreateTime(messageDOList.get(0).getCreateTime().getTime());
            chatMessageVO.setAnchorId(liveId.toString());
            chatMessageVO.setMsgType(messageDOList.get(0).getMsgType());
            //主播信息查询
            LiveUserVO liveUserVO = liveUserService.getLiveUserInfo(liveId);
            chatMessageVO.setNick(liveUserVO.getNickName());
            chatMessageVO.setAvatar(liveUserVO.getHead());
            //未读红点查询
            long noRead = this.count(new QueryWrapper<ChatMessageDO>()
                    .eq("to_id", liveId)
                    .eq("anchor_id", liveId)
                    .eq("chat_type", ChatMsgTypeEnum.PRIVATE.getValue()) //1:公聊、2私聊
                    .eq("readable", YnEnum.ZERO.getValue()) // 是否已读：0 未读 1 已读
                    .eq("anchor_del", YnEnum.ZERO.getValue()));
            chatMessageVO.setNoReadSum(Integer.parseInt(Long.toString(noRead)));
            if (liveService.getAllLiveing(Collections.singletonList(liveId)).size() > 0) {
                chatMessageVO.setLiveStatus(LiveStatus.LIVING);
            } else {
                chatMessageVO.setLiveStatus(LiveStatus.NOT_START);
            }
            pageResponse.setRecords(Collections.singletonList(chatMessageVO));
        }
        return pageResponse;
    }

    /**
     * 如果运营账号需要查询运营账号配置的主播列表
     * 原需求：返回正在直播的主播列表
     * 20240314修改需求：正在直播和未直播的主播都返回
     *
     * @param operateId
     * @return
     */
    @Override
    public List<LivingUserVO> queryPublicLivePage(Long operateId) {

        List<LiveUserVO> anchorUserVOS = liveUserService.queryLiveUserByOperate(operateId);
        //查询当前直播中的live
        List<LiveDO> liveDoList = null;
        if (CollectionUtils.isEmpty(anchorUserVOS)) {
            return null;
        } else {
            liveDoList = liveService.getAllLiveing(anchorUserVOS.stream().map(LiveUserVO::getId).collect(Collectors.toList()));
        }
        List<LivingUserVO> backList = new ArrayList<>();
        Map<String, Integer> noReadMap = new HashMap<>();
        Map<Long, LiveDO> liveDOMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(liveDoList)) {
            liveDOMap = liveDoList.stream().collect(Collectors.toMap(LiveDO::getUserId, Function.identity()));
            //未读红点查询
            List<Long> anchorIdList = liveDoList.stream().map(LiveDO::getUserId).collect(Collectors.toList());
            List<Long> groupIdList = liveDoList.stream().map(LiveDO::getId).collect(Collectors.toList());
            List<Map<String, Object>> mapList = this.listMaps(new QueryWrapper<ChatMessageDO>()
                    .select("anchor_id as anchorId, count(1) as noreadSum")
                    .in("anchor_id", anchorIdList)
                    .in("group_id", groupIdList)
                    .eq("identity_type", 0) //0普通用户
                    .eq("chat_type", ChatMsgTypeEnum.PUBLIC.getValue()) //1:公聊、2私聊
                    .eq("readable", YnEnum.ZERO.getValue()) // 是否已读：0 未读 1 已读
                    .eq("del_flag", YnEnum.ZERO.getValue())
                    .groupBy("anchor_id"));

            if (!CollectionUtils.isEmpty(mapList)) {
                for (Map<String, Object> map : mapList) {
                    noReadMap.put(String.valueOf(map.get("anchorId")), Integer.valueOf(String.valueOf(map.get("noreadSum"))));
                }
            }
        }
        for (LiveUserVO liveUserVO : anchorUserVOS) {
            LivingUserVO vo = new LivingUserVO();
            vo.setAnchorId(liveUserVO.getId().toString());
            vo.setAvatar(liveUserVO.getHead());
            vo.setNick(liveUserVO.getNickName());
            vo.setNoReadSum(noReadMap.getOrDefault(liveUserVO.getId().toString(), 0));
            LiveDO liveDO = liveDOMap.get(liveUserVO.getId());
            if (Objects.nonNull(liveDO)) {
                vo.setId(liveDO.getId());
                vo.setCompetitionName(liveDO.getCompetitionName());
                vo.setMatchId(liveDO.getMatchId());
                vo.setMatchType(liveDO.getMatchType());
                vo.setMatchTime(liveDO.getMatchTime());
                vo.setHomeTeamName(liveDO.getHomeTeamName());
                vo.setHomeTeamLogo(liveDO.getHomeTeamLogo());
                vo.setAwayTeamName(liveDO.getAwayTeamName());
                vo.setAwayTeamLogo(liveDO.getAwayTeamLogo());
                vo.setPlayUrl(liveDO.getPlayUrl());
                vo.setOpenTime(liveDO.getOpenTime());
                vo.setCreateTime(Timestamp.from(liveDO.getOpenTime().atZone(ZoneOffset.UTC).toInstant()).getTime());
                vo.setLiveStatus(liveDO.getLiveStatus());
                vo.setHotValue(liveDO.getHotValue());
                LiveOpeninfoVO usedOpenInfo = liveOpeninfoService.getUsedOpenInfo(liveDO.getUserId());
                if(Objects.nonNull(usedOpenInfo)) {
                    vo.setTitlePage(usedOpenInfo.getTitlePage());
                }else {
                    vo.setTitlePage(liveDO.getTitlePage());
                }
            } else {
                vo.setLiveStatus(LiveStatus.NOT_START);
                vo.setHotValue(Long.valueOf(0));
            }
            backList.add(vo);
        }
        backList.sort(Comparator.comparing(LivingUserVO::getHotValue).reversed());
        return backList;
    }

    @Override
    public List<LivingUserVO> queryLiveOnlineById(Long liveId) {
        List<LivingUserVO> backList = new ArrayList<>();
        //查询当前直播中的live
        List<Long> liveList = new ArrayList<>();
        liveList.add(liveId);
        List<LiveVo> liveVoList = liveService.getLivingByUserIds(liveList);
        LiveVo liveVo = null;
        Long noReadSum = 0L;
        if (!CollectionUtils.isEmpty(liveVoList)) {
            //一个主播只能在线开一个
            liveVo = liveVoList.get(0);
            //未读红点查询
            noReadSum = this.count(new QueryWrapper<ChatMessageDO>()
                    .eq("anchor_id", liveVo.getUserId())
                    .eq("group_id", liveVo.getId())
                    .eq("identity_type", 0) //0普通用户
                    .eq("chat_type", ChatMsgTypeEnum.PUBLIC.getValue()) //1:公聊、2私聊
                    .eq("readable", YnEnum.ZERO.getValue()) // 是否已读：0 未读 1 已读
                    .eq("del_flag", YnEnum.ZERO.getValue()));
        }
        LiveUserVO liveUserVO = liveUserService.getLiveUserInfo(liveId);
        LivingUserVO vo = new LivingUserVO();
        vo.setAnchorId(liveUserVO.getId().toString());
        vo.setAvatar(liveUserVO.getHead());
        vo.setNick(liveUserVO.getNickName());
        vo.setNoReadSum(Math.toIntExact(noReadSum));

        if (Objects.nonNull(liveVo)) {
            vo.setId(liveVo.getId());
            vo.setCompetitionName(liveVo.getCompetitionName());
            vo.setMatchId(liveVo.getMatchId());
            vo.setMatchType(liveVo.getMatchType());
            vo.setMatchTime(liveVo.getMatchTime());
            vo.setHomeTeamName(liveVo.getHomeTeamName());
            vo.setHomeTeamLogo(liveVo.getHomeTeamLogo());
            vo.setAwayTeamName(liveVo.getAwayTeamName());
            vo.setAwayTeamLogo(liveVo.getAwayTeamLogo());
            vo.setPlayUrl(liveVo.getPlayUrl());
            vo.setOpenTime(liveVo.getOpenTime());
            vo.setCreateTime(Timestamp.from(liveVo.getOpenTime().atZone(ZoneOffset.UTC).toInstant()).getTime());
            vo.setLiveStatus(liveVo.getLiveStatus());
            vo.setHotValue(liveVo.getHotValue());
        } else {
            vo.setLiveStatus(LiveStatus.NOT_START);
            vo.setHotValue(Long.valueOf(0));
        }
        backList.add(vo);
        return backList;
    }

    @Override
    public PageResponse<LiveChatMessageVO> queryPrivateUserByLivePage(Long loginUserId, UserByLivePageRequest pageRequest) {
        /**
         * 查询运营设置置顶操作
         */
        List<AppPinnedUserDO> appPinnedUserDOS = appPinnedUserService.queryUserSetTopList(loginUserId, pageRequest.getAnchorId());
        List<String> userTopList = new ArrayList<>();
        Map<Long, AppPinnedUserDO> appPinnedMap = new HashMap<>();
        if (Objects.nonNull(appPinnedUserDOS)) {
            userTopList.addAll(appPinnedUserDOS.stream().map(item -> String.valueOf(item.getUserId())).collect(Collectors.toList()));
            appPinnedMap = appPinnedUserDOS.stream().collect(Collectors.toMap(AppPinnedUserDO::getUserId, Function.identity()));
        }
        Page<ChatMessageDO> page = this.baseMapper.queryPrivateUserByLivePage(new Page<>(pageRequest.getCurrent(), pageRequest.getSize()), pageRequest.getAnchorId(), userTopList);
        PageResponse<LiveChatMessageVO> pageResponse = PageUtil.convert(page, LiveChatMessageVO.class);
        if (!CollectionUtils.isEmpty(pageResponse.getRecords())) {
            List<LiveChatMessageVO> messageVOList = pageResponse.getRecords();
            List<String> fromIdStrList = messageVOList.stream().map(ChatMessageVO::getFromId).collect(Collectors.toList());
            //用户查询
            List<AppUserDO> appUserDOList = appUserService.listByIds(fromIdStrList);
            Map<String, AppUserDO> appUserMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(appUserDOList)) {
                for (AppUserDO appUserDO : appUserDOList) {
                    appUserMap.put(appUserDO.getId().toString(), appUserDO);
                }
            }
            //未读红点查询
            List<Map<String, Object>> mapList = this.listMaps(new QueryWrapper<ChatMessageDO>()
                    .select("from_id as fromId, count(1) as noreadSum")
                    .eq("anchor_id", pageRequest.getAnchorId())
                    .eq("to_id", pageRequest.getAnchorId())
                    .in("from_id", fromIdStrList)
                    .eq("chat_type", ChatMsgTypeEnum.PRIVATE.getValue()) //1:公聊、2私聊
                    .eq("readable", YnEnum.ZERO.getValue()) // 是否已读：0 未读 1 已读
                    .eq("del_flag", YnEnum.ZERO.getValue())
                    .groupBy("from_id"));
            Map<String, Integer> noReadMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(mapList)) {
                for (Map<String, Object> map : mapList) {
                    noReadMap.put(String.valueOf(map.get("fromId")), Integer.valueOf(String.valueOf(map.get("noreadSum"))));
                }
            }
            //未读红点处理 昵称处理
            for (LiveChatMessageVO record : messageVOList) {
                record.setNoReadSum(noReadMap.getOrDefault(record.getFromId(), 0));
                if (appUserMap.containsKey(record.getFromId())) {
                    record.setAvatar(appUserMap.get(record.getFromId()).getHead());
                    record.setNick(appUserMap.get(record.getFromId()).getName());
                }
                AppPinnedUserDO appPinnedUserDO = appPinnedMap.get(Long.valueOf(record.getFromId()));
                if (Objects.nonNull(appPinnedUserDO)) {
                    record.setSetTop(true);
                    record.setSetTopTime(appPinnedUserDO.getCreateTime());
                } else {
                    record.setSetTop(false);
                }
            }
            //排序，倒序排列 置顶时间
            List<LiveChatMessageVO> resultList = Lists.newArrayList();
            List<LiveChatMessageVO> topList = messageVOList.stream().filter(item-> Objects.nonNull(item.getSetTopTime())).collect(Collectors.toList());
            topList.sort(Comparator.comparing(LiveChatMessageVO::getSetTopTime).reversed());
            messageVOList.removeAll(topList);
            resultList.addAll(topList);
            resultList.addAll(messageVOList);
            pageResponse.setRecords(resultList);
        }
        return pageResponse;
    }

    /**
     * 查询历史消息
     * @param messageSlidePageReqDto
     * @param flag（是否设置已读）
     * @return
     */
    @Override
    @Transactional
    public List<ChatMessageDO> getHistoryMessage(MessageSlidePageReqDto messageSlidePageReqDto, Boolean flag) {
        LambdaQueryWrapper<ChatMessageDO> wrapper = Wrappers.lambdaQuery(ChatMessageDO.class);
        if (ChatType.CHAT_TYPE_PUBLIC.getNumber() == Integer.parseInt(messageSlidePageReqDto.getChatType())) {
            // 群聊处理
            if (StringUtils.isBlank(messageSlidePageReqDto.getGroupId())) {
                throw new ValidateException("群聊消息的群聊ID不能为空");
            }
            wrapper.eq(ChatMessageDO::getGroupId, messageSlidePageReqDto.getGroupId())
                    .eq(ChatMessageDO::getChatType, ChatType.CHAT_TYPE_PUBLIC.getNumber());
            // APP端查询历史记录（用户无法查看已经被逻辑删除的消息）
            if (UserChannelEnum.APP.getFlag().equals(messageSlidePageReqDto.getSearchType())) {
                wrapper.eq(ChatMessageDO::getUserDel, DelEnum.NORMAL.code());
            }
            if (messageSlidePageReqDto.isAnchorSendOnly()) {
                /**
                 * 查询此主播下面的所有助理账号
                 */
                LiveDO liveDO = liveService.getById(Long.valueOf(messageSlidePageReqDto.getGroupId()));
                if (Objects.nonNull(liveDO)) {
                    List<LiveUserDO> liveUserDOS = liveUserService.list(Wrappers.lambdaQuery(LiveUserDO.class).eq(LiveUserDO::getIdentityType, IdentityType.OPERATOR.getCode()).like(LiveUserDO::getPossessLive, liveDO.getUserId()));
                    if (!CollectionUtils.isEmpty(liveUserDOS)) {
                        wrapper.in(ChatMessageDO::getFromId, liveUserDOS.stream().map(item -> String.valueOf(item.getId())).collect(Collectors.toList()));
                    }
                }
            }
        } else if (ChatType.CHAT_TYPE_PRIVATE.getNumber() == Integer.parseInt(messageSlidePageReqDto.getChatType())) {
            if (StringUtils.isBlank(messageSlidePageReqDto.getSearchId()) || StringUtils.isBlank(messageSlidePageReqDto.getUserId())) {
                throw new ValidateException("私聊消息的userId和searchId不能为空");
            }
            wrapper.eq(ChatMessageDO::getChatType, ChatType.CHAT_TYPE_PRIVATE.getNumber());
            // 如果当前用户是APP用户，查询与主播组聊天的信息:
            if (UserChannelEnum.APP.getFlag().equals(messageSlidePageReqDto.getSearchType())) {
                wrapper
                        .nested(i -> i
                                // 查询用户发给主播组的，from为用户，searchId为主播id；
                                .nested(j -> j.eq(ChatMessageDO::getFromId, messageSlidePageReqDto.getUserId()).eq(ChatMessageDO::getAnchorId, messageSlidePageReqDto.getSearchId()))
                                .or()
                                // 查询主播组发给用户的，to为用户， searchId为主播id;
                                .nested(k -> k.eq(ChatMessageDO::getToId, messageSlidePageReqDto.getUserId()).eq(ChatMessageDO::getAnchorId, messageSlidePageReqDto.getSearchId())))
                        // APP端查询历史记录（用户无法查看已经被逻辑删除的消息）
                        .eq(ChatMessageDO::getUserDel, DelEnum.NORMAL.code());

            }
            // 如果当前用户是LIVE用户，查询与普通用户聊天的信息:
            if (UserChannelEnum.LIVE.getFlag().equals(messageSlidePageReqDto.getSearchType())) {
                wrapper
                        .nested(i -> i
                                // 查询主播组发给用户的，anchorId为userId，toId为searchId；
                                .nested(j -> j.eq(ChatMessageDO::getAnchorId, messageSlidePageReqDto.getUserId()).eq(ChatMessageDO::getToId, messageSlidePageReqDto.getSearchId()))
                                .or()
                                // 查询用户发给主播组的，anchorId为userId，fromId为searchId;
                                .nested(k -> k.eq(ChatMessageDO::getAnchorId, messageSlidePageReqDto.getUserId()).eq(ChatMessageDO::getFromId, messageSlidePageReqDto.getSearchId())))
                        .eq(ChatMessageDO::getAnchorDel, DelEnum.NORMAL.code());
            }
        }
        if (wrapper == null) {
            return null;
        }
        if (messageSlidePageReqDto.getOffset() != null) {
            if (messageSlidePageReqDto.getInquiryMode().equals("up")) {
                wrapper = wrapper.lt(ChatMessageDO::getId, messageSlidePageReqDto.getOffset());
                wrapper = wrapper.orderByDesc(ChatMessageDO::getId).last("LIMIT " + messageSlidePageReqDto.getSize());
            } else {
                wrapper = wrapper.gt(ChatMessageDO::getId, messageSlidePageReqDto.getOffset());
                wrapper = wrapper.orderByAsc(ChatMessageDO::getId).last("LIMIT " + messageSlidePageReqDto.getSize());
            }
        }
        // 时间递增的雪花算法id的按时间排序方式，根据id来排序，比根据时间更快

        List<ChatMessageDO> messageDOS = chatMessageMapper.selectList(wrapper);
        if(flag) {
            //助理端只修改查看的消息，用户端全部置为已读
            if (UserChannelEnum.APP.getFlag().equals(messageSlidePageReqDto.getSearchType())){
                if(Objects.nonNull(messageSlidePageReqDto.getSearchId())) {
                    chatMessageMapper.update(null, new LambdaUpdateWrapper<ChatMessageDO>()
                            .eq(ChatMessageDO::getAnchorId, messageSlidePageReqDto.getSearchId())
                            .eq(ChatMessageDO::getToId, messageSlidePageReqDto.getUserId())
                            .eq(ChatMessageDO::getReadable, YnEnum.ZERO.getValue())
                            .set(ChatMessageDO::getReadable, YnEnum.ONE.getValue())
                            .set(ChatMessageDO::getUpdateTime, LocalDateTime.now(ZoneOffset.UTC)));
                }
            }else {
                List<ChatMessageDO> noReadList = messageDOS.stream().filter(a -> ObjectUtils.notEqual(a.getReadable(), 1) && a.getFromId().equals(messageSlidePageReqDto.getSearchId()) && a.getToId().equals(messageSlidePageReqDto.getUserId())).collect(Collectors.toList());
                noReadList.forEach(a -> a.setReadable(1));
                this.updateBatchById(noReadList);
            }
        }
        // 将结果正序展示，根据id
        messageDOS = messageDOS.stream().sorted(Comparator.comparing(ChatMessageDO::getId)).collect(Collectors.toList());
        return messageDOS;
    }

    @Override
    public Map<String, Integer> getLiveMsgSendByGroupIds(List<String> liveIdList) {
        List<Map<String, Object>> mapList = this.listMaps(new QueryWrapper<ChatMessageDO>()
                .select("group_id as groupId, count(group_id) as msgSendCount")
                .in("group_id", liveIdList)
                .eq("identity_type", IdentityType.APP_USER.getCode())
                .eq("del_flag", YnEnum.ZERO.getValue())
                .groupBy("group_id"));
        Map<String, Integer> backMap = new HashMap<>();
        if (CollectionUtils.isEmpty(mapList)) {
            return backMap;
        }
        for (Map<String, Object> map : mapList) {
            backMap.put(String.valueOf(map.get("groupId")), Integer.valueOf(String.valueOf(map.get("msgSendCount"))));
        }
        return backMap;
    }

    @Override
    public void anchorReadMessage(MessageReadRequest readRequest) {
        update(new LambdaUpdateWrapper<ChatMessageDO>()
                .eq(ChatMessageDO::getAnchorId, readRequest.getAnchorId())
                .eq(ChatMessageDO::getFromId, readRequest.getUserId())
                .in(!CollectionUtils.isEmpty(readRequest.getMessageIds()), ChatMessageDO::getId, readRequest.getMessageIds())
                .eq(ChatMessageDO::getReadable, YnEnum.ZERO.getValue())
                .set(ChatMessageDO::getReadable, YnEnum.ONE.getValue())
                .set(ChatMessageDO::getUpdateTime, LocalDateTime.now(ZoneOffset.UTC)));
    }

    @Override
    public List<ChatMessageDO> privateLatestMessage(MessageQueryRequest messageQueryRequest) {
        LambdaQueryWrapper<ChatMessageDO> wrapper = Wrappers.lambdaQuery(ChatMessageDO.class);
        wrapper.eq(ChatMessageDO::getChatType, ChatType.CHAT_TYPE_PRIVATE.getNumber());
        wrapper.nested(i -> i
                        // 查询主播组发给用户的
                        .nested(j -> j.eq(ChatMessageDO::getAnchorId, messageQueryRequest.getAnchorId()).eq(ChatMessageDO::getToId, messageQueryRequest.getUserId()))
                        .or()
                        // 查询用户发给主播组的
                        .nested(k -> k.eq(ChatMessageDO::getAnchorId, messageQueryRequest.getAnchorId()).eq(ChatMessageDO::getFromId, messageQueryRequest.getUserId())))
                .eq(ChatMessageDO::getAnchorDel, DelEnum.NORMAL.code());
        wrapper = wrapper.orderByDesc(ChatMessageDO::getId).last("LIMIT " + messageQueryRequest.getSize());
        List<ChatMessageDO> messageDOS = chatMessageMapper.selectList(wrapper);
        // 将结果正序展示，根据id
        messageDOS = messageDOS.stream().sorted(Comparator.comparing(ChatMessageDO::getId)).collect(Collectors.toList());
        //调用此接口所有置为已读
        chatMessageMapper.update(null, new LambdaUpdateWrapper<ChatMessageDO>()
                .eq(ChatMessageDO::getAnchorId, messageQueryRequest.getAnchorId())
                .eq(ChatMessageDO::getFromId, messageQueryRequest.getUserId())
                .eq(ChatMessageDO::getReadable, YnEnum.ZERO.getValue())
                .eq(ChatMessageDO::getChatType, ChatType.CHAT_TYPE_PRIVATE.getNumber())
                .set(ChatMessageDO::getReadable, YnEnum.ONE.getValue())
                .set(ChatMessageDO::getUpdateTime, LocalDateTime.now(ZoneOffset.UTC)));
        return messageDOS;
    }

    @Override
    public ChatMessageDO privateEarliestUnread(MessageQueryRequest2 messageQueryRequest) {
        //先查询用户发给主播最早未读消息ID
        LambdaQueryWrapper<ChatMessageDO> wrapper = Wrappers.lambdaQuery(ChatMessageDO.class);
        wrapper.eq(ChatMessageDO::getChatType, ChatType.CHAT_TYPE_PRIVATE.getNumber());
        wrapper.eq(ChatMessageDO::getAnchorId, messageQueryRequest.getAnchorId());
        wrapper.eq(ChatMessageDO::getFromId, messageQueryRequest.getUserId());
        wrapper.eq(ChatMessageDO::getAnchorDel, DelEnum.NORMAL.code());
        wrapper.eq(ChatMessageDO::getReadable, YnEnum.ZERO.getValue());
        wrapper.orderByAsc(ChatMessageDO::getId).last("LIMIT 1");
        ChatMessageDO one = this.getOne(wrapper);
        if (Objects.isNull(one)) {
            return null;
        }
//        LambdaQueryWrapper<ChatMessageDO> lambdaQuery = Wrappers.lambdaQuery(ChatMessageDO.class);
//        lambdaQuery.eq(ChatMessageDO::getChatType, ChatType.CHAT_TYPE_PRIVATE.getNumber());
//        wrapper.lt(ChatMessageDO::getId, one.getId());
//        lambdaQuery.nested(i -> i
//                        // 查询主播组发给用户的
//                        .nested(j -> j.eq(ChatMessageDO::getAnchorId, messageQueryRequest.getAnchorId()).eq(ChatMessageDO::getToId, messageQueryRequest.getUserId()))
//                        .or()
//                        // 查询用户发给主播组的
//                        .nested(k -> k.eq(ChatMessageDO::getAnchorId, messageQueryRequest.getAnchorId()).eq(ChatMessageDO::getFromId, messageQueryRequest.getUserId())))
//                .eq(ChatMessageDO::getAnchorDel, DelEnum.NORMAL.code());
//        lambdaQuery.orderByDesc(ChatMessageDO::getId).last("LIMIT " + messageQueryRequest.getSize());
//        List<ChatMessageDO> messageDOS = chatMessageMapper.selectList(lambdaQuery);
//        // 将结果正序展示，根据id
//        messageDOS = messageDOS.stream().sorted(Comparator.comparing(ChatMessageDO::getId)).collect(Collectors.toList());
        return one;
    }


}
