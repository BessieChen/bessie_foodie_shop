package com.imooc.service;

import com.imooc.utils.PagedGridResult;

import java.util.List;

public interface BaseSerivce {
    /**
     * 实现分页
     * @param list
     * @param page
     * @return
     */
    public PagedGridResult setterPagedGrid(List<?> list, Integer page);
}
