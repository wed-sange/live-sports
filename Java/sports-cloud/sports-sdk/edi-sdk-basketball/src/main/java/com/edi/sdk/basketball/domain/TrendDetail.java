package com.edi.sdk.basketball.domain;

import java.util.List;

import lombok.Data;

/**
 * 比赛趋势详情
 * 
 2023年5月31日 上午11:20:28
 */
@Data
public class TrendDetail {
	/**
	 * 小节数
	 */
	private Integer count;
	/**
	 * 小节时长
	 */
	private Integer per;
	/**
	 * 趋势变化(小节个数)，按比赛分钟数变化<br />
	 * example：[[4, 0, -2], [-2, 3, 1]]<br />
	 * Enum:<br />
	 * Array[4]<br />
	 * 0:Array[1]<br />
	 * 0:"第1节，趋势变化数据 - int"<br />
	 * 1:Array[1]<br />
	 * 0:"第2节，趋势变化数据 - int"<br />
	 * 2:Array[1]<br />
	 * 0:"第3节，趋势变化数据 - int"<br />
	 * 3:Array[1]<br />
	 * 0:"第4节，趋势变化数据 - int"<br />
	 */
	private List<Object> data;
}
