package com.hypocrite30.netty.in_out_boundhandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 1.在前面解码器将二进制byte转化成 Long 类型之后，业务逻辑是打印出来
 * 2.给客户端再发送 Long 类型数据，编码，然后发送
 * @Description: 自定义服务端 handler，
 * @Author: Hypocrite30
 * @Date: 2021/10/19 21:47
 */
public class MyServerHandler extends SimpleChannelInboundHandler<Long> {
    /**
     * 读取到客户端传来的数据，业务是打印出来
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Long msg) {
        System.out.println("从客户端" + ctx.channel().remoteAddress() + " 读取到long " + msg);
        //给客户端发送一个long
        // ctx.writeAndFlush(98765L);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
