package com.hypocrite30.netty.rpc.provider;

import com.hypocrite30.netty.rpc.netty.NettyServer;

/**
 * @Description: 服务方启动类，即 NettyServer
 * @Author: Hypocrite30
 * @Date: 2021/10/22 23:04
 */
public class ServerBootstrap {
    public static void main(String[] args) {
        NettyServer.startServer("127.0.0.1", 7000);
    }
}
