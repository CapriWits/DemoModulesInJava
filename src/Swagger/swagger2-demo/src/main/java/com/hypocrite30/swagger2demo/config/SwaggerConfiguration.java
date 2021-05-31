package com.hypocrite30.swagger2demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

/**
 * @Description: Swagger配置类
 * @Author: Hypocrite30
 * @Date: 2021/5/29 20:25
 */
@Configuration
/**
 * EnableSwagger2 - 是springfox提供的一个注解，代表swagger2相关技术开启。
 * 会扫描当前类所在包，及子包中所有的类型中的注解。做swagger文档的定值。
 */
@EnableSwagger2
@Profile({"dev", "test"}) // 开发环境和测试环境开启 swagger2，生产环境不开启
public class SwaggerConfiguration {
    // 配置docket以配置Swagger具体参数
    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo()) // 给docket上下文配置api描述信息
                // .enable(false) // 是否启用swagger2，在开发环境开启，在生产环境关闭
                .select() // 获取Docket中的选择器。 返回ApiSelectorBuilder。构建选择器的。如：扫描什么包的注解。
                .apis(RequestHandlerSelectors.basePackage("com.hypocrite30.swagger2demo.controller")) // 设定扫描哪个包(包含子包)中的注解。
                // 配置如何通过path过滤,即这里只扫描请求以/ControllerMapping开头的接口
                // any() // 任何请求都扫描, 一般都用any
                // none() // 任何请求都不扫描
                // regex(final String pathRegex) // 通过正则表达式控制
                // ant(final String antPattern) // 通过ant()控制
                .paths(PathSelectors.ant("/ControllerMapping/**"))
                .build();
    }

    private ApiInfo apiInfo() {
        // 方法一：使用构造方法产生ApiInfo，但是所有信息都要填写。
        // 作者信息
        // Contact contact = new Contact("name", "联系人url", "联系人email");
        // return new ApiInfo(
        //         "Swagger-title",
        //         "Swagger-description",
        //         "v1.0",
        //         "http://terms.service.url/组织链接",
        //         contact,
        //         "Apach 2.0 许可",
        //         "许可链接",
        //         new ArrayList<>() // 扩展
        // );
        // 方法二，使用创造者模式，builder来创建自定义配置
        return new ApiInfoBuilder()
                .contact(new Contact("name", "url", "email")) // swagger主体信息
                .title("Swagger-title")
                .termsOfServiceUrl("http://termsOfServiceUrl.cn")
                .description("Swagger-description")
                .version("1.3")
                .build();
    }
}
