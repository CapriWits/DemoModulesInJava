package com.hypocrite30.netty.async;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

/**
 * Netty 中会有很多耗时的操作，不可预料的操作，如数据库，网络请求，严重影响 Netty 对 Socket 的处理速度
 * 处理 Socket 属于 IO 线程，在同一线程还需要执行后续的耗时 handler 操作，拖慢速度
 * 所以有两种解决思路
 * 1. handler 加入线程池 「EventExecutorGroup」
 * 2. Context 加入线程池
 *
 * 1)第一种方式在 handler中添加异步，可能更加的自由，比如如果需要访问数据库，那就异步，如果不需要，就不异步，异步会拖长接口响应时间。
 *  因为需要将任务放进 mpscTask 中。如果IO 时间很短，task 很多，可能一个循环下来，都没时间执行整个task，导致响应时间达不到指标。
 * 2)第二种方式是 Netty标准方式(即加入到队列)，但是，这么做会将整个 handler 都交给业务线程池。不论耗时不耗时，都加入到队列里，不够灵活。
 * 3)各有优劣，从灵活性考虑，第一种较好
 * @Description: 异步执行任务
 * @Author: Hypocrite30
 * @Date: 2021/10/22 23:07
 */
public final class EchoServer {

    static final int PORT = Integer.parseInt(System.getProperty("port", "8007"));

    /**
     * 在 Server 这里创建出线程池，然后在添加 handler 时，指定 handler 添加进哪个 group，属于「Context添加线程池」
     * 创建 2 个子线程业务线程池
     */
    static final EventExecutorGroup group = new DefaultEventExecutorGroup(2);

    public static void main(String[] args) throws Exception {
        // Configure the server.
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new EchoServerHandler()); // 这种属于默认使用 IO线程处理 handler
                            //说明: 如果我们在addLast 添加handler ，前面有指定
                            //指定线程池 group, 那么该 handler 会优先加入到该线程池中，Context 会脱离IO线程，异步处理
                            p.addLast(group, new EchoServerHandler());
                        }
                    });
            // Start the server.
            ChannelFuture f = b.bind(PORT).sync();
            // Wait until the server socket is closed.
            f.channel().closeFuture().sync();
        } finally {
            // Shut down all event loops to terminate all threads.
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
