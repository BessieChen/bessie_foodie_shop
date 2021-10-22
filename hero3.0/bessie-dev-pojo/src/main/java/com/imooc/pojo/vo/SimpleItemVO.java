package com.imooc.pojo.vo;

/**
 * @program: bessie-dev
 * @description: 一级分类下最新的6件商品
 * @author: Bessie
 * @create: 2021-10-22 10:31
 **/
public class SimpleItemVO {
    private String itemId;
    private String itemName;
    private String itemUrl;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemUrl() {
        return itemUrl;
    }

    public void setItemUrl(String itemUrl) {
        this.itemUrl = itemUrl;
    }
}
