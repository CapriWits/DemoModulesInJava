package com.hypocrite30.netty.in_out_boundhandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 用自定义的编码器和解码器为案例，实现入站和出站
 * 这些 自定义 handler 在 server 和 client 之间的 channelpipline 中依次排列处理
 * 出站：需要将数据编码成二进制字节数据
 * 入站：对传输过来的二进制数据进行解码
 * @Description: 入站出站实例 Server
 * @Author: Hypocrite30
 * @Date: 2021/10/19 21:40
 */
public class MyServer {
    public static void main(String[] args) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap()
                    .group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new MyServerInitializer()); //自定义一个初始化类
            ChannelFuture channelFuture = serverBootstrap.bind(7000).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
