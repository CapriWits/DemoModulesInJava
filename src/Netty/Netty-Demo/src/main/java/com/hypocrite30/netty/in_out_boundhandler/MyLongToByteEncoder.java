package com.hypocrite30.netty.in_out_boundhandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 实现 MessageToByteEncoder 抽象类，指定待转化类型 Long，重写编码方法
 * @Description: Long转化为 二进制字节 类型编码器
 * @Author: Hypocrite30
 * @Date: 2021/10/19 21:54
 */
public class MyLongToByteEncoder extends MessageToByteEncoder<Long> {
    /**
     * 编码方法
     * @param ctx 上下文信息
     * @param msg 待编码数据
     * @param out 出站 ByteBuf
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, Long msg, ByteBuf out) {
        System.out.println("MyLongToByteEncoder encode 被调用");
        System.out.println("msg = " + msg);
        out.writeLong(msg);
    }
}
