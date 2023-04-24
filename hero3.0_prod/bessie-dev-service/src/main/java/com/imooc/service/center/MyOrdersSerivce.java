package com.imooc.service.center;

import com.imooc.pojo.Orders;
import com.imooc.pojo.Users;
import com.imooc.pojo.bo.center.CenterUserBO;
import com.imooc.pojo.vo.OrderStatusCountsVO;
import com.imooc.utils.PagedGridResult;

public interface MyOrdersSerivce {

    /**
     * 查询订单列表
     * @param userId
     * @param orderStatus
     * @param page
     * @param pageSize
     * @return
     */
    public PagedGridResult queryMyOrders(String userId,
                                         Integer orderStatus,
                                         Integer page,
                                         Integer pageSize);

    /**
     * 将订单状态更新为: 已发货, 待收货
     * @param orderId
     */
    public void updateDeliverOrderStatus(String orderId);

    /**
     * 根据 userId + orderId, 查找 Orders
     * @param userId    用户id
     * @param orderId   订单id
     * @return
     */
    public Orders queryMyOrdersByUserId(String userId, String orderId);

    /**
     * 将订单状态更新为: 交易成功
     * @param orderId
     */
    public boolean updateReceiveOrderStatus(String orderId);

    /**
     * 将订单状态更新为: 删除交易
     * @param orderId
     */
    public boolean updateDeleteOrderStatus(String userId, String orderId);

    /**
     * 获取用户的所有非终态订单的数量
     * @param userId
     * @return
     */
    public OrderStatusCountsVO getOrderStatusCounts(String userId);

    /**
     * 获取订单动向
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    public PagedGridResult getOrderTrend(String userId, Integer page, Integer pageSize);
}
