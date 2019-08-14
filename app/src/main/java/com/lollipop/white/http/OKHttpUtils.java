package com.lollipop.white.http;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

import com.lollipop.demo.BuildConfig;
import com.lollipop.white.listener.MyResultCallback;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by lollipop on 2018/10/29.
 */

public class OKHttpUtils {
    private static String Server_Url = BuildConfig.SERVER_URL;

    /**
     * 设置URL地址
     */
    public static void changeServerUrl(int index) {
        if (BuildConfig.IS_DEBUG){
            switch (index){
                case 1:
                    OKHttpUtils.Server_Url = "https://easy-mock.com/mock/5bd14fb37f2aec4f84b04d4f/bingtuan/";
                    break;
                case 2:
                    OKHttpUtils.Server_Url = "https://api.test.com/";
                    break;
            }
        } else {
            OKHttpUtils.Server_Url = "https://api.test.com/";
        }
    }

    /**
     *  登录
     */
    public static void login(String phone, String pass, MyResultCallback callback) {
        Map<String, String> params = new HashMap<>(2);
        params.put("phone", phone);
        params.put("pass", pass);
        OkHttpClientManager.getAsyn(Server_Url + "user/login", params, callback);
    }

    /**
     * 获取启屏广告
     */
    public static void queryArrress(final String id, final MyResultCallback callback) {
        new Thread(){

            @Override
            public void run() {
                super.run();
                JSONObject jsonObject = null;
                try {
                    String jsonStr = null;
                    Thread.sleep(1000);
                    if (TextUtils.isEmpty(id)){
                        jsonStr = "{\"error_code\":0,\"message\":\"\",\"data\":[{\"id\":110000,\"name\":\"北京市\",\"parent_id\":1},{\"id\":120000,\"name\":\"天津市\",\"parent_id\":1},{\"id\":130000,\"name\":\"河北省\",\"parent_id\":1},{\"id\":140000,\"name\":\"山西省\",\"parent_id\":1},{\"id\":150000,\"name\":\"内蒙古自治区\",\"parent_id\":1},{\"id\":210000,\"name\":\"辽宁省\",\"parent_id\":1},{\"id\":220000,\"name\":\"吉林省\",\"parent_id\":1},{\"id\":230000,\"name\":\"黑龙江省\",\"parent_id\":1},{\"id\":310000,\"name\":\"上海市\",\"parent_id\":1},{\"id\":320000,\"name\":\"江苏省\",\"parent_id\":1},{\"id\":330000,\"name\":\"浙江省\",\"parent_id\":1},{\"id\":340000,\"name\":\"安徽省\",\"parent_id\":1},{\"id\":350000,\"name\":\"福建省\",\"parent_id\":1},{\"id\":360000,\"name\":\"江西省\",\"parent_id\":1},{\"id\":370000,\"name\":\"山东省\",\"parent_id\":1},{\"id\":410000,\"name\":\"河南省\",\"parent_id\":1},{\"id\":420000,\"name\":\"湖北省\",\"parent_id\":1},{\"id\":430000,\"name\":\"湖南省\",\"parent_id\":1},{\"id\":440000,\"name\":\"广东省\",\"parent_id\":1},{\"id\":450000,\"name\":\"广西壮族自治区\",\"parent_id\":1},{\"id\":460000,\"name\":\"海南省\",\"parent_id\":1},{\"id\":500000,\"name\":\"重庆市\",\"parent_id\":1},{\"id\":510000,\"name\":\"四川省\",\"parent_id\":1},{\"id\":520000,\"name\":\"贵州省\",\"parent_id\":1},{\"id\":530000,\"name\":\"云南省\",\"parent_id\":1},{\"id\":540000,\"name\":\"西藏自治区\",\"parent_id\":1},{\"id\":610000,\"name\":\"陕西省\",\"parent_id\":1},{\"id\":620000,\"name\":\"甘肃省\",\"parent_id\":1},{\"id\":630000,\"name\":\"青海省\",\"parent_id\":1},{\"id\":640000,\"name\":\"宁夏回族自治区\",\"parent_id\":1},{\"id\":650000,\"name\":\"新疆维吾尔自治区\",\"parent_id\":1},{\"id\":710000,\"name\":\"台湾\",\"parent_id\":1},{\"id\":810000,\"name\":\"香港特别行政区\",\"parent_id\":1},{\"id\":820000,\"name\":\"澳门特别行政区\",\"parent_id\":1}]}";
                    } else {
                        int value = Integer.valueOf(id);
                        if (value % 10000 == 0){
                            jsonStr = "{\"error_code\":0,\"message\":\"\",\"data\":[{\"id\":410100,\"name\":\"郑州市\",\"parent_id\":410000},{\"id\":410200,\"name\":\"开封市\",\"parent_id\":410000},{\"id\":410300,\"name\":\"洛阳市\",\"parent_id\":410000},{\"id\":410400,\"name\":\"平顶山市\",\"parent_id\":410000},{\"id\":410500,\"name\":\"安阳市\",\"parent_id\":410000},{\"id\":410600,\"name\":\"鹤壁市\",\"parent_id\":410000},{\"id\":410700,\"name\":\"新乡市\",\"parent_id\":410000},{\"id\":410800,\"name\":\"焦作市\",\"parent_id\":410000},{\"id\":410881,\"name\":\"济源市\",\"parent_id\":410000},{\"id\":410900,\"name\":\"濮阳市\",\"parent_id\":410000},{\"id\":411000,\"name\":\"许昌市\",\"parent_id\":410000},{\"id\":411100,\"name\":\"漯河市\",\"parent_id\":410000},{\"id\":411200,\"name\":\"三门峡市\",\"parent_id\":410000},{\"id\":411300,\"name\":\"南阳市\",\"parent_id\":410000},{\"id\":411400,\"name\":\"商丘市\",\"parent_id\":410000},{\"id\":411500,\"name\":\"信阳市\",\"parent_id\":410000},{\"id\":411600,\"name\":\"周口市\",\"parent_id\":410000},{\"id\":411700,\"name\":\"驻马店市\",\"parent_id\":410000}]}";
                        } else if (value % 100 == 0){
                            jsonStr = "{\"error_code\":0,\"message\":\"\",\"data\":[{\"id\":411502,\"name\":\"浉河区\",\"parent_id\":411500},{\"id\":411503,\"name\":\"平桥区\",\"parent_id\":411500},{\"id\":411521,\"name\":\"罗山县\",\"parent_id\":411500},{\"id\":411522,\"name\":\"光山县\",\"parent_id\":411500},{\"id\":411523,\"name\":\"新县\",\"parent_id\":411500},{\"id\":411524,\"name\":\"商城县\",\"parent_id\":411500},{\"id\":411525,\"name\":\"固始县\",\"parent_id\":411500},{\"id\":411526,\"name\":\"潢川县\",\"parent_id\":411500},{\"id\":411527,\"name\":\"淮滨县\",\"parent_id\":411500},{\"id\":411528,\"name\":\"息县\",\"parent_id\":411500},{\"id\":411529,\"name\":\"其它区\",\"parent_id\":411500}]}";
                        } else {
                            jsonStr = "{\"error_code\":0,\"message\":\"\",\"data\":[{\"id\":110000,\"name\":\"北京市\",\"parent_id\":1},{\"id\":120000,\"name\":\"天津市\",\"parent_id\":1},{\"id\":130000,\"name\":\"河北省\",\"parent_id\":1},{\"id\":140000,\"name\":\"山西省\",\"parent_id\":1},{\"id\":150000,\"name\":\"内蒙古自治区\",\"parent_id\":1},{\"id\":210000,\"name\":\"辽宁省\",\"parent_id\":1},{\"id\":220000,\"name\":\"吉林省\",\"parent_id\":1},{\"id\":230000,\"name\":\"黑龙江省\",\"parent_id\":1},{\"id\":310000,\"name\":\"上海市\",\"parent_id\":1},{\"id\":320000,\"name\":\"江苏省\",\"parent_id\":1},{\"id\":330000,\"name\":\"浙江省\",\"parent_id\":1},{\"id\":340000,\"name\":\"安徽省\",\"parent_id\":1},{\"id\":350000,\"name\":\"福建省\",\"parent_id\":1},{\"id\":360000,\"name\":\"江西省\",\"parent_id\":1},{\"id\":370000,\"name\":\"山东省\",\"parent_id\":1},{\"id\":410000,\"name\":\"河南省\",\"parent_id\":1},{\"id\":420000,\"name\":\"湖北省\",\"parent_id\":1},{\"id\":430000,\"name\":\"湖南省\",\"parent_id\":1},{\"id\":440000,\"name\":\"广东省\",\"parent_id\":1},{\"id\":450000,\"name\":\"广西壮族自治区\",\"parent_id\":1},{\"id\":460000,\"name\":\"海南省\",\"parent_id\":1},{\"id\":500000,\"name\":\"重庆市\",\"parent_id\":1},{\"id\":510000,\"name\":\"四川省\",\"parent_id\":1},{\"id\":520000,\"name\":\"贵州省\",\"parent_id\":1},{\"id\":530000,\"name\":\"云南省\",\"parent_id\":1},{\"id\":540000,\"name\":\"西藏自治区\",\"parent_id\":1},{\"id\":610000,\"name\":\"陕西省\",\"parent_id\":1},{\"id\":620000,\"name\":\"甘肃省\",\"parent_id\":1},{\"id\":630000,\"name\":\"青海省\",\"parent_id\":1},{\"id\":640000,\"name\":\"宁夏回族自治区\",\"parent_id\":1},{\"id\":650000,\"name\":\"新疆维吾尔自治区\",\"parent_id\":1},{\"id\":710000,\"name\":\"台湾\",\"parent_id\":1},{\"id\":810000,\"name\":\"香港特别行政区\",\"parent_id\":1},{\"id\":820000,\"name\":\"澳门特别行政区\",\"parent_id\":1}]}";
                        }
                    }

                    jsonObject = new JSONObject(jsonStr);
                }catch (Exception e){
                    e.printStackTrace();
                }
                final JSONArray jsonArray = jsonObject.optJSONArray("data");
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onResponse(jsonArray);
                    }
                });

            }
        }.start();
    }

    /**
     * 版本更新
     *
     * @param callback
     */
    public static void updateVersion(String token, MyResultCallback callback) {
        Map<String, String> params = null;
        if (!TextUtils.isEmpty(token)){
            params = new HashMap<>();
            params.put("token", token);
        }
        OkHttpClientManager.getAsyn(Server_Url + "helper/version", params, callback);
    }

    /**
     * 上传文件
     */
    public static void uploadFile(String token, String filePath, MyResultCallback callback) {
        Map<String, String> params = new HashMap<>();
        params.put("token",token);
        Map<String, String> files = new HashMap<>();
        files.put("file",filePath);
        OkHttpClientManager.postAsyn(Server_Url + "helper/put-img",params, files, callback);
    }
}
