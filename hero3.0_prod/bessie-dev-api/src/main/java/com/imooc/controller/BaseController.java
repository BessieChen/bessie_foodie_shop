package com.imooc.controller;

import com.imooc.pojo.Orders;
import com.imooc.service.center.MyOrdersSerivce;
import com.imooc.utils.JSONReturn;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;

/**
 * @program: bessie-dev
 * @description: 基类controller
 * @author: Bessie
 * @create: 2021-10-22 22:19
 **/
public class BaseController {
    public static final Integer COMMENT_PAGE_SIZE = 20;
    public static final Integer COMMON_PAGE_SIZE = 5;
    public static final Integer PAGE_SIZE = 20;
    public static final String FOODIE_SHOPCART = "shopcart";
    public static final String IMAGE_USER_FACE_LOCATION = "/Users/bessie/Desktop/JOB/whole_systems/6_program/3_java/from_0_to_hero/software_used/workspaces/images/foodie/faces";
    // 或者你可以使用: public static final String image_user_face_location = File.separator + "Users" + File.separator + ... + File.separator + "faces";

    @Autowired
    private MyOrdersSerivce myOrdersSerivce;

    /**
     * 查看用户id是否和订单id关联, 如果不关联, 返回错误
     * @param userId
     * @param orderId
     * @return
     */
    public JSONReturn checkUserIdOrderId(String userId, String orderId)
    {
        Orders res = myOrdersSerivce.queryMyOrdersByUserId(userId, orderId);
        if(res != null)
        {
            return JSONReturn.ok(res);
        }
        return JSONReturn.errorMsg("用户id和订单id不匹配");
    }
}
