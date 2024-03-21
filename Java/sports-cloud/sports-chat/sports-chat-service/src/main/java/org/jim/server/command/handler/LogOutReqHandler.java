package org.jim.server.command.handler;

import org.apache.commons.collections4.CollectionUtils;
import org.jim.core.*;
import org.jim.core.config.ImConfig;
import org.jim.core.exception.ImException;
import org.jim.core.packets.*;
import org.jim.core.utils.JsonKit;
import org.jim.server.JimServerAPI;
import org.jim.server.command.AbstractCmdHandler;
import org.jim.server.protocol.ProtocolManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 登录消息命令处理器

 * @date 2018年4月10日 下午2:40:07
 */
public class LogOutReqHandler extends AbstractCmdHandler {

	private static Logger log = LoggerFactory.getLogger(LogOutReqHandler.class);

	@Override
	public ImPacket handler(ImPacket packet, ImChannelContext imChannelContext) throws ImException {
		LoginReqBody loginReqBody = JsonKit.toBean(packet.getBody(), LoginReqBody.class);
		LogOutRespBody logOutRespBody = LogOutRespBody.success();
		JimServerAPI.unbindUser(loginReqBody.getUserId());
		//初始化绑定或者解绑群组; todo 业务上目前用不到此逻辑
		//initGroup(imChannelContext, user);
		return ProtocolManager.Converter.respPacket(logOutRespBody, imChannelContext);
	}

	@Override
	public Command command() {
		return Command.COMMAND_LOGOUT_REQ;
	}

}
