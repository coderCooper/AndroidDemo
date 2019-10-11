package com.lollipop.white.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lollipop.demo.R;
import com.lollipop.white.act.adapter.TagAdapter;
import com.lollipop.white.bean.GoodsSpuBean;
import com.lollipop.white.bean.SkuInfoBean;
import com.lollipop.white.bean.TagBean;
import com.lollipop.white.listener.IntsResultListener;
import com.lollipop.white.listener.OnTagClickListener;
import com.lollipop.white.util.AppUtils;
import com.lollipop.white.util.Constants;
import com.lollipop.white.util.KbLog;
import com.lollipop.white.util.ScreenSizeUtil;
import com.lollipop.white.util.ToastUtil;
import com.lollipop.white.view.FlowTagLayout;
import com.lollipop.white.view.GoodsNumEditLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by lollipop on 18/06/18.
 */
public class SkuSelectDialog extends Dialog {
    private View mCreateView;
    public Animation mExitAnim;//退出动画
    public Animation mEnterAnim;//进入动画
    private ArrayList<FlowTagLayout> mFlowTagLayouts;
    private ArrayList<TextView> mFlowTitles;
    private ArrayList<View> mFlowLines;
    private View rl_content;
    private ImageView goodsImage;
    private TextView goodsNameTV;
    private TextView goodsAliseTV;
    private TextView goodsPriceTV;
    private TextView goodsOriginPriceTV;
    private TextView stockTV;
    private TextView standardTV;
    private Button btnOK;
    private GoodsNumEditLayout mNumLayout;
    private GoodsSpuBean goodsBean;
    private FlowTagLayout mPackageTagLayout;
    private int secSkuId;
    private int itemIndex;
    private int currentStock;
    private ArrayList<TagBean> mPackageTags;
    private IntsResultListener listener;

    public SkuSelectDialog(Context mActivity) {
        super(mActivity, R.style.loadingDialog);
        init(mActivity);
    }

    public void addListener(IntsResultListener listener) {
        this.listener = listener;
    }

    public void showWithSpu(GoodsSpuBean goodsBean) {

        if (goodsBean.getItemsObject() == null || goodsBean.getItemsObject().length() == 0) {
            dismiss();
            ToastUtil.showShortText("数据错误");
            return;
        }

        this.goodsBean = goodsBean;

        this.goodsBean.resetUserSkuIds();

        this.itemIndex = goodsBean.getDefault_item_index();

        AppUtils.imageShow(getContext(), goodsBean.getThumb(), goodsImage);

        goodsNameTV.setText(goodsBean.getName());

        if (TextUtils.isEmpty(goodsBean.getAlise())){
            goodsAliseTV.setVisibility(View.GONE);
        } else {
            goodsAliseTV.setVisibility(View.VISIBLE);
            goodsAliseTV.setText(goodsBean.getAlise());
        }

        int price = goodsBean.getPrice();
        int originPrice = goodsBean.getOriginPrice();

        goodsPriceTV.setText("¥" + price / 100.00);

        if (originPrice > price){
            goodsOriginPriceTV.setText("¥" + originPrice / 100.00);
        } else {
            goodsOriginPriceTV.setText(null);
        }

        currentStock = goodsBean.getStock();

        stockTV.setText("库存：" + currentStock);

        standardTV.setText(goodsBean.getStandard());

        mNumLayout.setMaxNum(goodsBean.getStock());

        int arrayLength = goodsBean.getSkuBeans().size();

        SkuInfoBean bean;

        if (arrayLength > mFlowTitles.size()){
            arrayLength = mFlowTitles.size();
        }

        for (int i = 0; i < arrayLength; i++) {
            bean = goodsBean.getSkuBeans().get(i);
            TextView mTitle = mFlowTitles.get(i);
            if (mTitle.getVisibility() != View.VISIBLE) {
                mTitle.setVisibility(View.VISIBLE);
            }
            mTitle.setText(bean.getTitle());

            View mLine = mFlowLines.get(i);
            if (mLine.getVisibility() != View.VISIBLE) {
                mLine.setVisibility(View.VISIBLE);
            }

            FlowTagLayout mFlowTagLayout = mFlowTagLayouts.get(i);
            if (mFlowTagLayout.getVisibility() != View.VISIBLE) {
                mFlowTagLayout.setVisibility(View.VISIBLE);
            }
            TagAdapter adapter = null;
            if (mFlowTagLayout.getAdapter() == null) {
                adapter = new TagAdapter(getContext());
                mFlowTagLayout.setAdapter(adapter);
            } else {
                adapter = (TagAdapter) mFlowTagLayout.getAdapter();
            }
            adapter.onlyAddAll(bean.getTags());
        }
        int secSkuId = this.goodsBean.getDefaultSkuId();
        changePackageTag(secSkuId);
        show();
    }

