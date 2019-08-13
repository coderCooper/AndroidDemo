package com.lollipop.white.act;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import org.json.JSONObject;

import com.lollipop.white.base.BaseActivity;
import com.lollipop.demo.R;
import com.lollipop.white.http.OKHttpUtils;
import com.lollipop.white.listener.MyResultCallback;
import com.lollipop.white.util.SPManager;
import com.lollipop.white.util.ToastUtil;

/**
 * Created by lollipop on 2019/3/31.
 */

public class LoginActivity extends BaseActivity {

    private EditText phoneEdit;
    private EditText passwordEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_login);
        phoneEdit = findViewById(R.id.edit01);
        passwordEdit = findViewById(R.id.edit02);
        findViewById(R.id.btn).setOnClickListener(clickListener);
    }

    private void login(){
        final String phoneStr = phoneEdit.getText().toString().trim();
        if (TextUtils.isEmpty(phoneStr)){
            ToastUtil.showShortText("请输入您的登录账号");
            return;
        }

        String passStr = passwordEdit.getText().toString().trim();
        if (TextUtils.isEmpty(passStr)){
            ToastUtil.showShortText("请输入您的登录密码");
            return;
        }
        OKHttpUtils.login(phoneStr, passStr, new MyResultCallback<JSONObject>(this,"登录中...") {

            @Override
            public void onFail(JSONObject response, int error, String msg) {
                showAlert(msg, "我知道了", null, null);
            }

            @Override
            public void onResponse(JSONObject response) {
                String token = response.optString("token");
                SPManager.getInstance(LoginActivity.this).saveLoginData(token,"name",phoneStr);
                toMainPage();
            }
        });
    }

    private void toMainPage(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private View.OnClickListener clickListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn:
                    login();
                    break;
            }
        }
    };
}
