package com.hypocrite30.netty.Test;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.nio.charset.Charset;

/**
 * LengthFieldBasedFrameDecoder 是 Netty 封装以「长度+内容」格式的解析器 handler
 * @Description: 按照 「内容长度」+「内容」格式传输数据
 * @Author: Hypocrite30
 * @Date: 2021/11/4 22:29
 */
public class TestLengthDecoder {
    public static void main(String[] args) {
        EmbeddedChannel channel = new EmbeddedChannel(
                /**
                 * maxFrameLength - 最大帧长度，帧不能超过这个大小
                 * lengthFieldOffset – 「内容长度信息」偏移量
                 * lengthFieldLength – 「内容长度信息」长度 Byte
                 * lengthAdjustment – 「内容长度信息」之后多少 Byte 才是内容，相当于中间跳过多少内容
                 * initialBytesToStrip – 整个数据帧需要剔除多长 Byte，为最后客户端 ByteBuf 的内容
                 */
                new LengthFieldBasedFrameDecoder(1024, 0, 4, 0, 4),
                new LoggingHandler(LogLevel.DEBUG),
                new ChannelInboundHandlerAdapter() {
                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) {
                        ByteBuf buf = (ByteBuf) msg;
                        System.out.println(buf.toString(Charset.defaultCharset()));
                    }
                }
        );

        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
        buf.writeInt(12);
        buf.writeBytes("hello, world".getBytes());
        buf.writeInt(6);
        buf.writeBytes("你好".getBytes());
        channel.writeInbound(buf);
    }
}
