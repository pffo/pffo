package com.ffo.ipiker.util;

import android.app.Application;
import android.content.Context;

/**
 * Author: huchunhua
 * Time: 2017/7/18 13:47
 * Package: com.ffo.ipiker.utils
 * Project: IPiker
 * Mail: 742296818@qq.com
 * Describe: 全局context获取
 */

public class MyApplication extends Application {

    private static Context context;


    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}