    private void init(Context mActivity) {
        mCreateView = LayoutInflater.from(mActivity).inflate(R.layout.dialog_sku_select, null);
        mCreateView.findViewById(R.id.iv_close).setOnClickListener(clickListener);
        btnOK = mCreateView.findViewById(R.id.btn_ok);
        btnOK.setOnClickListener(clickListener);
        setContentView(mCreateView);
        initView();
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager.LayoutParams params = this.getWindow().getAttributes();
        params.width = ScreenSizeUtil.getScreenWidth(mActivity);
        params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
        ;//水平居中、底部
        this.getWindow().setAttributes(params);
    }

    private void initView() {
        rl_content = findViewById(R.id.rl_content);
        goodsImage = (ImageView) findViewById(R.id.img0);
        goodsNameTV = (TextView) findViewById(R.id.tv00);
        goodsAliseTV = (TextView) findViewById(R.id.tv01);
        goodsPriceTV = (TextView) findViewById(R.id.tv02);
        goodsOriginPriceTV = (TextView) findViewById(R.id.tv05);
        goodsOriginPriceTV.setText(null);
        goodsOriginPriceTV.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        stockTV = (TextView) findViewById(R.id.tv03);
        standardTV = (TextView) findViewById(R.id.tv04);
        if (mFlowTagLayouts == null) {
            mFlowTagLayouts = new ArrayList<>();
        } else {
            mFlowTagLayouts.clear();
        }
        if (mFlowTitles == null) {
            mFlowTitles = new ArrayList<>();
        } else {
            mFlowTitles.clear();
        }
        if (mFlowLines == null) {
            mFlowLines = new ArrayList<>();
        } else {
            mFlowLines.clear();
        }
        mPackageTagLayout = (FlowTagLayout) findViewById(R.id.layout);
        mPackageTagLayout.setOnTagClickListener(tagClickListener);

        FlowTagLayout mFlowTagLayout = (FlowTagLayout) findViewById(R.id.layout0);
        mFlowTagLayout.setOnTagClickListener(tagClickListener);
        mFlowTagLayouts.add(mFlowTagLayout);

        mFlowTagLayout = (FlowTagLayout) findViewById(R.id.layout1);
        mFlowTagLayout.setOnTagClickListener(tagClickListener);
        mFlowTagLayouts.add(mFlowTagLayout);

        mFlowTagLayout = (FlowTagLayout) findViewById(R.id.layout2);
        mFlowTagLayout.setOnTagClickListener(tagClickListener);
        mFlowTagLayouts.add(mFlowTagLayout);

        mFlowTagLayout = (FlowTagLayout) findViewById(R.id.layout3);
        mFlowTagLayout.setOnTagClickListener(tagClickListener);
        mFlowTagLayouts.add(mFlowTagLayout);

        TextView title = (TextView) findViewById(R.id.text0);
        mFlowTitles.add(title);
        title = (TextView) findViewById(R.id.text1);
        mFlowTitles.add(title);
        title = (TextView) findViewById(R.id.text2);
        mFlowTitles.add(title);
        title = (TextView) findViewById(R.id.text3);
        mFlowTitles.add(title);

        View line = findViewById(R.id.view0);
        mFlowLines.add(line);

        line = findViewById(R.id.view1);
        mFlowLines.add(line);

        line = findViewById(R.id.view2);
        mFlowLines.add(line);

        line = findViewById(R.id.view3);
        mFlowLines.add(line);

        mNumLayout = findViewById(R.id.view);
    }

