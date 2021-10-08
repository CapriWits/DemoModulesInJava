package com.hypocrite30.pipe;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;

/**
 * Pipe 里有 Sink 和 Source 两个 channel
 * 线程将数据读到 pipe里的Sink
 * 另一个线程从 pipe 里的 Source 通道中取数据
 * @Description: pipe 测试
 * @Author: Hypocrite30
 * @Date: 2021/10/8 21:23
 */
public class PipeDemo {
    public static void main(String[] args) throws IOException {
        //1 获取管道
        Pipe pipe = Pipe.open();
        //2 获取sink通道
        Pipe.SinkChannel sinkChannel = pipe.sink();
        //3 创建缓冲区
        ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
        writeBuffer.put("hypocrite30".getBytes());
        writeBuffer.flip();
        //4 写入数据
        sinkChannel.write(writeBuffer);
        //5 获取source通道
        Pipe.SourceChannel sourceChannel = pipe.source();
        //6 读取数据
        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
        int length = sourceChannel.read(readBuffer);
        System.out.println(new String(readBuffer.array(), 0, length));
        //7 关闭通道
        sourceChannel.close();
        sinkChannel.close();
    }
}