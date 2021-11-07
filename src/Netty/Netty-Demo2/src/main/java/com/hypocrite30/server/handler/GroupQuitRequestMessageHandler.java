package com.hypocrite30.server.handler;

import com.hypocrite30.message.GroupJoinResponseMessage;
import com.hypocrite30.message.GroupQuitRequestMessage;
import com.hypocrite30.server.session.Group;
import com.hypocrite30.server.session.GroupSessionFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 拿到群名和用户名，退群
 * @Description: 退出群聊请求 handler
 * @Author: Hypocrite30
 * @Date: 2021/11/7 22:41
 */
@ChannelHandler.Sharable
public class GroupQuitRequestMessageHandler extends SimpleChannelInboundHandler<GroupQuitRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupQuitRequestMessage msg) throws Exception {
        Group group = GroupSessionFactory.getGroupSession().removeMember(msg.getGroupName(), msg.getUsername());
        if (group != null) {
            ctx.writeAndFlush(new GroupJoinResponseMessage(true, "已退出群" + msg.getGroupName()));
        } else {
            ctx.writeAndFlush(new GroupJoinResponseMessage(true, msg.getGroupName() + "群不存在"));
        }
    }
}
