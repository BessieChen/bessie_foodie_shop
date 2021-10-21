package com.imooc.mapper;

import com.imooc.pojo.vo.CategoryVO;

import java.util.List;

public interface CategoryMapperCustom {
    /**
     * 根据一级分类, 查询所有它对应的二三级分类
     * @param rootCat: 一级分类
     * @return
     */
    public List<CategoryVO> getSubCatList(Integer rootCat);
}
