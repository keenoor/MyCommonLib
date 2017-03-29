package me.keenor.androidcommon.util;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.text.TextUtils;

import java.util.List;

/**
 * Created by chenliuchun on 17/3/16.
 */

public class AppUtil {

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        AppUtil.context = context;
    }

    /**
     * 用于 module 内对是否 debug模式的判断，而在主 module 里面则不需要
     * @return
     */
    public boolean isDebug(){
        return AppUtil.getContext().getApplicationInfo() != null &&
                (AppUtil.getContext().getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
    }

    /**
     * 判断某个界面是否在前台
     *
     * @param context   当前的上下文
     * @param className 类全名
     */
    public static boolean isForeground(Context context, String className) {
        if (context == null || TextUtils.isEmpty(className)) {
            return false;
        }
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            if (className.equals(cpn.getClassName())) {
                return true;
            }
        }
        return false;
    }
}


