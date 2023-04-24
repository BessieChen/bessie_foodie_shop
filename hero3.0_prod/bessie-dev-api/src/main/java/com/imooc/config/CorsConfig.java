package com.imooc.config;

//import org.apache.catalina.filters.CorsFilter; -> 会报错: CorsFilter cannot be applied to UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * @program: bessie-dev
 * @description: 跨域
 * @author: Bessie
 * @create: 2021-10-19 23:37
 **/


@Configuration
public class CorsConfig {
    public CorsConfig()
    {

    }

    @Bean
    public CorsFilter setCorsFilter()
    {
        //1. 添加 cors 配置信息: 是否发送cookie信息, 允许请求的方式, 允许的header
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://localhost:8080");
        config.addAllowedOrigin("http://114.132.242.185:8080"); //生产环境配置的
        config.addAllowedOrigin("http://114.132.242.185");      //生产环境配置的
        config.addAllowedOrigin("http://bessie-chen.space:8080"); //生产环境配置的
        config.addAllowedOrigin("http://bessie-chen.space");      //生产环境配置的
        config.addAllowedOrigin("*");                               //生产环境配置的

        config.setAllowCredentials(true); //cookie
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");

        //2. 为 url 添加映射路径
        UrlBasedCorsConfigurationSource corsSource = new UrlBasedCorsConfigurationSource();
        corsSource.registerCorsConfiguration("/**", config);    //映射到所有的路径: /**

        //3. 返回重新定义好的 corsFilter
        return new CorsFilter(corsSource);

    }
}
