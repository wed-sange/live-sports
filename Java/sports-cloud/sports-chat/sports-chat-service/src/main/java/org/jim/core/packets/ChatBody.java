/**
 *
 */
package org.jim.core.packets;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * 版本: [1.0]
 * 功能说明:
 * 作者: WChao 创建时间: 2017年7月26日 上午11:34:44
 */
@Data
public class ChatBody extends Message {

    private static final long serialVersionUID = 5731474214655476286L;
    /**
     * 发送用户id;
     */
    private String from;
    /**
     * 发送用户头像;
     */
    private String fromAvatar;
    /**
     * 发送用户昵称;
     */
    private String fromNickName;
    /**
     * 接收者用户头像;
     */
    private String toAvatar;
    /**
     * 接收者用户昵称;
     */
    private String toNickName;

    /**
     * 主播id;
     */
    private String anchorId;
    /**
     * 目标用户id;
     */
    private String to;
    /**
     * 消息类型;(如：0:text、1:image、2:voice、3:video、4:music、5:news)
     */
    private Integer msgType;
    /**
     * 聊天类型;(如公聊、私聊)
     */
    private Integer chatType;
    /**
     * 消息内容;
     */
    private String content;
    /**
     * 消息发到哪个群组;
     */
    private String groupId;

    /**
     * 发送者等级（用户独有）;
     */
    private Integer level;

    /**
     * 发送者类型;
     */
    private Integer identityType;

    private ChatBody() {
    }

    private ChatBody(String id, String from, String to, Integer msgType, Integer chatType,
                     String content, String groupId, Integer cmd, Long createTime, Integer identityType,
                     JSONObject extras, String toAvatar, String toNickName, Integer level) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.msgType = msgType;
        this.chatType = chatType;
        this.content = content;
        this.identityType = identityType;
        this.groupId = groupId;
        this.cmd = cmd;
        this.createTime = createTime;
        this.extras = extras;
        this.toAvatar = toAvatar;
        this.toNickName = toNickName;
        this.level = level;
    }

    public static ChatBody.Builder newBuilder() {
        return new ChatBody.Builder();
    }

    public String getFrom() {
        return from;
    }

    public ChatBody setFrom(String from) {
        this.from = from;
        return this;
    }

    public String getTo() {
        return to;
    }

    public ChatBody setTo(String to) {
        this.to = to;
        return this;
    }

    public Integer getMsgType() {
        return msgType;
    }

    public ChatBody setMsgType(Integer msgType) {
        this.msgType = msgType;
        return this;
    }

    public String getContent() {
        return content;
    }

    public ChatBody setContent(String content) {
        this.content = content;
        return this;
    }
    public String getAnchorId() {
        return anchorId;
    }

    public ChatBody setAnchorId(String anchorId) {
        this.anchorId = anchorId;
        return this;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Integer getChatType() {
        return chatType;
    }

    public ChatBody setChatType(Integer chatType) {
        this.chatType = chatType;
        return this;
    }
    public Integer getIdentityType() {
        return identityType;
    }

    public ChatBody setIdentityType(Integer identityType) {
        this.identityType = identityType;
        return this;
    }

    public static class Builder extends Message.Builder<ChatBody, ChatBody.Builder> {
        /**
         * 来自user_id;
         */
        private String from;
        /**
         * 目标user_id;
         */
        private String to;
        /**
         * 消息类型;(如：0:text、1:image、2:voice、3:vedio、4:music、5:news)
         */
        private Integer msgType;
        /**
         * 聊天类型;(如公聊、私聊)
         */
        private Integer chatType;
        /**
         * 消息内容;
         */
        private String content;
        /**
         * 消息发到哪个群组;
         */
        private String groupId;
        /**
         * 消息发到哪个群组;
         */
        private Boolean read;

        private String toAvatar;

        private String toNickName;

        /**
         * 发送者类型;
         */
        private Integer identityType;

        private Integer level;

        private String fromPlat;

        public Builder() {
        }

        ;

        public Builder from(String from) {
            this.from = from;
            return this;
        }

        public Builder to(String to) {
            this.to = to;
            return this;
        }

        public Builder msgType(Integer msgType) {
            this.msgType = msgType;
            return this;
        }

        public Builder chatType(Integer chatType) {
            this.chatType = chatType;
            return this;
        }
        public Builder content(String content) {
            this.content = content;
            return this;
        }
        public Builder toAvatar(String showAvatar) {
            this.toAvatar = showAvatar;
            return this;
        }
        public Builder toNickName(String showNickName) {
            this.toNickName = showNickName;
            return this;
        }

        public Builder groupId(String groupId) {
            this.groupId = groupId;
            return this;
        }
        public Builder fromPlat(String fromPlat) {
            this.fromPlat = fromPlat;
            return this;
        }

        public Builder identityType(Integer identityType) {
            this.identityType = identityType;
            return this;
        }
        public Builder level(Integer level) {
            this.level = level;
            return this;
        }

        @Override
        protected Builder getThis() {
            return this;
        }

        @Override
        public ChatBody build() {
            return new ChatBody(this.id, this.from, this.to, this.msgType, this.chatType, this.content, this.groupId, this.cmd, this.createTime, this.identityType, this.extras, this.toAvatar, this.toNickName, this.level);
        }
    }
}
