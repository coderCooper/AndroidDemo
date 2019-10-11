package com.lollipop.white.bean;

import com.lollipop.white.util.Constants;

import java.util.Set;

/**
 * Created by lollipop on 2018/12/3.
 */

public class TagBean {

    public int status = Constants.NORMAL; // 1:默认状态，2，选中状态，3，不可用状态

    public String tagStr;   // 属性值

    //  该属性下可用的值
    private Set<Integer> skuIds;

    public Set<Integer> getSkuIds() {
        return skuIds;
    }

    public void setSkuIds(Set<Integer> skuIds) {
        this.skuIds = skuIds;
    }
}
