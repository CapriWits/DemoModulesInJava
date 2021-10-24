package com.hypocrite30.netty.rpc.netty;

import com.hypocrite30.netty.rpc.provider.ServiceImpl;
import com.hypocrite30.netty.rpc.service.IService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @Description: 服务器自定义 handler
 * @Author: Hypocrite30
 * @Date: 2021/10/23 21:44
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 服务端监听通道，获取请求信息，调用本地方法，将结果写回通道
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //获取客户端发送的消息，并调用服务
        System.out.println("服务器 channelRead 拿到的消息 msg = " + msg);
        //消息体为 协议头「IService#hello#」 + 数据，需要切割数据
        if (msg.toString().startsWith(IService.PROTOCOL_HEADER)) {
            // 取出最后一个 # 位置，再加 1 就是内容的位置
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
