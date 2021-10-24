package com.hypocrite30.netty.rpc.provider;

import com.hypocrite30.netty.rpc.service.IService;

/**
 * @Description: 服务提供者 实现类
 * @Author: Hypocrite30
 * @Date: 2021/10/22 23:00
 */
public class ServiceImpl implements IService {

    private static int count = 0; // 记录 hello 方法被调用的次数

    /**
     * 提供给消费方消费，返回一个结果
     * @param msg ServerHandler 截取协议头后面的信息
     * @return 按照格式处理字符串返回
     */
    @Override
    public String hello(String msg) {
        System.out.println("服务方 hello() 方法调用...");
        if (msg != null) {
            return "你好客户端，我收到你的消息 [" + msg + "] 第 " + (++count) + " 次";
        } else {
            return "你好客户端，我收到你的消息 [null]";
        }
    }
}
