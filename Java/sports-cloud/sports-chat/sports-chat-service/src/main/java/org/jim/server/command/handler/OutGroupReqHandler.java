package org.jim.server.command.handler;

import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.apache.logging.log4j.util.Strings;
import org.jim.core.ImChannelContext;
import org.jim.core.ImPacket;
import org.jim.core.ImSessionContext;
import org.jim.core.exception.ImException;
import org.jim.core.packets.*;
import org.jim.core.utils.JsonKit;
import org.jim.server.JimServerAPI;
import org.jim.server.command.AbstractCmdHandler;
import org.jim.server.processor.group.GroupCmdProcessor;
import org.jim.server.protocol.ProtocolManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sports.admin.manage.dao.entity.ChatGroupUserDO;
import org.sports.admin.manage.dao.mapper.ChatGroupUserMapper;
import org.sports.admin.manage.service.enums.YnEnum;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

public class OutGroupReqHandler extends AbstractCmdHandler {

    private static Logger log = LoggerFactory.getLogger(OutGroupReqHandler.class);
    private final ChatGroupUserMapper groupUserMapper = SpringUtil.getBean(ChatGroupUserMapper.class);
    @Override
    public Command command() {
        return Command.COMMAND_EXIT_GROUP_RESP;
    }

    @Override
    public ImPacket handler(ImPacket imPacket, ImChannelContext imChannelContext) throws ImException {
        Group outGroup = JsonKit.toBean(imPacket.getBody(),Group.class);
        String userId = outGroup.getUserId();
        String groupId = outGroup.getGroupId();
        //实际绑定之前执行处理器动作
        GroupCmdProcessor groupProcessor = this.getSingleProcessor(GroupCmdProcessor.class);
        if(Objects.nonNull(groupProcessor)){
            OutGroupRespBody outGroupRespBody =  groupProcessor.out(outGroup, imChannelContext);
            boolean joinGroupIsTrue = Objects.isNull(outGroupRespBody) || OutGroupResult.OUT_GROUP_RESULT_OK.getNumber() != outGroupRespBody.getResult().getNumber();
            if (joinGroupIsTrue) {
                outGroupRespBody = OutGroupRespBody.failed().setData(outGroupRespBody);
                ImPacket respPacket = ProtocolManager.Converter.respPacket(outGroupRespBody, imChannelContext);
                return respPacket;
            }
        }
        //处理完内容后
        JimServerAPI.unbindGroup(groupId, imChannelContext);
        //更新直播间退出状态
        outGroupUpdateDb(groupId, userId);
        //发退出房间通知  COMMAND_EXIT_GROUP_NOTIFY_RESP
        outGroupNotify(groupId, userId, imChannelContext);
        return null;
    }

    private void outGroupUpdateDb(String group, String userId) {
        if(Strings.isNotBlank(group)&&Strings.isNotBlank(userId)) {
            groupUserMapper.update(null, new LambdaUpdateWrapper<ChatGroupUserDO>()
                    .eq(ChatGroupUserDO::getGroupId, group)
                    .eq(ChatGroupUserDO::getUserId, userId)
                    .set(ChatGroupUserDO::getUpdateTime, LocalDateTime.now(ZoneOffset.UTC))
                    .set(ChatGroupUserDO::getLeaveStatus, YnEnum.ONE.getValue()));
        }

    }

    /**
     * 发送退出房间通知
     *
     * @param group
     * @param userId
     * @param imChannelContext
     */
    private void outGroupNotify(String group, String userId, ImChannelContext imChannelContext) throws ImException {
        ImSessionContext imSessionContext = imChannelContext.getSessionContext();
        User clientUser = imSessionContext.getImClientNode().getUser();
        User notifyUser;
        if(Objects.nonNull(clientUser)) {
            notifyUser = User.builder().userId(clientUser.getUserId()).lvNum(clientUser.getLvNum()).nick(clientUser.getNick()).status(UserStatusType.ONLINE.getStatus()).build();
        }else {
            notifyUser = User.builder().userId(userId).status(UserStatusType.ONLINE.getStatus()).build();
        }

        //发退出房间通知  COMMAND_EXIT_GROUP_NOTIFY_RESP
        OutGroupNotifyRespBody outGroupNotifyRespBody = OutGroupNotifyRespBody.success();
        outGroupNotifyRespBody.setGroup(group).setUser(notifyUser);
        JimServerAPI.sendToGroup(group, ProtocolManager.Converter.respPacket(outGroupNotifyRespBody, imChannelContext));
    }
}
