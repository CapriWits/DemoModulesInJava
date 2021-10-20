package com.hypocrite30.netty.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;

/**
 * @Description: 客户端发送信息，同时监听服务器发来的信息
 * @Author: Hypocrite30
 * @Date: 2021/10/20 20:28
 */
public class MyClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private int count;

    /**
     * 客户端发送数据
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        //使用客户端发送10条数据 hello,server 编号，编号可以模拟包的 id
        for (int i = 0; i < 10; ++i) {
            ByteBuf buffer = Unpooled.copiedBuffer("hello,server " + i, Charset.forName("utf-8"));
            ctx.writeAndFlush(buffer);
        }
    }

    /**
     * 客户端监听数据
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        byte[] buffer = new byte[msg.readableBytes()];
        msg.readBytes(buffer);
        String message = new String(buffer, Charset.forName("utf-8"));
        System.out.println("客户端接收到消息 = " + message);
        System.out.println("客户端接收消息数量 = " + (++this.count));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
