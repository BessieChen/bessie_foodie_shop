package com.imooc.service.impl.center;

import com.imooc.mapper.UsersMapper;
import com.imooc.pojo.Users;
import com.imooc.pojo.bo.center.CenterUserBO;
import org.apache.tomcat.jni.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @program: bessie-dev
 * @description: 用户中心信息展示
 * @author: Bessie
 * @create: 2021-10-23 22:04
 **/
@Service
public class CenterUserSerivce implements com.imooc.service.center.CenterUserSerivce {

    @Autowired
    private UsersMapper usersMapper;
    /**
     * 根据用户id, 查询用户信息
     *
     * @param userId
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users queryUserInfo(String userId) {
        //错误:
//        Users users = new Users();
//        users.setId(userId);
//        return usersMapper.selectOne(users);

        Users users = usersMapper.selectByPrimaryKey(userId);
        users.setPassword(null); //不要在用户中心展示密码
        return users;
    }

    /**
     * 根据用户id, BO. 更新用户信息, 最后把 Users 返回
     * 将前端传过来的 BO, 更新到 Users 结构体中
     * @param userId
     * @param centerUserBO
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Users updateUserInfo(String userId, CenterUserBO centerUserBO) {
        Users users = new Users();
        BeanUtils.copyProperties(centerUserBO, users);
        users.setId(userId);
        users.setUpdatedTime(new Date());
        usersMapper.updateByPrimaryKeySelective(users);
        return queryUserInfo(userId);
    }

    /**
     * 保存用户头像的url, 最后把 Users 返回
     *
     * @param userId
     * @param faceUrl
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Users updateUserFace(String userId, String faceUrl) {
        Users users = new Users();
        users.setId(userId);
        users.setFace(faceUrl);
        users.setUpdatedTime(new Date());
        usersMapper.updateByPrimaryKeySelective(users);
        return queryUserInfo(userId);
    }
}
