package com.imooc.controller.center;

import com.imooc.pojo.Users;
import com.imooc.service.center.CenterUserSerivce;
import com.imooc.utils.JSONReturn;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.util.StringUtil;

/**
 * @program: bessie-dev
 * @description: 用户中心
 * @author: Bessie
 * @create: 2021-10-23 21:58
 **/
@Api(value = "用户中心接口", tags = {"用户中心的相关接口"})
@RestController
@RequestMapping("center")
public class CenterController {
    @Autowired
    private CenterUserSerivce centerUserSerivce;

    @ApiOperation(value = "获取用户信息", notes = "获取用户信息", httpMethod = "GET")
    @GetMapping("/userInfo")
    public JSONReturn userInfo(
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId)
    {
        if(StringUtils.isBlank(userId))
        {
            return JSONReturn.errorMsg("用户id不能为空");
        }
        Users users = centerUserSerivce.queryUserInfo(userId);
        return JSONReturn.ok(users);
    }
}
