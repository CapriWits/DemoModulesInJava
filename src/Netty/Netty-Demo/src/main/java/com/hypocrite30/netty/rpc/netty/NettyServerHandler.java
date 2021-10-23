package com.hypocrite30.netty.rpc.netty;

import com.hypocrite30.netty.rpc.customer.ClientBootstrap;
import com.hypocrite30.netty.rpc.provider.ServiceImpl;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @Description: 服务器自定义 handler
 * @Author: Hypocrite30
 * @Date: 2021/10/23 21:44
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //获取客户端发送的消息，并调用服务
        System.out.println("msg = " + msg);
        //客户端在调用服务器的 api 时，需要定义一个协议
        //比如要求 每次发消息是都必须以某个字符串开头 "HelloService#hello#你好"
        if (msg.toString().startsWith(ClientBootstrap.providerName)) {
            String result = new ServiceImpl().hello(msg.toString().substring(msg.toString().lastIndexOf("#") + 1));
            ctx.writeAndFlush(result); // 将服务调用的结果回写
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
