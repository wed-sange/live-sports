package org.sports.chat.web.command;

import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.jim.core.ImChannelContext;
import org.jim.core.packets.ChatBody;
import org.jim.core.packets.ChatType;
import org.jim.server.config.ImServerConfig;
import org.jim.server.processor.chat.BaseAsyncChatMessageProcessor;
import org.jim.server.util.ChatKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sports.admin.manage.dao.entity.ChatMessageDO;
import org.sports.admin.manage.service.enums.YnEnum;
import org.sports.chat.service.enums.SentType;
import org.sports.chat.service.service.IChatService;
import org.sports.chat.service.util.PushMessageUtil;

import java.sql.Timestamp;

/**
 * 消息持久化处理器
 */
public class MysqlAsyncChatMessageProcessor extends BaseAsyncChatMessageProcessor {

    private static Logger logger = LoggerFactory.getLogger(MysqlAsyncChatMessageProcessor.class);

    private IChatService chatService = SpringUtil.getBean(IChatService.class);

    private PushMessageUtil pushMessageUtil = SpringUtil.getBean(PushMessageUtil.class);


    @Override
    public void doProcess(ChatBody chatBody, ImChannelContext imChannelContext) {

        String liveId = StringUtils.isBlank(chatBody.getAnchorId()) ? chatBody.getTo() : chatBody.getAnchorId();

        ChatMessageDO messageDO = new ChatMessageDO();
        String to = chatBody.getTo();
        messageDO.setId(Long.valueOf(chatBody.getId()));
        messageDO.setFromId(chatBody.getFrom());
        messageDO.setToId(to);
        messageDO.setAnchorId(liveId);
        messageDO.setCmd(chatBody.getCmd());
        messageDO.setMsgType(chatBody.getMsgType());
        messageDO.setChatType(chatBody.getChatType());
        messageDO.setAvatar(chatBody.getFromAvatar());
        messageDO.setNick(chatBody.getFromNickName());
        messageDO.setContent(chatBody.getContent());
        messageDO.setIdentityType(chatBody.getIdentityType());
        messageDO.setLevel(chatBody.getLevel());
        messageDO.setGroupId(chatBody.getGroupId());
        messageDO.setReadable(YnEnum.ZERO.getValue());
        messageDO.setCreateTime(new Timestamp(System.currentTimeMillis()));
        messageDO.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        boolean isStore = ImServerConfig.ON.equals(imServerConfig.getIsStore());
        boolean isOnline = ChatKit.isOnline(to, isStore);
        if (!isOnline) {//已推送
            messageDO.setSent(SentType.YES.getCode());
        } else { //待推送，离线消息
            messageDO.setSent(SentType.NO.getCode());
        }
        chatService.saveChatMessage(messageDO);

        //推送未读消息给各方
        if (ChatType.CHAT_TYPE_PRIVATE.getNumber() == chatBody.getChatType()) {
            pushMessageUtil.pushUnReadMsgCount(chatBody);

        }
    }

    public static void main(String[] args) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        System.out.println("args = " + timestamp.getTime());

    }

}