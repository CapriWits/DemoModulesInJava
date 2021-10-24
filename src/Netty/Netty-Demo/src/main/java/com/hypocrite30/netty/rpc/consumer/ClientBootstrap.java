package com.hypocrite30.netty.rpc.consumer;

import com.hypocrite30.netty.rpc.netty.NettyClient;
import com.hypocrite30.netty.rpc.service.IService;

/**
 * @Description: 消费者启动类
 * @Author: Hypocrite30
 * @Date: 2021/10/23 23:04
 */
public class ClientBootstrap {

    public static void main(String[] args) throws Exception {
        //创建一个消费者
        NettyClient consumer = new NettyClient("127.0.0.1", 9999);
        //通过代理对象，拿到 IService 对象，下面调用服务方法
        IService service = (IService) consumer.getBean(IService.class, IService.PROTOCOL_HEADER);
        for (; ; ) {
            Thread.sleep(2 * 1000);
            //通过代理对象调用服务提供者的方法(服务)
            String res = service.hello("Hello RPC...");
            System.out.println("调用的结果 res = " + res);
        }
    }
}
