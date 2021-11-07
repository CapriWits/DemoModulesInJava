package com.hypocrite30.server.handler;

import com.hypocrite30.message.GroupMembersRequestMessage;
import com.hypocrite30.message.GroupMembersResponseMessage;
import com.hypocrite30.server.session.GroupSessionFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Set;

/**
 * 根据群名称拿到群友名称，然后封装成响应信息返回
 * @Description:
 * @Author: Hypocrite30
 * @Date: 2021/11/7 22:38
 */
@ChannelHandler.Sharable
public class GroupMembersRequestMessageHandler extends SimpleChannelInboundHandler<GroupMembersRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupMembersRequestMessage msg) throws Exception {
        Set<String> members = GroupSessionFactory.getGroupSession()
                .getMembers(msg.getGroupName());
        ctx.writeAndFlush(new GroupMembersResponseMessage(members));
    }
}
