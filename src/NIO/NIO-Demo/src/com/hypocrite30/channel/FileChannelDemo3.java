package com.hypocrite30.channel;

import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

/**
 * Channel::transferFrom | transferTo 都可以实现通道间传输
 * @Description: 通道之间进行传输
 * @Author: Hypocrite30
 * @Date: 2021/10/4 18:09
 */
public class FileChannelDemo3 {
    public static void main(String[] args) throws Exception {
        // 创建两个fileChannel
        RandomAccessFile aFile = new RandomAccessFile("d:\\01.txt", "rw");
        FileChannel srcChannel = aFile.getChannel();

        RandomAccessFile bFile = new RandomAccessFile("d:\\02.txt", "rw");
        FileChannel desChannel = bFile.getChannel();

        //fromChannel 传输到 toChannel
        long position = 0; // 从 0 位置传输
        long size = srcChannel.size(); // 获取 channel 大小
        desChannel.transferFrom(srcChannel, position, size);
        // srcChannel.transferTo(position, size, desChannel);

        aFile.close();
        bFile.close();
    }
}