    private void packageTap(int poi) {
        if (poi == itemIndex) {
            return;
        }
        TagBean tagBean = mPackageTags.get(poi);
        if (tagBean.status == Constants.DISABLE) {
            return;
        }
        TagBean pretagBean = mPackageTags.get(itemIndex);
        if (pretagBean.status == Constants.SELECTED) {
            pretagBean.status = Constants.NORMAL;
        }
        itemIndex = poi;
        tagBean.status = Constants.SELECTED;
        TagAdapter adapter = (TagAdapter) mPackageTagLayout.getAdapter();
        adapter.notifyDataSetChanged();

        JSONArray jsonArray = goodsBean.getItemsArray(secSkuId);
        if (jsonArray == null) {
            ToastUtil.showShortText("数据缺失");
            return;
        }
        JSONObject jsonObject = null;
        if (this.itemIndex >= jsonArray.length()) {
            this.itemIndex = jsonArray.length() - 1;
        }
        jsonObject = jsonArray.optJSONObject(this.itemIndex);

        int price = goodsBean.getPrice(jsonObject);
        int originPrice = goodsBean.getOriginPrice(jsonObject);

        goodsPriceTV.setText("¥" + price / 100.00);

        if (originPrice > price){
            goodsOriginPriceTV.setText("¥" + originPrice / 100.00);
        } else {
            goodsOriginPriceTV.setText(null);
        }

        currentStock = goodsBean.getStock(jsonObject);

        stockTV.setText("库存：" + currentStock);

        mNumLayout.setMaxNum(goodsBean.getStock(jsonObject));
    }

    private OnTagClickListener tagClickListener = new OnTagClickListener() {

        @Override
        public void onItemClick(FlowTagLayout parent, View view, int position) {
            int ignorePoi = -1;
            switch (parent.getId()) {
                case R.id.layout:
                    packageTap(position);
                    return;
                case R.id.layout0:
                    ignorePoi = 0;
                    break;
                case R.id.layout1:
                    ignorePoi = 1;
                    break;
                case R.id.layout2:
                    ignorePoi = 2;
                    break;
                case R.id.layout3:
                    ignorePoi = 3;
                    break;
            }
            if (ignorePoi == -1) {
                return;
            }
            SkuInfoBean skuInfoBean = goodsBean.getSkuBeans().get(ignorePoi);
            if (skuInfoBean == null) {
                return;
            }
            ArrayList<TagBean> tags = skuInfoBean.getTags();
            TagBean tagBean0 = tags.get(position);

            if (tagBean0.status == Constants.DISABLE) {
                return;
            }
            int preIndex = skuInfoBean.getPreIndex();
            if (preIndex > -1 && preIndex != position) { // 取消选中就下面代码处理
                TagBean tagBean = tags.get(preIndex);
                if (tagBean.status == Constants.SELECTED) {
                    tagBean.status = Constants.NORMAL;
                }
            }
            skuInfoBean.setPreIndex(position);
            if (tagBean0.status == Constants.NORMAL) {
                tagBean0.status = Constants.SELECTED;
                // 求交集
                skuInfoBean.setSkuids(tagBean0.getSkuIds());
            } else if (tagBean0.status == Constants.SELECTED) {
                tagBean0.status = Constants.NORMAL;
                skuInfoBean.setSkuids(goodsBean.getAllSkuIds());
                skuInfoBean.setPreIndex(-1);
            }
            Set<Integer> skuids = handleSecSkuid();
            KbLog.e("aa" + skuids);
            if (skuids != null) {
                goodsBean.setUserSkuIds(skuids);
                goodsBean.setTagEnable();
            }
            if (mFlowTagLayouts != null) {
                int arrayLength = goodsBean.getSkuBeans().size();
                for (int i = 0; i < arrayLength; i++) {
                    FlowTagLayout mFlowTagLayout = mFlowTagLayouts.get(i);
                    mFlowTagLayout.reloadData();
                }
            }
        }
    };

