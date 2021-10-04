package com.hypocrite30.channel;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 对 Channel 的操作都需要借助 Buffer 来写入或读取数据
 * @Description: FileChannel 完成读操作
 * @Author: Hypocrite30
 * @Date: 2021/10/4 11:56
 */
public class FileChannelDemo1 {
    //FileChannel读取数据到buffer中
    public static void main(String[] args) throws Exception {
        //创建FileChannel
        RandomAccessFile randomAccessFile = new RandomAccessFile("d:\\01.txt", "rw");
        FileChannel fileChannel = randomAccessFile.getChannel();

        //创建Buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        //读取数据到buffer中
        int bytesRead = fileChannel.read(byteBuffer);
        while (bytesRead != -1) {
            System.out.println("读取了：" + bytesRead);
            // buffer切换到读模式 make buffer ready for read
            byteBuffer.flip();
            while (byteBuffer.hasRemaining()) {
                System.out.println((char) byteBuffer.get()); // read 1 byte at a time
            }
            byteBuffer.clear(); // make buffer ready for writing
            bytesRead = fileChannel.read(byteBuffer);
        }
        randomAccessFile.close();
        System.out.println("结束了");
    }
}
