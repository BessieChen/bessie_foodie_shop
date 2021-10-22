package com.imooc.service;

import com.imooc.pojo.Items;
import com.imooc.pojo.ItemsImg;
import com.imooc.pojo.ItemsParam;
import com.imooc.pojo.ItemsSpec;
import com.imooc.pojo.vo.CommentLevelCountsVO;
import com.imooc.pojo.vo.ItemCommentVO;
import com.imooc.utils.PagedGridResult;

import java.util.List;

public interface ItemService {
    /**
     * 根据商品id获取商品
     * @param itemId
     * @return
     */
    public Items queryItemById(String itemId);

    /**
     * 根据商品id获取商品图片
     * @param itemId
     * @return
     */
    public List<ItemsImg> queryItemImgList(String itemId);

    /**
     * 根据商品id获取商品规格
     * @param itemId
     * @return
     */
    public List<ItemsSpec> queryItemSpecList(String itemId);

    /**
     * 根据商品id获取商品参数
     * @param itemId
     * @return
     */
    public ItemsParam queryItemParam(String itemId);

    /**
     * 获取评价个数
     * @param itemId
     */
    public CommentLevelCountsVO queryCommentCounts(String itemId);

    /**
     * 获取商品评价
     * @param itemId
     * @param level
     * @param page
     * @param pageSize
     * @return
     */
    public PagedGridResult queryPagedComments(String itemId, Integer level, Integer page, Integer pageSize);

    /**
     * 获取商品搜索列表结果
     * @param keywords
     * @param sort
     * @param page
     * @param pageSize
     * @return
     */
    public PagedGridResult searchItems(String keywords, String sort, Integer page, Integer pageSize);

    /**
     * 根据分类搜索商品列表
     * @param catId
     * @param sort
     * @param page
     * @param pageSize
     * @return
     */
    public PagedGridResult searchItems(Integer catId, String sort, Integer page, Integer pageSize);
}
