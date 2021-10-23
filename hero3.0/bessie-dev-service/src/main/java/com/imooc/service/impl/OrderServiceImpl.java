package com.imooc.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imooc.enums.CommentLevelEnum;
import com.imooc.enums.OrderStatusEnums;
import com.imooc.enums.YesOrNo;
import com.imooc.mapper.*;
import com.imooc.pojo.*;
import com.imooc.pojo.bo.SubmitOrderBO;
import com.imooc.pojo.vo.CommentLevelCountsVO;
import com.imooc.pojo.vo.ItemCommentVO;
import com.imooc.pojo.vo.SearchItemsVO;
import com.imooc.pojo.vo.ShopcartVO;
import com.imooc.service.AddressService;
import com.imooc.service.ItemService;
import com.imooc.service.OrderService;
import com.imooc.utils.DesensitizationUtil;
import com.imooc.utils.PagedGridResult;
import org.apache.catalina.User;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.annotation.Order;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

/**
 * @program: bessie-dev
 * @description: 商品相关
 * @author: Bessie
 * @create: 2021-10-22 12:34
 **/
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private Sid sid;

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private OrderItemsMapper orderItemsMapper;

    @Autowired
    private OrderStatusMapper orderStatusMapper;

    @Autowired
    private AddressService addressService;

    @Autowired
    private ItemService itemService;

    /**
     * 根据前端传来的参数, 创建订单
     *
     * @param submitOrderBO
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public String createOrder(SubmitOrderBO submitOrderBO) {
        //1. 创建订单, 即把 Orders 这个类里面的所有变量都填充好
        /** Orders:
         * createdTime
         * extand
         * id                   :额外设置, 这个是主键, 需要 sid
         * isComment
         * isDelete
         * leftMsg
         * payMethod
         * postAmount           :额外设置, 设置为0
         * realPayAmount
         * receiverAddress
         * receiverMobile
         * receiverName
         * totalAmount
         * updatedTime
         * userId
         */

        /** SubmitOrderBO:
         * addressId
         * itemSpecIds
         * leftMsg
         * payMethod
         * userId
         */

        /** UserAddress: 注意这里不用 AddressBO
         * city
         * createdTime
         * detail
         * district
         * extand
         * id
         * isDefault
         * mobile
         * province
         * receiver
         * updatedTime
         * userId
         */

        Orders orders = new Orders();
        //1.1 将 SubmitOrderBO 里面的变量填充进 Orders
        String userId = submitOrderBO.getUserId();
        String addressId = submitOrderBO.getAddressId();
        orders.setUserId(userId);
        orders.setPayMethod(submitOrderBO.getPayMethod());
        orders.setLeftMsg(submitOrderBO.getLeftMsg());

        //1.2 将 UserAddress 里面的变量填充进 Orders
        UserAddress userAddress = addressService.queryUserAddress(userId, addressId);
        orders.setReceiverName(userAddress.getReceiver());
        orders.setReceiverMobile(userAddress.getMobile());
        orders.setReceiverAddress(userAddress.getProvince() + " " + userAddress.getCity() + " " + userAddress.getDistrict() + " " + userAddress.getDetail());

        //1.3 将其他变量, 填充进 Orders
        Integer postAmount = 0;
        orders.setPostAmount(postAmount);
        String orderId = sid.nextShort();
        orders.setId(orderId);               //填充主键
        orders.setIsComment(YesOrNo.NO.num);
        orders.setIsDelete(YesOrNo.NO.num);
        orders.setCreatedTime(new Date());
        orders.setUpdatedTime(new Date());

        //1.4 计算各个规格商品的价格 和 总价格
        String itemSpecIds = submitOrderBO.getItemSpecIds();
        String itemSpecIdArr[] = itemSpecIds.split(",");
        Integer totalAmount = 0;    //原价的总价
        Integer realPayAmount = 0;  //实际支付的总价


        for(String itemSpecId : itemSpecIdArr)
        {
            //2.0 根据规格id, 获取相应的商品规格
            ItemsSpec itemsSpec = itemService.queryItemsSpecBySpecId(itemSpecId);
            Integer buyCount = 1; //TODO: 购买的商品数量从 redis 获得, 现在暂定为 1
            totalAmount += itemsSpec.getPriceNormal() * buyCount;
            realPayAmount += itemsSpec.getPriceDiscount() * buyCount;

            //2.1 一个 itemSpecId, 对应一个 OrderItems, 现在开始填充 OrderItems
            /** OrderItems: 订单的各个商品
             * buyCounts
             * id
             * itemId
             * itemImg
             * itemName
             * itemSpecId
             * itemSpecName
             * orderId
             * price
             */
            //2.1.1 根据商品id{不是规格id}, 获取商品信息
            String itemId = itemsSpec.getItemId();
            Items item = itemService.queryItemById(itemId); //商品信息都存在 item 里面

            //2.1.2 根据商品id{不是规格id}, 获取商品图片
            ItemsImg itemsImg = itemService.queryItemMainImgById(itemId); //商品图片
            String url = itemsImg.getUrl() != null ? itemsImg.getUrl() : "";

            //2.1.3 开始填充 OrderItems
            OrderItems orderItems = new OrderItems();
            orderItems.setBuyCounts(buyCount);
            orderItems.setId(sid.nextShort());
            orderItems.setItemId(itemId);
            orderItems.setItemImg(url);
            orderItems.setItemName(item.getItemName());
            orderItems.setItemSpecId(itemSpecId);
            orderItems.setItemSpecName(itemsSpec.getName());
            orderItems.setOrderId(orderId);
            orderItems.setPrice(itemsSpec.getPriceDiscount()); //实际支付价格
            orderItemsMapper.insert(orderItems);

            //2.4 规格表, 需要扣除库存
            //如果执行失败, 这一句会 throw new RuntimeException();
            itemService.decreaseItemSpecStock(itemSpecId, buyCount);
        }

        //1.5 继续填充 Orders
        orders.setTotalAmount(totalAmount);
        orders.setRealPayAmount(realPayAmount);
        ordersMapper.insert(orders);


        //3.0 开始填充 OrderStatus
        /** OrderStatus: 订单状态
         * closeTime
         * commentTime
         * createdTime
         * deliverTime
         * orderId
         * orderStatus
         * payTime
         * successTime
         */
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderId(orderId);
        orderStatus.setOrderStatus(OrderStatusEnums.WAIT_PAY.num);
        orderStatus.setCreatedTime(new Date());
        orderStatusMapper.insert(orderStatus);

        return orderId;
    }
}
