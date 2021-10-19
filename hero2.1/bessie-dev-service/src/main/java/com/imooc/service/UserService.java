package com.imooc.service;

import com.imooc.pojo.Stu;
import com.imooc.pojo.Users;
import com.imooc.pojo.bo.UserBO;

/**
 * @program: bessie-dev
 * @description: stuservice
 * @author: Bessie
 * @create: 2021-10-18 14:21
 **/
public interface UserService {
    /**
     * 判断用户名是否存在
     * @param userName
     * @return
     */
    public boolean queryUsernameIsExist(String userName);

    /**
     * 注册用户
     * @param userBO
     *  定义在 pojo.bo
     * @return
     *  虽然可以返回 void, 但是还是返回 Users对象 给 controller
     */
    public Users createUser(UserBO userBO);

    /**
     * 用户登录
     * @param username
     * @param password
     * @return
     */
    public Users queryUserForLogin(String username, String password);
}
