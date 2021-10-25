package com.imooc.controller.center;

import com.imooc.controller.BaseController;
import com.imooc.enums.YesOrNo;
import com.imooc.pojo.OrderItems;
import com.imooc.pojo.Orders;
import com.imooc.pojo.bo.center.OrderItemsCommentBO;
import com.imooc.service.center.MyCommentsSerivce;
import com.imooc.service.center.MyOrdersSerivce;
import com.imooc.utils.JSONReturn;
import com.imooc.utils.PagedGridResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program: bessie-dev
 * @description: 我的订单
 * @author: Bessie
 * @create: 2021-10-23 08:55
 **/
@Api(value = "评论相关", tags = {"评论相关的api接口"})
@RestController
@RequestMapping("mycomments")
public class MyCommentsController extends BaseController {

    @Autowired
    private MyCommentsSerivce myCommentsSerivce;

    @ApiOperation(value = "查询待评价的规格", notes = "查询待评价的规格", httpMethod = "POST")
    @PostMapping("/pending")
    public JSONReturn pending(
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId,
            @ApiParam(name = "orderId", value = "订单id", required = true)
            @RequestParam String orderId)
    {

        if (StringUtils.isBlank(userId) || StringUtils.isBlank(orderId)) {
            return JSONReturn.errorMsg("用户ID和订单ID不能为空");
        }

        //1. 查看用户id是否和订单id关联, 如果不关联, 返回错误
        JSONReturn res = checkUserIdOrderId(userId, orderId);
        if(res.getStatus() != HttpStatus.OK.value()) //错: if(res.getStatus() != JSONReturn.ok())
        {
            return res;
        }

        //2. 检查这个订单是否评价过
        Orders myOrder = (Orders)res.getData();
        if(myOrder.getIsComment() == YesOrNo.YES.num)
        {
            return JSONReturn.errorMsg("已经评价过了, 不再评价");
        }

        //3. 返回待评价的所有规格
        List<OrderItems> list = myCommentsSerivce.queryPendingComment(orderId);
        return JSONReturn.ok(list);
    }

    @ApiOperation(value = "保存评论列表", notes = "保存评论列表", httpMethod = "POST")
    @PostMapping("/saveList")
    public JSONReturn saveList(
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId,
            @ApiParam(name = "orderId", value = "订单id", required = true)
            @RequestParam String orderId,
            @ApiParam(name = "commentList", value = "评价列表", required = true)
            @RequestBody List<OrderItemsCommentBO> commentList)
    {

        if (StringUtils.isBlank(userId) || StringUtils.isBlank(orderId)) {
            return JSONReturn.errorMsg("用户ID和订单ID不能为空");
        }
        if(commentList == null || commentList.isEmpty() || commentList.size() == 0)
        {
            return JSONReturn.errorMsg("评价列表不能为空");
        }

        //1. 查看用户id是否和订单id关联, 如果不关联, 返回错误
        JSONReturn res = checkUserIdOrderId(userId, orderId);
        if(res.getStatus() != HttpStatus.OK.value()) //错: if(res.getStatus() != JSONReturn.ok())
        {
            return res;
        }

        //打印评价: System.out.println(commentList);
        //2. 检查这个订单是否评价过
        myCommentsSerivce.saveComments(userId, orderId, commentList);
        return JSONReturn.ok();
    }

    @ApiOperation(value = "展示该用户的所有商品评价", notes = "展示该用户的所有商品评价", httpMethod = "POST")
    @PostMapping("/query")
    public JSONReturn query(
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId,
            @ApiParam(name = "page", value = "当前需要第几页", required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "每一页多少条", required = false)
            @RequestParam Integer pageSize)
    {
        //1. 判空
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
        PagedGridResult grid = myCommentsSerivce.queryMyComments(userId, page, pageSize);
        return JSONReturn.ok(grid);
    }




}
