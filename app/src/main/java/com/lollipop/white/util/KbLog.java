package com.lollipop.white.util;

import android.util.Log;

import com.lollipop.demo.BuildConfig;

/**
 * Created by lollipop on 2017/7/31.
 */

public class KbLog {

    public static void i(String tag,String msg){
        if (BuildConfig.IS_DEBUG){
            Log.i(tag,msg);
        }
    }

    public static void e(String tag,String msg){
        if (BuildConfig.IS_DEBUG){
            Log.e(tag,msg);
        }
    }


    public static void w(String tag,String msg){
        if (BuildConfig.IS_DEBUG){
            Log.w(tag,msg);
        }
    }

    public static void d(String tag,String msg){
        if (BuildConfig.IS_DEBUG){
            Log.d(tag,msg);
        }
    }
    public static void i(String msg){
        if (BuildConfig.IS_DEBUG){
            Log.i("info",msg);
        }
    }

    public static void e(String msg){
        if (BuildConfig.IS_DEBUG){
            Log.e("error",msg);
        }
    }


    public static void w(String msg){
        if (BuildConfig.IS_DEBUG){
            Log.w("warn",msg);
        }
    }

    public static void d(String msg){
        if (BuildConfig.IS_DEBUG){
            Log.d("debug",msg);
        }
    }

}
