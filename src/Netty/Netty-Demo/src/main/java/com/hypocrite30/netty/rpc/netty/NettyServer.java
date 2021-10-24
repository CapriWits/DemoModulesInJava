package com.hypocrite30.netty.rpc.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @Description: 服务调用，基于 netty
 * @Author: Hypocrite30
 * @Date: 2021/10/23 21:31
 */
public class NettyServer {

    /**
     * 对外暴露的启动方法
     */
    public static void startServer(String hostName, int port) {
        startServer0(hostName, port);
    }

    /**
     * 启动服务方法
     * @param hostname 主机名
     * @param port     端口
     */
    private static void startServer0(String hostname, int port) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap()
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new StringDecoder());
                            pipeline.addLast(new StringEncoder());
                            pipeline.addLast(new NettyServerHandler()); // 业务 handler
                        }
                    });
            ChannelFuture channelFuture = serverBootstrap.bind(hostname, port).sync();
            channelFuture.addListener((ChannelFutureListener) future -> {
                if (future.isSuccess())
                    System.out.println("监听端口 " + port + " 成功");
                else
                    System.out.println("监听端口 " + port + " 失败");
            });
            System.out.println("服务提供方开始提供服务...");
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
