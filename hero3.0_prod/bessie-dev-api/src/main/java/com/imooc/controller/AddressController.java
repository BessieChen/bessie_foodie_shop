package com.imooc.controller;

import com.imooc.pojo.UserAddress;
import com.imooc.pojo.bo.AddressBO;
import com.imooc.pojo.vo.ShopcartVO;
import com.imooc.service.AddressService;
import com.imooc.utils.JSONReturn;
import com.imooc.utils.MobileEmailUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @program: bessie-dev
 * @description: 地址相关
 * @author: Bessie
 * @create: 2021-10-23 08:55
 **/
@Api(value = "地址相关", tags = {"地址相关的api接口"})
@RestController
@RequestMapping("address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @ApiOperation(value = "商品加入购物车", notes = "商品加入购物车", httpMethod = "POST")
    @PostMapping("/list")
    public JSONReturn list(
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId)
    {
        if(StringUtils.isBlank(userId))
        {
            return JSONReturn.errorMsg("用户id不能为空");
        }
        List<UserAddress> list = addressService.queryAll(userId);
        return JSONReturn.ok(list);
    }

    @ApiOperation(value = "新增用户地址", notes = "新增用户地址", httpMethod = "POST")
    @PostMapping("/add")
    public JSONReturn add(
            @ApiParam(name = "addressBO", value = "用户地址BO", required = true)
            @RequestBody AddressBO addressBO)
    {
        //检查地址是否正确
        JSONReturn res = checkAddress(addressBO);
        if(res.getStatus() != 200){
            return res;
        }
        addressService.addNewUserAddress(addressBO);
        return JSONReturn.ok();
    }

    @ApiOperation(value = "修改用户地址", notes = "修改用户地址", httpMethod = "POST")
    @PostMapping("/update")
    public JSONReturn update(
            @ApiParam(name = "addressBO", value = "用户地址BO", required = true)
            @RequestBody AddressBO addressBO)
    {

        if(StringUtils.isBlank(addressBO.getAddressId()))
        {
            return JSONReturn.errorMsg("地址主键 addressId 不能为空");
        }
        addressService.updateUserAddress(addressBO);
        return JSONReturn.ok();
    }

    @ApiOperation(value = "删除用户地址", notes = "删除用户地址", httpMethod = "POST")
    @PostMapping("/delete")
    public JSONReturn delete(
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId,
            @ApiParam(name = "addressId", value = "地址主键id", required = true)
            @RequestParam String addressId)
    {

        if(StringUtils.isBlank(userId) || StringUtils.isBlank(addressId))
        {
            return JSONReturn.errorMsg(""); //虽然当 userId 和 addressId 是空的, 数据库也不删除任何东西. 但是我们没有必要去让db去执行一次delete, 因为会消耗db资源
        }
        addressService.deleteUserAddress(userId, addressId);
        return JSONReturn.ok();
    }

    @ApiOperation(value = "设置为默认地址", notes = "设置为默认地址", httpMethod = "POST")
    @PostMapping("/setDefault") //前端代码之前写成了 setDefalut, 它就是这么写的... orz
    public JSONReturn setDefault(
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId,
            @ApiParam(name = "addressId", value = "地址主键id", required = true)
            @RequestParam String addressId)
    {

        if(StringUtils.isBlank(userId) || StringUtils.isBlank(addressId))
        {
            return JSONReturn.errorMsg(""); //虽然当 userId 和 addressId 是空的, 数据库也不删除任何东西. 但是我们没有必要去让db去执行一次delete, 因为会消耗db资源
        }
        addressService.updateUserAddressToBeDefault(userId, addressId);
        return JSONReturn.ok();
    }

    /**
     * 检查地址是否正确
     * @param addressBO
     * @return
     */
    private JSONReturn checkAddress(AddressBO addressBO)
    {
        //1. 收货人
        String receiver = addressBO.getReceiver();
        if(StringUtils.isBlank(receiver))
        {
            return JSONReturn.errorMsg("收货人不能为空");
        }
        if(receiver.length() > 12)
        {
            return JSONReturn.errorMsg("收货人名字过长");
        }

        //2. 手机号
        String mobile = addressBO.getMobile();
        if(StringUtils.isBlank(mobile))
        {
            return JSONReturn.errorMsg("收货人手机号不能为空");
        }
        if(mobile.length() != 11)
        {
            return JSONReturn.errorMsg("手机号长度不正确");
        }
        if(!MobileEmailUtils.checkMobileIsOk(mobile))
        {
            return JSONReturn.errorMsg("手机号的格式不正确");
        }

        //3. 收货地址
        String province = addressBO.getProvince();
        String city = addressBO.getCity();
        String district = addressBO.getDistrict();
        String detail = addressBO.getDetail();
        if(StringUtils.isBlank(province) ||
                StringUtils.isBlank(city) ||
                StringUtils.isBlank(district) ||
                StringUtils.isBlank(detail))
        {
            return JSONReturn.errorMsg("收货地址不能为空");
        }

        return JSONReturn.ok();
    }


}
