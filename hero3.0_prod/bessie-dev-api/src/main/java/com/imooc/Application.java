package com.imooc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @program: bessie-dev
 * @description: 启动类
 * @author: Bessie
 * @create: 2021-09-23 21:57
 **/

@SpringBootApplication
@MapperScan(basePackages = "com.imooc.mapper")
@ComponentScan(basePackages = {"com.imooc", "org.n3r.idworker"})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}


//Error:java: error reading /Users/bessie/.m2/repository/org/apache/tomcat/embed/tomcat-embed-core/9.0.19/tomcat-embed-core-9.0.19.jar; zip file is empty