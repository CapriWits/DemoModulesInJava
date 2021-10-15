package com.hypocrite30.netty.buffer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * ByteBuf 示例
 * Unpooled.buffer(int initialCapacity) 创建 ByteBuf
 * @Description: Netty 包下的 ByteBuf
 * @Author: Hypocrite30
 * @Date: 2021/10/15 21:54
 */
public class NettyByteBuf01 {
    public static void main(String[] args) {
        /**
         * 1. 创建 对象，该对象包含一个数组arr , 是一个byte[10]
         *         2. 在 netty 的buffer中，不需要使用flip 进行反转
         *           底层维护了 readerindex 和 writerIndex
         *         3. 通过 readerindex 和  writerIndex 和 capacity，将 buffer 分成三个区域
         *         0 ~ readerindex 已经读取的区域
         *         readerindex ~ writerIndex ， 可读的区域
         *         writerIndex ~ capacity, 可写的区域
         */
        ByteBuf buffer = Unpooled.buffer(10);
        for (int i = 0; i < 10; i++) {
            buffer.writeByte(i);
        }
        System.out.println("capacity = " + buffer.capacity()); //10
        /**
         * getByte() 不会移动 readerindex，所以可读范围一直是 0(readerindex) ~ writeindex
         * readByte() 会移动 readerindex，0 ~ readerindex 已读，readerindex ~ writeindex 可读
         */
        // for(int i = 0; i<buffer.capacity(); i++) {
        //     System.out.println(buffer.getByte(i)); //
        // }
        for (int i = 0; i < buffer.capacity(); i++) {
            System.out.println(buffer.readByte());
        }
    }
}
