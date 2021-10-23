package com.imooc.enums;

public enum YesOrNo {
    NO(0, "不展示"),
    YES(1, "展示");

    public final Integer num;
    public final String type;

    YesOrNo(Integer num, String type)
    {
        this.num = num;
        this.type = type;
    }
}
