package com.lollipop.white.act;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.lollipop.demo.R;
import com.lollipop.white.bean.AddressBean;
import com.lollipop.white.bean.GoodsSpuBean;
import com.lollipop.white.dialog.AddressSecDialog;
import com.lollipop.white.dialog.SkuSelectDialog;
import com.lollipop.white.listener.AddressItemCallBack;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends Activity {

    private AddressBean addressBean;
    private Button btn01;

    private GoodsSpuBean mGoodsSpuBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        btn01 = findViewById(R.id.btn01);
        btn01.setOnClickListener(clickListener);
        findViewById(R.id.btn02).setOnClickListener(clickListener);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn01:
                    showAddressSec();
                    break;
                case R.id.btn02:
                    showSpuDialog();
                    break;
            }
        }
    };

    private void showAddressSec(){
        if (addressBean == null){
            addressBean = new AddressBean();
        }
        AddressSecDialog dialog = new AddressSecDialog(MainActivity.this);
        dialog.showWithCallback(addressBean, new AddressItemCallBack() {

            @Override
            public void result(int pId, int cId, int aId, String value) {
                btn01.setText(addressBean.getAddress0());
            }
        });
    }

    private void showSpuDialog(){
        if (mGoodsSpuBean == null){
            String json = "{\"id\":10086,\"package_id\":128,\"brand_name\":\"三全\",\"series_name\":\"千丝抓饼\",\"spu_name\":\"\",\"spu_alias\":\"\",\"type\":1,\"sales_entity_id\":2,\"default_item_id\":\"409\",\"default_item_type\":\"1\",\"standard\":\"320g/袋\",\"sku_info\":[{\"key\":\"口味\",\"values\":[{\"standard_value\":\"葱香味\",\"values\":[409]},{\"standard_value\":\"辣酱\",\"values\":[410]},{\"standard_value\":\"原味\",\"values\":[411]}]}],\"items\":{\"409\":[{\"item_id\":\"409\",\"item_type\":\"1\",\"unit_name\":\"1袋装\",\"stock\":1084,\"price\":700,\"seller_id\":\"2\"}],\"410\":[{\"item_id\":\"410\",\"item_type\":\"1\",\"unit_name\":\"1袋装\",\"stock\":1302,\"price\":700,\"seller_id\":\"2\"}],\"411\":[{\"item_id\":\"411\",\"item_type\":\"1\",\"unit_name\":\"1袋装\",\"stock\":1095,\"price\":700,\"seller_id\":\"2\"}]},\"image\":{\"409\":\"xxx409.jpg\",\"410\":\"xxx410.jpg\",\"411\":\"xxx411.jpg\"},\"name\":{\"409\":\"三全千丝抓饼320g葱香味\",\"410\":\"三全千丝抓饼320g辣酱\",\"411\":\"三全千丝抓饼320g原味\"},\"alias\":{\"409\":\"\",\"410\":\"\",\"411\":\"\"}}";
            mGoodsSpuBean = new GoodsSpuBean();
            try {
                JSONObject jsonObject = new JSONObject(json);
                mGoodsSpuBean.handleData(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        SkuSelectDialog dialog = new SkuSelectDialog(this);
        dialog.showWithSpu(mGoodsSpuBean);
    }
}
