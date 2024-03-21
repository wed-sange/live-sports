package com.edi.sdk.core;

import com.alibaba.fastjson2.annotation.JSONField;

/**
 * Request基类
 *
 2023年5月23日 上午10:23:47
 */
public interface EdiRequest {
	/**
	 * API地址
	 *
	 * @return
	 */
	@JSONField(serialize = false)
	String getPath();

	/**
	 * 请求方式
	 *
	 * @return
	 */
	@JSONField(serialize = false)
	default RequestMethod getMethod() {
		return RequestMethod.GET;
	}

}
