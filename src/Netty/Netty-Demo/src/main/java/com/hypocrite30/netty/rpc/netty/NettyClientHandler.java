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
 * 注：ChannelRead 和 Call() 要实现同步加锁，因为 call() wait()等待结果，最后接收结果，这一过程是原子的
 * @Description: 消费者自定义 handler
 * @Author: Hypocrite30
 * @Date: 2021/10/23 22:43
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter implements Callable<String> {

    private ChannelHandlerContext context; //其他方法「call()」需要上下文对象发送请求「writeAndFlush」
    private String result; //返回的结果
    private String parameter; //请求参数，协议头 + 数据

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
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("channelRead 被调用...");
        result = msg.toString();
        notify(); // 唤醒等待的线程「call()」
    }

    /**
     * (3) -> wait -> (5)
     * 被代理对象调用 call(), 发送数据给服务器，-> wait -> 等待被唤醒(channelRead) -> 返回结果
     * 在 NettyClient 类的 getBean里，生成的代理对象实现 invoke 方法，在 invoke 方法最后将 clienthandler 交给线程池
     * 执行，最后会调用到 call() 方法，call 方法负责把请求参数写给服务器，然后等待返回结果，本类的 channelRead 监听到
     * 服务端返回的结果后，唤醒 call()，然后返回数据 call() 结束
     * @return rpc 返回的数据
     */
    @Override
    public synchronized String call() throws Exception {
        System.out.println("call1 被调用 等待channelRead拿到服务端返回结果...");
        context.writeAndFlush(parameter); // 将请求参数发送到服务端
        //进行wait
        //等待 channelRead 方法获取到服务器返回的结果后，被 channelRead 唤醒
        wait();
        System.out.println("call2 被调用 已拿到返回结果...");
        return result; //服务方返回的结果
    }

    /**
     * (2)
     * 设置请求参数
     */
    void setParameter(String para) {
        System.out.println("setParameter ...");
        this.parameter = para;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
