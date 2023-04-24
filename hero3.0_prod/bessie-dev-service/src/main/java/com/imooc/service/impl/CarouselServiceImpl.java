package com.imooc.service.impl;

import com.imooc.mapper.CarouselMapper;
import com.imooc.pojo.Carousel;
import com.imooc.service.CarouselService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @program: bessie-dev
 * @description: 轮播图
 * @author: Bessie
 * @create: 2021-10-20 09:26
 **/
@Service
public class CarouselServiceImpl implements CarouselService {
    @Autowired
    private CarouselMapper carouselMapper;
    /**
     * 获得轮播图数据
     *
     * @param isShow
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<Carousel> queryAllCarousel(Integer isShow) {
        Example carousel = new Example(Carousel.class);
        carousel.orderBy("sort").desc();
        Example.Criteria criteria = carousel.createCriteria();
        criteria.andEqualTo("isShow", isShow);
        List<Carousel> res = carouselMapper.selectByExample(carousel);
        return res;
    }
}
