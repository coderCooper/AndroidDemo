package com.lollipop.white.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Set;

/**
 * Created by lollipop on 2018/11/23.
 */

public class SkuInfoBean implements Serializable {

    // sku的属性标题
    private String title;

    //  该属性下可用的值
    private ArrayList<TagBean> tags;

    // 之前选中的index
    private int preIndex;

    private Set<Integer> skuids;    // 目前该属性下的所用的skuids

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<TagBean> getTags() {
        return tags;
    }

    public void setTags(ArrayList<TagBean> tags) {
        this.tags = tags;
    }

    public int getPreIndex() {
        return preIndex;
    }

    public void setPreIndex(int preIndex) {
        this.preIndex = preIndex;
    }

    public void setSkuids(Set<Integer> skuids) {
        this.skuids = skuids;
    }

    public Set<Integer> getSkuids() {
        return skuids;
    }
}
