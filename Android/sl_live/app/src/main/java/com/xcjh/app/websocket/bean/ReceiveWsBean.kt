package com.xcjh.app.websocket.bean

/**
 * 接收数据
 *
 * 基础响应体
 */
data class ReceiveWsBean<T>(
    val code: String,   // 10007  10008  //具体错误编码
    val success: Int = 0,   // 0成功 1失败
    val command: Int,   // 6，对应的cmd：5
    val msg: String? = "",   // 消息内容
    val data: T? = null,   // 消息内容
)
/**
 * 推送比分数据
 *
 */
data class ReceiveChangeMsg(
    val awayHalfScore: Long,
    val awayScore: Long,
    val homeHalfScore: Long,
    val homeScore: Long,
    val matchId: Long,
    val matchType: Long,
    val status: Long,
    val runTime: Long,//
    var scoresDetail: List<List<Int>>? = listOf(),
)
data class FeedSystemNoticeBean(
    val bizId: String,//业务ID，反馈信息ID   bizId=""如果是空就是全局    不为空就是主播id
    val createTime: Long,//通知时间
    val noticeId: Long,//通知ID
    val reason: String,//禁言原因或者备注信息
    val title: String,//前端展示标题
    val userId: Long//用户ID
)
/** 推送维度消息
 *
 */
data class NoReadMsg(
    val totalCount: Int
)
/**
 * 群聊和私聊消息内容
 *
 */
data class ReceiveChatMsg(
    var id: String? = "",   // 消息ID
    var chatType: Int? = 1,   // 聊天类型，群聊：1， 私聊：2
    var msgType: Int? = 0, //消息类型，文字：0， 图片：1  (如：0:text、1:image、2:voice、3:video、4:music、5:news)
    var dataType: Int? = 0,
    var cmd: Int? = 11,   // 消息命令码 6，对应的cmd：5
    var anchorId: String? = null,   // 当前聊天主播的id【私聊独有，不管是发送还是接收】
    var content: String = "",   // 消息内容
    var createTime: Long? = 0,   // 消息时间
    var from: String? = "",   // 发送者ID
    var groupId: String? = null,   // 群聊ID【群聊独有】 群组ID
    var sendId: String? = "0", //消息唯一性，用来判断回调
    var sent: Int? = 0,//是否已发送: 0 正在发送 1 已发送 2 发送失败
    var toAvatar: String? = null, //接收者头像（主播、运营、助手均显示主播头像）
    var toNickName: String? = null, //接收者昵称（主播、运营、助手均显示主播昵称）
    var fromAvatar: String? = "", //发送者头像（主播、运营、助手均显示主播头像）
    var fromNickName: String? = "", //发送者昵称（主播、运营、助手均显示主播昵称）
    var level: String? = "0", //用户等级（发送者为用户时必填）
    val identityType: Int = 0, //发送者身份身份(0：普通用户，1主播 2助手 3运营)
    var noReadSum: Int = 0
)

/**
 * 接收到已读消息成功
 * cmd 23
 */
data class ReceiveReadWsBean(
    val cmd: Int? = null,
    val messageId: String? = "",//messageId
    val read: Int? = 1,// 0-未读， 1-已读
    val currentId: String? = null,//当前用户ID
    val channelType: Int? = 3,//消息发送平台：2-LIVE, 3-APP
    val toId: String? = "3",//接收人ID
)

/**
 * 直播流状态
 */
data class LiveStatus(
    val anchorId: String = "", //主播ID
    val id: String = "", //直播间ID
    val playUrl: String = "", // 播放地址
    val liveStatus: Int = 0,// 直播间状态：2：开播 3：关播
    val matchId: String = "",//比赛ID
    val awayTeamName:String="",//客队名字
    val competitionName:String="",//赛事名称
    val homeTeamName:String="",//主队名称
    val hotValue:Int=0,//热度值
    val nickName:String="",//主播昵称
    val rank:String="",//主播排名
    val userLogo:String="",//主播头像
    val matchType:String="",//比赛类型
    val coverImg:String="",//封面图

)