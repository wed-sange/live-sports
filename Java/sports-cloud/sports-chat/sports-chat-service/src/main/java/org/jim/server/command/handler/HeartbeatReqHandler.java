package org.jim.server.command.handler;

import org.jim.core.ImChannelContext;
import org.jim.core.ImPacket;
import org.jim.core.ImStatus;
import org.jim.core.exception.ImException;
import org.jim.core.packets.Command;
import org.jim.core.packets.HeartbeatBody;
import org.jim.core.packets.RespBody;
import org.jim.server.command.AbstractCmdHandler;
import org.jim.server.protocol.ProtocolManager;
import org.sports.chat.service.constant.WsResponseStatus;

import static org.tio.utils.zk.Zk.setData;

/**
 *
 */
public class HeartbeatReqHandler extends AbstractCmdHandler
{
	@Override
	public ImPacket handler(ImPacket packet, ImChannelContext channelContext) throws ImException
	{
		RespBody respBody = new RespBody(Command.COMMAND_HEARTBEAT_REQ);
		respBody.setCode(ImStatus.C10000.getCode());
		respBody.setSuccess(WsResponseStatus.TRUE);
		RespBody heartbeatBody = respBody.setData(new HeartbeatBody(Protocol.HEARTBEAT_BYTE));
		ImPacket heartbeatPacket = ProtocolManager.Converter.respPacket(heartbeatBody,channelContext);
		return heartbeatPacket;
	}

	@Override
	public Command command() {
		return Command.COMMAND_HEARTBEAT_REQ;
	}
}
