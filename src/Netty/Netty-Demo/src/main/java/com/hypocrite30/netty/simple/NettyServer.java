package com.hypocrite30.netty.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @Description: Netty 服务器示例
 * @Author: Hypocrite30
 * @Date: 2021/10/12 17:17
 */
public class NettyServer {
    public static void main(String[] args) throws Exception {
        /**
         * 创建BossGroup 和 WorkerGroup
         *         1. 创建两个线程组 bossGroup 和 workerGroup
         *         2. bossGroup 只是处理连接请求 , 真正的和客户端业务处理，会交给 workerGroup 完成
         *         3. 两个都是无限循环
         *         4. bossGroup 和 workerGroup 含有的子线程(NioEventLoop)的个数
         *           workerGroup 默认实际 cpu核数 * 2
         */
        //一般指定 bossGroup 只开一个线程，用于建立连接，或者分发到workerGroup进行网络读写
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            //创建服务器端的启动对象，配置参数
            ServerBootstrap bootstrap = new ServerBootstrap();
            //使用链式编程来进行设置
            bootstrap.group(bossGroup, workerGroup) //设置两个线程组
                    .channel(NioServerSocketChannel.class) //指定 NioSocketChannel 作为服务器的通道实现
                    .option(ChannelOption.SO_BACKLOG, 128) // 设置线程队列得到连接个数
                    .childOption(ChannelOption.SO_KEEPALIVE, true) //设置保持活动连接状态
                    // .handler(null) // 该 handler对应 bossGroup , childHandler 对应 workerGroup
                    .childHandler(new ChannelInitializer<SocketChannel>() {//创建一个通道初始化对象(匿名对象)
                        //给pipeline 设置处理器
                        @Override
                        protected void initChannel(SocketChannel socketChannel) {
                            //可以使用一个集合管理 SocketChannel，在推送消息时，可以将业务加入到各个channel 对应的 NIOEventLoop 的 taskQueue 或者 scheduleTaskQueue
                            //每条channel会使用pipeline配合使用，里面可以添加很多 handler 来实现不同业务，最终被添加到该 channel 对应 loop 的 taskQueue 执行
                            socketChannel.pipeline().addLast(new NettyServerHandler()); //给 workerGroup 的 NIOEventLoop 注册的管道设置自定义handler
                        }
                    });
            System.out.println("Server is ready ...");
            //绑定一个端口并且同步, 生成了一个 ChannelFuture 对象
            //启动服务器(并绑定端口)
            ChannelFuture channelFuture = bootstrap.bind(6668).sync();
            //给 channelFuture 注册监听器，监控我们关心的事件
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) {
                    if (channelFuture.isSuccess()) {
                        System.out.println("监听端口 6668 成功");
                    } else {
                        System.out.println("监听端口 6668 失败");
                    }
                }
            });
            //对关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        } finally {
            //优雅关闭
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
