package com.imooc.service.impl.center;

import com.github.pagehelper.PageHelper;
import com.imooc.enums.OrderStatusEnums;
import com.imooc.enums.YesOrNo;
import com.imooc.mapper.*;
import com.imooc.pojo.OrderItems;
import com.imooc.pojo.OrderStatus;
import com.imooc.pojo.Orders;
import com.imooc.pojo.bo.center.OrderItemsCommentBO;
import com.imooc.pojo.vo.MyCommentVO;
import com.imooc.pojo.vo.MyOrdersVO;
import com.imooc.service.BaseSerivce;
import com.imooc.service.center.MyCommentsSerivce;
import com.imooc.service.center.MyOrdersSerivce;
import com.imooc.service.impl.BaseServiceImpl;
import com.imooc.utils.PagedGridResult;
import org.aspectj.weaver.ast.Or;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.annotation.Order;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: bessie-dev
 * @description: 订单详情
 * @author: Bessie
 * @create: 2021-10-25 02:00
 **/
@Service
public class MyCommentsServiceImpl extends BaseServiceImpl implements MyCommentsSerivce {

    @Autowired
    private OrderItemsMapper orderItemsMapper;

    @Autowired
    private Sid sid;

    @Autowired
    private ItemsCommentsMapperCustom itemsCommentsMapperCustom;

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private OrderStatusMapper orderStatusMapper;

    /**
     * 查询所有待评价的商品
     *
     * @param orderId
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<OrderItems> queryPendingComment(String orderId) {
        OrderItems query = new OrderItems();
        query.setOrderId(orderId);
        return orderItemsMapper.select(query);
    }

    /**
     * 保存评价列表
     *
     * @param userId
     * @param orderId
     * @param list
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveComments(String userId, String orderId, List<OrderItemsCommentBO> list) {
        //以下略过, 错误的
        /*
            //需要更新3张表: 1. ItemsComments, 2. Orders, 3. OrderStatus
        */

        //1. 需要更新3张表: 1. items_comments, 2. orders, 3. order_status
        //1.1 将bo里面的东西, 填充好, 然后填入到 items_comment
        /**
         * 表: items_comments
         * commentLevel
         * content
         * createdTime      **: 需要额外添加, now()
         * id               **: 主键, 需要额外添加, #{item.commentId}
         * itemId
         * itemName
         * itemSpecId
         * sepcName
         * updatedTime      **: 需要额外添加, now()
         * userId           **: 需要额外添加, #{userId}
         *

         * 结构: OrderItemsCommentBO
         * commentId        **: 前端没有提供, 我们使用 sid 生成
         * commentLevel
         * content
         * itemId
         * itemName
         * itemSpecId
         * itemSpecName
        */
        for(OrderItemsCommentBO bo : list)
        {
            bo.setCommentId(sid.nextShort());
        }

        //1.2 list里面的每个元素, 最后是通过 .xml 中的 collection 的 item 一个一个inserts进入表中的
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId); //因为 表items_comments 中有 userId
        map.put("commentList", list); //注意对应 collection="commentList" 中的字段
        itemsCommentsMapperCustom.saveComments(map);

        //2. 更新第二张表: orders. 将这个订单的 is_comment 设置为 true
        //我的方法: 通过example修改
        Orders target = new Orders();
        target.setIsComment(YesOrNo.YES.num);
        Example source = new Example(Orders.class);
        Example.Criteria criteria = source.createCriteria();
        criteria.andEqualTo("userId", userId);
        criteria.andEqualTo("id", orderId); //注意, Orders.java 里面的id是orderId的意思
        criteria.andEqualTo("isComment", YesOrNo.NO.num);
        ordersMapper.updateByExampleSelective(target, source);

        //老师的方法: 通过主键 orderId 去修改
//        Orders target2 = new Orders();
//        target2.setUserId(orderId);
//        target2.setIsComment(YesOrNo.YES.num);
//        ordersMapper.updateByPrimaryKeySelective(target2);

        //3. 更新第三张表: order_status
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderId(orderId);            // 通过主键 orderId 去修改
        orderStatus.setCommentTime(new Date());
        orderStatusMapper.updateByPrimaryKeySelective(orderStatus);
    }

    /**
     * 查询评价列表
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult queryMyComments(String userId, Integer page, Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        PageHelper.startPage(page, pageSize);
        List<MyCommentVO> list = itemsCommentsMapperCustom.queryMyComments(map);
        return setterPagedGrid(list, page);
    }
}
