package com.hypocrite30.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Scanner;

/**
 * @Description: NIO 聊天室 客户端
 * @Author: Hypocrite30
 * @Date: 2021/10/9 15:57
 */
public class ChatClient {

    /**
     * 客户端启动
     * @param name 客户端名称
     */
    public void start(String name) throws IOException {
        // 接收服务器响应数据
        Selector selector = Selector.open();
        // 连接服务端
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 8888));
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);

        // 创建线程，单独完成接收服务器传来的连接信息
        new Thread(() -> {
            try {
                for (; ; ) {
                    if (selector.select() == 0)
                        continue;
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        if (key.isReadable()) {
                            readOperation(selector, key);
                        }
                        iterator.remove();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        // 客户端输入聊天内容
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String message = scanner.nextLine();
            if (message.length() > 0)
                socketChannel.write(Charset.forName("UTF-8").encode(name + ": " + message));
        }
    }

    /**
     * 可读状态处理
     * @param selector 选择器，用于注册和获取所有的注册通道
     * @param key      获取 key 对应的可读通道，获取通道信息
     */
    private void readOperation(Selector selector, SelectionKey key) throws IOException {
        // 选择到的可读key取出channel
        SocketChannel socketChannel = (SocketChannel) key.channel();
        // 用于从channel中读取数据
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        StringBuilder message = new StringBuilder();
        while (socketChannel.read(byteBuffer) > 0) {
            byteBuffer.flip();
            message.append(Charset.forName("UTF-8").decode(byteBuffer));
        }
        // 再次注册到选择器，监听可读状态
        socketChannel.register(selector, SelectionKey.OP_READ);
        // 这里不需要再广播，只是输出服务器传回来的连接成功的信息
        if (message.length() > 0) {
            System.out.println(message);
        }
    }
}