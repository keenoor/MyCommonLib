package me.keenor.androidcommon.util;

import android.content.pm.ApplicationInfo;
import android.util.Log;

import java.util.Locale;

import me.keenor.androidcommon.ContextUtil;

/**
 * 日志输出工具类
 * 华为手机只能输出i\w\e的log，需要注意
 * 一般建议使用高级别的Log.i3/w3等
 * Created by chen_liuchun on 2016/4/6.
 */
public class LogUtil {

    // 当前项目log标志
    private static final String TAG = "common_lib";
    // 是否输出log
    private static boolean sIsLog;

    static {
        // 由于在被依赖 module 里面无论如何，BuildConfig.DEBUG 始终为 false，导致判断失效
        // 因此，在非主 module 里面判断是否是 DEBUG 模式的最佳办法如下
        sIsLog = ContextUtil.getContext().getApplicationInfo() != null &&
                (ContextUtil.getContext().getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
    }

    // 直接使用Log
    public static void v(String tag, String msg) {
        if (sIsLog) { Log.v(tag, msg); }
    }
    public static void d(String tag, String msg) {
        if (sIsLog) { Log.d(tag, msg); }
    }
    public static void i(String tag, String msg) {
        if (sIsLog) { Log.i(tag, msg); }
    }
    public static void w(String tag, String msg) {
        if (sIsLog) { Log.w(tag, msg); }
    }
    public static void e(String tag, String msg) {
        if (sIsLog) { Log.e(tag, msg); }
    }

    //不需要定义TAG，直接打印日志信息
    public static void v1(String msg) {
        if (sIsLog) { Log.v(getTag(), msg); }
    }

    public static void d1(String msg) {
        if (sIsLog) { Log.d(getTag(), msg); }
    }
    public static void i1(String msg) {
        if (sIsLog) { Log.i(getTag(), msg); }
    }
    public static void w1(String msg) {
        if (sIsLog) { Log.w(getTag(), msg); }
    }
    public static void e1(String msg) {
        if (sIsLog) { Log.e(getTag(), msg); }
    }

    //不需要定义TAG，打印线程ID，方法名和输出信息
    public static void v2(String msg) {
        if (sIsLog) { Log.v(getTag(), buildMessage(msg)); }
    }

    public static void d2(String msg) {
        if (sIsLog) { Log.d(getTag(), buildMessage(msg)); }
    }
    public static void i2(String msg) {
        if (sIsLog) { Log.i(getTag(), buildMessage(msg)); }
    }
    public static void w2(String msg) {
        if (sIsLog) { Log.w(getTag(), buildMessage(msg)); }
    }
    public static void e2(String msg) {
        if (sIsLog) { Log.e(getTag(), buildMessage(msg)); }
    }

    //不需要定义TAG，打印线程名称，类名, 方法名, 行号等，并定位行
    public static void v3(String msg) {
        if (sIsLog) { Log.v(getTag(), getMsgFormat(msg)); }
    }

    public static void d3(String msg) {
        if (sIsLog) { Log.d(getTag(), getMsgFormat(msg)); }
    }
    public static void i3(String msg) {
        if (sIsLog) { Log.i(getTag(), getMsgFormat(msg)); }
    }
    public static void w3(String msg) {
        if (sIsLog) { Log.w(getTag(), getMsgFormat(msg)); }
    }
    public static void e3(String msg) {
        if (sIsLog) { Log.e(getTag(), getMsgFormat(msg)); }
    }

    /**
     * 获取到调用者的类名
     * @return 调用者的类名
     */
    private static String getTag() {
        StackTraceElement[] trace = new Throwable().fillInStackTrace()
                .getStackTrace();
        String callingClass = "";
        for (int i = 2; i < trace.length; i++) {
            Class<?> clazz = trace[i].getClass();
            if (!clazz.equals(LogUtil.class)) {
                callingClass = trace[i].getClassName();
                callingClass = callingClass.substring(callingClass
                        .lastIndexOf('.') + 1);
                break;
            }
        }
        return TAG + "***" + callingClass;
    }

    /**
     * 获取线程ID，方法名和输出信息
     * @param msg
     * @return
     */
    private static String buildMessage(String msg) {
        StackTraceElement[] trace = new Throwable().fillInStackTrace()
                .getStackTrace();
        String caller = "";
        for (int i = 2; i < trace.length; i++) {
            Class<?> clazz = trace[i].getClass();
            if (!clazz.equals(LogUtil.class)) {
                caller = trace[i].getMethodName();
                break;
            }
        }
        return String.format(Locale.US, "[%s] %s: %s", Thread.currentThread()
                .getName(), caller, msg);
    }

    /**
     * 获取相关数据:类名,方法名,行号等.用来定位行
     * @return
     */
    private static String getFunctionName() {
        StackTraceElement[] sts = Thread.currentThread().getStackTrace();
        if (sts != null) {
            for (StackTraceElement st : sts) {
                if (st.isNativeMethod()) {
                    continue;
                }
                if (st.getClassName().equals(Thread.class.getName())) {
                    continue;
                }
                if (st.getClassName().equals(LogUtil.class.getName())) {
                    continue;
                }
                return "[ Thread:" + Thread.currentThread().getName() + ", at " + st.getClassName() + "." + st.getMethodName()
                        + "(" + st.getFileName() + ":" + st.getLineNumber() + ")" + " ]";
            }
        }
        return null;
    }

    /**
     * 输出格式定义
     * @param msg
     * @return
     */
    private static String getMsgFormat(String msg) {
        return msg + " ;" + getFunctionName();
    }
}