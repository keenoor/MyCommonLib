package me.keenor.androidcommon;

import android.annotation.SuppressLint;
import android.content.Context;

/**
 * Created by chenliuchun on 17/3/16.
 */

public class ContextUtil {

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        ContextUtil.context = context;
    }
}
