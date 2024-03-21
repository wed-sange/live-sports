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
public class  LoginRespBody extends RespBody {
	
	private static final long serialVersionUID = 1L;
	private String token;
	private User user;

	public LoginRespBody(){
		this.setCommand(Command.COMMAND_LOGIN_RESP);
	}

	public LoginRespBody(Status status){
		this(status,null);
	}

	public LoginRespBody(Status status , User user){
		this(status, user, null);
	}

	public LoginRespBody(Status status , User user, String token){
		super(Command.COMMAND_LOGIN_RESP, status);
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

	public static LoginRespBody success(){
		LoginRespBody body = new LoginRespBody(ImStatus.C10000);
		body.setSuccess(WsResponseStatus.TRUE);
		return body;
	}

	public static LoginRespBody failed(){
		LoginRespBody body = new LoginRespBody(ImStatus.C10008);
		body.setSuccess(WsResponseStatus.FALSE);
		return body;
	}

	public static LoginRespBody failed(String msg){
		LoginRespBody loginRespBody = new LoginRespBody(ImStatus.C10008);
		loginRespBody.setMsg(msg);
		loginRespBody.setSuccess(WsResponseStatus.FALSE);
		return loginRespBody;
	}
}
