package com.imooc.service.impl;

import com.github.pagehelper.PageInfo;
import com.imooc.service.BaseSerivce;
import com.imooc.utils.PagedGridResult;

import java.util.List;

/**
 * @program: bessie-dev
 * @description: 通用的服务
 * @author: Bessie
 * @create: 2021-10-25 02:06
 **/
public class BaseServiceImpl implements BaseSerivce {
    /**
     * 实现分页
     *
     * @param list
     * @param page
     * @return
     */
    @Override
    public PagedGridResult setterPagedGrid(List<?> list, Integer page)
    {
        PageInfo<?> pageList = new PageInfo<>(list);
        PagedGridResult grid = new PagedGridResult();
        grid.setPage(page);
        grid.setRows(list);
        grid.setTotal(pageList.getPages());
        grid.setRecords(pageList.getTotal());
        //System.out.println(pageList.getTotal());
        return grid;
    }
}
