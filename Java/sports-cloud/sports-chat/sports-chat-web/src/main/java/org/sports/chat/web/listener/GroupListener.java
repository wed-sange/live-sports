package org.sports.chat.web.listener;

import cn.hutool.extra.spring.SpringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.jim.core.ImChannelContext;
import org.jim.core.ImSessionContext;
import org.jim.core.exception.ImException;
import org.jim.core.packets.Group;
import org.jim.core.packets.JoinGroupNotifyRespBody;
import org.jim.core.packets.User;
import org.jim.core.packets.UserStatusType;
import org.jim.core.utils.JsonKit;
import org.jim.server.JimServerAPI;
import org.jim.server.listener.AbstractImGroupListener;
import org.jim.server.protocol.ProtocolManager;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sports.admin.manage.service.constant.RedisCacheConstant;
import org.sports.springboot.starter.cache.toolkit.CacheUtil;

/**
 * 群组绑定监听器
 *

 * 2017年5月13日 下午10:38:36
 */
public class GroupListener extends AbstractImGroupListener {
    private RedissonClient redissonClient = SpringUtil.getBean(RedissonClient.class);


    private static Logger logger = LoggerFactory.getLogger(GroupListener.class);

    @Override
    public void doAfterBind(ImChannelContext imChannelContext, Group group) throws ImException {
        logger.info("群组:{},绑定成功!", JsonKit.toJSONString(group));
        if (StringUtils.isBlank(imChannelContext.getUserId()) || "undefined".equals(imChannelContext.getUserId())) {
            return;
        }
        //发送进房间通知;
        joinGroupNotify(group, imChannelContext);
    }

    /**
     * @param imChannelContext
     * @param group
     * @throws Exception

     */
    @Override
    public void doAfterUnbind(ImChannelContext imChannelContext, Group group) throws ImException {
        logger.info("群组:{},退出成功!", JsonKit.toJSONString(group));
    }


    /**
     * 发送进房间通知;
     *
     * @param group            群组对象
     * @param imChannelContext
     */
    public void joinGroupNotify(Group group, ImChannelContext imChannelContext) throws ImException {
        ImSessionContext imSessionContext = imChannelContext.getSessionContext();
        User clientUser = imSessionContext.getImClientNode().getUser();
        String anchorId = (String) redissonClient.getMap(RedisCacheConstant.LIVEID_ANCHORID_MAP).get(group.getGroupId());
        boolean contains = false;
        if (Strings.isNotBlank(anchorId)) {
            contains = redissonClient.getSet(CacheUtil.buildKey(RedisCacheConstant.APP_ANCHOR_USER_FORBIDDEN, anchorId)).contains(clientUser.getUserId());
         }

        User notifyUser = User.builder()
                .userId(clientUser.getUserId())
                .avatar(clientUser.getAvatar())
                .lvNum(clientUser.getLvNum())
                .nick(clientUser.getNick())
                .status(UserStatusType.ONLINE.getStatus())
                .mute(contains)
                .build();
        String groupId = group.getGroupId();
        //发进房间通知  COMMAND_JOIN_GROUP_NOTIFY_RESP
        JoinGroupNotifyRespBody joinGroupNotifyRespBody = JoinGroupNotifyRespBody.success();
        joinGroupNotifyRespBody.setGroup(groupId).setUser(notifyUser);
        JimServerAPI.sendToGroup(groupId, ProtocolManager.Converter.respPacket(joinGroupNotifyRespBody, imChannelContext));
    }

}
