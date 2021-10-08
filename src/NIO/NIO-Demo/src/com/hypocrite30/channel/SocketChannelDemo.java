package com.hypocrite30.channel;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @Description: SocketChannel 测试
 * @Author: Hypocrite30
 * @Date: 2021/10/5 16:05
 */
public class SocketChannelDemo {
    public static void main(String[] args) throws Exception {
        //1.有参构造创建SocketChannel
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("www.baidu.com", 80));
        // 无参构造，需要单独 connect 建立TCP连接
        // SocketChannel socketChannel = SocketChannel.open();
        // socketChannel.connect(new InetSocketAddress("www.baidu.com", 80));
        //非阻塞
        socketChannel.configureBlocking(false);
        //读操作
        ByteBuffer byteBuffer = ByteBuffer.allocate(16);
        socketChannel.read(byteBuffer);
        socketChannel.close();
        System.out.println("read over"); //非阻塞模式则会执行结束
    }
}