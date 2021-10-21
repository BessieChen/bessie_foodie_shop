package com.imooc.pojo.vo;

/**
 * @program: bessie-dev
 * @description: 三级分类
 * @author: Bessie
 * @create: 2021-10-20 10:17
 **/
public class SubCategoryVO {
    private Integer subId;
    private String subName;
    private Integer subType;

    public Integer getSubId() {
        return subId;
    }

    public void setSubId(Integer subId) {
        this.subId = subId;
    }

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    public Integer getSubType() {
        return subType;
    }

    public void setSubType(Integer subType) {
        this.subType = subType;
    }

    public Integer getSubFatherId() {
        return subFatherId;
    }

    public void setSubFatherId(Integer subFatherId) {
        this.subFatherId = subFatherId;
    }

    private Integer subFatherId;
}
