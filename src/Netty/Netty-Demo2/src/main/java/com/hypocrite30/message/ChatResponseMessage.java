package com.hypocrite30.message;

import lombok.Data;
import lombok.ToString;

/**
 * @Description: 聊天响应消息
 * @Author: Hypocrite30
 * @Date: 2021/11/7 22:28
 */
@Data
@ToString(callSuper = true)
public class ChatResponseMessage extends AbstractResponseMessage {

    private String from;
    private String content;

    public ChatResponseMessage(boolean success, String reason) {
        super(success, reason);
    }

    public ChatResponseMessage(String from, String content) {
        this.from = from;
        this.content = content;
    }

    @Override
    public int getMessageType() {
        return ChatResponseMessage;
    }
}
