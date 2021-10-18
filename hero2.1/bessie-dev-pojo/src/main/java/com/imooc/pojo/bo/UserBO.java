package com.imooc.pojo.bo;

/**
 * @program: bessie-dev
 * @description:
 * @author: Bessie
 * @create: 2021-10-19 04:53
 **/
public class UserBO {
    private String userName;
    private String password;
    private String confirmPassword;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
