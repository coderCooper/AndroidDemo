package com.lollipop.white.listener;

import android.view.View;

import com.lollipop.white.view.FlowTagLayout;

/**
 * Created by HanHailong on 15/10/20.
 */
public interface OnTagClickListener {
    void onItemClick(FlowTagLayout parent, View view, int position);
}