package com.imooc.enums;

public enum PayMethod {
    WEIXIN(1, "微信"),
    ALIPAY(2, "支付宝");
    public final Integer num;
    public final String type;
    PayMethod(Integer num, String type)
    {
        this.num = num;
        this.type = type;
    }
}
