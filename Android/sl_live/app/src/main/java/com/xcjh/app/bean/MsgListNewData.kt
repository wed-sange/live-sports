package com.xcjh.app.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_listdb")
class MsgListNewData {
    @ColumnInfo(name = "avatar")
    var avatar: String? = null

    @ColumnInfo(name = "content")
    var content: String? = null

    @ColumnInfo(name = "createTime")
    var createTime: Long = 0

    @ColumnInfo(name = "msgType")
    var msgType: Int = 0
    @ColumnInfo(name = "dataType")
    var dataType: Int = 1
    @ColumnInfo(name = "fromId")
    var fromId: String = ""
    @ColumnInfo(name = "sent")
    var sent: Int? = 0//是否已发送: 0 正在发送 1 已发送 2 发送失败
    @ColumnInfo(name = "anchorId")
    var anchorId: String? = null
    @ColumnInfo(name = "withId")
    var withId: String? = null
    @ColumnInfo(name = "sendId")
    var sendId: String? = "0" //消息唯一性，用来判断回调
    @ColumnInfo(name = "id")
    var id: String? = null

    @ColumnInfo(name = "nick")
    var nick: String? = null

    @ColumnInfo(name = "noReadSum")
    var noReadSum: Int = 0

    @PrimaryKey(autoGenerate = true)
    var idd = 0

}