    private Set<Integer> handleSecSkuid() {
        ArrayList<SkuInfoBean> skuInfoBeans = goodsBean.getSkuBeans();
        SkuInfoBean bean;
        Set<Integer> mixedSkuIds = null;
        if (skuInfoBeans.size() == 1) {
            bean = skuInfoBeans.get(0);
            mixedSkuIds = bean.getSkuids();
        } else {
            mixedSkuIds = handleSkuId(skuInfoBeans, null, 1);
        }
        int secSkuId = 0;
        if (mixedSkuIds == null || mixedSkuIds.size() == 0) {
            ToastUtil.showShortText("数据有错误");
            return null;
        } else {
            secSkuId = (int) mixedSkuIds.toArray()[0];
        }

        JSONArray jsonArray = goodsBean.getItemsArray(secSkuId);
        if (jsonArray == null) {
            ToastUtil.showShortText("数据缺失");
            return mixedSkuIds;
        }
        JSONObject jsonObject = null;
        if (this.itemIndex >= jsonArray.length()) {
            this.itemIndex = jsonArray.length() - 1;
        }
        jsonObject = jsonArray.optJSONObject(this.itemIndex);

        changePackageTag(secSkuId);
        // 修改价格展示和sku信息展示
        String secSkuIdStr = String.valueOf(secSkuId);
        int price = goodsBean.getPrice(jsonObject);
        int originPrice = goodsBean.getOriginPrice(jsonObject);
        goodsPriceTV.setText("¥" + price / 100.00);
        if (originPrice > price){
            goodsOriginPriceTV.setText("¥" + originPrice / 100.00);
        } else {
            goodsOriginPriceTV.setText(null);
        }
        currentStock = goodsBean.getStock(jsonObject);
        stockTV.setText("库存：" + currentStock);
        mNumLayout.setMaxNum(goodsBean.getStock(jsonObject));
        AppUtils.imageShow(getContext(), goodsBean.getThumbWithSku(secSkuIdStr), goodsImage);
        goodsNameTV.setText(goodsBean.getNameWithSku(secSkuIdStr));

        return mixedSkuIds;
    }

    private void changePackageTag(int secSkuId) {
        this.secSkuId = secSkuId;
        TagAdapter adapter = null;
        if (mPackageTagLayout.getAdapter() == null) {
            adapter = new TagAdapter(getContext());
            mPackageTagLayout.setAdapter(adapter);
        } else {
            adapter = (TagAdapter) mPackageTagLayout.getAdapter();
        }
        JSONArray jsonArray = goodsBean.getItemsArray(secSkuId);
        if (jsonArray == null) {
            ToastUtil.showShortText("数据缺失");
            return;
        }
        JSONObject jsonObject;
        int jsonSize = jsonArray.length();
        if (mPackageTags == null) {
            mPackageTags = new ArrayList<TagBean>();
        } else {
            mPackageTags.clear();
        }
        TagBean tagBean;
        for (int i = 0; i < jsonSize; i++) {
            jsonObject = jsonArray.optJSONObject(i);
            tagBean = new TagBean();
            tagBean.tagStr = jsonObject.optString("unit_name");
            mPackageTags.add(tagBean);
            if (0 == jsonObject.optInt("stock")) {
                tagBean.status = Constants.DISABLE;
            } else {
                if (i == itemIndex) {
                    tagBean.status = Constants.SELECTED;
                }
            }
        }
        adapter.clearAndAddAll(mPackageTags);
    }

