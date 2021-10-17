package com.hypocrite30.netty.groupchat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;

/**
 * @Description: 群聊室 服务端 handler
 * @Author: Hypocrite30
 * @Date: 2021/10/17 21:59
 */
public class GroupChatServerHandler extends SimpleChannelInboundHandler<String> {
    //定义一个channle 组，管理所有的channel，也可以使用 List<Channel>，然后遍历来管理
    //GlobalEventExecutor.INSTANCE) 是全局的事件执行器，是一个单例
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * handlerAdded 表示连接建立，一旦连接，第一个被执行
     * 将当前 channel 加入到  channelGroup
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        //将该客户加入聊天的信息推送给其它在线的客户端
        // writeAndFlush 方法会将 channelGroup 中所有的 channel 遍历，并发送消息，不需要自己遍历
        channelGroup.writeAndFlush("[客户端]" + channel.remoteAddress() + " 加入聊天" + sdf.format(new java.util.Date()) + " \n");
        channelGroup.add(channel); // 添加进 channelGroup
    }

    /**
     * 断开连接, 将xx客户离开信息推送给当前在线的客户
     * channel关闭，即下线，group 会自动移除 channel，size 也会减一，不用手动操作
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush("[客户端]" + channel.remoteAddress() + " 离开了\n");
        System.out.println("下线后 channelGroup size = " + channelGroup.size());
    }

    /**
     * 表示channel 处于活动状态, 提示 xx上线
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println(ctx.channel().remoteAddress() + " 上线了~");
    }

    /**
     * 表示channel 处于不活动状态, 提示 xx离线了
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println(ctx.channel().remoteAddress() + " 离线了~");
    }

    //读取数据
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) {
        //获取到当前channel
        Channel channel = ctx.channel();
        //这时我们channelGroup, 根据不同的情况，回送不同的消息
        channelGroup.forEach(ch -> {
            if (channel != ch) { //不是当前的channel,转发消息
                ch.writeAndFlush("[客户]" + channel.remoteAddress() + " 发送了消息" + msg + "\n");
            } else {//回显自己发送的消息给自己
                ch.writeAndFlush("[自己]发送了消息" + msg + "\n");
            }
        });
    }

    /**
     * 异常处理，关闭通道
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(cause);
        ctx.close();
    }
}
