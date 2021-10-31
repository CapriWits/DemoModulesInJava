package com.hypocrite30.netty.c4;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import static com.hypocrite30.netty.c4.TestByteBuf.log;

/**
 * @Description: 切片「零拷贝」公用内存
 * @Author: Hypocrite30
 * @Date: 2021/10/31 22:12
 */
public class TestSlice {
    public static void main(String[] args) {
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer(10);
        buf.writeBytes(new byte[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j'});
        log(buf);

        // 在切片过程中，没有发生数据复制
        ByteBuf f1 = buf.slice(0, 5);
        f1.retain(); // retain 引用计数加一
        // 'a','b','c','d','e', 'x'
        ByteBuf f2 = buf.slice(5, 5);
        f2.retain();
        log(f1);
        log(f2);

        System.out.println("释放原有 byteBuf 内存");
        buf.release();
        log(f1);

        f1.release(); // 最后再单独释放引用计数
        f2.release();
        /*System.out.println("========================");
        f1.setByte(0, 'b');
        log(f1);
        log(buf);*/
    }
}

