package com.lollipop.white.util;

import android.app.Activity;
import android.content.Intent;

import java.util.ArrayList;
import java.util.Stack;

import com.lollipop.white.act.LoginActivity;
import com.lollipop.white.act.MainActivity;

/**
 * activity堆栈式管理
 *
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @created 2014年10月30日 下午6:22:05
 *
 */
public class AppManager {
    public static final String TAG_EXIT = "exit";

    private Activity act;

    private static Stack<Activity> activityStack;

    private static AppManager instance;

    private AppManager() {}

    public Activity getCurrentAct() {
        return act;
    }

    /**
     * 单一实例
     */
    public static AppManager getAppManager() {
        if (instance == null) {
            instance = new AppManager();
        }
        return instance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        act = activity;
        activityStack.add(activity);
    }

    public void remove(Activity act) {
        if (activityStack != null) {
            activityStack.remove(act);
            if (activityStack.size() > 0){
                this.act = activityStack.lastElement();
            }
        }
    }
    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        ArrayList<Activity> deleteLists = new ArrayList<Activity>();
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                deleteLists.add(activity);
//                finishActivity(activity);
            }
        }
        for(Activity ac:deleteLists){
            activityStack.remove(ac);
            ac.finish();
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    public void finishAllActivityWithout(Activity activity) {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            Activity act =  activityStack.get(i);
            if (act != null && act != activity) {
                act.finish();
            }
        }
        activityStack.clear();
        activityStack.add(activity);
    }

    public void toLogin(){
        if (act instanceof LoginActivity){
            return;
        }
//        AppManager accountManage = AppManager.getAppManager();
//        accountManage.finishAllActivity();
        SPManager manager = SPManager.getInstance();
        if (manager != null){
            manager.userLogout();
        }
        Activity activity = null;
        if (act == null){
            if (activityStack != null){
                activity = activityStack.get(0);
            }
        } else {
            activity = act;
        }
        if (activity == null){
            return;
        }
        if (activity instanceof MainActivity){
            Intent intent = new Intent();
            intent.setAction(Constants.TO_LOGINPAGE_ACTION);
            activity.sendBroadcast(intent);
        } else {
            Intent in = new Intent(activity, MainActivity.class);   // 当前如果为MainActivity，onNewIntent不会被执行
            in.putExtra(TAG_EXIT, true);
            activity.startActivity(in);
        }
    }
}