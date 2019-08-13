package com.lollipop.white.act;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.lollipop.demo.R;
import com.lollipop.white.base.BaseActivity;
import com.lollipop.white.util.SPManager;

public class LanuchActivity extends BaseActivity {

    private final int NEXTSTEPT = 100;

    private Handler myHandler = new Handler(){

        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            if (msg.what == NEXTSTEPT) {
                checkLogin();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_lanuch);
        myHandler.sendEmptyMessageDelayed(NEXTSTEPT,111);
    }

    private void checkLogin(){
        Context mContext = LanuchActivity.this;
        SPManager spManager = SPManager.getInstance(mContext);
        String token = spManager.getUserToken();

//        if (TextUtils.isEmpty(token)) {
//            startActivity(new Intent(mContext, LoginActivity.class));
//        } else {
            Intent intent = new Intent(mContext, MainActivity.class);
            startActivity(intent);
//        }
        finish();
    }
}
