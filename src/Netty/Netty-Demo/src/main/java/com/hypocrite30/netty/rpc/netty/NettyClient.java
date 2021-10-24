package com.hypocrite30.netty.rpc.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.lang.reflect.Proxy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description: 消费者，基于 Netty
 * @Author: Hypocrite30
 * @Date: 2021/10/23 23:03
 */
public class NettyClient {

    private static String hostname; // 主机号
    private static int port; // 端口

    //创建线程池，线程数为 CPU 核数
    private static ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private static NettyClientHandler clientHandler; // 代理方法也要调用 handler，保证是同一个 handler
    private int count = 0; // 记录代理调用的次数

    public NettyClient(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    /**
     * 编写方法使用代理模式，获取一个代理对象
     * @param serivceClass    待调用服务端方法的接口 class「IService.class」
     * @param PROTOCOL_HEADER 协议头 「IService#hello#」
     * @return 代理对象
     */
    public Object getBean(final Class<?> serivceClass, final String PROTOCOL_HEADER) {
        return Proxy.newProxyInstance(
                Thread.currentThread().getContextClassLoader(),
                new Class<?>[]{serivceClass},
                // invoke(Object proxy, Method method, Object[] args)
                // 客户端每调用一次 hello, 就会执行一次，该方法绑定在代理对象中
                (proxy, method, args) -> {
                    System.out.println("(proxy, method, args) 进入...." + (++count) + " 次");
                    if (clientHandler == null) {
                        initClient(hostname, port);
                    }
                    //设置请求数据，协议头 + 请求数据「hello()的入参」
                    clientHandler.setParameter(PROTOCOL_HEADER + args[0]);
                    //把 clientHandler 提交到线程池执行，并获得结果
                    return executor.submit(clientHandler).get(); // get()将 ClientHandler call() 的返回结果拿出来
                }
        );
    }

    /**
     * 初始化客户端
     * 注：不能在最后面关闭通道，只是初始化过程，后续用此通道借助代理对象发起请求
     */
    private static void initClient(String hostname, int port) {
        clientHandler = new NettyClientHandler();
        NioEventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true) // TCP 不延时
                .handler(new ChannelInitializer<SocketChannel>() {
                             @Override
                             protected void initChannel(SocketChannel ch) {
                                 ChannelPipeline pipeline = ch.pipeline();
                                 pipeline.addLast(new StringDecoder());
                                 pipeline.addLast(new StringEncoder());
                                 pipeline.addLast(clientHandler);
                             }
                         }
                );
        try {
            ChannelFuture channelFuture = bootstrap.connect(hostname, port).sync();
            // channelFuture.channel().closeFuture().sync(); // 这里不能关闭，会让 ChannelActive 之后的步骤执行不下去
        } catch (Exception e) {
            e.printStackTrace();
        }
        // finally {
        //     group.shutdownGracefully(); // 不可以关闭循环组，这是初始化方法，后续还要生成代理对象发起请求！
        // }
    }
}
