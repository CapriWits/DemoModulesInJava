package com.hypocrite30.bio;

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 客户端测试：telnet 127.0.0.1 8888
 * CTRL + ]
 * send hello
 * @Description: BIO 服务器
 * @Author: Hypocrite30
 * @Date: 2021/10/11 21:44
 */
public class BIOServer {
    public static void main(String[] args) throws Exception {
        //1. 创建一个线程池
        //2. 如果有客户端连接，就创建一个线程，与之通讯(单独写一个方法)
        ExecutorService newCachedThreadPool = Executors.newCachedThreadPool();
        //创建ServerSocket
        ServerSocket serverSocket = new ServerSocket(8888);
        System.out.println("Server starting ....");
        while (true) {
            System.out.println("Thread id = " + Thread.currentThread().getId() + " 名字 = " + Thread.currentThread().getName());
            System.out.println("waiting for connection ...");
            //监听，等待客户端连接
            final Socket socket = serverSocket.accept();
            System.out.println("Connected ...");
            //就创建一个线程，与之通讯(单独写一个方法)
            newCachedThreadPool.execute(() -> {
                handler(socket);
            });
        }
    }

    //编写一个handler方法，和客户端通讯
    public static void handler(Socket socket) {
        try {
            System.out.println("Thread id = " + Thread.currentThread().getId() + " 名字 = " + Thread.currentThread().getName());
            byte[] bytes = new byte[1024];
            //通过socket 获取输入流
            InputStream inputStream = socket.getInputStream();
            //循环的读取客户端发送的数据
            int read;
            while ((read = inputStream.read(bytes)) != -1) {
                System.out.println("Thread id = " + Thread.currentThread().getId() + " 名字 = " + Thread.currentThread().getName());
                System.out.println("reading ...");
                System.out.println(new String(bytes, 0, read)); //输出客户端发送的数据
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("Close connection ...");
            if (socket != null) {
                try {
                    socket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
