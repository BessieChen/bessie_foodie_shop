package com.imooc.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imooc.enums.CommentLevelEnum;
import com.imooc.enums.YesOrNo;
import com.imooc.mapper.*;
import com.imooc.pojo.*;
import com.imooc.pojo.vo.CommentLevelCountsVO;
import com.imooc.pojo.vo.ItemCommentVO;
import com.imooc.pojo.vo.SearchItemsVO;
import com.imooc.pojo.vo.ShopcartVO;
import com.imooc.service.ItemService;
import com.imooc.utils.DesensitizationUtil;
import com.imooc.utils.PagedGridResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.swing.*;
import java.util.*;

/**
 * @program: bessie-dev
 * @description: 商品相关
 * @author: Bessie
 * @create: 2021-10-22 12:34
 **/
@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemsMapper itemsMapper;

    @Autowired
    private ItemsImgMapper itemsImgMapper;

    @Autowired
    private ItemsSpecMapper itemsSpecMapper;

    @Autowired
    private ItemsParamMapper itemsParamMapper;

    @Autowired
    private ItemsCommentsMapper itemsCommentsMapper;

    @Autowired
    private ItemsMapperCustom itemsMapperCustom;

    /**
     * 根据商品id获取商品
     *
     * @param itemId
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Items queryItemById(String itemId) {
        return itemsMapper.selectByPrimaryKey(itemId);
    }

    /**
     * 根据商品id获取商品图片
     *
     * @param itemId
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ItemsImg> queryItemImgList(String itemId) {
        Example itemsImgExp = new Example(ItemsImg.class);
        Example.Criteria criteria = itemsImgExp.createCriteria();
        criteria.andEqualTo("itemId", itemId);
        return itemsImgMapper.selectByExample(itemsImgExp);
    }

    /**
     * 根据商品id获取商品规格
     *
     * @param itemId
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ItemsSpec> queryItemSpecList(String itemId) {
        Example itemsSpecExp = new Example(ItemsSpec.class);
        Example.Criteria criteria = itemsSpecExp.createCriteria();
        criteria.andEqualTo("itemId", itemId);
        return itemsSpecMapper.selectByExample(itemsSpecExp);
    }

    /**
     * 根据商品id获取商品参数
     *
     * @param itemId
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ItemsParam queryItemParam(String itemId) {
        Example itemsParamExp = new Example(ItemsParam.class);
        Example.Criteria criteria = itemsParamExp.createCriteria();
        criteria.andEqualTo("itemId", itemId);
        return itemsParamMapper.selectOneByExample(itemsParamExp);
    }

    /**
     * 获取评价个数
     *
     * @param itemId
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public CommentLevelCountsVO queryCommentCounts(String itemId) {
        Integer good = getLevelCounts(itemId, CommentLevelEnum.GOOD.type);
        Integer normal = getLevelCounts(itemId, CommentLevelEnum.NORMAL.type);
        Integer bad = getLevelCounts(itemId, CommentLevelEnum.BAD.type);
        Integer total = good + normal + bad;

        CommentLevelCountsVO commentLevelCountsVO = new CommentLevelCountsVO();
        commentLevelCountsVO.setBadCounts(bad);
        commentLevelCountsVO.setNormalCounts(normal);
        commentLevelCountsVO.setGoodCounts(good);
        commentLevelCountsVO.setTotalCounts(total);
        return commentLevelCountsVO;
    }

    private Integer getLevelCounts(String itemId, Integer level)
    {
        ItemsComments condition = new ItemsComments();
        condition.setItemId(itemId);
        if(level != null)
        {
            condition.setCommentLevel(level);
        }
        return itemsCommentsMapper.selectCount(condition);
    }

    /**
     * 获取商品评价
     * 通过 controller 传过来的2个参数, 组装成map
     * @param itemId
     * @param level
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult queryPagedComments(String itemId, Integer level, Integer page, Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("itemId", itemId);
        map.put("level", level);

        /**
         * page: 第几页
         * pageSize: 每页显示多少条
         */
        PageHelper.startPage(page, pageSize);
        List<ItemCommentVO> list =  itemsMapperCustom.queryItemComments(map);

        //实现个人信息的脱敏
        for(ItemCommentVO vo : list)
        {
            vo.setNickname(DesensitizationUtil.commonDisplay(vo.getNickname()));
        }

        return setterPagedGrid(list, page);
    }

    /**
     * 将结果分页
     * @param list
     * @param page
     * @return
     */
    public PagedGridResult setterPagedGrid(List<?> list, Integer page)
    {
        PageInfo<?> pageList = new PageInfo<>();
        PagedGridResult grid = new PagedGridResult();
        grid.setPage(page);
        grid.setRows(list);
        grid.setTotal(pageList.getPages());
        grid.setRecords(pageList.getTotal());
        return grid;
    }

    /**
     * 获取商品搜索列表结果
     *
     * @param keywords
     * @param sort
     * @param page
     * @param pageSize
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult searchItems(String keywords, String sort, Integer page, Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("keywords", keywords);
        map.put("sort", sort);

        /**
         * page: 第几页
         * pageSize: 每页显示多少条
         */
        PageHelper.startPage(page, pageSize);
        List<SearchItemsVO> list =  itemsMapperCustom.searchItems(map);
        return setterPagedGrid(list, page);
    }

    /**
     * 根据分类搜索商品列表
     *
     * @param catId
     * @param sort
     * @param page
     * @param pageSize
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult searchItems(Integer catId, String sort, Integer page, Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("catId", catId);
        map.put("sort", sort);

        /**
         * page: 第几页
         * pageSize: 每页显示多少条
         */
        PageHelper.startPage(page, pageSize);
        List<SearchItemsVO> list =  itemsMapperCustom.searchItemsByThirdCat(map);
        return setterPagedGrid(list, page);
    }

    /**
     * 根据前端传来的 specIds, 返回购物车的商品数据
     *
     * @param specIds
     * @return 每个商品是一个 ShopcartBO, 全部的商品都装在 List<> 中
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ShopcartVO> queryItemsBySpecIds(String specIds) {
        String ids[] = specIds.split(",");
        List<String> specIdsList = new ArrayList<>();
        Collections.addAll(specIdsList, ids); //目标:specIdsList, 源: ids
        System.out.println(ids);

        List<ShopcartVO> res = itemsMapperCustom.queryItemsBySpecIds(specIdsList);
        return res;
    }

    /**
     * 根据规格id: specId, 获得相应的商品规格
     *
     * @param specId
     * @return
     */
    @Override
    public ItemsSpec queryItemsSpecBySpecId(String specId) {
        ItemsSpec itemsSpec = new ItemsSpec();
        itemsSpec.setId(specId);
        return itemsSpecMapper.selectOne(itemsSpec);
    }

    /**
     * 根据商品id: itemId, 获得相应的商品图片
     *
     * @param itemId
     * @return
     */
    @Override
    public ItemsImg queryItemMainImgById(String itemId) {
        ItemsImg itemsImg = new ItemsImg();
        itemsImg.setItemId(itemId);
        itemsImg.setIsMain(YesOrNo.YES.num);
        return itemsImgMapper.selectOne(itemsImg);
    }

    /**
     * 创建订单之后, 扣除商品库存
     *
     * 方式:
         *  synchronized 不推荐使用，集群下无用，性能低下
         *  锁数据库: 不推荐，导致数据库性能低下
         *  分布式锁 zookeeper redis: 之后会使用
     * 伪代码:
         *  lockUtil.getLock(); -- 加锁
         *  1. 查询库存
         *         int stock = 10;
         *  2. 判断库存，是否能够减少到0以下
         *         if (stock - buyCounts < 0) {
         *              提示用户库存不够
         *             10 - 3 -3 - 5 = -1
         *         }
         *  lockUtil.unLock(); -- 解锁
     * @param specId
     * @param buyCount
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void decreaseItemSpecStock(String specId, int buyCount) {
        // 使用自定义 mapper 中实现的乐观锁:
        int res = itemsMapperCustom.decreaseItemSpecStock(specId, buyCount);
        if(res != 1) //说明失败, 因为成功是1
        {
            throw new RuntimeException("订单创建失败, 原因: 库存不足");
        }
    }
}
