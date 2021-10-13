package com.hypocrite30.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * @Description: Netty 客户端自定义 handler
 * @Author: Hypocrite30
 * @Date: 2021/10/12 20:49
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    /**
     * 通道就绪就会触发该方法
     * @param ctx 上下文对象
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("Client ctx: " + ctx);
        ctx.writeAndFlush(Unpooled.copiedBuffer("Hi! Server ...", CharsetUtil.UTF_8));
    }

    /**
     * 读取数据实际(可以读取客户端发送的消息)
     * @param ctx 上下文对象, 含有 管道pipeline, 通道channel, 地址
     * @param msg 客户端发送的数据 默认Object
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("服务器回复的消息: " + buf.toString(CharsetUtil.UTF_8));
        System.out.println("服务器的地址： " + ctx.channel().remoteAddress());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}