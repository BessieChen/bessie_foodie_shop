package com.imooc.service;

import com.imooc.pojo.Category;
import com.imooc.pojo.vo.CategoryVO;

import java.util.List;

public interface CategoryService {
    /**
     * 获得所有一级分类
     * @return
     */
    public List<Category> queryAllRootLevelCat();

    /**
     * 根据一级分类, 获得所有的二三级分类
     * @param rootCat
     * @return
     */
    public List<CategoryVO> getSubCatList(Integer rootCat);
}
