package org.sports.chat.web.helper.mysql;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jim.core.ImChannelContext;
import org.jim.core.ImSessionContext;
import org.jim.core.cache.redis.RedisCache;
import org.jim.core.cache.redis.RedisCacheManager;
import org.jim.core.config.ImConfig;
import org.jim.core.exception.ImException;
import org.jim.core.listener.AbstractImStoreBindListener;
import org.jim.core.message.MessageHelper;
import org.jim.core.packets.Group;
import org.jim.core.packets.User;
import org.jim.core.packets.UserStatusType;
import org.jim.server.config.ImServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * 消息持久化绑定监听器

 * @date 2018年4月8日 下午4:12:31
 */
public class MysqlImStoreBindListener extends AbstractImStoreBindListener {

	private static Logger logger = LoggerFactory.getLogger(MysqlImStoreBindListener.class);

	private static final String SUFFIX = ":";

	public MysqlImStoreBindListener(ImConfig imConfig, MessageHelper messageHelper){
		super(imConfig, messageHelper);
	}
	
	@Override
	public void onAfterGroupBind(ImChannelContext imChannelContext, Group group) throws ImException {
		if(!isStore() || StringUtils.isBlank(imChannelContext.getUserId()) || "undefined".equals(imChannelContext.getUserId())) {
			return;
		}
		initGroupUsers(group, imChannelContext);
		//用户加入群组
		this.messageHelper.addGroupUser(imChannelContext.getUserId(), group.getGroupId());
	}
	public void initGroupUsers(Group group ,ImChannelContext imChannelContext){
		String groupId = group.getGroupId();
		if(!isStore()) {
			return;
		}
		String userId = imChannelContext.getUserId();
		if(StringUtils.isEmpty(groupId) || StringUtils.isEmpty(userId)) {
			return;
		}
		String group_user_key = groupId+SUFFIX+USER;
		RedisCache groupCache = RedisCacheManager.getCache(GROUP);
		List<String> users = groupCache.listGetAll(group_user_key);
		if(!users.contains(userId)){
			groupCache.listPushTail(group_user_key, userId);
		}
		initUserGroups(userId, groupId);
		ImSessionContext imSessionContext = imChannelContext.getSessionContext();
		User onlineUser = imSessionContext.getImClientNode().getUser();
		if(onlineUser == null) {
			return;
		}
		List<Group> groups = onlineUser.getGroups();
		if(groups == null) {
			return;
		}
		for(Group storeGroup : groups){
			if(!groupId.equals(storeGroup.getGroupId())){
				continue;
			}
			groupCache.put(groupId+SUFFIX+INFO, storeGroup);
			break;
		}
	}

	/**
	 * 初始化用户拥有哪些群组;
	 * @param userId
	 * @param group
	 */
	public void initUserGroups(String userId, String group){
		if(!isStore()) {
			return;
		}
		if(StringUtils.isEmpty(group) || StringUtils.isEmpty(userId)) {
			return;
		}
		List<String> groups = RedisCacheManager.getCache(USER).listGetAll(userId+SUFFIX+GROUP);
		if(groups.contains(group)){
			return;
		}
		RedisCacheManager.getCache(USER).listPushTail(userId+SUFFIX+GROUP, group);
	}

	@Override
	public void onAfterGroupUnbind(ImChannelContext imChannelContext, Group group) throws ImException {
		if(!isStore() || StringUtils.isBlank(imChannelContext.getUserId()) || "undefined".equals(imChannelContext.getUserId())) {
			return;
		}
		this.messageHelper.removeGroupUser(imChannelContext.getUserId(), group.getGroupId());
	}

	@Override
	public void onAfterUserBind(ImChannelContext imChannelContext, User user) throws ImException {
		if(!isStore() || Objects.isNull(user)) {
			return;
		}
		user.setStatus(UserStatusType.ONLINE.getStatus());
		this.messageHelper.updateUserTerminal(user);
		initUserInfo(user);
	}

	@Override
	public void onAfterUserUnbind(ImChannelContext imChannelContext, User user) throws ImException {
		if(!isStore() || Objects.isNull(user)) {
			return;
		}
		user.setStatus(UserStatusType.OFFLINE.getStatus());
		this.messageHelper.updateUserTerminal(user);
	}

	public void initUserInfo(User user){
		if(!isStore() || user == null) {
			return;
		}
		String userId = user.getUserId();
		if(StringUtils.isEmpty(userId)) {
			return;
		}
		RedisCache userCache = RedisCacheManager.getCache(USER);
		userCache.put(userId+SUFFIX+INFO, user.clone());
		List<Group> friends = user.getFriends();
		if(CollectionUtils.isEmpty(friends)){
			return;
		}
		userCache.put(userId+SUFFIX+FRIENDS, (Serializable) friends);
	}

	/**
	 * 是否开启持久化;
	 * @return
	 */
	public boolean isStore(){
		ImServerConfig imServerConfig = ImServerConfig.Global.get();
		return ImServerConfig.ON.equals(imServerConfig.getIsStore());
	}

}
