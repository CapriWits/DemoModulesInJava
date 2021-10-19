package com.hypocrite30.netty.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.time.LocalDateTime;

/**
 * @Description: websocket实现服务器和浏览器的长连接
 * @Author: Hypocrite30
 * @Date: 2021/10/19 17:25
 */
// TextWebSocketFrame 类型，表示一个文本帧(frame)
public class MyTextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) {
        System.out.println("服务器收到消息 " + msg.text());
        //回复消息
        ctx.channel().writeAndFlush(new TextWebSocketFrame("服务器时间" + LocalDateTime.now() + " " + msg.text()));
    }

    /**
     * 当web客户端连接后， 触发方法
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        //id 表示唯一的值，LongText 是唯一的 ShortText 不是唯一
        System.out.println("handlerAdded 被调用 LongID = " + ctx.channel().id().asLongText());
        System.out.println("handlerAdded 被调用 ShortID = " + ctx.channel().id().asShortText());
    }

    /**
     * 浏览器关闭，调用 handlerRemoved
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        System.out.println("handlerRemoved 被调用" + ctx.channel().id().asLongText());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println("异常发生 " + cause.getMessage());
        ctx.close();
    }
}
