package com.hypocrite30.buffer;

import org.junit.Test;

import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

/**
 * @Description: buffer 测试
 * @Author: Hypocrite30
 * @Date: 2021/10/5 20:45
 */
public class BufferDemo1 {

    /**
     * 测试 ByteBuffer 读
     */
    @Test
    public void ByteBufferRead() throws Exception {
        //FileChannel
        RandomAccessFile aFile = new RandomAccessFile("d:\\01.txt", "rw");
        FileChannel fileChannel = aFile.getChannel();

        //创建buffer，大小
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        //读
        int bytesRead = fileChannel.read(buffer);
        while (bytesRead != -1) {
            // make buffer ready to read
            buffer.flip();
            while (buffer.hasRemaining()) {
                System.out.println((char) buffer.get());
            }
            buffer.clear(); // make buffer ready to write
            bytesRead = fileChannel.read(buffer);
        }
        aFile.close();
        fileChannel.close();
    }

    /**
     * 测试 IntBuffer 存放与取出
     */
    @Test
    public void IntBufferRead() throws Exception {
        //创建buffer
        IntBuffer buffer = IntBuffer.allocate(8);
        //buffer放
        for (int i = 0; i < buffer.capacity(); i++) {
            int j = 2 * (i + 1);
            buffer.put(j);
        }
        //转换为读模式
        buffer.flip();
        //获取
        while (buffer.hasRemaining()) {
            int value = buffer.get();
            System.out.println(value + " ");
        }

        // // 1、获取Selector选择器
        // Selector selector = Selector.open();
        // // 2、获取通道
        // ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // // 3.设置为非阻塞
        // serverSocketChannel.configureBlocking(false);
        // // 4、绑定连接
        // serverSocketChannel.bind(new InetSocketAddress(9999));
        // // 5、将通道注册到选择器上,并制定监听事件为：“接收”事件
        // serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

}
