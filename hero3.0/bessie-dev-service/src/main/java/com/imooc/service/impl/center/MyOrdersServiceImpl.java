package com.imooc.service.impl.center;

import com.github.pagehelper.PageHelper;
import com.imooc.enums.OrderStatusEnums;
import com.imooc.enums.YesOrNo;
import com.imooc.mapper.OrderStatusMapper;
import com.imooc.mapper.OrdersMapper;
import com.imooc.mapper.OrdersMapperCustom;
import com.imooc.pojo.OrderStatus;
import com.imooc.pojo.Orders;
import com.imooc.pojo.vo.MyOrdersVO;
import com.imooc.service.center.MyOrdersSerivce;
import com.imooc.service.impl.BaseServiceImpl;
import com.imooc.utils.PagedGridResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
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
public class MyOrdersServiceImpl extends BaseServiceImpl implements MyOrdersSerivce  {

    @Autowired
    private OrdersMapperCustom ordersMapperCustom;

    @Autowired
    private OrderStatusMapper orderStatusMapper;

    @Autowired
    private OrdersMapper ordersMapper;

    /**
     * 查询订单列表
     *
     * @param userId
     * @param orderStatus
     * @param page
     * @param pageSize
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult queryMyOrders(String userId, Integer orderStatus, Integer page, Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        if(orderStatus != null)
        {
            map.put("orderStatus", orderStatus);
        }

        PageHelper.startPage(page, pageSize);
        List<MyOrdersVO> list = ordersMapperCustom.queryMyOrders(map);
        return setterPagedGrid(list, page);
    }

    /**
     * 将订单状态更新为: 已发货, 待收货
     * @param orderId
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateDeliverOrderStatus(String orderId) {

        //需要修改为 updateOrder
        OrderStatus updateOrder = new OrderStatus();
        updateOrder.setOrderStatus(OrderStatusEnums.WAIT_RECEIVE.num);   //目标状态: 已发货, 待收货
        updateOrder.setDeliverTime(new Date());

        // example 需要修改
        Example example = new Example(OrderStatus.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orderId", orderId);
        criteria.andEqualTo("orderStatus", OrderStatusEnums.WAIT_DELIVER.num);   //之前的状态: 已付款, 待发货

        orderStatusMapper.updateByExampleSelective(updateOrder, example);
    }


    /**
     * 根据 userId + orderId, 查找 Orders
     * @param userId    用户id
     * @param orderId   订单id
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Orders queryMyOrdersByUserId(String userId, String orderId)
    {
        Orders res = new Orders();
        res.setUserId(userId);
        res.setId(orderId);
        return ordersMapper.selectOne(res);
    }

    /**
     * 将订单状态更新为: 交易成功
     * 注意 OrderStatus 里面没有变量 userId
     * @param orderId
     */
    @Override
    public boolean updateReceiveOrderStatus(String orderId) {
        OrderStatus target = new OrderStatus();
        target.setOrderId(orderId);
        target.setOrderStatus(OrderStatusEnums.SUCCESS.num);
        target.setSuccessTime(new Date());
        target.setSuccessTime(new Date());

        Example source = new Example(OrderStatus.class);
        Example.Criteria criteria = source.createCriteria();
        criteria.andEqualTo("orderId", orderId);
        criteria.andEqualTo("orderStatus", OrderStatusEnums.WAIT_RECEIVE.num);

        int res = orderStatusMapper.updateByExampleSelective(target, source);
        return res == 1; //如果==1, 说明成功
    }

    /**
     * 将订单状态更新为: 删除交易
     * 注意, 不是真的删除, 所以不用 mapper 的 delete()函数, 而是使用 updateByExampleSelective()
     * @param userId
     * @param orderId
     */
    @Override
    public boolean updateDeleteOrderStatus(String userId, String orderId) {
        Orders target = new Orders();
        target.setIsDelete(YesOrNo.YES.num);
        target.setUserId(userId);
        target.setUpdatedTime(new Date());

        Example source = new Example(Orders.class);
        Example.Criteria criteria = source.createCriteria();
        criteria.andEqualTo("id", orderId);     //注意 orderId 是主键, 所以是 id
        criteria.andEqualTo("userId", userId);

        int res = ordersMapper.updateByExampleSelective(target, source);
        return res == 1; //如果==1, 说明成功
    }
}
