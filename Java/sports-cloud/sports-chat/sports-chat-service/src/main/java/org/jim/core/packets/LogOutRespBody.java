/**
 * 
 */
package org.jim.core.packets;

import org.jim.core.ImStatus;
import org.jim.core.Status;
import org.sports.chat.service.constant.WsResponseStatus;

/**
 * 版本: [1.0]
 * 功能说明: 
 * 作者: WChao 创建时间: 2017年9月12日 下午3:15:28
 */
public class LogOutRespBody extends RespBody {
	
	private static final long serialVersionUID = 1L;
	private String token;
	private User user;

	public LogOutRespBody(){
		this.setCommand(Command.COMMAND_LOGOUT_REQ);
	}

	public LogOutRespBody(Status status){
		this(status,null);
	}

	public LogOutRespBody(Status status , User user){
		this(status, user, null);
	}

	public LogOutRespBody(Status status , User user, String token){
		super(Command.COMMAND_LOGOUT_REQ, status);
		this.user = user;
		this.token = token;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public static LogOutRespBody success(){
		LogOutRespBody body = new LogOutRespBody(ImStatus.C10000);
		body.setSuccess(WsResponseStatus.TRUE);
		return body;

	}

	public static LogOutRespBody failed(){
		LogOutRespBody body = new LogOutRespBody(ImStatus.C10115);
		body.setSuccess(WsResponseStatus.FALSE);
		return body;
	}

	public static LogOutRespBody failed(String msg){
		LogOutRespBody loginRespBody = new LogOutRespBody(ImStatus.C10115);
		loginRespBody.setSuccess(WsResponseStatus.FALSE);
		loginRespBody.setMsg(msg);
		return loginRespBody;
	}
}
