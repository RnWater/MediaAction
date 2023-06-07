package com.henry.mediaaction.utils;

import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Logger {
    //是否debug模式，true则打印日志，false则不打印日志
    private static boolean isDebug = true;
    private static final String TAG=Logger.class.getSimpleName();

    public static void setIsDebug(boolean isDebug) {
        Logger.isDebug = isDebug;
    }

    public static boolean getIsDebug() {
        return Logger.isDebug;
    }
    /**
     * print error msg
     * @param msg
     */
    public static void e(String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        if (isDebug) {
            Log.e(TAG, msg);
        }
    }
    /**
     * print error msg
     * @param tag
     * @param msg
     */
    public static void e(String tag, String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        if (isDebug) {
            Log.e(tag, msg);
        }
    }

    /**
     * 打印全部数据
     * @param tag
     * @param msg
     */
    public static void e_all(String tag, String msg) {
        e_all(tag,msg,2000);
    }
    /**
     * 打印全部数据
     * @param tag
     * @param msg
     */
    public static void e_all(String tag, String msg,int count) {
        try {
            List<String> sub = new ArrayList<>();
            String service = msg;
            while (service.length() > count) {
                String newService = service.substring(0, count);
                sub.add(newService);
                service = service.substring(count);
            }
            sub.add(service);
            for (String ser : sub) {
                Logger.e(tag, ser);
            }
        } catch (Exception e) {
            Logger.e("我的异常e_all", e.getMessage());
        }
    }
    /**
     * print error msg with exception
     * @param tag
     * @param msg
     * @param tr
     */
    public static void e(String tag, String msg, Throwable tr) {
        if (isDebug) {
            Log.e(tag, msg, tr);
        }
    }

    /**
     * print info msg
     * @param tag
     * @param msg
     */
    public static void i(String tag, String msg) {
        if (isDebug) {
            Log.i(tag, msg);
        }
    }

    /**
     * print info msg with exception
     * @param tag
     * @param msg
     * @param tr
     */
    public static void i(String tag, String msg, Throwable tr) {
        if (isDebug) {
            Log.i(tag, msg, tr);
        }
    }

    /**
     * print debug msg
     * @param tag
     * @param msg
     */
    public static void d(String tag, String msg) {
        if (isDebug) {
            Log.d(tag, msg);
        }
    }

    /**
     * print debug msg with exception
     * @param tag
     * @param msg
     * @param tr
     */
    public static void d(String tag, String msg, Throwable tr) {
        if (isDebug) {
            Log.d(tag, msg, tr);
        }
    }

    /**
     * print warning msg
     * @param tag
     * @param msg
     */
    public static void w(String tag, String msg) {
        if (isDebug) {
            Log.w(tag, msg);
        }
    }

    /**
     * print warning msg with exception
     * @param tag
     * @param msg
     * @param tr
     */
    public static void w(String tag, String msg, Throwable tr) {
        if (isDebug) {
            Log.w(tag, msg, tr);
        }
    }

    /**
     * print warning msg with exception
     * @param tag
     * @param tr
     */
    public static void w(String tag, Throwable tr) {
        if (isDebug) {
            Logger.w(tag, tr);
        }
    }

    /**
     * print verbose msg
     * @param tag
     * @param msg
     */
    public static void v(String tag, String msg) {
        if (isDebug) {
            Log.v(tag, msg);
        }
    }

    /**
     * print verbose msg with exception
     * @param tag
     * @param msg
     * @param tr
     */
    public static void v(String tag, String msg, Throwable tr) {
        if (isDebug) {
            Log.v(tag, msg, tr);
        }
    }
}
