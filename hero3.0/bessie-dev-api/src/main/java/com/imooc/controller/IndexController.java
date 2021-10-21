package com.imooc.controller;

import com.fasterxml.jackson.core.json.JsonReadContext;
import com.imooc.enums.ShowYesOrNo;
import com.imooc.pojo.Carousel;
import com.imooc.pojo.Category;
import com.imooc.pojo.vo.CategoryVO;
import com.imooc.service.CarouselService;
import com.imooc.service.CategoryService;
import com.imooc.utils.JSONReturn;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @program: bessie-dev
 * @description: 首页
 * @author: Bessie
 * @create: 2021-10-20 09:33
 **/
@Api(value = "首页", tags = {"首页展示的相关接口"})
@RestController
@RequestMapping("index")
public class IndexController {

    @Autowired
    private CarouselService carouselService;

    @Autowired
    private CategoryService categoryService;

    @ApiOperation(value = "获取轮播图", notes = "获取轮播图", httpMethod = "GET")
    @GetMapping("/carousel")
    public JSONReturn carousel()
    {
        List<Carousel> res = carouselService.queryAllCarousel(ShowYesOrNo.YES.num);
        return JSONReturn.ok(res);
    }

    @ApiOperation(value = "获得一级分类", notes = "获得一级分类", httpMethod = "GET")
    @GetMapping("/cats")
    public JSONReturn cats()
    {
        List<Category> res = categoryService.queryAllRootLevelCat();
        return JSONReturn.ok(res);
    }

    @ApiOperation(value = "获得二三级分类", notes = "获得二三级分类", httpMethod = "GET")
    @GetMapping("/subCat/{rootCat}")
    public JSONReturn subCat(
            @ApiParam(name = "rootCat", value = "一级分类id", required = true)
            @PathVariable Integer rootCat)
    {
        if(rootCat == null)
        {
            return JSONReturn.errorMsg("分类不存在");
        }
        List<CategoryVO> res = categoryService.getSubCatList(rootCat);
        return JSONReturn.ok(res);
    }
}

