/**
 * 
 */
package org.sports.chat.web.command;

import org.jim.core.ImChannelContext;
import org.jim.core.ImPacket;
import org.jim.core.exception.ImException;
import org.jim.server.processor.handshake.WsHandshakeProcessor;

/**

 *
 */
public class MyWsHandshakeProcessor extends WsHandshakeProcessor {

	@Override
	public void onAfterHandshake(ImPacket packet, ImChannelContext imChannelContext) throws ImException {

//		if(packet != null){
//			JimServerAPI.send(imChannelContext, packet);
//		}
	}
}
