package com.hypocrite30.netty.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @Description: 服务端 WorkGroup 的 NIOEventLoop 注册的 channel 添加 handler
 * @Author: Hypocrite30
 * @Date: 2021/10/13 17:14
 */
public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) {
        //向管道加入处理器
        //得到管道
        ChannelPipeline pipeline = ch.pipeline();
        //加入一个netty 提供的httpServerCodec codec =>[coder - decoder]
        //1. HttpServerCodec 是 netty 提供的处理 http 的 编-解码器
        pipeline.addLast("MyHttpServerCodec", new HttpServerCodec());
        //2. 增加一个自定义的handler
        pipeline.addLast("MyHttpServerHandler", new HttpServerHandler());
        System.out.println("Channel initialized suceesssful ...");
    }
}
