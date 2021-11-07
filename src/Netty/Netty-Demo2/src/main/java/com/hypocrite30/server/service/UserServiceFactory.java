package com.hypocrite30.server.service;

/**
 * @Description: UserService 工厂类
 * @Author: Hypocrite30
 * @Date: 2021/11/7 22:02
 */
public abstract class UserServiceFactory {
    private static UserService userService = new UserServiceMemoryImpl();

    public static UserService getUserService() {
        return userService;
    }
}
