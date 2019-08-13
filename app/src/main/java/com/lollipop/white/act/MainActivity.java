package com.lollipop.white.act;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.lollipop.demo.R;
import com.lollipop.white.bean.AddressBean;
import com.lollipop.white.dialog.AddressSecDialog;
import com.lollipop.white.listener.AddressItemCallBack;

public class MainActivity extends Activity {

    private AddressBean addressBean;
    private Button btn01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        btn01 = findViewById(R.id.btn01);
        btn01.setOnClickListener(clickListener);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn01:
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
                    break;
            }
        }
    };
}
