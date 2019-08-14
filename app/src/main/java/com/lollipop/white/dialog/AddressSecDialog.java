package com.lollipop.white.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.lollipop.demo.R;
import com.lollipop.white.bean.AddressBean;
import com.lollipop.white.http.OKHttpUtils;
import com.lollipop.white.listener.AddressItemCallBack;
import com.lollipop.white.listener.MyResultCallback;
import com.lollipop.white.util.ScreenSizeUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by lollipop on 18/06/18.
 */
public class AddressSecDialog extends Dialog {
    public enum ShowIndex {
        Province, City, Arae
    }
    private View mCreateView;
    public Animation mExitAnim;//退出动画
    public Animation mEnterAnim;//进入动画

    private View proView;
    private ListView mListView;
    private TextView pTv;       // 省
    private TextView cTv;       // 市
    private TextView aTv;       // 区
    private View line0;         // 省市
    private View line1;         // 市县
    private View point0;        // 省
    private View point1;        // 市
    private View point2;        // 区
    private AddressItemAdapter adapter;
    private AddressItemCallBack callback;
    private ArrayList<EnteryBean> provinces;
    private ArrayList<EnteryBean> cities;
    private ArrayList<EnteryBean> araes;
    private ArrayList<EnteryBean> curDatas;
    private int pId;
    private int cId;
    private int aId;
    private AddressBean preBean;
    private ShowIndex mShowIndex = ShowIndex.Province;

    public AddressSecDialog(Activity mActivity) {
        super(mActivity, R.style.loadingDialog);
        init(mActivity);
    }

    public void showWithCallback(AddressBean bean, AddressItemCallBack callback) {
        this.callback = callback;
        this.preBean = bean;
        if (bean.getProId() == 0 || bean.getCityId() == 0 || bean.getAraeId() == 0){
            pTv.setVisibility(View.VISIBLE);
            point0.setVisibility(View.VISIBLE);
            point0.setBackgroundResource(R.drawable.radius_red_hollow_5);
            request(null);
            show();
            return;
        }
        this.pId = bean.getProId();
        this.cId = bean.getCityId();
        this.aId = bean.getAraeId();
        int color = getContext().getResources().getColor(R.color.text_333333);
        pTv.setVisibility(View.VISIBLE);
        cTv.setVisibility(View.VISIBLE);
        aTv.setVisibility(View.VISIBLE);
        line0.setVisibility(View.VISIBLE);
        line1.setVisibility(View.VISIBLE);
        point0.setVisibility(View.VISIBLE);
        point0.setBackgroundResource(R.drawable.radius_red_5);
        point1.setVisibility(View.VISIBLE);
        point1.setBackgroundResource(R.drawable.radius_red_5);
        point2.setVisibility(View.VISIBLE);
        point2.setBackgroundResource(R.drawable.radius_red_5);
        pTv.setText(bean.getProName());
        cTv.setText(bean.getCityName());
        aTv.setText(bean.getAraeName());
        pTv.setTextColor(color);
        cTv.setTextColor(color);
        aTv.setTextColor(color);
        request(String.valueOf(bean.getCityId()));
        mShowIndex = ShowIndex.Arae;
        show();
    }

    private void init(Activity mActivity) {
        mCreateView = LayoutInflater.from(mActivity).inflate(R.layout.dialog_address_select, null);
        setContentView(mCreateView);
        initView();
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager.LayoutParams params=this.getWindow().getAttributes();
        params.width = ScreenSizeUtil.getScreenWidth(mActivity);
        params.height = (int) (ScreenSizeUtil.getScreenHeight() * 0.8);
        params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;;//水平居中、底部
        this.getWindow().setAttributes(params);
    }

    private void initView(){
        mCreateView.findViewById(R.id.iv_close).setOnClickListener(clickListener);
        proView = mCreateView.findViewById(R.id.progressBar);
        mListView = mCreateView.findViewById(R.id.listview);
        mListView.setOnItemClickListener(itemClickListener);
        pTv = mCreateView.findViewById(R.id.tv00);
        cTv = mCreateView.findViewById(R.id.tv01);
        aTv = mCreateView.findViewById(R.id.tv02);
        line0 = mCreateView.findViewById(R.id.line0);
        line1 = mCreateView.findViewById(R.id.line1);
        point0 = mCreateView.findViewById(R.id.point0);
        point1 = mCreateView.findViewById(R.id.point1);
        point2 = mCreateView.findViewById(R.id.point2);
        pTv.setOnClickListener(clickListener);
        cTv.setOnClickListener(clickListener);
        aTv.setOnClickListener(clickListener);
        curDatas = new ArrayList<>();
    }

