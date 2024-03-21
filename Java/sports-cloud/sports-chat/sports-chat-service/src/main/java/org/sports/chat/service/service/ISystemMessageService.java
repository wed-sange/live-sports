package org.sports.chat.service.service;


import org.jim.core.packets.Command;

public interface ISystemMessageService {
    void sendMessageToAllUser(Command command, String pushToAllWsDto);
    void sendMessageToGroupUser(Command command,String groupId, String pushToAllWsDto);

}
