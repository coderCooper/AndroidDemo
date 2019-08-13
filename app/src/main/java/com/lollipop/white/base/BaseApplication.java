package com.lollipop.white.base;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import java.util.List;

/**
 *
 * Created by lollipop on 19/03/31.
 *
 */
public class BaseApplication extends Application{

    private static BaseApplication mApp;

    public BaseApplication() {
        mApp = this;
    }

    public static BaseApplication getInstance() {
        return mApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        String curProcessName = getProcessName(this, android.os.Process.myPid());
        if (TextUtils.isEmpty(curProcessName)){
            return;
        }
    }


    public static String getProcessName(Context cxt, int pid) {
        ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps != null && !runningApps.isEmpty()) {
            for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
                if (procInfo.pid == pid) {
                    return procInfo.processName;
                }
            }
        }


        return null;
    }
}