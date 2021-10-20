package com.hypocrite30.netty.protocoltcp;

/**
 * 协议指定数据的长度
 * 发包的时候将数据长度跟数据一起发过去，收包的时候，读取数据长度，再读取数据，解决粘包拆包问题
 * @Description: 自定义协议包
 * @Author: Hypocrite30
 * @Date: 2021/10/20 20:34
 */
public class MessageProtocol {

    private int len; // 协议规定的数据长度，解决粘包拆包问题的关键
    private byte[] content; //消息数据

    /* getter setter */
    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
