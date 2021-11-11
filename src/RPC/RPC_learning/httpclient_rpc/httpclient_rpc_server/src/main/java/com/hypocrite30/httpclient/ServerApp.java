package com.hypocrite30.httpclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Description: httpclient 服务端，端口 80
 * @Author: Hypocrite30
 * @Date: 2021/11/11 21:06
 */
@SpringBootApplication
public class ServerApp {
    public static void main(String[] args) {
        SpringApplication.run(ServerApp.class, args);
    }
}
