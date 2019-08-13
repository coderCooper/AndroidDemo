package com.lollipop.white.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.lollipop.white.util.ScreenSizeUtil;
import com.lollipop.demo.R;

/**
 * Created by lollipop on 18/06/18.
 */
public class UrlShowDialog extends Dialog {
    private WebView mWebview;
    private View mainLayout;
    private int maxHeight;

    public UrlShowDialog(Context mContext) {
        super(mContext, R.style.loadingDialog);
        init(mContext);
    }

    public void showWithurl(String url){
        mWebview.loadUrl(url);
        show();
    }


    private void init(Context mContext) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_url_show, null);
        mainLayout = view.findViewById(R.id.dialog_viewlayout);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mainLayout.getLayoutParams();
        DisplayMetrics size = ScreenSizeUtil.getDisplayMetrics(mContext);
        maxHeight = size.heightPixels * 8 / 10;
        layoutParams.width = size.widthPixels * 8 / 10;
        layoutParams.height = maxHeight;
        mainLayout.setLayoutParams(layoutParams);
        view.findViewById(R.id.btn).setOnClickListener(clickListener);
        RelativeLayout webParentView = view.findViewById(R.id.layout);
        initWebView();
        webParentView.addView(mWebview, 0);

        setContentView(view);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }
    private void initWebView(){
        if(mWebview==null){
            mWebview = new WebView(getContext().getApplicationContext());
            mWebview.setWebViewClient(new MyWebViewClient());
            WebSettings webSettings = mWebview.getSettings();
            if (Build.VERSION.SDK_INT >= 21) {
                webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            }

            // 设置支持javascript脚本
            webSettings.setJavaScriptEnabled(true);

            // 设置此属性，可任意比例缩放
            webSettings.setUseWideViewPort(true);
            // 设置不出现缩放工具
            webSettings.setBuiltInZoomControls(false);
            // 设置不可以缩放
            webSettings.setSupportZoom(false);
            webSettings.setDisplayZoomControls(false);

            //自适应屏幕
            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
            // 自适应 屏幕大小界面
            webSettings.setLoadWithOverviewMode(true);
        }
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
//            KbLog.e("webview contentHeight=" + view.getContentHeight());


//            int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
//            if (h < maxHeight){
//                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mainLayout.getLayoutParams();
//                layoutParams.height = h;
//                mainLayout.setLayoutParams(layoutParams);
//            }
////            // 重新测量
//            view.measure(w, h);
        }
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn:
                    dismiss();
                    break;
            }
        }
    };
}
