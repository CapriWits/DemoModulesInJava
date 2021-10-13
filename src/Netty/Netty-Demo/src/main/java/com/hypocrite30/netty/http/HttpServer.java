package com.hypocrite30.netty.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @Description: Http 服务 服务端
 * @Author: Hypocrite30
 * @Date: 2021/10/13 17:12
 */
public class HttpServer {
    public static void main(String[] args) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ServerChannelInitializer()); // 自定义 channel 初始化器，向 pipeline 添加 handler
            ChannelFuture channelFuture = serverBootstrap.bind(9999).sync();
            channelFuture.addListener((ChannelFutureListener) future -> {
                if (channelFuture.isSuccess())
                    System.out.println("监听端口 6668 成功");
                else
                    System.out.println("监听端口 6668 失败");
            });
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
