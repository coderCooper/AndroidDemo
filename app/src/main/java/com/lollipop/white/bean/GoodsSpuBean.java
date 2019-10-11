package com.lollipop.white.bean;

import android.text.TextUtils;

import com.lollipop.white.util.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by lollipop on 2018/11/22.
 */

public class GoodsSpuBean implements Serializable {
    private int requestDid;         // 详情接口获取信息的参数
    private int item_type;               // 1. 标准skuItem；2.经销商自建的规格；3.标准组合商品；4.自建组合商品；
    private String standard;        // 属性值
    private int default_item_index;
    private int defaultSkuId;

    private JSONArray skuArray;
    private JSONObject imagesObject;
    private JSONObject nameObject;
    private JSONObject itemsObject;     //   {"item_id": 1,"stock": 765,"price": "101"}
    // 下面参数是计算得出
    private String name;            // 目前选中的sku的商品名称
    private String alias;           // 目前选中的sku的商品简称
    private String thumb;           // 目前选中的sku的图片
    private int price;              // 目前选中的sku的价格
    private int originPrice;              // 目前选中的sku的价格
    private int stock;              // 目前选中的sku的库存
    private ArrayList<SkuInfoBean> skuBeans;    // 属性列表按顺序排列
    private Set<Integer> allSkuIds;    // 属性列表按顺序排列
    private Set<Integer> userSkuIds;    // 用户选择的skuids，一般为1个
    private String dealerTag;

    // 325后修改版本
    private int item_id;
    private String property;           // sku属性
    private int cartNum;               // 购物车已有数量

    // http://showdoc.bingtuanego.com/web/#/1?page_id=55
    public void handleData(JSONObject jsonObject) {
        String property = jsonObject.optString("property"); // 通过此参数判断是否是新旧数据，修改于20190325
        if (TextUtils.isEmpty(property)){   // 旧数据
            this.requestDid = jsonObject.optInt("id");

            // 筛除默认id对应的规格，规格放在第0位
            JSONArray skuArray = jsonObject.optJSONArray("sku_info");
            if (skuArray != null && skuArray.length() > 0) {
                this.cartNum = -1;
                int default_item_id = jsonObject.optInt("default_item_id");
                int default_item_type = jsonObject.optInt("default_item_type");
                this.item_type = default_item_type;
                this.item_id = default_item_id;
                this.name = jsonObject.optString("spu_name");
                this.alias = jsonObject.optString("spu_alias");
                this.skuArray = skuArray;
                this.standard = jsonObject.optString("standard");
                this.imagesObject = jsonObject.optJSONObject("image");
                this.nameObject = jsonObject.optJSONObject("name");
                this.itemsObject = jsonObject.optJSONObject("items");

                if (this.itemsObject == null || this.itemsObject.length() == 0){
                    return;
                }

                this.allSkuIds = new HashSet<>();

                // 循环遍历 1.根据default_item_id获取default_sku 2. 获取所有可用的skuid
                int default_sku = -1;
                Iterator<String> iter = this.itemsObject.keys();
                JSONArray itemJsonArray;
                JSONObject itemJson;
                while(iter.hasNext()){
                    String skuIdStr = iter.next();
                    Integer skuId = Integer.valueOf(skuIdStr);  //把Object型强转成int型
                    this.allSkuIds.add(skuId);

                    if (default_sku == -1) {
                        itemJsonArray = this.itemsObject.optJSONArray(skuIdStr);
                        int size = itemJsonArray.length();
                        for (int i = 0; i < size; i++) {
                            itemJson = itemJsonArray.optJSONObject(i);
                            int itemId = itemJson.optInt("item_id");
                            int item_type = itemJson.optInt("item_type");
                            if (itemId == default_item_id && item_type == default_item_type) {
                                default_sku = skuId;
                                this.default_item_index = i;
                                break;
                            }
                        }
                    }
                }
                if (default_sku == -1) {
                    iter = this.itemsObject.keys();
                    String skuIdStr = iter.next();
                    default_sku = Integer.valueOf(skuIdStr);  //把Object型强转成int型
                }
                this.defaultSkuId = default_sku;
                String default_sku_id = String.valueOf(default_sku);

                this.thumb = this.imagesObject.optString(default_sku_id);
                if (TextUtils.isEmpty(this.name)){
                    this.name = this.nameObject.optString(default_sku_id);
                }
                JSONArray itemArray = this.itemsObject.optJSONArray(default_sku_id);
                JSONObject itemObject = itemArray.optJSONObject(this.default_item_index);
                this.price = itemObject.optInt("price");
                this.stock = itemObject.optInt("stock");
                this.originPrice = itemObject.optInt("original_price");
                this.cartNum = -1;
                this.dealerTag = jsonObject.optString("dealer_self_support");
            } else {
                this.item_id = jsonObject.optInt("package_id");
                this.item_type = jsonObject.optInt("type");
                this.name = jsonObject.optString("spu_name");
                this.alias = jsonObject.optString("spu_alias");
                this.thumb = jsonObject.optString("thumb");
                this.price = jsonObject.optInt("price");
                this.stock = jsonObject.optInt("stock");
                this.originPrice = jsonObject.optInt("original_price");
//                ShoppingManager manager = ShoppingManager.getInstance();
//                if (manager != null){
//                    String key = item_type + "$" + item_id;
//                    this.cartNum = manager.getGoodsNum(key);
//                }
                this.dealerTag = jsonObject.optString("dealer_self_support");
            }
        } else {
            this.requestDid = jsonObject.optInt("id");
            this.item_id = jsonObject.optInt("item_id");
            this.item_type = jsonObject.optInt("item_type");
            this.name = jsonObject.optString("name");
            this.alias = jsonObject.optString("alias");
            this.thumb = jsonObject.optString("image");
            this.standard = jsonObject.optString("standard");
            this.property = property;
            this.price = jsonObject.optInt("price");
            this.stock = jsonObject.optInt("stock");
            this.originPrice = jsonObject.optInt("original_price");
            this.dealerTag = jsonObject.optString("dealer_self_support");

//            ShoppingManager manager = ShoppingManager.getInstance();
//            if (manager != null){
//                String key = item_type + "$" + item_id;
//                this.cartNum = manager.getGoodsNum(key);
//            }
        }
    }

