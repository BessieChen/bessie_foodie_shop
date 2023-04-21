package com.imooc.config;

//import org.apache.catalina.filters.CorsFilter; -> 会报错: CorsFilter cannot be applied to UrlBasedCorsConfigurationSource

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

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
//        config.addAllowedOrigin("http://114.132.242.185:8080/"); //生产环境配置的
//        config.addAllowedOrigin("http://114.132.242.185");      //生产环境配置的
//        config.addAllowedOrigin("http://bessie-chen.space:8080"); //生产环境配置的
//        config.addAllowedOrigin("http://bessie-chen.space");      //生产环境配置的
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


/**
 * @author others, 不采用
 * 使用addCorsMappings(CorsRegistry registry)配置之后再使用自定义拦截器时跨域相关配置就会失效。
 * 原因是请求经过的先后顺序问题，当请求到来时会先进入拦截器中，而不是进入Mapping映射中，所以返回的头信息中并没有配置的跨域信息。浏览器就会报跨域异常。
 * 所以此处使用CorsFilter过滤器解决跨域问题
 */
/*@Configuration
public class CorsConfig {
    private CorsConfiguration corsConfig() {
        CorsConfiguration config = new CorsConfiguration();
        *//* 请求常用的三种配置，*代表允许所有，当时你也可以自定义属性（比如header只能带什么，只能是post方式等等）
         *//*
//        config.addAllowedOriginPattern("*");
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);
        return config;
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig());
        return new CorsFilter(source);
    }
}*/



