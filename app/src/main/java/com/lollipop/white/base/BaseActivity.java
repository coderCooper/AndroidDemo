package com.lollipop.white.base;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.lollipop.white.util.AppManager;

/**
 * 基类
 * Created by lollipop on 2019/3/31.
 */

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        AppManager.getAppManager().remove(this);
        super.onDestroy();
    }

    protected void showAlert(String msg, String ok, String cancel, DialogInterface.OnClickListener listener){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("系统提示");
        builder.setMessage(msg);
        if (!TextUtils.isEmpty(cancel)) {
            builder.setNegativeButton(cancel, null);
        }
        //确定
        builder.setPositiveButton(ok, listener);

        builder.show();
    }
}
