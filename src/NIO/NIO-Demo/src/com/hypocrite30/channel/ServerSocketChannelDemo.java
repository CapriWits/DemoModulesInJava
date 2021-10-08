package com.hypocrite30.channel;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * ServerSocketChannel 本质上属于监听器，不传递数据，只监听和创建 SocketChannel 对象
 * @Description: ServerSocketChannel 测试
 * @Author: Hypocrite30
 * @Date: 2021/10/4 21:24
 */
public class ServerSocketChannelDemo {
    public static void main(String[] args) throws Exception {
        //端口号
        int port = 8888;
        // wrap 包装一个装有 byte 数组内容的 buffer
        ByteBuffer byteBuffer = ByteBuffer.wrap("hypocrite30".getBytes());
        //1.打开通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //2.绑定，用channel自带的bind来绑定Socket地址
        serverSocketChannel.bind(new InetSocketAddress(port));
        // 借助通道内自带的socket来bind绑定地址
        // serverSocketChannel.socket().bind(new InetSocketAddress(port));
        //3.设置非阻塞模式
        serverSocketChannel.configureBlocking(false);
        //4.监听新连接传入
        while (true) {
            System.out.println("Waiting for connections");
            //这里返回的是 SocketChannel 对象，serversocketchannel本身不创建对象
            SocketChannel socketChannel = serverSocketChannel.accept();
            if (socketChannel == null) { //没有连接传入
                System.out.println("null");
                Thread.sleep(2000);
            } else {
                System.out.println("Incoming connection from: " + socketChannel.socket().getRemoteSocketAddress());
                byteBuffer.rewind(); //position读指针回到0
                socketChannel.write(byteBuffer); // 数据写进 channel 中
                // do something...
                socketChannel.close();
            }
        }
    }
}