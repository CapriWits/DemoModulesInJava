package com.hypocrite30.netty.groupchat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @Description: 群聊室 客户端 handler
 * @Author: Hypocrite30
 * @Date: 2021/10/17 22:10
 */
public class GroupChatClientHandler extends SimpleChannelInboundHandler<String> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) {
        System.out.println(msg.trim());
    }
}
