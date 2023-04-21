package com.imooc.controller;

import com.imooc.pojo.Items;
import com.imooc.pojo.ItemsImg;
import com.imooc.pojo.ItemsParam;
import com.imooc.pojo.ItemsSpec;
import com.imooc.pojo.vo.CommentLevelCountsVO;
import com.imooc.pojo.vo.ItemInfoVO;
import com.imooc.pojo.vo.ShopcartVO;
import com.imooc.service.ItemService;
import com.imooc.utils.JSONReturn;
import com.imooc.utils.PagedGridResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.util.StringUtil;

import java.util.List;

/**
 * @program: bessie-dev
 * @description: 商品
 * @author: Bessie
 * @create: 2021-10-22 13:50
 **/
@Api(value = "商品接口", tags = {"商品信息展示的相关接口"})
@RestController
@RequestMapping("items")
public class ItemsController extends BaseController{
    @Autowired
    private ItemService itemService;

    @ApiOperation(value = "查询商品详情", notes = "查询商品详情", httpMethod = "GET")
    @GetMapping("/info/{itemId}")
    public JSONReturn info(
        @ApiParam(name = "itemId", value = "商品id", required = true)
        @PathVariable String itemId)
    {
        if(StringUtils.isBlank(itemId))
        {
            return JSONReturn.errorMsg(null);
        }
        Items item = itemService.queryItemById(itemId);
        List<ItemsImg> itemImgList = itemService.queryItemImgList(itemId);
        List<ItemsSpec> itemSpecList = itemService.queryItemSpecList(itemId);
        ItemsParam itemParams = itemService.queryItemParam(itemId);

        ItemInfoVO itemInfoVO = new ItemInfoVO();
        itemInfoVO.setItem(item);
        itemInfoVO.setItemImgList(itemImgList);
        itemInfoVO.setItemSpecList(itemSpecList);
        itemInfoVO.setItemParams(itemParams);

        return JSONReturn.ok(itemInfoVO);
    }

    @ApiOperation(value = "查询商品评价数量", notes = "查询商品评价数量", httpMethod = "GET")
    @GetMapping("/commentLevel")
    public JSONReturn commentLevel(
            @ApiParam(name = "itemId", value = "商品id", required = true)
            @RequestParam String itemId)
    {
        if(StringUtils.isBlank(itemId))
        {
            return JSONReturn.errorMsg(null);
        }
        CommentLevelCountsVO commentLevelCountsVO = itemService.queryCommentCounts(itemId);
        return JSONReturn.ok(commentLevelCountsVO);
    }

    @ApiOperation(value = "查询商品评价", notes = "查询商品评价", httpMethod = "GET")
    @GetMapping("/comments")
    public JSONReturn comments(
            @ApiParam(name = "itemId", value = "商品id", required = true)
            @RequestParam String itemId,
            @ApiParam(name = "level", value = "评价等级", required = true)
            @RequestParam Integer level,
            @ApiParam(name = "page", value = "当前需要第几页", required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "每一页多少条", required = false)
            @RequestParam Integer pageSize
            )
    {
        if(StringUtils.isBlank(itemId))
        {
            return JSONReturn.errorMsg("商品id不能为空");
        }
        if(page == null)
        {
            page = 1;
        }
        if(pageSize == null)
        {
            pageSize = COMMENT_PAGE_SIZE;
        }
        PagedGridResult grid = itemService.queryPagedComments(itemId, level, page, pageSize);
        return JSONReturn.ok(grid);
    }

    @ApiOperation(value = "搜索商品列表", notes = "搜索商品列表", httpMethod = "GET")
    @GetMapping("/search")
    public JSONReturn search(
            @ApiParam(name = "keywords", value = "关键词", required = true)
            @RequestParam String keywords,
            @ApiParam(name = "sort", value = "排序方式", required = false)
            @RequestParam String sort,
            @ApiParam(name = "page", value = "当前需要第几页", required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "每一页多少条", required = false)
            @RequestParam Integer pageSize
    )
    {
        //业务逻辑可以是: 如果 keywords == 0, 可以显示全部商品, 或者提示不能为空
        if(StringUtils.isBlank(keywords))
        {
            return JSONReturn.errorMsg("关键词不能为空");
        }
        if(sort == null)
        {
            sort = "k";
        }
        if(page == null)
        {
            page = 1;
        }
        if(pageSize == null)
        {
            pageSize = PAGE_SIZE;
        }
        PagedGridResult grid = itemService.searchItems(keywords, sort, page, pageSize);
        return JSONReturn.ok(grid);
    }

    @ApiOperation(value = "通过分类id搜索商品列表", notes = "通过分类id搜索商品列表", httpMethod = "GET")
    @GetMapping("/catItems")
    public JSONReturn catItems(
            @ApiParam(name = "catId", value = "分类id", required = true)
            @RequestParam Integer catId,
            @ApiParam(name = "sort", value = "排序方式", required = false)
            @RequestParam String sort,
            @ApiParam(name = "page", value = "当前需要第几页", required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "每一页多少条", required = false)
            @RequestParam Integer pageSize
    )
    {
        //业务逻辑可以是: 如果 keywords == 0, 可以显示全部商品, 或者提示不能为空
        if(catId == null)
        {
            return JSONReturn.errorMsg("分类id不能为空");
        }
        if(sort == null)
        {
            sort = "k";
        }
        if(page == null)
        {
            page = 1;
        }
        if(pageSize == null)
        {
            pageSize = PAGE_SIZE;
        }
        PagedGridResult grid = itemService.searchItems(catId, sort, page, pageSize);
        return JSONReturn.ok(grid);
    }

    /**
     * 用户如果长时间没有登录网站, 商品金额和规格可能过期, 此函数是用于刷新商品价格
     * @param itemSpecIds
     * @return
     */
    @ApiOperation(value = "刷新商品价格", notes = "刷新商品价格", httpMethod = "GET")
    @GetMapping("/refresh")
    public JSONReturn refresh(
            @ApiParam(name = "itemSpecIds", value = "拼接的规格ids", required = true, example = "1001,1002,1003")
            @RequestParam String itemSpecIds)
    {
        if(StringUtils.isBlank(itemSpecIds))
        {
            return JSONReturn.ok(); //返回成功也是没关系的, 就是空
        }
        List<ShopcartVO> list = itemService.queryItemsBySpecIds(itemSpecIds);

        return JSONReturn.ok(list);
    }
}
