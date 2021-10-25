package com.imooc.mapper;

import com.imooc.my.mapper.MyMapper;
import com.imooc.pojo.ItemsComments;
import com.imooc.pojo.vo.MyCommentVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ItemsCommentsMapperCustom extends MyMapper<ItemsComments> {

    /**
     * 保存商品评价
     * @param map
     */
    public void saveComments(Map<String, Object> map);

    /**
     * 展示商品评价
     * @param map
     * @return
     */
    public List<MyCommentVO> queryMyComments(@Param("paramsMap") Map<String, Object> map);

}