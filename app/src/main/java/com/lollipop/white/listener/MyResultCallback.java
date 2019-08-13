package com.lollipop.white.listener;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.lollipop.white.dialog.LoadingDialog;
import com.lollipop.demo.R;

/**
 * Created by xieshengqi on 15/11/12.
 */
public abstract class MyResultCallback<T> {

    public Type mType;
    private boolean isShowDialog;
    private Context context;
    private LoadingDialog dialog;

    static Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return parameterized.getActualTypeArguments()[0];
    }

    public MyResultCallback() {
        this.isShowDialog = false;
        mType = getSuperclassTypeParameter(getClass());
    }

    public MyResultCallback(Context context, boolean isShowDialog) {
        this(context,"加载中...",isShowDialog,false);
    }

    public MyResultCallback(Context context, String hintString) {
        this(context,hintString,true,false);
    }

    public MyResultCallback(Context context, String hintString, boolean isShowDialog) {
        this(context,hintString,isShowDialog,false);
    }

    public MyResultCallback(Context context, String hintString, boolean isShowDialog, boolean isTouch) {
        this.isShowDialog = isShowDialog;
        this.context = context;
        mType = getSuperclassTypeParameter(getClass());
        initDialog();
        if (dialog != null){
            if (!TextUtils.isEmpty(hintString)){
                dialog.setHintText(hintString);
            }
            if (isTouch) {
                dialog.setCanceledOnTouchOutside(false);
            }
        }
    }

    private void initDialog() {
        if (isShowDialog) {
            dialog = new LoadingDialog(context, R.style.loadingDialog);
        }
    }

    public void onBefore() {
        if (isShowDialog && dialog != null && !dialog.isShowing()) {
            dialog.show();
        }
    }

    public void onAfter() {
        if (context == null){
            return;
        }
        if (context instanceof Activity){
            Activity act = (Activity)context;
            if (act.isFinishing()){
                return;
            }
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR1 && act.isDestroyed()){
                return;
            }
        }
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    // 失败了回调
    public abstract void onFail(T response, int error, String msg);

    // 成功了回调
    public abstract void onResponse(T response);
}