    private void setAdapter(int poi){
        if (adapter == null){
            adapter = new AddressItemAdapter(getContext());
            adapter.setSecPoi(poi);
            mListView.setAdapter(adapter);
            mListView.smoothScrollToPosition(poi + 3);      // 多移动3个位置，防止在底部
        } else {
            adapter.setSecPoi(poi);
            adapter.notifyDataSetChanged();
            mListView.smoothScrollToPosition(poi + 3);      // 多移动3个位置，防止在底部
        }
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_close:
                    if (callback != null){
                        callback.result(0,0,0,null);
                    }
                    dismiss();
                    break;
                case R.id.tv00:
                {
                    int color = getContext().getResources().getColor(R.color.text_333333);
                    cTv.setTextColor(color);
                    aTv.setTextColor(color);
                    pTv.setTextColor(getContext().getResources().getColor(R.color.text_FF2801));
                    request(null);
                    mShowIndex = ShowIndex.Province;
                }
                    break;
                case R.id.tv01:
                {
                    int color = getContext().getResources().getColor(R.color.text_333333);
                    pTv.setTextColor(color);
                    aTv.setTextColor(color);
                    cTv.setTextColor(getContext().getResources().getColor(R.color.text_FF2801));
                    request(String.valueOf(pId));
                    mShowIndex = ShowIndex.City;
                }
                    break;
                case R.id.tv02:
                {
                    int color = getContext().getResources().getColor(R.color.text_333333);
                    pTv.setTextColor(color);
                    cTv.setTextColor(color);
                    aTv.setTextColor(getContext().getResources().getColor(R.color.text_FF2801));
                    request(String.valueOf(cId));
                    mShowIndex = ShowIndex.Arae;
                }
                    break;
            }
        }
    };

    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            if (proView.getVisibility() == View.VISIBLE){
                return;
            }
            EnteryBean bean = curDatas.get(i);
            switch (mShowIndex){
                case Arae:
                {   // 区
                    point2.setBackgroundResource(R.drawable.radius_red_5);
                    aTv.setText(bean.name);
                    aId = bean.id;
                    aTv.setTextColor(getContext().getResources().getColor(R.color.text_333333));
                    if (callback != null){
                        String p = pTv.getText().toString().trim();
                        String c = cTv.getText().toString().trim();
                        String a = aTv.getText().toString().trim();
                        String detail = null;
                        if (TextUtils.equals(p,c)){
                            detail = p + a;
                        } else {
                            detail = p + c + a;
                        }
                        preBean.setProId(pId);
                        preBean.setCityId(cId);
                        preBean.setAraeId(aId);
                        preBean.setProName(p);
                        preBean.setCityName(c);
                        preBean.setAraeName(a);
                        preBean.setAddress0(detail);
                        callback.result(pId, cId, aId, detail);
                    }
                    dismiss();
                }
                    break;
                case City:
                {    // 市
                    cTv.setText(bean.name);
                    cId = bean.id;
                    cTv.setTextColor(getContext().getResources().getColor(R.color.text_333333));
                    aTv.setTextColor(getContext().getResources().getColor(R.color.text_FF2801));
                    aTv.setVisibility(View.VISIBLE);
                    aTv.setText("请选择区/县");
                    request(String.valueOf(bean.id));
                    mShowIndex = ShowIndex.Arae;
                    line1.setVisibility(View.VISIBLE);
                    point1.setBackgroundResource(R.drawable.radius_red_5);
                    point2.setVisibility(View.VISIBLE);
                    point2.setBackgroundResource(R.drawable.radius_red_hollow_5);
                }
                    break;
                case Province:
                {
                    pTv.setText(bean.name);
                    pId = bean.id;
                    pTv.setTextColor(getContext().getResources().getColor(R.color.text_333333));
                    cTv.setTextColor(getContext().getResources().getColor(R.color.text_FF2801));
                    cTv.setVisibility(View.VISIBLE);
                    cTv.setText("请选择城市");
                    aTv.setVisibility(View.GONE);
                    line1.setVisibility(View.GONE);
                    point2.setVisibility(View.GONE);
                    request(String.valueOf(bean.id));
                    mShowIndex = ShowIndex.City;
                    line0.setVisibility(View.VISIBLE);
                    point0.setBackgroundResource(R.drawable.radius_red_5);
                    point1.setVisibility(View.VISIBLE);
                    point1.setBackgroundResource(R.drawable.radius_red_hollow_5);
                }
                    break;
            }
        }
    };

    private void request(final String parent){
        proView.setVisibility(View.VISIBLE);
        OKHttpUtils.queryArrress(parent, new MyResultCallback<JSONArray>() {

            @Override
            public void onFail(JSONArray response, int error, String msg) {
                proView.setVisibility(View.GONE);
            }

            @Override
            public void onResponse(JSONArray response) {
                proView.setVisibility(View.GONE);

                ArrayList<EnteryBean> enterys = new ArrayList<EnteryBean>();

                switch (mShowIndex){
                    case Arae:
                    {
                        if (araes == null){
                            araes = new ArrayList<EnteryBean>();
                        } else {
                            araes.clear();
                        }
                        int secPoi = handleJsonArray(enterys, aId, response);
                        araes.addAll(enterys);
                        curDatas.clear();
                        curDatas.addAll(araes);
                        setAdapter(secPoi);
                    }
                        break;
                    case City:
                    {
                        if (cities == null){
                            cities = new ArrayList<EnteryBean>();
                        } else {
                            cities.clear();
                        }
                        int secPoi = handleJsonArray(enterys, cId, response);
                        cities.addAll(enterys);
                        curDatas.clear();
                        curDatas.addAll(cities);
                        setAdapter(secPoi);
                    }
                        break;
                    case Province:
                    {
                        if (provinces == null){
                            provinces = new ArrayList<EnteryBean>();
                        } else {
                            provinces.clear();
                        }
                        int secPoi = handleJsonArray(enterys, pId, response);
                        provinces.addAll(enterys);
                        curDatas.clear();
                        curDatas.addAll(provinces);
                        setAdapter(secPoi);
                    }
                        break;
                }
            }
        });
    }

    private int handleJsonArray(ArrayList<EnteryBean> enterys, int secId, JSONArray response){
        if (response == null){
            return -1;
        }
        int length = response.length();
        JSONObject jsonObject = null;
        EnteryBean bean;
        int secPoi = -1;
        for (int i = 0; i < length; i++){
            jsonObject = response.optJSONObject(i);
            bean = new EnteryBean();
            bean.handleJson(jsonObject);
            enterys.add(bean);
            if (secId == bean.id){
                secPoi = i;
            }
        }
        return secPoi;
    }

    private void showAlert(){
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle(getResources().getString(R.string.sys_notice));
//        builder.setMessage(msg);
//        if (!TextUtils.isEmpty(cancel)) {
//            builder.setNegativeButton(cancel, null);
//        }
//        //确定
//        builder.setPositiveButton(ok, listener);
//
//        builder.show();
    }

    private class EnteryBean {
        public int id;        // id
        public String name;      // name

        public void handleJson(JSONObject jsonObject){
            this.id = jsonObject.optInt("id");
            this.name = jsonObject.optString("name");
        }
    }

    private class AddressItemAdapter extends BaseAdapter {

        private Context mCon;
        private int secPoi;

        public AddressItemAdapter(Context mCon){
            this.mCon = mCon;
        }

        public void setSecPoi(int secPoi){
            this.secPoi = secPoi;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public int getCount() {
            if (curDatas != null){
                return curDatas.size();
            }
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            viewHolder holer;
            if (view == null){
                view = LayoutInflater.from(mCon).inflate(R.layout.item_address_sec, null);
                holer = new viewHolder();
                holer.valueTv = view.findViewById(R.id.tv00);
                view.setTag(holer);
            } else {
                holer = (viewHolder)view.getTag();
            }
            final EnteryBean bean = curDatas.get(i);
            holer.valueTv.setText(bean.name);
            if (i == secPoi){
                holer.valueTv.setBackgroundResource(R.color.white);
                holer.valueTv.setTextColor(mCon.getResources().getColor(R.color.text_FF2801));
            } else {
                holer.valueTv.setBackgroundResource(R.color.text_F1F1F1);
                holer.valueTv.setTextColor(mCon.getResources().getColor(R.color.text_333333));
            }
            return view;
        }

        private class viewHolder{
            TextView valueTv;
        }
    }

    /**
     * 进入动画
     */
    private void enterAnimation(){
        if(mEnterAnim==null){
            mEnterAnim=new TranslateAnimation(1, 0, 1, 0, 1, 1, 1, 0);
            mEnterAnim.setDuration(500);
        }
        mCreateView.startAnimation(mEnterAnim);
    }
    /**
     * 退出动画
     */
    private void exitAnimation(){
        if(mExitAnim==null){
            mExitAnim=new TranslateAnimation(1, 0, 1, 0, 1, 0, 1, 1);
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
                            AddressSecDialog.super.dismiss(); //动画完成执行关闭
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
        if (mExitAnim != null){
            return false;
        }
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            dismiss();
        }
        return super.onKeyDown(keyCode, event);
    }
}
