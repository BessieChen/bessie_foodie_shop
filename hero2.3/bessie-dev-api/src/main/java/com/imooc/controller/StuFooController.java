package com.imooc.controller;

import com.imooc.service.StuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @program: bessie-dev
 * @description: Helloworld
 * @author: Bessie
 * @create: 2021-09-23 22:49
 **/

@ApiIgnore
@RestController
public class StuFooController {
    @Autowired
    private StuService stuService;

    @GetMapping("/getInfo")
    public Object getInfo(int id)
    {
        return stuService.getStuInfo(id);
    }

    @PostMapping("/saveStu")
    public Object saveStu()
    {
        stuService.saveStu();
        return "ok";
    }

    @PostMapping("/updateStu")
    public Object updateStu(int id)
    {
        stuService.updateStu(id);
        return "ok";
    }

    @PostMapping("deleteStu")
    public Object deleteStu(int id)
    {
        stuService.deleteStu(id);
        return "ok";
    }

}
