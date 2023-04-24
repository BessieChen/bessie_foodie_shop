package com.imooc.service;

import com.imooc.pojo.Category;
import com.imooc.pojo.vo.CategoryVO;
import com.imooc.pojo.vo.NewItemsVO;

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

    /**
     * 根据 rootCatId, 获取该类下的最新的6个商品
     * @param rootCatId
     * @return
     */
    public List<NewItemsVO> getSixNewItemsLazy(Integer rootCatId);
}
