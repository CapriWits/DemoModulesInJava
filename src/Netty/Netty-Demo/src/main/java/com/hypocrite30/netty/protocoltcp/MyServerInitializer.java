package com.hypocrite30.netty.protocoltcp;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @Description: Server handler 初始化器
 * @Author: Hypocrite30
 * @Date: 2021/10/20 20:49
 */
public class MyServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new MyMessageDecoder());//解码器
        pipeline.addLast(new MyMessageEncoder());//编码器
        pipeline.addLast(new MyServerHandler());
    }
}
