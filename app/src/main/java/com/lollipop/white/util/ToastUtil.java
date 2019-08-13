package com.lollipop.white.util;

import android.text.TextUtils;
import android.widget.Toast;

import com.lollipop.white.base.BaseApplication;

/**
 * Created by xieshengqi on 15/11/17.
 */
public class ToastUtil {
    private static Toast mToast;

    public static void showLongText(String content) {
        if (TextUtils.isEmpty(content)) {
            return;
        }
        if (mToast == null) {
            mToast = Toast.makeText(BaseApplication.getInstance(), content, Toast.LENGTH_LONG);
        } else {
            mToast.setText(content);
        }
        mToast.show();
    }
    public static void showShortText(String text) {
        showLongText(text);
        mToast.setDuration(Toast.LENGTH_SHORT);
    }
}
