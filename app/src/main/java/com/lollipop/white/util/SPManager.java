package com.lollipop.white.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.lollipop.white.base.BaseApplication;

/**
 * 轻量级缓存
 * Created by lollipop on 15/11/17.
 */
public class SPManager {
    private final String APPNAME = "lollipop";
    private SharedPreferences shp;

    private static SPManager sharedp;

    // 新版本之后
    public final String USERTOKEN = "userToken";   // 每次请求的token
    private String userToken;       // token缓存
    private String userAccount;     // 手机号缓存

    private SPManager(Context context) {
        super();
        shp = context.getSharedPreferences(APPNAME, Context.MODE_PRIVATE);
    }

    public static SPManager getInstance(Context context) {
        if (sharedp == null) {
            sharedp = new SPManager(context);
        }
        return sharedp;
    }

    public static SPManager getInstance() {
        if (sharedp == null) {
            sharedp = new SPManager(BaseApplication.getInstance());
        }
        return sharedp;
    }

    public void saveLoginData(String token,String name, String account) {
        this.userToken = token;
        this.userAccount = account;
        SharedPreferences.Editor edit = shp.edit();
        edit.putString(USERTOKEN, token);
        edit.commit();
    }

    public String getUserToken() {
        if (TextUtils.isEmpty(userToken)){
            userToken = shp.getString(USERTOKEN,null);
        }
        return userToken;
    }

    public void userLogout() {
        userToken = null;
        SharedPreferences.Editor edit = shp.edit();
        edit.putString(USERTOKEN, null);
        edit.commit();
    }
}