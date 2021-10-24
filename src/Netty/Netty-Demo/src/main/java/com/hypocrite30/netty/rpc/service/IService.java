package com.hypocrite30.netty.rpc.service;

/**
 * 公共接口表示 服务端拥有方法的实现，提供给消费者调用
 * 消费者实际只需要拥有该接口即可，接下来全部交给代理对象请求服务，返回结果值即可实现 rpc
 * @Description: rpc 公共接口
 * @Author: Hypocrite30
 * @Date: 2021/10/22 22:59
 */
public interface IService {

    /**
     * 消费者需要将协议头连同请求数据一起发送，服务端需要把协议头和数据切割，所以放在公共接口上
     * 协议头，格式为 服务 服务实现类 + 方法名
     */
    String PROTOCOL_HEADER = "IService#hello#";

    String hello(String msg);
}
