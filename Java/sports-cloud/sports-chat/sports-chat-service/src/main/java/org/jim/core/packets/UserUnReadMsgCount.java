/**
 * 
 */
package org.jim.core.packets;

import lombok.Data;

/**
 * 版本: [1.0]
 * 功能说明: 用户未读消息总数
 */
@Data
public class UserUnReadMsgCount extends Message{

	private static final long serialVersionUID = -3817755433171220952L;
	/**
	 * 总数
	 */
	private Long totalCount;


}
