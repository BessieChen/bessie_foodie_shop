package com.imooc.controller;

import com.imooc.pojo.vo.ShopcartVO;
import com.imooc.service.ItemService;
import com.imooc.utils.JSONReturn;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @program: bessie-dev
 * @description: 购物车相关
 * @author: Bessie
 * @create: 2021-10-23 04:58
 **/
@Api(value = "购物车接口controller", tags = {"购物车接口相关的api"})
@RestController
@RequestMapping("shopcart")
public class ShopcartController {

    @Autowired
    private ItemService itemService;

    @ApiOperation(value = "商品加入购物车", notes = "商品加入购物车", httpMethod = "POST")
    @PostMapping("/add")
    public JSONReturn add(
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId,
            @ApiParam(name = "shopcartVO", value = "购物车BO", required = true)
            @RequestBody ShopcartVO shopcartVO,
            HttpServletRequest request,
            HttpServletResponse response)
    {
        if(StringUtils.isBlank(userId))
        {
            return JSONReturn.errorMsg("用户id不能为空");
        }
//        System.out.println(shopcartBO.getBuyCounts());
//        System.out.println(shopcartBO.getItemName());

        //TODO  登录情况下, 添加商品到购物车, 会同步到 redis 缓存
        return JSONReturn.ok();
    }

    @ApiOperation(value = "商品从购物车中删除", notes = "商品从购物车中删除", httpMethod = "POST")
    @PostMapping("/del")
    public JSONReturn del(
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId,
            @ApiParam(name = "itemSpecId", value = "要移除的商品", required = true)
            @RequestParam String itemSpecId,
            HttpServletRequest request,
            HttpServletResponse response)
    {
        if(StringUtils.isBlank(userId) || StringUtils.isBlank(itemSpecId))
        {
            return JSONReturn.errorMsg("参数不能为空");
        }
        //TODO  登录情况下, 商品从购物车中删除, 会同步到 redis 缓存
        return JSONReturn.ok();
    }

}
