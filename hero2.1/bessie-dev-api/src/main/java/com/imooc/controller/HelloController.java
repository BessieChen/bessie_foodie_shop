package com.imooc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @program: bessie-dev
 * @description: Helloworld
 * @author: Bessie
 * @create: 2021-09-23 22:49
 **/

//@Controller
@ApiIgnore
@RestController
public class HelloController {
    @GetMapping("/hello")
    public Object Hello()
    {
        return "Hello World~";
    }

    @GetMapping("/setSession")
    public Object setSession(HttpServletRequest request)
    {
        HttpSession session = request.getSession();
        session.setAttribute("userInfo", "new value");
        session.setMaxInactiveInterval(3600);
        session.getAttribute("userInfo");
        return "ok";
    }

}
