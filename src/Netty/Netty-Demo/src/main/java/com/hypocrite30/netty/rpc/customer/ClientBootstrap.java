package com.hypocrite30.netty.rpc.customer;

import com.hypocrite30.netty.rpc.netty.NettyClient;
import com.hypocrite30.netty.rpc.provider.ServiceImpl;

/**
 * @Description:
 * @Author: Hypocrite30
 * @Date: 2021/10/23 23:04
 */
public class ClientBootstrap {
    //这里定义协议头
    public static final String providerName = "HelloService#hello#";

    public static void main(String[] args) throws  Exception{

        //创建一个消费者
        NettyClient customer = new NettyClient();

        //创建代理对象
        ServiceImpl service = (ServiceImpl) customer.getBean(ServiceImpl.class, providerName);

        for (;; ) {
            Thread.sleep(2 * 1000);
            //通过代理对象调用服务提供者的方法(服务)
            String res = service.hello("你好 dubbo~");
            System.out.println("调用的结果 res= " + res);
        }
    }
}
