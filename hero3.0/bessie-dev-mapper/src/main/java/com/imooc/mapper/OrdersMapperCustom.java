package com.imooc.mapper;

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
}
