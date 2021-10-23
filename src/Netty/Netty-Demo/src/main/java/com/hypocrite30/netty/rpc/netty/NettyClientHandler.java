package com.hypocrite30.netty.rpc.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Callable;

/**
 * 需要继承 Callable 方法，重写 call()
 * 运行顺序是注解的 (1)~(5)
 * 1.先与服务器连接，将上下文信息保存到成员变量 context
 * 2.设置参数，然后局部变量 parameter 保存起来
 * 3.代理对象调用重写的 call() 方法后，wait() 等待结果
 * 4.channelRead 读到服务端传回来的处理结果，保存到成员变量 result，然后 notify() 唤醒
 * 5.call() 被唤醒，拿到处理后的数据，可以进行后序操作，完成 RPC 流程「消费者角度」
 * @Description: 消费者自定义 handler
 * @Author: Hypocrite30
 * @Date: 2021/10/23 22:43
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter implements Callable<String> {

    private ChannelHandlerContext context;//上下文
    private String result; //返回的结果
    private String parameter; //客户端调用方法时，传入的参数

    /**
     * (1)
     * 与服务端连接创建后，调用 Active 方法
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelActive 被调用...");
        context = ctx; // 在其它方法会使用到 ctx
    }

    /**
     * (4)
     * 读取服务端处理后的数据，存到成员变量，再唤醒等待线程
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("channelRead 被调用...");
        result = msg.toString();
        notify(); // 唤醒等待的线程
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    /**
     * (3) -> wait -> (5)
     * 被代理对象调用 call(), 发送数据给服务器，-> wait -> 等待被唤醒(channelRead) -> 返回结果
     */
    @Override
    public synchronized String call() throws Exception {
        System.out.println("call1 被调用...");
        context.writeAndFlush(parameter); // 将请求参数发送到服务端
        //进行wait
        //等待 channelRead 方法获取到服务器返回的结果后，被 channelRead 唤醒
        wait();
        System.out.println("call2 被调用...");
        return result; //服务方返回的结果
    }

    /**
     * (2)
     * 设置参数
     */
    void setParameter(String para) {
        System.out.println("setParameter ...");
        this.parameter = para;
    }
}
