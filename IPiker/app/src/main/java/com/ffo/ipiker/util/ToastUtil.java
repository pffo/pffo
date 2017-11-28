package com.ffo.ipiker.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Author: huchunhua
 * Time: 2017/9/29 13:56
 * Describe: 统一Toast工具类
 */

public class ToastUtil {

    public static final int TOAST_ALL = 0x0001; // 没有任何影响得提示
    public static final int TOAST_DEVELOPMENT = 0x0002; // 开发人员提示
    public static final int TOAST_TEST = 0X0003; // 测试人员提示
    public static final int TOAST_USER = 0X0004; //给用户看的提示
    public static final int TOAST_HIDE = 0X0005; //隐藏提示
    public static int mVersion = TOAST_DEVELOPMENT; // 默认只给开发人员看

    public static final int SHOW_SHORT = Toast.LENGTH_SHORT; // 对应toast
    public static final int SHOW_LONG = Toast.LENGTH_LONG; //对应tast

    public static Context mContext;


    public static void show(String msg, int duration) {
        show(msg, duration, TOAST_DEVELOPMENT);//默认为给开发人员看的提示
    }

    public static void show(Context context, String msg, int version) {
        show(context, msg, SHOW_SHORT, version);
    }

    public static void show(String msg, int duration, int version) {
        show(null, msg, duration, version);
    }

    private static void show(Context context, String msg, int duration, int version) {
        if (context != null) {
            showToast(context, msg, duration, version);
        } else {
            if (mContext == null) {
                mContext = MyApplication.getContext();
            }
            showToast(mContext, msg, duration, version);
        }
    }

    private static void showToast(Context context, String msg, int duration, int version) {
        if (mVersion >= version) {
            if (duration >= SHOW_LONG) {
                duration = SHOW_LONG;
            } else {
                duration = SHOW_SHORT;
            }
            Toast.makeText(context, msg, duration).show();
        }
    }
}
