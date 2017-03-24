package me.keenor.androidcommon.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;

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
}
