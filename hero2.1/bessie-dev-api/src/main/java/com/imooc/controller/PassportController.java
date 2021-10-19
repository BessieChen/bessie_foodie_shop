package com.imooc.controller;

import com.fasterxml.jackson.core.json.JsonReadContext;
import com.imooc.pojo.bo.UserBO;
import com.imooc.service.UserService;
import com.imooc.utils.JSONReturn;
import com.imooc.utils.JsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * @program: bessie-dev
 * @description:
 * @author: Bessie
 * @create: 2021-10-19 04:20
 **/
@Api(value = "注册登录", tags = "用于注册登录的相关api")
@RestController
@RequestMapping("passport")
public class PassportController {
    @Autowired
    private UserService userService;

    @ApiOperation(value = "用户名是否存在", notes = "用户是否存在", httpMethod = "GET")
    @GetMapping("/userNameIsExist")
    public JSONReturn userNameIsExist(@RequestParam String userName)
    {
        //用户名是否为空
        if(StringUtils.isBlank(userName)) //使用的是 org.apache.common.lang3
        {
            return JSONReturn.errorMsg("用户名为空");
        }
        //用户名是否已经存在
        if(userService.queryUsernameIsExist(userName))
        {
            return JSONReturn.errorMsg("用户名已经存在");
        }
        return JSONReturn.ok();
    }

    @ApiOperation(value = "用户注册", notes = "用户注册", httpMethod = "POST")
    @PostMapping("/regist")
    public JSONReturn regist(@RequestBody UserBO userBO)
    {
        //后端也要检验 userBO
        String userName = userBO.getUsername();
        String password = userBO.getPassword();
        String confirmPassword = userBO.getConfirmPassword();

        //判空
        if(StringUtils.isBlank(userName) || StringUtils.isBlank(password) || StringUtils.isBlank(confirmPassword))
        {
            return JSONReturn.errorMsg("用户名, 密码不能为空");
        }

        //用户名是否存在
        if(userService.queryUsernameIsExist(userName))
        {
            return JSONReturn.errorMsg("用户名已存在");
        }

        //密码长度不能少于6位
        if(password.length() < 6)
        {
            return JSONReturn.errorMsg("密码长度不能少于6位");
        }

        //密码是否一致
        if(!password.equals(confirmPassword))
        {
            return JSONReturn.errorMsg("密码不一致");
        }

        //注册
        userService.createUser(userBO);
        return JSONReturn.ok();
    }

}
