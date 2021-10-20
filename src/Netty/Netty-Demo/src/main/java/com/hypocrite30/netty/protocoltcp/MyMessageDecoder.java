package com.hypocrite30.netty.protocoltcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * ReplayingDecoder 不需要 ByteBuf readableBytes() 判断字长读取
 * @Description: 自定义解码器
 * @Author: Hypocrite30
 * @Date: 2021/10/20 20:43
 */
public class MyMessageDecoder extends ReplayingDecoder<Void> {
    /**
     * 获取数据长度，读取该长度对象，封装好协议包，然后交给下一个 handler 处理业务
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        System.out.println("MyMessageDecoder decode 被调用");
        //需要将得到二进制字节码-> MessageProtocol 数据包(对象)
        int length = in.readInt();
        byte[] content = new byte[length];
        in.readBytes(content);
        //封装成 MessageProtocol 对象，放入 out， 传递下一个handler业务处理
        MessageProtocol messageProtocol = new MessageProtocol();
        messageProtocol.setLen(length);
        messageProtocol.setContent(content);
        out.add(messageProtocol);
    }
}
