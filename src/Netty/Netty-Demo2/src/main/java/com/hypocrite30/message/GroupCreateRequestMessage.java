package com.hypocrite30.message;

import lombok.Data;
import lombok.ToString;

import java.util.Set;

/**
 * @Description: 创建群聊请求信息
 * @Author: Hypocrite30
 * @Date: 2021/11/7 22:31
 */
@Data
@ToString(callSuper = true)
public class GroupCreateRequestMessage extends Message {
    private String groupName; // 群聊名称
    private Set<String> members; // 群友名称集合

    public GroupCreateRequestMessage(String groupName, Set<String> members) {
        this.groupName = groupName;
        this.members = members;
    }

    @Override
    public int getMessageType() {
        return GroupCreateRequestMessage;
    }
}
