package com.hypocrite30.server.session;

/**
 * @Description: Session 工厂类
 * @Author: Hypocrite30
 * @Date: 2021/11/7 22:08
 */
public abstract class SessionFactory {

    private static Session session = new SessionMemoryImpl();

    public static Session getSession() {
        return session;
    }
}
