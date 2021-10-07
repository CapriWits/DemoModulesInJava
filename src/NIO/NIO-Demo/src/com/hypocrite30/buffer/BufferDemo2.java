package com.hypocrite30.buffer;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 1. 缓冲区分片
 * 2. 只读缓冲区
 * 3. 直接缓冲区
 * 4. 内存映射文件I/O
 * @Description: 四种缓冲区操作
 * @Author: Hypocrite30
 * @Date: 2021/10/5 21:07
 */
public class BufferDemo2 {

    /**
     * 缓冲区分片，数据共享
     */
    @Test
    public void testSlice() {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        for (int i = 0; i < buffer.capacity(); i++) {
            buffer.put((byte) i);
        }
        //创建子缓冲区, 数组 3~7 位置为子缓冲区
        buffer.position(3);
        buffer.limit(7);
        ByteBuffer slice = buffer.slice();
        //改变子缓冲区内容
        for (int i = 0; i < slice.capacity(); i++) {
            byte b = slice.get(i);
            b *= 10;
            slice.put(i, b);
        }
        buffer.position(0);
        buffer.limit(buffer.capacity());
        while (buffer.remaining() > 0) {
            System.out.println(buffer.get());
        }
    }

    /**
     * 只读缓冲区，创建完全相同的缓冲区，只读，且数据共享
     */
    @Test
    public void ReadOnlyyBuffer() {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        for (int i = 0; i < buffer.capacity(); i++) {
            buffer.put((byte) i);
        }
        //创建只读缓冲区
        ByteBuffer readOnlyBuffer = buffer.asReadOnlyBuffer();
        //改变原缓冲区，可读缓冲区的内容可会随之变化
        for (int i = 0; i < buffer.capacity(); i++) {
            byte b = buffer.get(i);
            b *= 10;
            buffer.put(i, b);
        }
        readOnlyBuffer.position(0);
        readOnlyBuffer.limit(buffer.capacity());
        while (readOnlyBuffer.remaining() > 0) {
            System.out.println(readOnlyBuffer.get());
        }
        /**
         * 0
         * 10
         * 20
         * 30
         * 40
         * 50
         * 60
         * 70
         * 80
         * 90
         */
    }

    //直接缓冲区
    @Test
    public void DirectBuffer() throws Exception {
        FileInputStream inputStream = new FileInputStream("d:\\01.txt");
        FileChannel inChannel = inputStream.getChannel();
        FileOutputStream outputStream = new FileOutputStream("d:\\02.txt");
        FileChannel outChannel = outputStream.getChannel();
        // 创建直接缓冲区
        ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
        while (true) {
            buffer.clear();
            if (inChannel.read(buffer) == -1) {
                break;
            }
            buffer.flip();
            outChannel.write(buffer);
        }
        inputStream.close();
        outputStream.close();
        inChannel.close();
        outChannel.close();
    }

    static private final int start = 0; // 映射起点位置
    static private final int size = 1024; // 映射总大小

    //内存映射文件I/O
    @Test
    public void b04() throws Exception {
        RandomAccessFile randomAccessFile = new RandomAccessFile("d:\\01.txt", "rw");
        FileChannel fileChannel = randomAccessFile.getChannel();
        //通过channel.map获取文件内存映射
        MappedByteBuffer buffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, start, size);
        // 在 100 位置插入 a
        buffer.put(100, (byte) 97);
        randomAccessFile.close();
        fileChannel.close();
    }

}
