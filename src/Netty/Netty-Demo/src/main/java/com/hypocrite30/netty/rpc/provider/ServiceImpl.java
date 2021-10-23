package com.hypocrite30.netty.rpc.provider;

import com.hypocrite30.netty.rpc.service.IService;

/**
 * @Description: 服务提供者 实现类
 * @Author: Hypocrite30
 * @Date: 2021/10/22 23:00
 */
public class ServiceImpl implements IService {
    /**
     * 提供给消费方消费，返回一个结果
     * @param msg
     * @return
     */
    @Override
    public String hello(String msg) {
        System.out.println("服务方收到客户端消息 = " + msg);
        if (msg != null) {
            return "你好客户端，我收到你的消息 [" + msg + "]";
        } else {
            return "你好客户端，我收到你的消息 [null]";
        }
    }
}
