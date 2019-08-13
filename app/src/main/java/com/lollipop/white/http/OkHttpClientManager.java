package com.lollipop.white.http;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import com.lollipop.white.dialog.UrlShowDialog;
import com.lollipop.white.listener.MyResultCallback;
import com.lollipop.white.util.AppManager;
import com.lollipop.white.util.AppUtils;
import com.lollipop.white.util.KbLog;
import com.lollipop.white.util.ToastUtil;
import com.lollipop.demo.BuildConfig;
import com.lollipop.white.base.BaseApplication;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by lollipop on 15/8/17.
 */
public class OkHttpClientManager {
    private static final String TAG = "OkHttpClientManager";
    /**
     * 超时时间
     */
    private static final int CONNECTTIME = 30 * 1000;

    private static OkHttpClientManager mInstance;
    private OkHttpClient mOkHttpClient;
    private Handler mDelivery;

    private GetDelegate mGetDelegate = new GetDelegate();

    private PostDelegate mPostDelegate = new PostDelegate();

    private OkHttpClientManager() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .readTimeout(CONNECTTIME, TimeUnit.MILLISECONDS)
                .writeTimeout(CONNECTTIME, TimeUnit.MILLISECONDS)
                .connectTimeout(CONNECTTIME, TimeUnit.MILLISECONDS)
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                });
        mOkHttpClient = builder.build();
        mDelivery = new Handler(Looper.getMainLooper());
    }

    public static OkHttpClientManager getInstance() {
        if (mInstance == null) {
            synchronized (OkHttpClientManager.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpClientManager();
                }
            }
        }
        return mInstance;
    }

    private static String encode(String value){
        try {
            return URLEncoder.encode(value,"UTF-8");
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return "";
    }

    public GetDelegate getGetDelegate() {
        return mGetDelegate;
    }

    public PostDelegate getPostDelegate() {
        return mPostDelegate;
    }

    /**
     * ============Get方便的访问方式============
     */

    private static boolean networkConneced() {
        Context context = BaseApplication.getInstance().getApplicationContext();
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null) {
            return networkInfo.isAvailable();
        }
        return false;
    }

    public static void getAsyn(String url, MyResultCallback callback) {
        if (!networkConneced()){
            Request.Builder reqBuilder = new Request.Builder();
            reqBuilder.url(url);
            callback.onFail(null, -1, "当前网络无连接");
            callback.onAfter();
            return;
        }
        getInstance().getGetDelegate().getAsyn(url, callback, null,false);
    }

    public static void postAsyn(String url, Map<String, String> params, Map<String, String> filePaths, final MyResultCallback callback) {
        if (!networkConneced()){
            Request.Builder reqBuilder = new Request.Builder();
            reqBuilder.url(url);
            callback.onFail(null, -1, "当前网络无连接");
            callback.onAfter();
            return;
        }
        getInstance().getPostDelegate().postAsyn(url, params, filePaths, callback);
    }

    public static void postAsyn(String url, Map<String, String> params, final MyResultCallback callback) {
        if (!networkConneced()){
            Request.Builder reqBuilder = new Request.Builder();
            reqBuilder.url(url);
            callback.onFail(null, -1, "当前网络无连接");
            callback.onAfter();
            return;
        }
        getInstance().getPostDelegate().postAsyn(url, params, callback, null);
    }

    public static void getAsyn(String url, Map<String, String> params, final MyResultCallback callback) {
        getAsyn(url,params,callback,false);
    }

    public static void getAsyn(String url, Map<String, String> params, final MyResultCallback callback, boolean ignore3002) {
        if (!networkConneced()){
            Request.Builder reqBuilder = new Request.Builder();
            reqBuilder.url(url);
            callback.onFail(null, -1, "当前网络无连接");
            callback.onAfter();
            return;
        }
        if (params != null && params.size() > 0){
            StringBuffer sb = new StringBuffer(url);
            if (url.contains("?")) {
                sb.append("&");
            } else {
                sb.append("?");
            }

            for (String key : params.keySet()) {
                sb.append(key);
                sb.append("=");
                String value = params.get(key);
                if (TextUtils.isEmpty(value)){
                    sb.append("");
                } else {
                    sb.append(encode(value));
                }
                sb.append("&");
            }
            int length = sb.length();
            sb.delete(length - 1, length);
            url = sb.toString().replace(" ","");
        }

        getInstance().getGetDelegate().getAsyn(url, callback, null,ignore3002);
    }

    private Param[] map2Params(Map<String, String> params) {
        if (params == null) return new Param[0];
        int size = params.size();
        Param[] res = new Param[size];
        Set<Map.Entry<String, String>> entries = params.entrySet();
        int i = 0;
        for (Map.Entry<String, String> entry : entries) {
            res[i++] = new Param(entry.getKey(), entry.getValue());
        }
        return res;
    }

    private String getRequestParams(String url){
        StringBuffer sb = new StringBuffer(url);
        if (url.contains("?")) {
            sb.append("&");
        } else {
            sb.append("?");
        }
        sb.append("appversion=");
        sb.append(BuildConfig.VERSION_NAME);
        sb.append("&devicename=");
        sb.append(encode(android.os.Build.MODEL));
        sb.append("&osversion=");
        sb.append(encode(android.os.Build.VERSION.RELEASE));
        sb.append("&osname=Android");
        String result = sb.toString().replace(" ","");
        KbLog.d("url=",result);
        return result;
    }

    private void deliveryResult(String requestUrl, MyResultCallback callback, Request request) {
        deliveryResult(requestUrl,callback,request,false);
    }

    private void deliveryResult(final String requestUrl, MyResultCallback callback, final Request request,final boolean ignore3002) {
        final MyResultCallback resCallBack = callback;
        //UI thread
        if (callback != null) {
            callback.onBefore();
        }
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (resCallBack == null){
                    return;
                }
                sendFailedCallback(null, -2, e.getMessage(), resCallBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (resCallBack == null){
                    return;
                }
                String string = null;
                try {
                    string = response.body().string();

                    KbLog.d("response:", string);

                    JSONObject jsonObject = new JSONObject(string);

                    int code = jsonObject.optInt("code");

                    String msg = jsonObject.optString("message");

                    if (!ignore3002 && 1002 == code) {
                        boolean result = checkLogin(code, msg, resCallBack);

                        if (result) {
                            return;
                        }
                    }

                    JSONObject urlJson = jsonObject.optJSONObject("url_msg");
                    String showUrl = null;
                    if (urlJson != null && urlJson instanceof JSONObject && urlJson.length() > 0){
                        final String url = urlJson.optString("url");
                        if (!TextUtils.isEmpty(url)){
                            int block = urlJson.optInt("block");
                            if (1 == block){
                                code = -1;
                                if (TextUtils.isEmpty(msg)){
                                    msg = "未知错误，被服务器阻止";
                                }
                            }
                            showUrl = url;
                        }
                    }

                    Object mObject;
                    if (resCallBack.mType == String.class){
                        mObject = jsonObject.optString("data");
                    } else if (resCallBack.mType == JSONArray.class){
                        mObject = jsonObject.optJSONArray("data");
                    } else {
                        mObject = jsonObject.optJSONObject("data");
                    }
                    if (1 == code){
                        sendSuccessCallback(mObject, resCallBack);
                    } else {
                        sendFailedCallback(mObject, code, msg, resCallBack);
                    }
                    if (!TextUtils.isEmpty(showUrl)){
                        final String url = showUrl;
                        mDelivery.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                UrlShowDialog dialog = new UrlShowDialog(AppManager.getAppManager().getCurrentAct());
                                dialog.showWithurl(url);
                            }
                        }, 315);
                    }
                } catch (IOException e) {
                    sendFailedCallback(null, -2, e.getMessage(), resCallBack);
                } catch (JSONException e) {
                    sendFailedCallback(null, -3, "数据格式错误", resCallBack);
                }
            }
        });
    }

    private void sendFailedCallback(final Object response, final int error, final String msg, final MyResultCallback callback) {
        if (callback == null){
            return;
        }
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                String message = msg;
                if (TextUtils.isEmpty(message)){
                    message = "网络请求错误";
                }
                callback.onAfter();
                callback.onFail(response,error,message);
            }
        });
    }

    private void sendSuccessCallback(final Object resultJson, final MyResultCallback callback) {
        if (callback == null){
            return;
        }
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                callback.onAfter();
                callback.onResponse(resultJson);
            }
        });
    }

    private boolean checkLogin(int error_code, final String error_msg, final MyResultCallback callback) {
        if (error_code != 1002) {
            return false;
        }

        mDelivery.post(new Runnable() {

            @Override
            public void run() {
                if (callback != null){
                    callback.onAfter();
                }
                ToastUtil.showShortText("您的登录已过期，请重新登录");
                AppManager.getAppManager().toLogin();
            }
        });

        return true;
    }

    private Request buildPostFormRequest(String url, Param[] params, Object tag) {
        if (params == null) {
            params = new Param[0];
        }

        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        for (Param param : params) {
            formBodyBuilder.add(param.key, param.value);
        }
        RequestBody requestBody = formBodyBuilder.build();


        Request.Builder reqBuilder = new Request.Builder();
        reqBuilder.url(url)
                .post(requestBody);

        if (tag != null) {
            reqBuilder.tag(tag);
        }
        return reqBuilder.build();
    }

    public OkHttpClient client() {
        return mOkHttpClient;
    }

    //====================PostDelegate=======================
    public class PostDelegate {
        private final MediaType MEDIA_TYPE_STREAM = MediaType.parse("application/octet-stream;charset=utf-8");
        private final MediaType MEDIA_TYPE_STRING = MediaType.parse("text/plain;charset=utf-8");

        public void postAsyn(String url, Map<String, String> params, final MyResultCallback callback, Object tag) {
            KbLog.d("debug",params.toString());
            Param[] paramsArr = map2Params(params);
            postAsyn(url, paramsArr, callback, tag);
        }

        public void postAsyn(final String url, final Map<String, String> params, final Map<String, String> filePaths, final MyResultCallback callback) {

            if (callback != null) {
                callback.onBefore();
            }

            new Thread(){

                @Override
                public void run() {
                    super.run();

                    final String urll = getRequestParams(url);

                    final MultipartBody.Builder builder = new MultipartBody.Builder();
                    builder.setType(MultipartBody.FORM);
                    if (params != null){
                        // 遍历参数
                        for (Object key : params.keySet()) {
                            builder.addFormDataPart(key.toString(), params.get(key));
                        }
                    }
                    if (filePaths != null){
                        for (Object key : filePaths.keySet()) {

                            String filePath = filePaths.get(key);

                            Bitmap bitmap = AppUtils.compressImageFromFile(filePath, 1024f);// 按尺寸压缩图片

                            if (bitmap != null){
                                File file1 = AppUtils.compressImage(bitmap);  //按质量压缩图片

                                builder.addFormDataPart(key.toString(), file1.getName(), RequestBody.create(MediaType.parse("image/png"), file1));

                            }
                         }
                    }

                    mDelivery.post(new Runnable() {
                        @Override
                        public void run() {
                            RequestBody requestBody = builder.build();

                            Request request = new Request.Builder()
                                    .url(urll)
                                    .post(requestBody)
                                    .build();
                            deliveryResult(urll, callback, request);
                        }
                    });
                }

            }.start();
        }

        /**
         * 异步的post请求
         */
        public void postAsyn(String url, Param[] params, final MyResultCallback callback, Object tag) {
            if (params==null||params.length==0){//当无参时,使用get方式请求
                getAsyn(url,callback);
                return;
            }
            url = getRequestParams(url);
            Request request = buildPostFormRequest(url, params, tag);
            deliveryResult(url, callback, request);
        }
    }

    //====================GetDelegate=======================
    public class GetDelegate {

        private Request buildGetRequest(String url, Object tag) {
            Request.Builder builder = new Request.Builder()
                    .url(url);

            if (tag != null) {
                builder.tag(tag);
            }

            return builder.build();
        }

        /**
         * 通用的方法
         */
        public Response get(Request request) throws IOException {
            Call call = mOkHttpClient.newCall(request);
            Response execute = call.execute();
            return execute;
        }

        /**
         * 同步的Get请求
         */
        public Response get(String url) throws IOException {
            return get(url, null);
        }

        public Response get(String url, Object tag) throws IOException {
            final Request request = buildGetRequest(url, tag);
            return get(request);
        }

        public void getAsyn(String url, final MyResultCallback callback, Object tag, boolean ignore3002) {
            url = getRequestParams(url);
            final Request request = buildGetRequest(url, tag);
            deliveryResult(url, callback, request,ignore3002);
        }
    }
}

