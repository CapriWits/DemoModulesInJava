package com.hypocrite30;

import com.hypocrite30.client.ChatClient;

import java.io.IOException;

/**
 * @Description: NIO 聊天室客户端1 启动类
 * @Author: Hypocrite30
 * @Date: 2021/10/9 17:42
 */
public class ClientMain1 {
    public static void main(String[] args) {
        try {
            new ChatClient().start("client1");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
