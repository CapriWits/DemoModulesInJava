package com.hypocrite30.netty.in_out_boundhandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * 继承 ByteToMessageDecoder 需要对 ByteBuf readableBytes()进行判断，再解码数据
 * 继承 ReplayingDecoder 则不需要
 * @Description: 字节转化为 Long 类型解码器
 * @Author: Hypocrite30
 * @Date: 2021/10/20 17:09
 */
public class MyByteToLongDecoder2 extends ReplayingDecoder<Void> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        System.out.println("MyByteToLongDecoder2 被调用");
        //在 ReplayingDecoder 不需要判断数据是否足够读取，内部会进行处理判断
        out.add(in.readLong());
    }
}
