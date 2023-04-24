package com.imooc.mapper;

import com.imooc.pojo.vo.CategoryVO;
import com.imooc.pojo.vo.NewItemsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CategoryMapperCustom {
    /**
     * 根据一级分类, 查询所有它对应的二三级分类
     * @param rootCat: 一级分类
     * @return
     */
    public List<CategoryVO> getSubCatList(Integer rootCat);

    /**
     * 根据一级分类, 获得最新的6个商品的信息
     * @param map
     * @return
     */
    public List<NewItemsVO> getSixNewItems(@Param("paramsMap") Map<String, Object> map);
}
