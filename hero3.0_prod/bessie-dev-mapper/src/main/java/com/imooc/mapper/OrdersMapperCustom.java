package com.imooc.mapper;

import com.imooc.pojo.OrderStatus;
import com.imooc.pojo.vo.MyOrdersVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface OrdersMapperCustom {

    /**
     * 获取所有的订单
     * @param map
     * @return
     */
    public List<MyOrdersVO> queryMyOrders(@Param("paramsMap") Map<String, Object> map);

    /**
     * 获得订单状态数量
     * @param map
     * @return
     */
    public Integer getMyOrderStatusCounts(@Param("paramsMap") Map<String, Object> map);

    /**
     * 获取非终态订单
     * @param map
     * @return
     */
    public List<OrderStatus> getMyOrderTrend(@Param("paramsMap") Map<String, Object> map);
}
