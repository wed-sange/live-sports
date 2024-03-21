package org.sports.chat.service.service.impl;

import org.jim.core.packets.Command;
import org.jim.core.ws.Opcode;
import org.jim.core.ws.WsResponsePacket;
import org.jim.server.JimServerAPI;
import org.jim.server.JimServerCache;
import org.sports.chat.service.service.ISystemMessageService;
import org.springframework.stereotype.Service;
import org.tio.core.Tio;
import org.tio.core.TioConfig;

@Service
public class ISystemMessageServiceImpl implements ISystemMessageService {

    @Override
    public void sendMessageToAllUser(Command command, String pushToAllWsDto) {
        // todo 调整为redis中监听字段
        WsResponsePacket wsResponsePacket = new WsResponsePacket();
        wsResponsePacket.setCommand(command);
        wsResponsePacket.setBody(pushToAllWsDto.getBytes());
        wsResponsePacket.setWsOpcode(Opcode.TEXT);
        TioConfig tioConfig = JimServerCache.getTioConfig();
        Tio.sendToAll(tioConfig, wsResponsePacket);
    }

    @Override
    public void sendMessageToGroupUser(Command command, String groupId, String pushToAllWsDto) {
        WsResponsePacket wsResponsePacket = new WsResponsePacket();
        wsResponsePacket.setCommand(command);
        wsResponsePacket.setBody(pushToAllWsDto.getBytes());
        wsResponsePacket.setWsOpcode(Opcode.TEXT);
        JimServerAPI.sendToGroup(groupId, wsResponsePacket);
    }

}
