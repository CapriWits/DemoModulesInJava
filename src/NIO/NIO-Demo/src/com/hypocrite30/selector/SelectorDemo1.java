package com.hypocrite30.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @Description: 测试 Selector
 * @Author: Hypocrite30
 * @Date: 2021/10/8 17:11
 */
public class SelectorDemo1 {
    public static void main(String[] args) throws IOException {
        //创建selecotr
        Selector selector = Selector.open();
        //开启ServerSocketChannel通道做测试
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //使用选择器的时候，必须是非阻塞
        serverSocketChannel.configureBlocking(false);
        //绑定连接
        serverSocketChannel.bind(new InetSocketAddress(9999));
        //将通道注册到选择器上，注册的选择键是「可接收」
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        //查询已经就绪通道操作
        Set<SelectionKey> selectionKeys = selector.selectedKeys();
        //遍历集合
        Iterator<SelectionKey> iterator = selectionKeys.iterator();
        while (iterator.hasNext()) {
            SelectionKey key = iterator.next();
            //判断key就绪状态操作
            if (key.isAcceptable()) {
                // a connection was accepted by a ServerSocketChannel.
            } else if (key.isConnectable()) {
                // a connection was established with a remote server.
            } else if (key.isReadable()) {
                // a channel is ready for reading
            } else if (key.isWritable()) {
                // a channel is ready for writing
            }
            iterator.remove();
        }
    }
}
