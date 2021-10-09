package com.hypocrite30.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * @Description: NIO 聊天室 服务端
 * @Author: Hypocrite30
 * @Date: 2021/10/9 15:57
 */
public class ChatServer {

    /**
     * 服务器启动
     * 用到 Selector 选择器管理通道
     * 服务通道监听端口，非阻塞，接收状态，注册到 Selector
     * 轮询监听所有通道的状态，进行选择
     */
    public void start() throws IOException {
        //1 创建Selector选择器
        Selector selector = Selector.open();
        //2 创建ServerSocketChannel通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //3 为channel通道绑定监听端口
        serverSocketChannel.bind(new InetSocketAddress(8888));
        //设置非阻塞模式
        serverSocketChannel.configureBlocking(false);
        //4 把channel通道注册到selector选择器上
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("Server starting...");
        //5 轮询等待有新的连接接入
        for (; ; ) {
            // 进行一次选择，为空则跳过，等待下次
            if (selector.select() == 0)
                continue;
            // selectedKeys() 选出被选中的通道
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                // 就绪状态，调用对方方法实现业务
                if (key.isAcceptable()) {
                    acceptOperation(serverSocketChannel, selector);
                    // 可读状态
                } else if (key.isReadable()) {
                    readOperation(selector, key);
                }
                iterator.remove(); // 处理之后需要删除，防止下次select()重复处理
            }
        }
    }

    /**
     * 可读状态处理
     * @param selector 选择器，用于注册和获取所有的注册通道
     * @param key      获取 key 对应的可读通道，获取通道信息
     */
    private void readOperation(Selector selector, SelectionKey key) throws IOException {
        //1 从 SelectionKey 获取到已经就绪的通道
        SocketChannel socketChannel = (SocketChannel) key.channel();
        //2 创建 buffer，接收信息
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        //3 拼接通道信息
        StringBuilder message = new StringBuilder();
        //4 读信息
        while ((socketChannel.read(byteBuffer) > 0)) {
            byteBuffer.flip();
            message.append(Charset.forName("UTF-8").decode(byteBuffer));
        }
        //5 再次注册到选择器中，设置为可读状态
        socketChannel.register(selector, SelectionKey.OP_READ);
        //6 向除了自己以外的其他通道广播信息
        if (message.length() > 0) {
            System.out.println(message);
            BroadcastToClients(message.toString(), selector, socketChannel);
        }
    }

    /**
     * 广播到其他客户端
     * @param message
     * @param selector      selector.keys()来获取被选中的所有通道
     * @param socketChannel 用于排除自己，其他通道都要广播一遍
     */
    private void BroadcastToClients(String message, Selector selector, SocketChannel socketChannel) throws IOException {
        // 获取全部通道，而不是准备就绪的已选择通道，因为广播不需要其他通道准备就绪
        Set<SelectionKey> keySet = selector.keys();
        Iterator<SelectionKey> iterator = keySet.iterator();
        while (iterator.hasNext()) {
            SelectionKey key = iterator.next();
            SelectableChannel otherChannel = key.channel();
            // 除了通道自己，其他通道都广播信息
            if (otherChannel instanceof SocketChannel && otherChannel != socketChannel) {
                ((SocketChannel) otherChannel).write(Charset.forName("UTF-8").encode(message));
            }
        }
    }

    /**
     * 接入状态处理
     * @param serverSocketChannel 服务端 channel
     * @param selector            选择器
     */
    private void acceptOperation(ServerSocketChannel serverSocketChannel, Selector selector) throws IOException {
        // 接入状态，从服务端通道获取 Socket 通道
        SocketChannel socketChannel = serverSocketChannel.accept();
        // 设置非阻塞模式
        socketChannel.configureBlocking(false);
        // channel注册到selector选择器上，监听可读状态
        socketChannel.register(selector, SelectionKey.OP_READ);
        // 客户端回复信息
        socketChannel.write(Charset.forName("UTF-8").encode("Welcome to chat room..."));
    }
}