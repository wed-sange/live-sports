/**
 * 
 */
package org.jim.core.packets;

import org.jim.core.ImStatus;
import org.jim.core.Status;
import org.sports.chat.service.constant.WsResponseStatus;

/**
 * 版本: [1.0]
 * 功能说明: 退出群组响应消息体
 * 作者: WChao 创建时间: 2017年7月26日 下午5:09:20
 */
public class OutGroupRespBody extends RespBody {
	
	private static final long serialVersionUID = 6635620192752369689L;
	public OutGroupResult result;
	public String group;
	public String userId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}


	public OutGroupRespBody(){
		this(Command.COMMAND_EXIT_GROUP_NOTIFY_RESP, null);
	}

	public OutGroupRespBody(Integer code, String msg){
		super(code, msg);
		this.command = Command.COMMAND_EXIT_GROUP_NOTIFY_RESP;
	}

	public OutGroupRespBody(Status status){
		this(Command.COMMAND_EXIT_GROUP_NOTIFY_RESP, status);
	}

	public OutGroupRespBody(Command command , Status status){
		super(command, status);
	}

	public OutGroupResult getResult() {
		return result;
	}

	public OutGroupRespBody setResult(OutGroupResult result) {
		this.result = result;
		return this;
	}

	public String getGroup() {
		return group;
	}

	public OutGroupRespBody setGroup(String group) {
		this.group = group;
		return this;
	}

	public OutGroupRespBody setData(Object data){
		super.setData(data);
		return this;
	}

	public static OutGroupRespBody success(){
		OutGroupRespBody outGroupRespBody = new OutGroupRespBody(ImStatus.C10000);
		outGroupRespBody.setSuccess(WsResponseStatus.TRUE);
		outGroupRespBody.setResult(OutGroupResult.OUT_GROUP_RESULT_OK);
		return outGroupRespBody;
	}

	public static OutGroupRespBody failed(){
		OutGroupRespBody outGroupRespBody = new OutGroupRespBody(ImStatus.C10112);
		outGroupRespBody.setSuccess(WsResponseStatus.FALSE);
		outGroupRespBody.setResult(OutGroupResult.OUT_GROUP_RESULT_UNKNOWN);
		return outGroupRespBody;
	}

}
