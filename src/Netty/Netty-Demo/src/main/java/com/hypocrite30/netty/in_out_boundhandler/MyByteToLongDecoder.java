package com.hypocrite30.netty.in_out_boundhandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 按照 Long 类型 8 字节进行解码，实现 ByteToMessageDecoder 抽象类
 * @Description: 字节转化为 Long 类型解码器
 * @Author: Hypocrite30
 * @Date: 2021/10/19 21:43
 */
public class MyByteToLongDecoder extends ByteToMessageDecoder {
    /**
     * decode 会根据接收的数据，被调用多次, 直到确定没有新的元素被添加到list
     * 或者是 ByteBuf 没有更多的可读字节为止
     * 如果list out 不为空，就会将list的内容传递给下一个 channelinboundhandler 处理, 该处理器的方法也会被调用多次
     * @param ctx 上下文对象
     * @param in  入站 ByteBuf
     * @param out List 集合，将解码后的数据传给下一个 handler
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        System.out.println("MyByteToLongDecoder 被调用");
        //因为 long 8 个字节, 需要判断有 8 个字节，才能读取一个long
        if (in.readableBytes() >= 8) {
            out.add(in.readLong());
        }
    }
}
