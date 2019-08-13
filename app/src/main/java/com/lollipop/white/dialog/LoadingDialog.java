package com.lollipop.white.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.lollipop.demo.R;

/**
 * 加载框
 * Created by xieshengqi on 15/11/12.
 */
public class LoadingDialog extends Dialog {
    private TextView mText;

    public LoadingDialog(Context context) {
        super(context);
        init(context);
    }

    protected LoadingDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }

    public LoadingDialog(Context context, int theme) {
        super(context, theme);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
        mText = (TextView) view.findViewById(R.id.tv_hint);
        setContentView(view);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.6f;
        lp.dimAmount = 0.8f;
        getWindow().setAttributes(lp);
        getWindow().getAttributes().gravity = Gravity.CENTER;
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }


    public TextView getmText() {
        return mText;
    }

    public void setmText(TextView mText) {
        this.mText = mText;
    }

    public void setHintText(int rId) {
        mText.setText(rId);
    }

    public void setHintText(String text) {
        mText.setText(text);
    }
}
