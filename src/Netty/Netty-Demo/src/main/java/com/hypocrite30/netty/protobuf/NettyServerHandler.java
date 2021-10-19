package com.hypocrite30.netty.protobuf;

import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.util.CharsetUtil;

/**
 * 使用 SimpleChannelInboundHandler，并指定 POJO对象
 * @Description: 自定义 handler
 * @Author: Hypocrite30
 * @Date: 2021/10/19 18:28
 */
public class NettyServerHandler extends SimpleChannelInboundHandler<StudentPOJO.Student> {

    @Override
    public void channelRead0(ChannelHandlerContext ctx, StudentPOJO.Student msg) {
        //读取从客户端发送的StudentPojo.Student
        System.out.println("客户端发送的数据 id = " + msg.getId() + " 名字 = " + msg.getName());
    }

    //数据读取完毕
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.copiedBuffer("数据读取完毕", CharsetUtil.UTF_8));
    }

    //处理异常, 一般是需要关闭通道
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
