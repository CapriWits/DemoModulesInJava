package com.hypocrite30.netty.protocoltcp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;
import java.util.UUID;


/**
 * @Description: 服务端业务
 * @Author: Hypocrite30
 * @Date: 2021/10/20 20:50
 */
public class MyServerHandler extends SimpleChannelInboundHandler<MessageProtocol> {

    private int count;

    /**
     * 服务器读到消息
     * 从协议包里面拿数据长度，数据内容
     * UUID随机生成值 作为服务器给客户端的返回值
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
        int len = msg.getLen();
        byte[] content = msg.getContent();
        System.out.println("服务器接收到信息如下 ");
        System.out.println("长度 = " + len);
        System.out.println("内容 = " + new String(content, Charset.forName("utf-8")));
        System.out.println("服务器接收到消息包数量 = " + (++this.count));
        //回复消息
        String responseMsg = UUID.randomUUID().toString();
        int responseLen = responseMsg.getBytes("utf-8").length;
        byte[] responsecontent = responseMsg.getBytes("utf-8");
        //构建一个协议包
        MessageProtocol messageProtocol = new MessageProtocol();
        messageProtocol.setLen(responseLen);
        messageProtocol.setContent(responsecontent);
        ctx.writeAndFlush(messageProtocol); // 写回给客户端
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
