package com.imooc.controller;

import com.fasterxml.jackson.core.json.JsonReadContext;
import com.imooc.pojo.Users;
import com.imooc.pojo.bo.UserBO;
import com.imooc.service.UserService;
import com.imooc.utils.CookieUtils;
import com.imooc.utils.JSONReturn;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.MD5Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    public JSONReturn regist(@RequestBody UserBO userBO, HttpServletRequest request, HttpServletResponse response)
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
        //没有设置 cookie: userService.createUser(userBO);
        Users user = userService.createUser(userBO);

        //3. 添加 cookie, 这样登陆之后, 跳转页面就能知道用户的 cookie 信息
        user = setNullProperties(user);
        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(user), true);

        return JSONReturn.ok();
    }

    @ApiOperation(value = "用户登录", notes = "用户登录", httpMethod = "POST")
    @PostMapping("/login")
    public JSONReturn login(@RequestBody UserBO userBo, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        String username = userBo.getUsername();
        String password = userBo.getPassword();

//        System.out.println(username);
//        System.out.println(password);

        //1. 判空
        if(StringUtils.isBlank(username) || StringUtils.isBlank(password))
        {
            return JSONReturn.errorMsg("用户名 or 密码不能为空");
        }

        //2. 传递给 service, 实现登录{即判断用户名输入的密码是否正确}
        Users user = userService.queryUserForLogin(username, MD5Utils.getMD5Str(password));

        if(user == null)
        {
            return JSONReturn.errorMsg("用户名或者密码不正确");
        }

        //3. 添加 cookie, 这样登陆之后, 跳转页面就能知道用户的 cookie 信息
        user = setNullProperties(user); //把敏感信息清除
        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(user), true);

        return JSONReturn.ok();

    }

    @ApiOperation(value = "用户退出", notes = "用户退出", httpMethod = "POST")
    @PostMapping("/logout")
    public JSONReturn logout(@RequestParam String userId, HttpServletRequest request, HttpServletResponse response)
    {
        //1. 清除 cookie, 这样用户退出就不会留着 cookie 被其他人看到了
        CookieUtils.deleteCookie(request, response, "user");

        //2. 清除购物车 TODO
        //3. 分布式会话, 清除用户数据 TODO
        return JSONReturn.ok();
    }

    private Users setNullProperties(Users user)
    {
        user.setPassword(null);
        user.setMobile(null);
        user.setEmail(null);
        user.setCreatedTime(null);
        user.setUpdatedTime(null);
        user.setBirthday(null);
        return user;
    }

}