    /**
     * @param skuIdsPre
     * @param position  必须从1开始
     * @return
     */
    private Set<Integer> handleSkuId(ArrayList<SkuInfoBean> skuInfos, Set<Integer> skuIdsPre, int position) {
        if (skuIdsPre == null) {
            skuIdsPre = skuInfos.get(position - 1).getSkuids();
        }
        Set<Integer> skuIdsNext = skuInfos.get(position).getSkuids();

        Set<Integer> resultSkuIds = new HashSet<>(skuIdsPre);

        resultSkuIds.retainAll(skuIdsNext);

        if (position == skuInfos.size() - 1) {
            return resultSkuIds;
        } else {
            return handleSkuId(skuInfos, resultSkuIds, position + 1);
        }
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_close:
                    dismiss();
                    break;
                case R.id.btn_ok:
                    addToCard();
                    break;
            }
        }
    };

    private void addToCard() {

        boolean packageSec = false;
        for (TagBean tagBean : mPackageTags) {
            if (tagBean.status == Constants.SELECTED) {
                packageSec = true;
            }
        }
        if (!packageSec) {
            if (currentStock == 0){
                ToastUtil.showShortText("该商品库存为0");
            } else {
                ToastUtil.showShortText("请选择包装方式");
            }
            return;
        }
        // 先检查是否有项目没填写
        int arrayLength = goodsBean.getSkuBeans().size();

        SkuInfoBean bean;

        for (int i = 0; i < arrayLength; i++) {
            bean = goodsBean.getSkuBeans().get(i);
            if (bean.getPreIndex() < 0) {
                ToastUtil.showShortText(String.format("请选择%s", bean.getTitle()));
                return;
            }
        }
        ArrayList<SkuInfoBean> skuInfoBeans = goodsBean.getSkuBeans();
        Set<Integer> mixedSkuIds = null;
        if (skuInfoBeans.size() == 1) {
            bean = skuInfoBeans.get(0);
            mixedSkuIds = bean.getSkuids();
        } else {
            mixedSkuIds = handleSkuId(skuInfoBeans, null, 1);
        }
        int secSkuId = 0;
        if (mixedSkuIds == null || mixedSkuIds.size() == 0) {
            ToastUtil.showShortText("数据有错误");
            return;
        } else {
            secSkuId = (int) mixedSkuIds.toArray()[0];
        }

        JSONArray jsonArray = goodsBean.getItemsArray(secSkuId);
        if (jsonArray == null) {
            ToastUtil.showShortText("数据缺失");
            return;
        }
        JSONObject jsonObject = null;
        if (this.itemIndex >= jsonArray.length()) {
            this.itemIndex = jsonArray.length() - 1;
        }
        jsonObject = jsonArray.optJSONObject(this.itemIndex);
        final int itemId = jsonObject.optInt("item_id");
        final int itemType = jsonObject.optInt("item_type");
        int num = mNumLayout.getCurrentNum();

        // 如果listener 不为空，则调用者需要执行添加到购物车的行为
        if (listener != null) {
            listener.result(itemId, itemType, num);

            // 产品sisi需求，不隐藏，20190321
//            dismiss();
        } else {
//            ShoppingManager.getInstance(getContext()).addGoods(itemType, itemId, num, new MyResultCallback<JSONObject>() {
//
//                @Override
//                public void onFail(JSONObject response, int status, String msg) {
//                    ToastUtil.showShortText(msg);
//                }
//
//                @Override
//                public void onResponse(JSONObject response) {
//                    ToastUtil.showShortText("在购物车等你哟");
//                    // 产品sisi需求，不隐藏，20190321
//                    // dismiss();
//                }
//            });
        }
    }

    /**
     * 进入动画
     */
    private void enterAnimation() {
        if (mEnterAnim == null) {
            mEnterAnim = new TranslateAnimation(1, 0, 1, 0, 1, 1, 1, 0);
            mEnterAnim.setDuration(500);
        }
        mCreateView.startAnimation(mEnterAnim);
    }

    /**
     * 退出动画
     */
    private void exitAnimation() {
        if (mExitAnim == null) {
            mExitAnim = new TranslateAnimation(1, 0, 1, 0, 1, 0, 1, 1);
            mExitAnim.setDuration(500);
            mExitAnim.setAnimationListener(
                    new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            SkuSelectDialog.super.dismiss(); //动画完成执行关闭
                        }
                    });
        }
        mCreateView.startAnimation(mExitAnim);
    }

    /**
     * 执行动画
     */
    @Override
    public void dismiss() {
        exitAnimation();
    }

    @Override
    public void show() {
        super.show();
        enterAnimation();//进入动画
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            dismiss();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void AddCartAnimation() {
        //计算动画开始/结束点的坐标的准备工作
        //得到父布局的起始点坐标（用于辅助计算动画开始/结束时的点的坐标）
        int[] parentLoc = new int[2];
        mCreateView.getLocationInWindow(parentLoc);

        //得到商品图片的坐标（用于计算动画开始的坐标）
        int startLoc[] = new int[2];
        goodsImage.getLocationInWindow(startLoc);

        //购物车控件的坐标(用于计算动画结束后的坐标)
        int endLoc[] = new int[2];
        btnOK.getLocationInWindow(endLoc);

        float startX = startLoc[0] - parentLoc[0] + goodsImage.getWidth() / 2;
        float startY = startLoc[1] - parentLoc[1] + goodsImage.getHeight() / 2;

        //商品掉落后的终点坐标：购物车起始点-父布局起始点+购物车图片的1/5
        float toX = endLoc[0] - parentLoc[0] + btnOK.getWidth() / 2;
        float toY = endLoc[1] - parentLoc[1] + btnOK.getHeight() * 2 / 5;

        //透明度和缩放动画，动画持续时间和动画透明度可以自己根据想要的效果调整
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(280);

        ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 0.1f, 1f, 0.1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(300);

        //平移动画X轴 计算X轴要平移的距离，设置动画的偏移时间由透明度和缩放动画持续时间决定
        TranslateAnimation translateAnimationX = new TranslateAnimation(0,
                toX - startX, 0, 0);
        translateAnimationX.setStartOffset(300);
        translateAnimationX.setDuration(700);
        //设置线性插值器
        translateAnimationX.setInterpolator(new LinearInterpolator());

        //平移动画Y轴 同X轴
        TranslateAnimation translateAnimationY = new TranslateAnimation(0, 0,
                0, toY - startY);
        translateAnimationY.setStartOffset(300);
        translateAnimationY.setDuration(700);
        //设置加速插值器
        translateAnimationY.setInterpolator(new AccelerateInterpolator());


        //动画集合
        final AnimationSet set = new AnimationSet(false);
        set.addAnimation(alphaAnimation);
        set.addAnimation(scaleAnimation);
        set.addAnimation(translateAnimationX);
        set.addAnimation(translateAnimationY);
        set.setAnimationListener(new Animation.AnimationListener() {
            @Override


            public void onAnimationStart(Animation animation) {
                goodsImage.setVisibility(View.VISIBLE);
            }

            @Override


            public void onAnimationEnd(Animation animation) {
                //动画执行完成
                goodsImage.setVisibility(View.INVISIBLE);
                goodsImage.clearAnimation();
                set.cancel();
                animation.cancel();
            }

            @Override


            public void onAnimationRepeat(Animation animation) {

            }

        });
        //设置动画播放完以后消失，终止填充
        set.setFillAfter(false);
        goodsImage.startAnimation(set);
    }
}
