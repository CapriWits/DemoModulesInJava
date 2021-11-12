package com.hypocrite30.httpclient;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.hypocrite30.httppojo.User;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;


/**
 * 补充：
 * property - 类型中getter和setter的命名。去除set|get，剩余部分首字母转小写。
 * <bean><property name="abc"></property></bean>
 * field - 类型中定义的实例变量。
 * <p>
 * ajax -> 请求， 参数的json格式的字符串对象。
 * <p>
 * 使用Main方法，测试HttpClient技术。
 * @Author: Hypocrite30
 * @Date: 2021/11/11 21:17
 */
public class TestHttpClient {
    public static void main(String[] args) throws Exception {
        // testGetNoParams();
        // testGetParams();
        testPost();
    }

    /**
     * 无参数GET请求
     * 使用浏览器，访问网站的过程是：
     * 1、 打开浏览器
     * 2、 输入地址
     * 3、 访问
     * 4、 看结果
     * 使用HttpClient，访问WEB服务的过程：
     * 1、 创建客户端，相当于打开浏览器
     * 2、 创建请求地址， 相当于输入地址
     * 3、 发起请求， 相当于访问网站（回车键）
     * 4、 处理响应结果， 相当于浏览器显示结果
     */
    public static void testGetNoParams() throws IOException {
        // 创建客户端对象
        HttpClient client = HttpClients.createDefault();
        // 创建请求地址
        HttpGet get = new HttpGet("http://localhost:80/test");
        // 发起请求，接收响应对象
        HttpResponse response = client.execute(get);
        // 获取响应体。 响应数据是一个基于HTTP协议标准字符串封装的对象。
        // 所以，响应体和响应头，都是封装的HTTP协议数据。直接使用可能有乱码或解析错误
        HttpEntity entity = response.getEntity();
        // 通过HTTP实体工具类，转换响应体数据。 使用的字符集是UTF-8
        String responseString = EntityUtils.toString(entity, "UTF-8");
        System.out.println("服务器响应数据是 - [ " + responseString + " ]");
        // 回收资源
        client = null;
    }

    /**
     * 有参数GET请求
     * @throws IOException
     */
    public static void testGetParams() throws IOException, URISyntaxException {
        HttpClient client = HttpClients.createDefault();
        // 基于Builder构建请求地址
        URIBuilder builder = new URIBuilder("http://localhost/params");
        // 基于单参数传递，构建请求地址
        // builder.addParameter("name", "zhangsan");
        // builder.addParameter("password", "root");
        // URI uri = builder.build();
        // 基于多参数传递，构建请求地址
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("name", "zhangsan"));
        nvps.add(new BasicNameValuePair("password", "root"));
        builder.addParameters(nvps);
        URI uri = builder.build();
        System.out.println(uri.toASCIIString());

        String result = EntityUtils.toString(client.execute(new HttpGet(uri)).getEntity());
        System.out.println(result);
    }

    /**
     * POST请求
     */
    public static void testPost() throws Exception {
        HttpClient client = HttpClients.createDefault();
        // 1无参数Post请求
        HttpPost post = new HttpPost("http://localhost/test");
        HttpResponse response = client.execute(post);
        System.out.println(EntityUtils.toString(response.getEntity(), "UTF-8"));

        // 2有参数的Post请求
        // 请求头传递参数。和Get请求携带参数的方式一致。
        URIBuilder builder = new URIBuilder("http://localhost/params");
        builder.addParameter("name", "post");
        builder.addParameter("password", "postPassword");
        HttpResponse postResponse = client.execute(new HttpPost(builder.build()));
        System.out.println(EntityUtils.toString(postResponse.getEntity(), "UTF-8"));

        // 3请求体传递参数
        HttpPost bodyParamsPost = new HttpPost("http://localhost/bodyParams");
        // 定义请求协议体，设置请求参数。 使用请求体传递参数的时候，需要定义请求体格式。默认是表单格式。
        // 使用URIBuilder构建的URI对象，就是请求体传递参数的。
        User u1 = new User();
        u1.setName("name1");
        u1.setPassword("password1");
        User u2 = new User();
        u2.setName("name2");
        u2.setPassword("password2");
        List<User> users = new ArrayList<>();
        users.add(u1);
        users.add(u2);
        // 把集合users -> JSON字符串
        // 创建Jackson中的转换器对象
        ObjectMapper objectMapper = new ObjectMapper();
        // java对象转换成JSON格式字符串
        String paramsString = objectMapper.writeValueAsString(users);
        System.out.println(paramsString);
        // 拼接一个JSON格式字符串，表示请求参数， 一个List<User>
        // String paramsString = "[" + u1.toString() + ", " + u2.toString() + "]";
        HttpEntity entity = new StringEntity(paramsString, "application/json", "UTF-8");
        bodyParamsPost.setEntity(entity);

        String responseString = EntityUtils.toString(client.execute(bodyParamsPost).getEntity(), "UTF-8");
        /* 下面演示 json 通过 jackson 转换回 java 对象 */
        // String userString = responseString.substring(1, responseString.indexOf("},") + 1);
        // User responseUser = objectMapper.readValue(userString, User.class);
        // System.out.println(responseUser);
        //
        // // 构建一个Jackson识别的Java类型映射。
        // JavaType valueType =
        //         objectMapper.getTypeFactory().constructParametricType(List.class, User.class);
        // List<User> responseList = objectMapper.readValue(responseString, valueType);
        //
        // System.out.println(responseList);
    }
}
