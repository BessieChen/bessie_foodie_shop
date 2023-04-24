package com.imooc.enums;

public enum ShowYesOrNo {
    NO(0, "不展示"),
    YES(1, "展示");

    public final Integer num;
    public final String type;

    ShowYesOrNo(Integer num, String type)
    {
        this.num = num;
        this.type = type;
    }
}
