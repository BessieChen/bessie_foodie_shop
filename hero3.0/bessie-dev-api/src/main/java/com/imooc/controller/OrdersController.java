package com.imooc.controller;

import com.imooc.enums.PayMethod;
import com.imooc.pojo.UserAddress;
import com.imooc.pojo.bo.AddressBO;
import com.imooc.pojo.bo.SubmitOrderBO;
import com.imooc.service.AddressService;
import com.imooc.service.OrderService;
import com.imooc.utils.CookieUtils;
import com.imooc.utils.JSONReturn;
import com.imooc.utils.MobileEmailUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static com.imooc.controller.BaseController.FOODIE_SHOPCART;

/**
 * @program: bessie-dev
 * @description: 地址相关
 * @author: Bessie
 * @create: 2021-10-23 08:55
 **/
@Api(value = "订单相关", tags = {"订单相关的api接口"})
@RestController
@RequestMapping("orders")
public class OrdersController {

    @Autowired
    private OrderService orderService;

    @ApiOperation(value = "创建订单", notes = "创建订单", httpMethod = "POST")
    @PostMapping("/create")
    public JSONReturn list(
            @ApiParam(name = "submitOrderBO", value = "提交订单BO", required = true)
            @RequestBody SubmitOrderBO submitOrderBO,
            HttpServletRequest request,
            HttpServletResponse response)
    {
        //测试用: System.out.println(submitOrderBO.getPayMethod());
        if(submitOrderBO.getPayMethod() != PayMethod.WEIXIN.num
        && submitOrderBO.getPayMethod() != PayMethod.ALIPAY.num)
        {
            return JSONReturn.errorMsg("支付方式不支持");
        }

        //1. 创建订单, 即把 Orders 这个类里面的所有变量都填充好
        /** Orders
         * createdTime
         * extand
         * id
         * isComment
         * isDelete
         * leftMsg
         * payMethod
         * postAmount
         * realPayAmount
         * receiverAddress
         * receiverMobile
         * receiverName
         * totalAmount
         * updatedTime
         * userId
         */
        String orderId = orderService.createOrder(submitOrderBO);

        //2. 移除购物车中已购买的商品
        /**
         * 1001
         * 2002 -> 用户购买
         * 3003 -> 用户购买
         * 4004
         */
        // TODO 整合redis之后，完善购物车中的已结算商品清除，并且同步到前端的cookie
        CookieUtils.setCookie(request, response, FOODIE_SHOPCART, "", true);

        //3. 向支付中心发送当前订单, 用户保存支付中心的订单数据
        return JSONReturn.ok(orderId);
    }


}
