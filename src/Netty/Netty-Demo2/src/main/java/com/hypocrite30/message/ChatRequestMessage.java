package com.hypocrite30.message;

import lombok.Data;
import lombok.ToString;

/**
 * @Description: 聊天请求信息
 * @Author: Hypocrite30
 * @Date: 2021/11/7 22:26
 */
@Data
@ToString(callSuper = true)
public class ChatRequestMessage extends Message {
    private String content; // 信息内容
    private String to; // 目的地用户名
    private String from; // 始发地用户名

    public ChatRequestMessage() {
    }

    public ChatRequestMessage(String from, String to, String content) {
        this.from = from;
        this.to = to;
        this.content = content;
    }

    @Override
    public int getMessageType() {
        return ChatRequestMessage;
    }
}
