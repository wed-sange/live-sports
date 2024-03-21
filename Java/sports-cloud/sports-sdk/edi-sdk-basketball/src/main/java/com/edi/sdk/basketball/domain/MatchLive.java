package com.edi.sdk.basketball.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * 实时统计
 * 
 2023年5月30日 下午2:35:40
 */
@Data
public class MatchLive {
	/**
	 * 比赛id
	 */
	private Integer id;
	/**
	 * 比分字段说明<br />
	 * example：[2783605, 8, 0, [0, 0, 0, 0, 0], [0, 0, 0, 0, 0]] <br />
	 * Enum: <br />
	 * Array[5] <br />
	 * 0:"比赛id - int" <br />
	 * 1:"比赛状态，详见状态码->比赛状态 - int" <br />
	 * 2:"小节剩余时间(秒) - int" <br />
	 * 3:Array[5] <br />
	 * 0:"主队第1节分数 - int" <br />
	 * 1:"主队第2节分数 - int" <br />
	 * 2:"主队第3节分数 - int" <br />
	 * 3:"主队第4节分数 - int" <br />
	 * 4:"主队加时分数 - int" <br />
	 * 4:Array[5] <br />
	 * 0:"客队第1节分数 - int" <br />
	 * 1:"客队第2节分数 - int" <br />
	 * 2:"客队第3节分数 - int" <br />
	 * 3:"客队第4节分数 - int" <br />
	 * 4:"客队加时分数 - int"
	 */
	private List<Object> score;
	/**
	 * 加时赛比分字段说明（大于1个加时才有该字段，每一位为1节加时比分）<br />
	 * example：[[8, 9, 6], [8, 9, 8]] <br />
	 * Enum: <br />
	 * Array[2] <br />
	 * 0:Array[3] <br />
	 * 0:"主队第1节加时分数 - int" <br />
	 * 1:"主队第2节加时分数 - int" <br />
	 * 2:"主队第n节加时分数 - int" <br />
	 * 1:Array[3]<br />
	 * 0:"客队第1节加时分数 - int" <br />
	 * 1:"客队第2节加时分数 - int" <br />
	 * 2:"客队第n节加时分数 - int" <br />
	 */
	@JsonProperty(value = "over_time_scores")
	private List<List<Integer>> overTimeScores;
	/**
	 * 技术统计字段，可能不存在<br />
	 * [<br />
	 * 暂停数：当前小节剩余暂停数，次节重置，需自行记录<br />
	 * 犯规数：当前小节犯规数，次节重置，需自行记录<br />
	 * example：[1, 8, 9]<br />
	 * Enum:<br />
	 * Array[3]<br />
	 * 0:"统计类型，请参考 状态码->技术统计 - int"<br />
	 * 1:"主队数值 - int"<br />
	 * 2:"客队数值 - int"<br />
	 * ]
	 */
	private List<List<Integer>> stats;
	/**
	 * 文字直播字段说明，可能不存在<br />
	 * tlive字段为列表，表示不同节数的数据<br />
	 * example：[["5^11:45^1^0^0-0^阿隆·戈登 两分投篮不中^12051^1^0^0^25,8"], []]<br />
	 * Enum:<br />
	 * Array[2]<br />
	 * 0:Array[2]<br />
	 * 0:"(兼容忽略)^时间^主客队（0-中立，1-主队，2-客队）^0^客队比分-主队比分^说明^(兼容忽略) - string"<br />
	 * 1:"(兼容忽略)^时间^主客队（0-中立，1-主队，2-客队）^0^客队比分-主队比分^说明^(兼容忽略) - string"<br />
	 * 1:Array[1]<br />
	 * 0:"后续小节，同第一小节"<br />
	 */
	private List<List<Object>> tlive;
	/**
	 * 阵容字段说明，可能不存在<br />
	 * example：[[[1126,"J.J. 巴里亚","J.J. 巴里亚","J.J.
	 * Barea","https://cdn.sportnanoapi.com/basketball/player/143a4b49049f75c3f7ce5e5275c0d26f.png","5","27^1-9^1-5^0-0^0^7^7^4^0^0^1^2^+2^3^1^0^1"]],[],"0^37-86^9-30^11-19^7^39^46^24^12^4^13^18^0^94","0^33-89^9-34^10-15^10^44^54^20^8^2^16^15^0^85"]<br
	 * />
	 * Enum:<br />
	 * Array[4]<br />
	 * 0:Array[1]<br />
	 * 0:Array[7]<br />
	 * 0:"主队球员id - int"<br />
	 * 1:"中文名称 - string"<br />
	 * 2:"粤语名称 - string"<br />
	 * 3:"英文名称 - string"<br />
	 * 4:"球员logo - string"<br />
	 * 5:"球衣号 - string"<br />
	 * 6:"在场持续时间^命中次数-投篮次数^三分球投篮命中次数-三分投篮次数^罚球命中次数-罚球投篮次数^进攻篮板^防守篮板^总的篮板^助攻数^抢断数^盖帽数^失误次数^个人犯规次数^+/-值^得分^是否出场(1-出场，0-没出场)^是否在场上（0-在场上，1-没在场上）（用于赛中）^是否是替补（1-替补，0-首发）
	 * - string"<br />
	 * 1:Array[1]<br />
	 * 0:Array[7]<br />
	 * 0:"客队球员id - int"<br />
	 * 1:"中文名称 - string"<br />
	 * 2:"粤语名称 - string"<br />
	 * 3:"英文名称 - string"<br />
	 * 4:"球员logo - string"<br />
	 * 5:"球衣号 - string"<br />
	 * 6:"在场持续时间^命中次数-投篮次数^三分球投篮命中次数-三分投篮次数^罚球命中次数-罚球投篮次数^进攻篮板^防守篮板^总的篮板^助攻数^抢断数^盖帽数^失误次数^个人犯规次数^+/-值^得分^是否出场(1-出场，0-没出场)^是否在场上（0-在场上，1-没在场上）（用于赛中）^是否是替补（1-替补，0-首发）
	 * - string"<br />
	 * 2:"主队球队数据：(兼容忽略)^命中次数-投篮次数^三分球投篮命中次数-三分投篮次数^罚球命中次数-罚球投篮次数^进攻篮板^防守篮板^总的篮板^助攻数^抢断数^盖帽数^失误次数^个人犯规次数^(兼容忽略)^得分
	 * - string"<br />
	 * 3:"客队球队数据：(兼容忽略)^命中次数-投篮次数^三分球投篮命中次数-三分投篮次数^罚球命中次数-罚球投篮次数^进攻篮板^防守篮板^总的篮板^助攻数^抢断数^盖帽数^失误次数^个人犯规次数^(兼容忽略)^得分
	 * - string"<br />
	 */
	private List<Object> players;
}