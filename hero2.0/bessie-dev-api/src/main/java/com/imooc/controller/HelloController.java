package com.imooc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: bessie-dev
 * @description: Helloworld
 * @author: Bessie
 * @create: 2021-09-23 22:49
 **/

//@Controller
@RestController
public class HelloController {
    @GetMapping("/hello")
    public Object Hello()
    {
        return "Hello World~";
    }

}
