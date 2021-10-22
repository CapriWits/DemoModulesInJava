package com.hypocrite30.netty.async;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

import java.util.concurrent.Callable;

/**
 * @Description: 演示了两种方式
 * @Author: Hypocrite30
 * @Date: 2021/10/22 23:17
 */
@Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * group 就是充当业务线程池，可以将任务提交到该线程池
     * 创建了16个线程
     * 这种属于 handler 加入到线程池中，脱离 IO线程「处理selector」，在线程池中单独运行
     */
    static final EventExecutorGroup group = new DefaultEventExecutorGroup(16);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("EchoServer Handler 的线程是 = " + Thread.currentThread().getName());

        /**
         * 解决方案1 用户程序自定义的普通任务
         * 按照原来的方法处理耗时任务
         * 这种方式仍然使用的是 IO 线程，最终还是达不到异步的效果，必须添加进线程池才能真正异步执行
         */
        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5 * 1000);
                    System.out.println("EchoServerHandler execute 线程是=" + Thread.currentThread().getName());
                    ctx.writeAndFlush(Unpooled.copiedBuffer("hello, 客户端", CharsetUtil.UTF_8));
                } catch (Exception ex) {
                    System.out.println("发生异常" + ex.getMessage());
                }
            }
        });


        /**
         * 将任务提交到 group线程池
         * 这种方式属于「handler添加进线程池」，可以实现异步处理，添加进的线程池在本类上方
         * 添加的方式就是 「EventExecutorGroup.submit(Callable<T> task)」
         */
        group.submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                //接收客户端信息
                ByteBuf buf = (ByteBuf) msg;
                byte[] bytes = new byte[buf.readableBytes()];
                buf.readBytes(bytes);
                String body = new String(bytes, "UTF-8");
                //休眠10秒
                Thread.sleep(10 * 1000);
                System.out.println("group.submit 的  call 线程是 = " + Thread.currentThread().getName());
                ctx.writeAndFlush(Unpooled.copiedBuffer("客户端", CharsetUtil.UTF_8));
                return null;

            }
        });

        System.out.println("go on ");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        //cause.printStackTrace();
        ctx.close();
    }
}
