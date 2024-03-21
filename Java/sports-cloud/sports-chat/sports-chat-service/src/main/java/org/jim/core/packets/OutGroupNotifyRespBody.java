/**
 * 
 */
package org.jim.core.packets;

import org.jim.core.ImStatus;
import org.jim.core.Status;
import org.sports.chat.service.constant.WsResponseStatus;

/**
 * 版本: [1.0]
 * 功能说明: 进入群组通知消息体
 * 作者: WChao 创建时间: 2017年7月26日 下午5:14:04
 */
public class OutGroupNotifyRespBody extends RespBody{

	private static final long serialVersionUID = 3828976681110713803L;
	private User user;
	private String group;

	public OutGroupNotifyRespBody(){
		super(Command.COMMAND_EXIT_GROUP_NOTIFY_RESP, null);
	}

	public OutGroupNotifyRespBody(Integer code, String msg){
		super(Command.COMMAND_EXIT_GROUP_NOTIFY_RESP, null);
		this.code = code;
		this.msg = msg;
	}

	public OutGroupNotifyRespBody(Status status){
		super(Command.COMMAND_EXIT_GROUP_NOTIFY_RESP,status);
	}

	public OutGroupNotifyRespBody(Command command, Status status){
		super(command,status);
	}
	public User getUser() {
		return user;
	}

	public OutGroupNotifyRespBody setUser(User user) {
		this.user = user;
		return this;
	}
	public String getGroup() {
		return group;
	}

	public OutGroupNotifyRespBody setGroup(String group) {
		this.group = group;
		return this;
	}

	public static OutGroupNotifyRespBody success(){
		OutGroupNotifyRespBody body = new OutGroupNotifyRespBody(ImStatus.C10000);
		body.setSuccess(WsResponseStatus.TRUE);
		return body;
	}

	public static OutGroupNotifyRespBody failed(){
		OutGroupNotifyRespBody body = new OutGroupNotifyRespBody(ImStatus.C10012);
		body.setSuccess(WsResponseStatus.FALSE);
		return body;
	}

}
