package com.imooc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @program: bessie-dev
 * @description:
 * @author: Bessie
 * @create: 2021-10-19 22:57
 **/

@Configuration
@EnableSwagger2
public class Swagger2 {
    //路径名: http://localhost:8088/swagger-ui.html
    //路径名: http://localhost:8088/doc.html
    //配置 swagger2 的核心配置 docket
    @Bean
    public Docket createRestApi()
    {
        return new Docket(DocumentationType.SWAGGER_2)  //指定api类型为 swagger2
                .apiInfo(setApiInfo())                  //文档汇总信息
                .select().apis(RequestHandlerSelectors.
                        basePackage("com.imooc.controller")) //指定 controller 包的位置
                .paths(PathSelectors.any())             //选择所有的controller: any
                .build();
    }

    private ApiInfo setApiInfo()
    {
        return new ApiInfoBuilder()
                .title("贝茜小屋")
                .contact(new Contact("陈贝茜",
                        "http://www.bessie-shop.com",
                        "bchen372@gatech.edu"))
                .description("贝茜小屋的api文档")
                .version("1.0.1")
                .termsOfServiceUrl("http://www.bessie-shop.com")
                .build();
    }
}
