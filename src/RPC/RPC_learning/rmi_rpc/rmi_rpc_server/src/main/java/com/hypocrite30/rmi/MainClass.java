package com.hypocrite30.rmi;

import com.hypocrite30.rmi.api.FirstInterface;
import com.hypocrite30.rmi.impl.FirstRMIImpl;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

/**
 * 主方法，创建一个服务实现对象，提供服务，并注册到Registry上。
 * RMI的Registry在创建的时候，会自动启动一个子线程，并升级为守护线程（服务线程|精灵线程）。提供持久的服务。
 * @Author: Hypocrite30
 * @Date: 2021/11/12 22:37
 */
public class MainClass {
    public static void main(String[] args) {
        try {
            System.out.println("服务器启动中...");
            // 创建服务对象
            FirstInterface first = new FirstRMIImpl();
            // 注册到Registry（注册中心）上。
            LocateRegistry.createRegistry(9999);
            // 绑定一个服务到注册中心。提供命名，格式为：rmi://ip:port/别名
            // 如果服务重复，抛出异常。 重复的定义是命名冲突。
            // Naming.bind("rmi://localhost:9999/first", first);
            // 重新绑定一个服务到自注册中心。 和bind的区别是，命名冲突，直接覆盖。
            Naming.rebind("rmi://localhost:9999/first", first);
            System.out.println("服务器启动完毕！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}