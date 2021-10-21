package com.imooc.enums;

public enum CategoryType {
    ROOTLEVEL(1, "一级分类"),
    SECONDLEVEL(2, "二级分类"),
    THIRDLEVEL(3, "三级分类");

    public final Integer num;
    public final String type;
    CategoryType(Integer num, String type)
    {
        this.num = num;
        this.type = type;
    }
}
