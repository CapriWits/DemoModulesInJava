package com.hypocrite30.channel;

import org.junit.Test;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.charset.Charset;

/**
 * @Description: DatagramChannel 测试
 * @Author: Hypocrite30
 * @Date: 2021/10/5 16:44
 */
public class DatagramChannelDemo {

    //发送的实现，send不需要把地址绑定在channel上，在发送的时候传入发送地址即可
    @Test
    public void sendDatagram() throws Exception {
        //打开 DatagramChannel
        DatagramChannel sendDatagramChannel = DatagramChannel.open();
        //绑定地址和端口
        InetSocketAddress sendAddress = new InetSocketAddress("127.0.0.1", 9999);
        //发送
        while (true) {
            ByteBuffer byteBuffer = ByteBuffer.wrap("Data".getBytes("UTF-8"));
            sendDatagramChannel.send(byteBuffer, sendAddress);
            System.out.println("Have been sent...");
            Thread.sleep(1000);
        }
    }

    //接收的实现，接收channel需要绑定端口，一直监听
    @Test
    public void receiveDatagram() throws Exception {
        //打开DatagramChannel
        DatagramChannel receiveDatagramChannel = DatagramChannel.open();
        //绑定
        receiveDatagramChannel.bind(new InetSocketAddress(9999));
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        //接收
        while (true) {
            byteBuffer.clear();
            SocketAddress socketAddress = receiveDatagramChannel.receive(byteBuffer);
            byteBuffer.flip();
            System.out.println(socketAddress.toString());
            System.out.println(Charset.forName("UTF-8").decode(byteBuffer)); //解码
        }
    }

    /**
     * 连接 read 和 write
     * UDP 并不存在真正意义上的连接，这里的连接是向特定服务地址用read和write接收发送数据包
     */
    @Test
    public void testConnect() throws Exception {
        //打开DatagramChannel
        DatagramChannel connChannel = DatagramChannel.open();
        //绑定
        connChannel.bind(new InetSocketAddress(9999));
        //连接
        connChannel.connect(new InetSocketAddress("127.0.0.1", 9999));
        //write方法
        connChannel.write(ByteBuffer.wrap("Data".getBytes("UTF-8")));
        //read
        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
        while (true) {
            readBuffer.clear();
            connChannel.read(readBuffer);
            readBuffer.flip();
            System.out.println(Charset.forName("UTF-8").decode(readBuffer));
        }
    }
}
