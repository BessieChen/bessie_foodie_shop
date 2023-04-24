package com.imooc.service;

import com.imooc.pojo.Stu;

/**
 * @program: bessie-dev
 * @description: stuservice
 * @author: Bessie
 * @create: 2021-10-18 14:21
 **/
public interface StuService {
    public Stu getStuInfo(int id);
    public void saveStu();
    public void updateStu(int id);
    public void deleteStu(int id);
}
