package com.hypocrite30.server.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: UserService实现类，存储账号密码，模拟登录
 * @Author: Hypocrite30
 * @Date: 2021/11/7 22:03
 */
public class UserServiceMemoryImpl implements UserService {

    private Map<String, String> allUserMap = new ConcurrentHashMap<>();

    /**
     * 账号-密码
     */ {
        allUserMap.put("zhangsan", "123");
        allUserMap.put("lisi", "123");
        allUserMap.put("wangwu", "123");
        allUserMap.put("zhaoliu", "123");
        allUserMap.put("qianqi", "123");
    }

    @Override
    public boolean login(String username, String password) {
        String pass = allUserMap.get(username);
        if (pass == null) {
            return false;
        }
        return pass.equals(password);
    }
}
