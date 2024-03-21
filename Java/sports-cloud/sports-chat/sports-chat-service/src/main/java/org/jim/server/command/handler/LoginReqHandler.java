package org.jim.server.command.handler;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import org.apache.commons.collections4.CollectionUtils;
import org.jim.core.*;
import org.jim.core.config.ImConfig;
import org.jim.core.exception.ImException;
import org.jim.core.message.MessageHelper;
import org.jim.core.packets.*;
import org.jim.core.protocol.IProtocol;
import org.jim.core.utils.JsonKit;
import org.jim.server.ImServerChannelContext;
import org.jim.server.JimServerAPI;
import org.jim.server.command.AbstractCmdHandler;
import org.jim.server.command.CommandManager;
import org.jim.server.processor.login.LoginCmdProcessor;
import org.jim.server.config.ImServerConfig;
import org.jim.server.protocol.ProtocolManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Objects;

/**
 * 登录消息命令处理器

 * @date 2018年4月10日 下午2:40:07
 */
public class LoginReqHandler extends AbstractCmdHandler {

	private static Logger log = LoggerFactory.getLogger(LoginReqHandler.class);

	@Override
	public ImPacket handler(ImPacket packet, ImChannelContext imChannelContext) throws ImException {
		ImServerChannelContext imServerChannelContext = (ImServerChannelContext)imChannelContext;
		LoginReqBody loginReqBody = JsonKit.toBean(packet.getBody(), LoginReqBody.class);
		LoginCmdProcessor loginProcessor = this.getSingleProcessor(LoginCmdProcessor.class);
		LoginRespBody loginRespBody = LoginRespBody.success();
		User user = getUserByProcessor(imChannelContext, loginProcessor, loginReqBody, loginRespBody);
		IProtocol protocol = imServerChannelContext.getProtocolHandler().getProtocol();
		if (ObjectUtil.isNotNull(user)) {
			user.setTerminal(Objects.isNull(protocol) ? ImConst.Protocol.UNKNOWN : protocol.name());
		}
		JimServerAPI.bindUser(imServerChannelContext, user);
		if (ObjectUtil.isNotNull(loginProcessor)) {
			loginProcessor.onSuccess(user, imChannelContext);
		}
		if (ObjectUtil.isNotNull(user)) {
			return ProtocolManager.Converter.respPacket(loginRespBody, imChannelContext);
		}
		return null;
	}

	/**
	 * 根据用户配置的自定义登录处理器获取业务组装的User信息
	 * @param imChannelContext 通道上下文
	 * @param loginProcessor 登录自定义业务处理器
	 * @param loginReqBody 登录请求体
	 * @param loginRespBody 登录响应体
	 * @return 用户组装的User信息
	 * @throws ImException
	 */
	private User getUserByProcessor(ImChannelContext imChannelContext, LoginCmdProcessor loginProcessor, LoginReqBody loginReqBody, LoginRespBody loginRespBody)throws ImException{
		if(Objects.isNull(loginProcessor)){
			User user = User.builder().userId(loginReqBody.getUserId()).status(UserStatusType.ONLINE.getStatus()).build();
			return user;
		}
		loginRespBody = loginProcessor.doLogin(loginReqBody, imChannelContext);

		if (Objects.isNull(loginRespBody) || loginRespBody.getCode() != ImStatus.C10000.getCode()) {
			log.error("login failed, loginReqBody:{}", JSON.toJSONString(loginReqBody));
			log.error("login failed, loginRespBody:{}", JSON.toJSONString(loginRespBody));
			loginProcessor.onFailed(imChannelContext);
			JimServerAPI.bSend(imChannelContext, ProtocolManager.Converter.respPacket(loginRespBody, imChannelContext));
			return null;
		}
		return loginProcessor.getUser(loginReqBody, imChannelContext);
	}

	/**
	 * 初始化绑定或者解绑群组;
	 */
	public void initGroup(ImChannelContext imChannelContext , User user)throws ImException{
		String userId = user.getUserId();
		List<Group> groups = user.getGroups();
		if(CollectionUtils.isEmpty(groups)){
			return;
		}
		ImServerConfig imServerConfig = ImConfig.Global.get();
		boolean isStore = ImServerConfig.ON.equals(imServerConfig.getIsStore());
		MessageHelper messageHelper = imServerConfig.getMessageHelper();
		List<String> groupIds = null;
		if(isStore){
			groupIds = messageHelper.getGroups(userId);
		}
		//绑定群组
		for(Group group : groups){
			if(isStore && CollectionUtils.isNotEmpty(groupIds)){
				groupIds.remove(group.getGroupId());
			}
			ImPacket groupPacket = new ImPacket(Command.COMMAND_JOIN_GROUP_REQ,JsonKit.toJsonBytes(group));
			try {
				JoinGroupReqHandler joinGroupReqHandler = CommandManager.getCommand(Command.COMMAND_JOIN_GROUP_REQ, JoinGroupReqHandler.class);
				joinGroupReqHandler.handler(groupPacket, imChannelContext);
			} catch (Exception e) {
				log.error(e.toString(),e);
			}
		}
		if(isStore && groupIds != null){
			for(String groupId : groupIds){
				messageHelper.getBindListener().onAfterGroupUnbind(imChannelContext, Group.newBuilder().groupId(groupId).build());
			}
		}
	}
	@Override
	public Command command() {
		return Command.COMMAND_LOGIN_REQ;
	}

}
