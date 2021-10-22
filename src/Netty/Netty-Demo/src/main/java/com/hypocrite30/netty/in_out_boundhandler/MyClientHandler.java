package com.hypocrite30.netty.in_out_boundhandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 1.接收来自服务器的响应消息
 * 2.发送数据。先将 Long 类型的数据编码成byte二进制，然后再发送数据
 * @Description: 自定义客户端 handler
 * @Author: Hypocrite30
 * @Date: 2021/10/19 21:56
 */
public class MyClientHandler extends SimpleChannelInboundHandler<Long> {
    //接收数据
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Long msg) {
        System.out.println("服务器的ip = " + ctx.channel().remoteAddress());
        System.out.println("收到服务器消息 = " + msg);
    }

    //发送数据
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("MyClientHandler 发送数据");
        ctx.writeAndFlush(123456L); //发送的是一个 long
    }

    /**
     * 细节
     * 1. 如果发送数据 是 16个字节 "abcdabcdabcdabcd" ，则服务器会调用两次解码器，调用几次取决于数据的长短
     * 2. 关于 Netty handler 执行顺序：InBoundHandler按照注册「顺序执行」，OutBoundHandler按照注册「倒序执行」
     *      所以执行顺序是 ClientHandler发信息 -> Client 编码 -> Server 解码 -> ServerHandler 打印信息
     * 3. 该处理器的前一个 handler 是  MyLongToByteEncoder「指的是在初始化handler的注册顺序」
     *      MyLongToByteEncoder 父类  MessageToByteEncoder
     *      MessageToByteEncoder 中的 write 方法如下分析
     *          会进行发送信息的格式判断，根据泛型进行限定，如本例子是用 Long，发送的 String，则会 false
     *          所以不会走里面的编码方法，但是else还是会写出站，只是不会执行编码方法
     *          因此编写 Encoder 是要注意传入的数据类型和处理的数据类型一致
     */
        /*
         public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        ByteBuf buf = null;
        try {
            if (acceptOutboundMessage(msg)) { //判断当前msg 是不是应该处理的类型，如果是就处理，不是就跳过encode
                @SuppressWarnings("unchecked")
                I cast = (I) msg;
                buf = allocateBuffer(ctx, cast, preferDirect);
                try {
                    encode(ctx, cast, buf); //指定的Long类型，但实际发送的是String，所以不会进行编码
                } finally {
                    ReferenceCountUtil.release(cast);
                }

                if (buf.isReadable()) {
                    ctx.write(buf, promise);
                } else {
                    buf.release();
                    ctx.write(Unpooled.EMPTY_BUFFER, promise);
                }
                buf = null;
            } else {
                ctx.write(msg, promise); //但是还是会写出站，只是不会编码
            }
        }
        */
    // ctx.writeAndFlush(Unpooled.copiedBuffer("abcdabcdabcdabcd",CharsetUtil.UTF_8));
}