    // 每次重新打开模态页都重置
    public void resetUserSkuIds() {
        if (skuBeans == null) {
            skuBeans = new ArrayList<>();
        } else {
            skuBeans.clear();
        }
        if (userSkuIds == null) {
            userSkuIds = new HashSet<>();
        } else {
            userSkuIds.clear();
        }
        userSkuIds.add(this.defaultSkuId);
        int arrayLength = skuArray.length();
        int tagsLength = 0;
        JSONObject skuObj;
        ArrayList<TagBean> tags = null;
        TagBean mTagBean;
        JSONArray tagsArray;
        JSONObject tagObj;
        SkuInfoBean bean;
        Set<Integer> skuIds = null;
        for (int i = 0; i < arrayLength; i++) {
            skuObj = skuArray.optJSONObject(i);
            bean = new SkuInfoBean();
            skuBeans.add(bean);
            String title = skuObj.optString("key");
            tags = new ArrayList<>();
            bean.setTitle(title);
            bean.setTags(tags);
            tagsArray = skuObj.optJSONArray("values");
            tagsLength = tagsArray.length();
            for (int j = 0; j < tagsLength; j++) {
                tagObj = tagsArray.optJSONObject(j);
                String tag = tagObj.optString("standard_value");
                mTagBean = new TagBean();
                mTagBean.tagStr = tag;
                mTagBean.status = Constants.NORMAL;
                skuIds = new HashSet<>();
                mTagBean.setSkuIds(skuIds);
                tags.add(mTagBean);
                JSONArray skuIdJsonArray = tagObj.optJSONArray("values");
                int lengthK = skuIdJsonArray.length();
                for (int k = 0; k < lengthK; k++) {
                    int skuId = skuIdJsonArray.optInt(k);
                    skuIds.add(skuId);
                    if (skuId == this.defaultSkuId){
                        bean.setPreIndex(j);
                        bean.setSkuids(skuIds);
                        mTagBean.status = Constants.SELECTED;
                    }
                }
            }
        }
        setTagEnable();
    }

    public void setTagEnable() {
        int size = skuBeans.size();
        if (size == 1){
            return;
        }
        SkuInfoBean bean;
        for (int i = 0; i < size; i++) {
            Set<Integer> skuIdswithOutCurrens = handleOtherSkuids(i);
            bean = skuBeans.get(i);
            ArrayList<TagBean> tags = bean.getTags();
            for (TagBean tagBean : tags) {
                Set<Integer> newSkuIds = new HashSet<>(tagBean.getSkuIds());
                newSkuIds.retainAll(skuIdswithOutCurrens);
                if (newSkuIds.size() > 0){
                    if (tagBean.status != Constants.SELECTED){
                        tagBean.status = Constants.NORMAL;
                    }
                } else {
                    tagBean.status = Constants.DISABLE;
                }
            }
        }
    }

