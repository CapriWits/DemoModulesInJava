package com.hypocrite30.httpclient.controller;

import com.hypocrite30.httppojo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Author: Hypocrite30
 * @Date: 2021/11/11 21:08
 */
@Controller
public class TestController {
    /**
     * CrossOrigin - 跨域请求注解。在响应头上增加跨域处理允许。 可以让ajax跨域请求当前的服务方法。
     * 如果用于注解类型（必须是控制器），代表当前控制器中所有的方法，都允许跨域访问。
     */
    // 使用请求体传递请求参数。
    @RequestMapping(value = "/bodyParams", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    @CrossOrigin
    public String bodyParams(@RequestBody List<User> users) {
        System.out.println(users);
        return users.toString();
    }

    @RequestMapping(value = "/params", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String params(String name, String password) {
        System.out.println("name - " + name + " ; password - " + password);
        return "{\"msg\":\"登录成功\", \"user\":{\"name\":\"" + name + "\",\"password\":\"" + password + "\"}}";
    }

    @RequestMapping(value = "/test", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String test() {
        return "{\"msg\":\"处理返回\"}";
    }
}
