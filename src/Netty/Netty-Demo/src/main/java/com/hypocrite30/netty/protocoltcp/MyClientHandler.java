package com.hypocrite30.netty.protocoltcp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;

/**
 * @Description: 客户端业务
 * @Author: Hypocrite30
 * @Date: 2021/10/20 20:36
 */
public class MyClientHandler extends SimpleChannelInboundHandler<MessageProtocol> {

    private int count; // 给每个数据加上编号

    /**
     * 客户端发送数据
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        for (int i = 0; i < 5; i++) {
            String msg = "今天天气冷，吃火锅";
            byte[] content = msg.getBytes(Charset.forName("utf-8"));
            int length = msg.getBytes(Charset.forName("utf-8")).length;
            //创建协议包对象
            MessageProtocol messageProtocol = new MessageProtocol();
            messageProtocol.setLen(length);
            messageProtocol.setContent(content);
            ctx.writeAndFlush(messageProtocol);
        }
    }

    /**
     * 客户端接收客户端发来的 UUID
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
        int len = msg.getLen();
        byte[] content = msg.getContent();
        System.out.println("客户端接收到消息如下");
        System.out.println("长度 = " + len);
        System.out.println("内容 = " + new String(content, Charset.forName("utf-8")));
        System.out.println("客户端接收消息数量 = " + (++this.count));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("异常消息 = " + cause.getMessage());
        ctx.close();
    }
}
