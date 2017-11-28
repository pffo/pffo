package com.ffo.ipiker.application;

import android.app.Application;
import android.content.Context;

import com.ffo.ipiker.util.ToastUtil;


/**
 * Author: huchunhua
 * Time: 2017/8/30 16:20
 * Package: com.ffo.ipiker.application
 * Project: IPiker
 * Mail: 742295818@qq.com
 * Describe: 一句话描述
 */

public class MyApplication extends Application {

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getContext();

        ToastUtil.mVersion = ToastUtil.TOAST_DEVELOPMENT;//初始化Toast工具类
//      LeakCanary.install(this);//内存监测
    }

    public static Context getContext() {
        return context;
    }
}
