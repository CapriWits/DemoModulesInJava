package com.hypocrite30.channel;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @Description: FileChannel 完成写操作
 * @Author: Hypocrite30
 * @Date: 2021/10/4 17:53
 */
public class FileChannelDemo2 {
    public static void main(String[] args) throws Exception {
        // 打开FileChannel
        RandomAccessFile randomAccessFile = new RandomAccessFile("d:\\01.txt", "rw");
        FileChannel fileChannel = randomAccessFile.getChannel();
        //创建buffer对象
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        String newData = "data";
        byteBuffer.clear();
        //写入内容
        byteBuffer.put(newData.getBytes());
        byteBuffer.flip();
        //FileChannel完成最终实现
        while (byteBuffer.hasRemaining()) {
            fileChannel.write(byteBuffer);
        }
        //关闭
        fileChannel.close();
    }
}
