package com.imooc.service;

import com.imooc.pojo.Carousel;

import java.util.List;

public interface CarouselService {
    /**
     * 获得轮播图数据
     * @param isShow
     * @return
     */
    public List<Carousel> queryAllCarousel(Integer isShow);
}
