/**
 * 
 */
package org.jim.core.packets;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 版本: [1.0]
 * 功能说明: 主播、助理、运营读取用户消息通知对象
 */
@Data
public class AnchorReadUserMessage extends Message{


	@Schema(description = "主播ID")
	private String anchorId;

	@Schema(description = "主播头像")
	private String avatar;

	@Schema(description = "主播昵称")
	private String nick;

	@Schema(description = "未读消息数量")
	private long noReadSum;

	private ChatUserInfo chatUserInfo;


}
