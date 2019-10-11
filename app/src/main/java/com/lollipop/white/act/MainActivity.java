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
            String json = "{\"code\":1,\"message\":\"\",\"url_msg\":[],\"data\":{\"id\":41848,\"package_id\":788,\"brand_name\":\"我爱夏天\",\"series_name\":\"星座系列\",\"spu_name\":\"\",\"spu_alias\":\"\",\"type\":1,\"sales_entity_id\":2,\"default_item_id\":\"1331\",\"default_item_type\":\"1\",\"title_image\":[\"https://admin.pe.bingtuanwang.net/sites/default/files/styles/bteg_title_image/public/bteg_upload/20180503/pic_a8595eba2120b7222fce7d731b33d528_1636b009d60c496f2c996de483e6cfe1.jpg?itok=dOU9wmqV\"],\"detail_image\":[\"https://admin.pe.bingtuanwang.net/sites/default/files/styles/bteg_title_image/public/bteg_upload/20180503/pic_a8595eba2120b7222fce7d731b33d528_1636b009d60c496f2c996de483e6cfe1.jpg?itok=dOU9wmqV\"],\"standard\":\"85g/支 30支/箱\",\"sku_info\":[{\"key\":\"口味\",\"values\":[{\"standard_value\":\"爱上白羊座香草\",\"values\":[1329]},{\"standard_value\":\"爱上射手座咖啡\",\"values\":[1330]},{\"standard_value\":\"爱上狮子座芒果\",\"values\":[1331]},{\"standard_value\":\"爱上双子座薄荷\",\"values\":[1332]},{\"standard_value\":\"爱上水瓶座香芋\",\"values\":[1333]},{\"standard_value\":\"爱上天秤座草莓\",\"values\":[1334]}]}],\"items\":{\"1332\":[{\"item_id\":\"1332\",\"item_type\":\"1\",\"unit_name\":\"1箱装\",\"stock\":961,\"price\":9900,\"seller_id\":\"2\",\"dealer_self_support\":\"\"}],\"1334\":[{\"item_id\":\"1334\",\"item_type\":\"1\",\"unit_name\":\"1箱装\",\"stock\":99,\"price\":9900,\"seller_id\":\"2\",\"dealer_self_support\":\"\"}],\"1333\":[{\"item_id\":\"1333\",\"item_type\":\"1\",\"unit_name\":\"1箱装\",\"stock\":98,\"price\":9900,\"seller_id\":\"2\",\"dealer_self_support\":\"\"}],\"1329\":[{\"item_id\":\"1329\",\"item_type\":\"1\",\"unit_name\":\"1箱装\",\"stock\":887,\"price\":8800,\"seller_id\":\"2\",\"dealer_self_support\":\"\"}],\"1330\":[{\"item_id\":\"1330\",\"item_type\":\"1\",\"unit_name\":\"1箱装\",\"stock\":762,\"price\":7700,\"seller_id\":\"2\",\"dealer_self_support\":\"\"}],\"1331\":[{\"item_id\":\"1331\",\"item_type\":\"1\",\"unit_name\":\"1箱装\",\"stock\":665,\"price\":6600,\"seller_id\":\"2\",\"dealer_self_support\":\"\"}]},\"image\":{\"1329\":\"https://admin.pe.bingtuanwang.net/sites/default/files/styles/bteg_title_image/public/bteg_upload/20180503/pic_12e7b30b7ec4a42439512bd88c146013_b9e406d8f253b39f81b438c2e788e05e.jpg?itok=k6OdzU2T\",\"1330\":\"https://admin.pe.bingtuanwang.net/sites/default/files/styles/bteg_title_image/public/bteg_upload/20180503/pic_a8595eba2120b7222fce7d731b33d528_1636b009d60c496f2c996de483e6cfe1.jpg?itok=dOU9wmqV\",\"1331\":\"https://admin.pe.bingtuanwang.net/sites/default/files/styles/bteg_title_image/public/bteg_upload/20180503/pic_889ee87dccba8d06016c19bc49a92e41_76cc15be03088829abf1233c2222a3c3.jpg?itok=n__H5sFl\",\"1332\":\"https://admin.pe.bingtuanwang.net/sites/default/files/styles/bteg_title_image/public/bteg_upload/20180503/pic_2529ad0cc261aab893fd65931d9f0f9c_c709ba46b9246527d850a5be668044a9.jpg?itok=5ld6a_bm\",\"1333\":\"https://admin.pe.bingtuanwang.net/sites/default/files/styles/bteg_title_image/public/bteg_upload/20180503/pic_1e5fd4ffae0015fd0604ede54be16b8d_a78ef277ac778d44e1aee7f8fc81ab5a.jpg?itok=sB_oRU2K\",\"1334\":\"https://admin.pe.bingtuanwang.net/sites/default/files/styles/bteg_title_image/public/bteg_upload/20180503/pic_89411e6f61fe9ce65b4388b7cbcaf0d0_e40b57695795336fa7d9a9094d8f3c3c.jpg?itok=uHxAbPvz\"},\"name\":{\"1329\":\"我爱夏天星座系列爱上白羊座香草\",\"1330\":\"我爱夏天星座系列爱上射手座咖啡\",\"1331\":\"我爱夏天星座系列爱上狮子座芒果\",\"1332\":\"我爱夏天星座系列爱上双子座薄荷\",\"1333\":\"我爱夏天星座系列爱上水瓶座香芋\",\"1334\":\"我爱夏天星座系列爱上天秤座草莓\"},\"alias\":{\"1329\":\"\",\"1330\":\"\",\"1331\":\"\",\"1332\":\"\",\"1333\":\"\",\"1334\":\"\"},\"suggested_price\":{\"1332\":\"\",\"1334\":\"\",\"1333\":\"\",\"1329\":\"\",\"1330\":\"\",\"1331\":\"\"},\"dealer_self_support\":\"\",\"payoffline\":1}}";
            mGoodsSpuBean = new GoodsSpuBean();
            try {
                JSONObject jsonObject = new JSONObject(json);
                mGoodsSpuBean.handleData(jsonObject.optJSONObject("data"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        SkuSelectDialog dialog = new SkuSelectDialog(this);
        dialog.showWithSpu(mGoodsSpuBean);
    }
}
