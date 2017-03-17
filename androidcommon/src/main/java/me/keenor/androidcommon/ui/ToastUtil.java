package me.keenor.androidcommon.ui;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import me.keenor.androidcommon.ContextUtil;

/**
 * Author:      chenliuchun
 * Date:        17/3/9
 * Description: 吐司工具类，任意线程内使用
 * Modification History:
 * Date       Author       Version     Description
 * -----------------------------------------------------
 */

public class ToastUtil {

    private static Toast toast;

    private ToastUtil() {
    }

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
            toast = Toast.makeText(ContextUtil.getContext(), text, Toast.LENGTH_SHORT);
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
