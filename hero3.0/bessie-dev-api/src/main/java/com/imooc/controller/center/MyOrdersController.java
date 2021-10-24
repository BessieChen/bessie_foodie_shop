package com.imooc.controller.center;

import com.imooc.controller.BaseController;
import com.imooc.enums.PayMethod;
import com.imooc.pojo.Orders;
import com.imooc.pojo.bo.SubmitOrderBO;
import com.imooc.service.OrderService;
import com.imooc.service.center.MyOrdersSerivce;
import com.imooc.utils.CookieUtils;
import com.imooc.utils.JSONReturn;
import com.imooc.utils.PagedGridResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.imooc.controller.BaseController.FOODIE_SHOPCART;

/**
 * @program: bessie-dev
 * @description: 我的订单
 * @author: Bessie
 * @create: 2021-10-23 08:55
 **/
@Api(value = "我的订单相关", tags = {"我的订单相关的api接口"})
@RestController
@RequestMapping("myorders")
public class MyOrdersController extends BaseController {

    @Autowired
    private MyOrdersSerivce myOrdersSerivce;

    @ApiOperation(value = "查询商品评价", notes = "查询商品评价", httpMethod = "POST")
    @PostMapping("/query")
    public JSONReturn query(
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId,
            @ApiParam(name = "orderStatus", value = "订单状态", required = true)
            @RequestParam Integer orderStatus,
            @ApiParam(name = "page", value = "当前需要第几页", required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "每一页多少条", required = false)
            @RequestParam Integer pageSize
    )
    {
        System.out.println(orderStatus);
        if(StringUtils.isBlank(userId))
        {
            return JSONReturn.errorMsg("用户id不能为空");
        }
        if(page == null)
        {
            page = 1;
        }
        if(pageSize == null)
        {
            pageSize = COMMON_PAGE_SIZE;
        }
        PagedGridResult grid = myOrdersSerivce.queryMyOrders(userId, orderStatus, page, pageSize);
        return JSONReturn.ok(grid);
    }

    @ApiOperation(value="商家发货", notes="商家发货", httpMethod = "GET")
    @GetMapping("/deliver")
    public JSONReturn deliver(
            @ApiParam(name = "orderId", value = "订单id", required = true)
            @RequestParam String orderId){

        if (StringUtils.isBlank(orderId)) {
            return JSONReturn.errorMsg("订单ID不能为空");
        }
        myOrdersSerivce.updateDeliverOrderStatus(orderId);
        return JSONReturn.ok();
    }

    @ApiOperation(value="确认收货", notes="确认收货", httpMethod = "POST")
    @PostMapping("/confirmReceive")
    public JSONReturn confirmReceive(
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId,
            @ApiParam(name = "orderId", value = "订单id", required = true)
            @RequestParam String orderId)  {

        if (StringUtils.isBlank(userId) || StringUtils.isBlank(orderId)) {
            return JSONReturn.errorMsg("用户ID和订单ID不能为空");
        }

        //1. 查看用户id是否和订单id关联, 如果不关联, 返回错误
        JSONReturn res = checkUserIdOrderId(userId, orderId);
        if(res.getStatus() != HttpStatus.OK.value()) //错: if(res.getStatus() != JSONReturn.ok())
        {
            return res;
        }
        if(!myOrdersSerivce.updateReceiveOrderStatus(orderId))
        {
            JSONReturn.errorMsg("确认收货失败");
        }
        return JSONReturn.ok();
    }

    @ApiOperation(value="删除订单", notes="删除订单", httpMethod = "POST")
    @PostMapping("/delete")
    public JSONReturn delete(
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId,
            @ApiParam(name = "orderId", value = "订单id", required = true)
            @RequestParam String orderId){


        if (StringUtils.isBlank(userId) || StringUtils.isBlank(orderId)) {
            return JSONReturn.errorMsg("用户ID和订单ID不能为空");
        }

        //1. 查看用户id是否和订单id关联, 如果不关联, 返回错误
        JSONReturn res = checkUserIdOrderId(userId, orderId);
        if(res.getStatus() != HttpStatus.OK.value()) //错: if(res.getStatus() != JSONReturn.ok())
        {
            return res;
        }
        if(!myOrdersSerivce.updateDeleteOrderStatus(userId, orderId))
        {
            JSONReturn.errorMsg("删除订单失败");
        }
        return JSONReturn.ok();
    }

    /**
     * 查看用户id是否和订单id关联, 如果不关联, 返回错误
     * @param userId
     * @param orderId
     * @return
     */
    private JSONReturn checkUserIdOrderId(String userId, String orderId)
    {
        Orders res = myOrdersSerivce.queryMyOrdersByUserId(userId, orderId);
        if(res != null)
        {
            return JSONReturn.ok();
        }
        return JSONReturn.errorMsg("用户id和订单id不匹配");
    }

}
