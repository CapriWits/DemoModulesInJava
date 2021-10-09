package com.hypocrite30;

import com.hypocrite30.client.ChatClient;

import java.io.IOException;

/**
 * @Description: NIO 聊天室客户端2 启动类
 * @Author: Hypocrite30
 * @Date: 2021/10/9 20:27
 */
public class ClientMain2 {
    public static void main(String[] args) {
        try {
            new ChatClient().start("client2");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
