package com.hypocrite30.server.handler;

import com.hypocrite30.message.LoginRequestMessage;
import com.hypocrite30.message.LoginResponseMessage;
import com.hypocrite30.server.service.UserServiceFactory;
import com.hypocrite30.server.session.SessionFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 登录信息对象拿用户名密码，尝试登录，成功则建立会话，失败设置失败原因，最后返回给客户端
 * @Description: 处理登录请求信息 handler
 * @Author: Hypocrite30
 * @Date: 2021/11/7 22:00
 */
@ChannelHandler.Sharable // channel 间成员变量共享
public class LoginRequestMessageHandler extends SimpleChannelInboundHandler<LoginRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestMessage msg) throws Exception {
        String username = msg.getUsername();
        String password = msg.getPassword();
        boolean isLogin = UserServiceFactory.getUserService().login(username, password);
        LoginResponseMessage message;
        if (isLogin) {
            SessionFactory.getSession().bind(ctx.channel(), username); // 建立会话
            message = new LoginResponseMessage(true, "登录成功");
        } else {
            message = new LoginResponseMessage(false, "用户名或密码不正确");
        }
        ctx.writeAndFlush(message); // 信息返回客户端
    }
}
