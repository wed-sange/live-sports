/**
 * 
 */
package org.jim.server.protocol.ws;

import org.jim.core.ImChannelContext;
import org.jim.core.ImPacket;
import org.jim.core.ImStatus;
import org.jim.core.config.ImConfig;
import org.jim.core.exception.ImDecodeException;
import org.jim.core.exception.ImException;
import org.jim.core.http.HttpRequest;
import org.jim.core.http.HttpRequestDecoder;
import org.jim.core.http.HttpResponse;
import org.jim.core.http.HttpResponseEncoder;
import org.jim.core.packets.Command;
import org.jim.core.packets.Message;
import org.jim.core.packets.RespBody;
import org.jim.core.protocol.AbstractProtocol;
import org.jim.core.utils.JsonKit;
import org.jim.core.ws.*;
import org.jim.server.JimServerAPI;
import org.jim.server.command.AbstractCmdHandler;
import org.jim.server.command.CommandManager;
import org.jim.server.config.ImServerConfig;
import org.jim.server.protocol.AbstractProtocolHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.Objects;

/**
 * 版本: [1.0]
 * 功能说明:
 */
public class WsProtocolHandler extends AbstractProtocolHandler {
	
	private Logger logger = LoggerFactory.getLogger(WsProtocolHandler.class);
	
	private WsConfig wsServerConfig;

	private IWsMsgHandler wsMsgHandler;
	private String aesKey = "3667644840989962";

	
	public WsProtocolHandler() {
		this.protocol = new WsProtocol(new WsConvertPacket());
	}
	
	public WsProtocolHandler(WsConfig wsServerConfig, AbstractProtocol protocol) {
		super(protocol);
		this.wsServerConfig = wsServerConfig;
	}
	@Override
	public void init(ImServerConfig imServerConfig) {
		WsConfig wsConfig = imServerConfig.getWsConfig();
		if(Objects.isNull(wsConfig)){
			wsConfig = WsConfig.newBuilder().build();
			imServerConfig.setWsConfig(wsConfig);
		}
		IWsMsgHandler wsMsgHandler = wsConfig.getWsMsgHandler();
		if(Objects.isNull(wsMsgHandler)){
			wsConfig.setWsMsgHandler(new WsMsgHandler());
		}
		this.wsServerConfig = wsConfig;
		this.wsMsgHandler = wsServerConfig.getWsMsgHandler();
		logger.info("WebSocket Protocol  initialized");
	}

	@Override
	public ByteBuffer encode(ImPacket imPacket, ImConfig imConfig, ImChannelContext imChannelContext) {
		WsSessionContext wsSessionContext = (WsSessionContext)imChannelContext.getSessionContext();
		WsResponsePacket wsResponsePacket = (WsResponsePacket)imPacket;
		if (wsResponsePacket.getCommand() == Command.COMMAND_HANDSHAKE_RESP) {
			//握手包
			HttpResponse handshakeResponsePacket = wsSessionContext.getHandshakeResponsePacket();
			return HttpResponseEncoder.encode(handshakeResponsePacket, imChannelContext,true);

		}else{
			//todo
//			byte[] encodeBody = AESUtils.encrypt(wsResponsePacket.getBody(), (String) imChannelContext.get("aesKey"));
//			if(Opcode.TEXT.equals(wsResponsePacket.getWsOpcode())){
//				try {如何需要AES加密
//					wsResponsePacket.setWsBodyText(new String(encodeBody,"UTF-8"));
//				} catch (UnsupportedEncodingException e) {
//					throw new RuntimeException(e);
//				}
//			}
//			wsResponsePacket.setBody(encodeBody);
			return WsServerEncoder.encode(wsResponsePacket , imChannelContext);
		}
	}

	@Override
	public ImPacket decode(ByteBuffer buffer, int limit, int position, int readableLength, ImChannelContext imChannelContext) throws ImDecodeException {
		WsSessionContext wsSessionContext = (WsSessionContext)imChannelContext.getSessionContext();
		//握手
		if(!wsSessionContext.isHandshaked()){
			HttpRequest  httpRequest = HttpRequestDecoder.decode(buffer,imChannelContext,true);
			if(httpRequest == null) {
				return null;
			}
			//升级到WebSocket协议处理
			HttpResponse httpResponse = WsServerDecoder.updateWebSocketProtocol(httpRequest, imChannelContext);
			if (httpResponse == null) {
				throw new ImDecodeException("http协议升级到webSocket协议失败");
			}
			wsSessionContext.setHandshakeRequestPacket(httpRequest);
			wsSessionContext.setHandshakeResponsePacket(httpResponse);

			WsRequestPacket wsRequestPacket = new WsRequestPacket();
			wsRequestPacket.setHandShake(true);
			wsRequestPacket.setCommand(Command.COMMAND_HANDSHAKE_REQ);
			return wsRequestPacket;
		}else{
			WsRequestPacket wsRequestPacket = WsServerDecoder.decode(buffer, imChannelContext);
			if(wsRequestPacket == null) {
				return null;
			}
			Command command = null;
			String sendId = null;
			if(wsRequestPacket.getWsOpcode() == Opcode.CLOSE){
				command = Command.COMMAND_CLOSE_REQ;
			}else{
				try{
					//todo 如果需要AES加密
//					byte[] decodeBody = AESUtils.decrypt(wsRequestPacket.getBody(), (String) imChannelContext.get("aesKey"));
//					if(Opcode.TEXT.equals(wsRequestPacket.getWsOpcode())){
//						wsRequestPacket.setWsBodyText(new String(decodeBody,"UTF-8"));
//					}
//					wsRequestPacket.setBody(decodeBody);
					Message message = JsonKit.toBean(wsRequestPacket.getBody(),Message.class);
					command = Command.forNumber(message.getCmd());
					sendId = message.getSendId();
				}catch(Exception e){
					return wsRequestPacket;
				}
			}
			wsRequestPacket.setCommand(command);
			wsRequestPacket.setSendId(sendId);
			return wsRequestPacket;
		}
	}
	@Override
	public void handler(ImPacket imPacket, ImChannelContext imChannelContext) throws ImException {
		WsRequestPacket wsRequestPacket = (WsRequestPacket) imPacket;
		AbstractCmdHandler cmdHandler = CommandManager.getCommand(wsRequestPacket.getCommand());
		if(cmdHandler == null){
			//是否ws分片发包尾帧包
			if(!wsRequestPacket.isWsEof()) {
				return;
			}
			ImPacket wsPacket = new ImPacket(Command.COMMAND_UNKNOW, new RespBody(Command.COMMAND_UNKNOW,ImStatus.C10017).toByte());
			JimServerAPI.send(imChannelContext, wsPacket);
			return;
		}
		ImPacket response = cmdHandler.handler(wsRequestPacket, imChannelContext);
		if(Objects.nonNull(response)){
			JimServerAPI.send(imChannelContext, response);
		}
	}

	public WsConfig getWsServerConfig() {
		return wsServerConfig;
	}

	public void setWsServerConfig(WsConfig wsServerConfig) {
		this.wsServerConfig = wsServerConfig;
	}

	public IWsMsgHandler getWsMsgHandler() {
		return wsMsgHandler;
	}

	public void setWsMsgHandler(IWsMsgHandler wsMsgHandler) {
		this.wsMsgHandler = wsMsgHandler;
	}

}
