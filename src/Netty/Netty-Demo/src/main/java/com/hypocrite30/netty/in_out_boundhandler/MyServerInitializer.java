package com.hypocrite30.netty.in_out_boundhandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @Description: 自定义服务端初始化 handler
 * @Author: Hypocrite30
 * @Date: 2021/10/19 21:41
 */
public class MyServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //入站的handler进行解码 自定义解码器 MyByteToLongDecoder
        pipeline.addLast(new MyByteToLongDecoder());
        // pipeline.addLast(new MyByteToLongDecoder2());
        //出站的handler进行编码
        // pipeline.addLast(new MyLongToByteEncoder());
        //自定义的handler 处理业务逻辑
        pipeline.addLast(new MyServerHandler());
    }
}
