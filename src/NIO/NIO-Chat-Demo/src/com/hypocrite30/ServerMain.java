package com.hypocrite30;

import com.hypocrite30.server.ChatServer;

import java.io.IOException;

/**
 * @Description: NIO 聊天室服务器启动类
 * @Author: Hypocrite30
 * @Date: 2021/10/9 16:01
 */
public class ServerMain {
    public static void main(String[] args) {
        try {
            new ChatServer().start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}