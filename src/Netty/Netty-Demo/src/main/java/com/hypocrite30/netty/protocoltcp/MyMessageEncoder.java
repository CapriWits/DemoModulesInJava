package com.hypocrite30.netty.protocoltcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @Description: 自定义编码器
 * @Author: Hypocrite30
 * @Date: 2021/10/20 20:40
 */
public class MyMessageEncoder extends MessageToByteEncoder<MessageProtocol> {
    /**
     * 编码器，把协议包里的信息长度 和 信息字节数组 发送出去
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, MessageProtocol msg, ByteBuf out) {
        System.out.println("MyMessageEncoder encode 方法被调用");
        out.writeInt(msg.getLen());
        out.writeBytes(msg.getContent());
    }
}
