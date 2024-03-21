package org.sports.app.service.service.impl;

import org.apache.logging.log4j.util.Strings;
import org.sports.admin.manage.dao.entity.AppUserNoticeDO;
import org.sports.admin.manage.dao.enums.DateType;
import org.sports.admin.manage.dao.req.AppUserMsgPageRequest;
import org.sports.admin.manage.dao.req.ChatMessageVO;
import org.sports.admin.manage.service.service.AppUserNoticeService;
import org.sports.admin.manage.service.service.IChatMessageService;
import org.sports.admin.manage.service.service.IFeedbackService;
import org.sports.admin.manage.service.service.ILiveUserService;
import org.sports.admin.manage.service.vo.LiveUserVO;
import org.sports.app.service.service.IMessageService;
import org.sports.springboot.starter.convention.page.PageResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * APP用户消息处理实现类
 */
@Service
public class MessageServiceImpl implements IMessageService {

    @Resource
    private IChatMessageService chatMessageService;
    @Resource
    private IFeedbackService feedbackService;
    @Resource
    private ILiveUserService liveUserService;
    @Resource
    private AppUserNoticeService appUserNoticeService;

    @Override
    public PageResponse<ChatMessageVO> queryMsgByPage(AppUserMsgPageRequest pageRequest, Long userId){
        //查询聊天消息列表
        PageResponse<ChatMessageVO> pageResponse = chatMessageService.queryMsgByPage(pageRequest, userId);
        if(!CollectionUtils.isEmpty(pageResponse.getRecords())){
            List<ChatMessageVO> messageVOList = pageResponse.getRecords();
            //主播信息查询
            List<String> anchorIdStrList = messageVOList.stream().map(ChatMessageVO::getAnchorId).distinct().collect(Collectors.toList());
            List<Long> anchorIdList = anchorIdStrList.stream().map(i -> Long.valueOf(i)).collect(Collectors.toList());
            List<LiveUserVO> liveUserVoList = liveUserService.getLiveUserList(anchorIdList);
            Map<Long, LiveUserVO> liveUserVoMap = liveUserVoList.stream().collect(Collectors.toMap(LiveUserVO::getId, i -> i));
            //未读红点查询
            Map<String, Integer> noReadMap = chatMessageService.countNoReadMsg(anchorIdStrList, userId);
            //主播信息/未读红点处理
            Long anchorId;
            for (ChatMessageVO record : messageVOList) {
                anchorId = Long.valueOf(record.getAnchorId());
                if(liveUserVoMap.containsKey(anchorId)){
                    record.setNick(liveUserVoMap.get(anchorId).getNickName());
                    record.setAvatar(liveUserVoMap.get(anchorId).getHead());
                }
                if(noReadMap.containsKey(record.getAnchorId())){
                    record.setNoReadSum(noReadMap.get(record.getAnchorId()));
                }else{
                    record.setNoReadSum(0);
                }
            }
        }
        if(Strings.isEmpty(pageRequest.getUserName())){
            //查询系统通知最大一条
            AppUserNoticeDO appUserNoticeDO = appUserNoticeService.getLastInfoByUserId(userId);
            if(appUserNoticeDO != null){
                ChatMessageVO chatMessageVO = new ChatMessageVO();
                chatMessageVO.setId(appUserNoticeDO.getId());
                chatMessageVO.setAnchorId("0");
                chatMessageVO.setDataType(DateType.FEEDBACK.getCode());
                chatMessageVO.setContent(appUserNoticeDO.getTitle());
                chatMessageVO.setNoReadSum(Integer.valueOf(String.valueOf(appUserNoticeService.countNoRead(userId))));
                chatMessageVO.setCreateTime(Timestamp.from(appUserNoticeDO.getCreateTime().atZone(ZoneOffset.UTC).toInstant()).getTime());
                pageResponse.getRecords().add(chatMessageVO);
                List<ChatMessageVO> list= pageResponse.getRecords().stream().sorted(Comparator.comparing(ChatMessageVO::getCreateTime).reversed()).collect(Collectors.toList());
                pageResponse.setRecords(list);
            }
        }
        return pageResponse;
    }

    @Override
    public void delRecordsByAnchorId(String anchorId, Long userId){
        if("0".equals(anchorId)){
            //删除所有系统消息
            appUserNoticeService.delNoticeByUserId(userId);
        }else{
            //删除对应聊天记录
            chatMessageService.delRecordsByAnchorId(userId, anchorId);
        }
    }

    @Override
    public void cancelUnread(String anchorId, Long userId){
        if("0".equals(anchorId)){
            //清除所有反馈通知红点
            appUserNoticeService.readAllNoticeInfo(userId);
        }else{
            //清楚对应聊天红点
            chatMessageService.cancelAllUnreadMsgByUserId(userId, anchorId);
        }
        //推送消息
    }

    @Override
    public void cancelAllUnread(Long userId){
        //清除所有反馈通知红点
        appUserNoticeService.readAllNoticeInfo(userId);
        //清楚对应聊天红点
        chatMessageService.cancelAllUnreadMsgByUserId(userId);
    }

}
