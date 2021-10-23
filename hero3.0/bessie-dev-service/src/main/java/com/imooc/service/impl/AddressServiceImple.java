package com.imooc.service.impl;

import com.imooc.enums.YesOrNo;
import com.imooc.mapper.UserAddressMapper;
import com.imooc.pojo.UserAddress;
import com.imooc.pojo.bo.AddressBO;
import com.imooc.service.AddressService;
import org.apache.catalina.User;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @program: bessie-dev
 * @description: 地址相关
 * @author: Bessie
 * @create: 2021-10-23 09:04
 **/
@Service
public class AddressServiceImple implements AddressService {

    @Autowired
    private UserAddressMapper userAddressMapper;

    @Autowired
    private Sid sid;
    /**
     * 根据用户id, 获取用户的所有地址
     * @param userId
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<UserAddress> queryAll(String userId) {
        UserAddress user = new UserAddress();
        user.setUserId(userId);
        return userAddressMapper.select(user);
    }

    /**
     * 新增用户地址
     *
     * @param addressBO
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void addNewUserAddress(AddressBO addressBO) {
        //1. 判断当前的用户, 是否有至少一个地址, 如果一个都没有, 这个新增的地址就是默认地址
        List<UserAddress> list = queryAll(addressBO.getUserId()); // 或者 this.queryAll(), 也就是用当前的 AddressService
        Integer setDefault = 0;
        if(list == null || list.isEmpty() || list.size() == 0)
        {
            setDefault = 1;
        }

        // 新增地址的时候, addressBO 的 addressId{地址id主键} 是 null
        // System.out.println(addressBO.getAddressId()); 测试过了, 的确是 null


        //2. 将 BO 里面的数据保存到数据库
        UserAddress newAddress = new UserAddress();
        BeanUtils.copyProperties(addressBO, newAddress); //注意, 这个函数是 (源, 目标)
        /** UserAddress:
         *  city
         *  userId
         *  detail
         *  district
         *  mobile
         *  province
         *  receiver
         *  createdTime     **: 需要额外设置
         *  updatedTime     **: 需要额外设置
         *  extand          **: 需要额外设置
         *  id              **: 需要额外设置, 主键
         *  isDefault       **: 需要额外设置
         */
        /** AddressBO:
         * addressId        **: 对应 UserAddress 的 id, 主键. 前端在查询 AddressBO 的时候, 前端能获得这个值
         * city
         * detail
         * district
         * mobile
         * province
         * receiver
         * userId
         */
        newAddress.setId(sid.nextShort());      //因为参数中的 BO 不能提供地址主键, 主键需要全局唯一
        newAddress.setIsDefault(setDefault);    //这个地址是否是该用户的默认地址, 这个是否设置为默认地址, 在新增地址这个函数中, 是 service 来确定的
        newAddress.setCreatedTime(new Date());
        newAddress.setUpdatedTime(new Date());

        //3. 加入到数据库
        userAddressMapper.insert(newAddress);
    }

    /**
     * 更新用户地址
     * @param addressBO
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateUserAddress(AddressBO addressBO) {
        //1. 为了更新数据库中的 UserAddress, 我们要设置我们的目标样子 temp
        UserAddress temp = new UserAddress();
        // BeanUtils.copyProperties(temp, addressBO); 注意, 这个函数是 (源, 目标)
        BeanUtils.copyProperties(addressBO, temp);

        //2. 因为 copyProperties 只能拷贝变量名一样的变量, 对于其他的变量, 需要额外设置
        temp.setId(addressBO.getAddressId()); //地址id主键, 在 UserAddress 中叫做 id, 在 AddressBO 中叫做 addressId
        temp.setUpdatedTime(new Date());

        //System.out.println(temp.getProvince());

        //3. 通过通用 mapper 更新
        userAddressMapper.updateByPrimaryKeySelective(temp); //为什么不需要设置 isDefault? 因为我们用了 Selective() 也就是没有设置的就不会修改{即不修改之前的 isDefault 值}
    }

    /**
     * 删除用户地址
     *
     * @param userId
     * @param addressId
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void deleteUserAddress(String userId, String addressId) {
        //1. 为了删除数据库中的 UserAddress, 我们要设置要删除的条目的 userId + addressId, 需要满足这两个条件的条目
        UserAddress temp = new UserAddress();
        temp.setUserId(userId);
        temp.setId(addressId);
        //2. 通过通用 mapper 删除
        userAddressMapper.delete(temp);
        //3. byb: 如果删除的这个地址是默认地址, 我们会把用户另外的一个地址设置为默认
        List<UserAddress> list = queryAll(userId); // 或者 this.queryAll(), 也就是用当前的 AddressService
        if(list != null && list.size() > 0)
        {
            this.updateUserAddressToBeDefault(userId, list.get(0).getId());
        }
        //如果没有任何地址, 就不用设置了
    }

    /**
     * 将地址设置为默认地址, 其他地址都变成非默认
     *
     * @param userId
     * @param addressId
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateUserAddressToBeDefault(String userId, String addressId) {
        //1. 查找该用户的所有地址, 把这些地址都设置为 非默认
        UserAddress temp = new UserAddress();
        temp.setUserId(userId);
        temp.setIsDefault(YesOrNo.YES.num);
        List<UserAddress> setUnDefault = userAddressMapper.select(temp);

        //2. 将这些地址都设置为 非默认
        for(UserAddress item : setUnDefault)
        {
            item.setIsDefault(YesOrNo.NO.num);
            userAddressMapper.updateByPrimaryKeySelective(item);    // UserAddress 的主键是 id, 而不是 userId
        }

        //3. 将这个地址设置为 默认
        UserAddress defaultAddress = new UserAddress();
        defaultAddress.setUserId(userId);
        defaultAddress.setId(addressId);    //设置主键
        defaultAddress.setIsDefault(YesOrNo.YES.num);
        userAddressMapper.updateByPrimaryKeySelective(defaultAddress);

    }
}
