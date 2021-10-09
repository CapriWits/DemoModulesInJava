package com.hypocrite30.asyncfilechannel;

import org.junit.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.Future;

/**
 * 用 Future / CompletionHandler 完成异步 读和写
 * @Description: 异步文件通道
 * @Author: Hypocrite30
 * @Date: 2021/10/8 22:09
 */
public class AsyncFileChannelDemo {

    /**
     * 用 CompletionHandler 异步写操作
     */
    @Test
    public void writeAsyncFileComplate() throws IOException {
        //1 创建AsynchronousFileChannel
        Path path = Paths.get("d:\\01.txt");
        AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(path, StandardOpenOption.WRITE);
        //2 创建Buffer
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        //3 write方法
        buffer.put("hypocrite30".getBytes());
        buffer.flip();
        fileChannel.write(buffer, 0, buffer, new CompletionHandler<Integer, ByteBuffer>() {
            @Override
            public void completed(Integer result, ByteBuffer attachment) {
                System.out.println("bytes written: " + result);
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
            }
        });
        System.out.println("write over");
    }

    /**
     * 用 Future 异步写操作
     */
    @Test
    public void writeAsyncFileFuture() throws IOException {
        //1 创建AsynchronousFileChannel
        Path path = Paths.get("d:\\01.txt");
        AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(path, StandardOpenOption.WRITE);
        //2 创建Buffer
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        //3 write方法
        buffer.put("hypocrite30".getBytes());
        buffer.flip();
        Future<Integer> future = fileChannel.write(buffer, 0);
        while (!future.isDone()) ;
        buffer.clear();
        System.out.println("write over");
    }

    /**
     * 用 CompletionHandler 异步读
     */
    @Test
    public void readAsyncFileChannelComplate() throws Exception {
        //1 创建AsynchronousFileChannel
        Path path = Paths.get("d:\\01.txt");
        AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(path, StandardOpenOption.READ);
        //2 创建Buffer
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        //3 调用channel的read方法得到Future
        fileChannel.read(buffer, 0, buffer, new CompletionHandler<Integer, ByteBuffer>() {
            //读取文件成功的执行方法
            @Override
            public void completed(Integer result, ByteBuffer attachment) {
                System.out.println("result: " + result);
                attachment.flip();
                byte[] data = new byte[attachment.limit()];
                attachment.get(data);
                System.out.println(new String(data));
                attachment.clear();
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                //读取文件失败的执行方法
            }
        });
    }

    /**
     * 用Future方式异步读
     */
    @Test
    public void readAsyncFileChannelFuture() throws Exception {
        //1 创建AsynchronousFileChannel
        Path path = Paths.get("d:\\01.txt");
        AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(path, StandardOpenOption.READ);
        //2 创建Buffer
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        //3 调用channel的read方法得到Future
        Future<Integer> future = fileChannel.read(buffer, 0);
        //4 判断是否完成 isDone,返回true
        while (!future.isDone()) ;
        //5 读取数据到buffer里面
        buffer.flip();
        // while (buffer.remaining() > 0) {
        //     System.out.println(buffer.get()); //这样只会输出ASCII
        // }
        byte[] data = new byte[buffer.limit()];
        buffer.get(data);
        System.out.println(new String(data)); // 用此方式将原数据打印出来
        buffer.clear();
    }
}
