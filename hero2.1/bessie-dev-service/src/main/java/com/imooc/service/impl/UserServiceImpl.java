package com.imooc.service.impl;

import com.imooc.enums.Gender;
import com.imooc.mapper.StuMapper;
import com.imooc.mapper.UsersMapper;
import com.imooc.pojo.Stu;
import com.imooc.pojo.Users;
import com.imooc.pojo.bo.UserBO;
import com.imooc.service.StuService;
import com.imooc.service.UserService;
import com.imooc.utils.DateUtil;
import com.imooc.utils.MD5Utils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;

/**
 * @program: bessie-dev
 * @description:
 * @author: Bessie
 * @create: 2021-10-18 14:35
 **/

@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private UsersMapper userMapper;

    //全局唯一主键, 使用 org.n3r.idworker
    @Autowired
    private Sid sid;

    private static final String FACE_URL = "http://122.152.205.72:88/group1/M00/00/05/CpoxxFw_8_qAIlFXAAAcIhVPdSg994.png";

    /**
     * 判断用户名是否存在
     *
     * @param userName
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean queryUsernameIsExist(String userName) {
//        Example userExample = new Example(UsersMapper.class);
        Example userExample = new Example(Users.class);
        Example.Criteria userCriteria = userExample.createCriteria();
        userCriteria.andEqualTo("username", userName);
        Users user = userMapper.selectOneByExample(userExample);
        return user != null;
    }

    /**
     * 注册用户
     *
     * @param userBO 定义在 pojo.bo
     * @return 虽然可以返回 void, 但是还是返回 Users对象 给 controller
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Users createUser(UserBO userBO) {
        //开始设置 User 对象
        Users user = new Users();

        //全局唯一主键, 使用 org.n3r.idworker
        String id = sid.nextShort();
        user.setId(id);

        //用户名
        user.setUsername(userBO.getUsername());

        //密码, MD5Utils 加密
        try {
            user.setPassword(MD5Utils.getMD5Str(userBO.getPassword()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        //用户名和昵称同名
        user.setNickname(userBO.getUsername());

        //使用默认头像
        user.setFace(FACE_URL);

        //默认生日, 使用 DateUtil
        user.setBirthday(DateUtil.stringToDate("1900-01-01"));

        //性别
        user.setSex(Gender.man.type);

        //日期
        user.setCreatedTime(new Date());
        user.setUpdatedTime(new Date());

        //暂时不设置邮箱
        //user.setEmail();

        //插入到数据库
        userMapper.insert(user);
        return user;
    }


    /**
     * 用户登录: 判断用户名输入的密码是否正确
     * @param username
     * @param password
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users queryUserForLogin(String username, String password) {
        Example example = new Example(Users.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username", username);
        criteria.andEqualTo("password", password);
        Users user = userMapper.selectOneByExample(example);
        return user;
    }
}
