package me.keenor.androidcommon.ui;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import me.keenor.androidcommon.util.AppUtil;

/**
 * Author:      chenliuchun
 * Date:        17/3/9
 * Description: 吐司工具类，任意线程内使用
 * 注意：上下文对象为applicationContext，尽量不使用页面的Context
 * Modification History:
 * Date       Author       Version     Description
 * -----------------------------------------------------
 */

public class ToastUtil {

    private static Toast toast;

    private ToastUtil() {
    }

    /**
     * 默认显示时间为Short
     * @param text
     */
    public static void show(final CharSequence text) {
        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
            showMain(text);
        } else {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    showMain(text);
                }
            });
        }
    }

    private static void showMain(CharSequence text) {
        if (toast == null) {
            toast = Toast.makeText(AppUtil.getContext(), text, Toast.LENGTH_SHORT);
        } else {
            toast.setText(text);
        }
        toast.show();
    }

    public static void cancel() {
        if (toast != null) {
            toast.cancel();
        }
    }

}