    private Set<Integer> handleOtherSkuids(int currentSkuIndex){
        ArrayList<SkuInfoBean> skubeans = new ArrayList<SkuInfoBean>();
        int size = skuBeans.size();
        SkuInfoBean bean;
        for (int i = 0; i < size; i++) {
            if (i == currentSkuIndex) {
                continue;
            }
            bean = skuBeans.get(i);
            skubeans.add(bean);
        }
        if (skubeans.size() == 1){
            bean = skubeans.get(0);
            return bean.getSkuids();
        }

        return handleSkuId(skubeans, null, 1);
    }

    private Set<Integer> handleSkuId(ArrayList<SkuInfoBean> skuInfos, Set<Integer> skuIdsPre, int position) {
        if (skuIdsPre == null){
            skuIdsPre = skuInfos.get(position - 1).getSkuids();
        }
        Set<Integer> skuIdsNext = skuInfos.get(position).getSkuids();

        Set<Integer> resultSkuIds = new HashSet<>(skuIdsPre);

        resultSkuIds.retainAll(skuIdsNext);

        if (position == skuInfos.size() - 1){
            return resultSkuIds;
        } else {
            return handleSkuId(skuInfos, resultSkuIds, position + 1);
        }
    }

    public int getDefaultSkuId() {
        return defaultSkuId;
    }

    public void setUserSkuIds(Set<Integer> userSkuIds) {
        this.userSkuIds = userSkuIds;
    }

    public Set<Integer> getAllSkuIds() {
        return allSkuIds;
    }

    public int getItem_id() {
        return item_id;
    }

    public int getRequestDid() {
        return requestDid;
    }

    /**
     * 1. 标准skuItem；2.经销商自建的规格；3.标准组合商品；4.自建组合商品；
     * @return
     */
    public int getItem_type() {
        return item_type;
    }

    public void setCartNum(int cartNum) {
        this.cartNum = cartNum;
    }

    public String getStandard() {
        return standard;
    }

    public String getProperty() {
        return property;
    }

    public String getName() {
        return name;
    }

    public String getAlise() {
        return alias;
    }

    public String getThumb() {
        return thumb;
    }

    public int getPrice() {
        return price;
    }

    public int getOriginPrice() {
        return originPrice;
    }

    public int getStock() {
        return stock;
    }

    public int getCartNum() {
        return cartNum;
    }

    public JSONArray getSkuArray() {
        return skuArray;
    }

    public ArrayList<SkuInfoBean> getSkuBeans() {
        return skuBeans;
    }

    public String getNameWithSku(String skuId) {
        if (nameObject != null){
            return nameObject.optString(skuId);
        }
        return "";
    }

    public String getThumbWithSku(String skuId) {
        if (imagesObject != null){
            return imagesObject.optString(skuId);
        }
        return "";
    }

    public JSONArray getItemsArray(int skuId) {
        if (itemsObject != null){
            JSONArray jsonArray =  itemsObject.optJSONArray(String.valueOf(skuId));
            return jsonArray;
        }
        return null;
    }

    public JSONObject getItemsObject() {
        return itemsObject;
    }

    public int getPrice(JSONObject jsonObject) {
        return jsonObject.optInt("price");
    }

    public int getOriginPrice(JSONObject jsonObject) {
        return jsonObject.optInt("original_price");
    }

    public int getStock(JSONObject jsonObject) {
        return jsonObject.optInt("stock");
    }

    public int getItemId(JSONObject jsonObject) {
        return jsonObject.optInt("item_id");
    }

    public String getItemUnit(JSONObject jsonObject) {
        return jsonObject.optString("unit_name");
    }

    public int getDefault_item_index() {
        return default_item_index;
    }

    public void updateCartNum(){
//        ShoppingManager manager = ShoppingManager.getInstance();
//        if (manager != null){
//            String key = item_type + "$" + item_id;
//            this.cartNum = manager.getGoodsNum(key);
//        }
    }

    public String getDealerTag() {
        return dealerTag;
    }
}
