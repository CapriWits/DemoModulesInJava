package com.hypocrite30.filelock;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * 文件通道可以加文件锁，这是进程级别的锁，多线程清空仍然可以改写，达不到锁效果
 * 分为 「共享锁」和「排它锁」
 * 共享锁：有一个进程有锁，其他进程只能读，不能写
 * 排它锁：有锁的进程独享读写，其他进程不可以读写
 * @Description: 文件锁
 * @Author: Hypocrite30
 * @Date: 2021/10/8 21:28
 */
public class FileLockDemo {
    public static void main(String[] args) throws Exception {
        String input = "hypocrite30";
        System.out.println("input:" + input);
        ByteBuffer buffer = ByteBuffer.wrap(input.getBytes());
        String filePath = "d:\\01.txt";
        Path path = Paths.get(filePath);
        // 添加Path路径对象和写+追加操作
        FileChannel channel = FileChannel.open(path, StandardOpenOption.WRITE, StandardOpenOption.APPEND);
        channel.position(channel.size() - 1);
        //加锁
        FileLock lock = channel.lock(); // 排它锁/独占锁，可以进行读和写
        // FileLock lock = channel.lock(0L, Long.MAX_VALUE, true); // 共享锁，只能读，不能写
        System.out.println("是否共享锁：" + lock.isShared());
        channel.write(buffer);
        channel.close();
        //读文件
        readFile(filePath);
    }

    private static void readFile(String filePath) throws Exception {
        FileReader fileReader = new FileReader(filePath);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        System.out.println("读取出内容：");
        String str;
        while ((str = bufferedReader.readLine()) != null) {
            System.out.println(" " + str);
        }
        fileReader.close();
        bufferedReader.close();
    }
}
