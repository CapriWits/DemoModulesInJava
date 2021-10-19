package com.hypocrite30.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * 1. SimpleChannelInboundHandler 是 ChannelInboundHandlerAdapter 子类
 * 2. HttpObject 客户端和服务器端相互通讯的数据被封装成 HttpObject
 * @Description: Http 服务端自定义 handler
 * @Author: Hypocrite30
 * @Date: 2021/10/13 17:15
 */
public class HttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {


    /**
     * 读取客户端数据
     * @param ctx        上下文对象
     * @param httpObject Http对象
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject httpObject) throws URISyntaxException {
        System.out.println("channel = " + ctx.channel() + " pipeline=" + ctx
                .pipeline() + " 通过pipeline获取channel" + ctx.pipeline().channel());
        System.out.println("当前ctx的handler = " + ctx.handler());

        //判断 msg 是不是 httprequest 请求
        if (httpObject instanceof HttpRequest) {
            System.out.println("ctx 类型 = " + ctx.getClass());
            // 证明：pipeline 和 分发的 handler 都是一个客户端「浏览器」独有的，不共享
            System.out.println("pipeline hashcode = " + ctx.pipeline().hashCode() + " HttpServerHandler hash = " + this.hashCode());
            System.out.println("msg 类型 = " + httpObject.getClass());
            System.out.println("客户端地址" + ctx.channel().remoteAddress());

            //转换成 HttpRequest
            HttpRequest httpRequest = (HttpRequest) httpObject;
            //获取 uri, 过滤指定的资源
            URI uri = new URI(httpRequest.uri());
            //避免一次请求相应两次
            if ("/favicon.ico".equals(uri.getPath())) {
                System.out.println("请求了 favicon.ico, 不做响应");
                return;
            }
            //回复信息给浏览器 [http协议]
            ByteBuf byteBuf = Unpooled.copiedBuffer("hello, 我是服务器", CharsetUtil.UTF_8);
            //构造一个http的相应，即 HttpResponse
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, byteBuf);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain;charset=utf-8");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, byteBuf.readableBytes());
            //将构建好 response返回
            ctx.writeAndFlush(response);
        }
    }
}
