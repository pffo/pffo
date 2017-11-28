package com.ffo.ipiker.util;

/**
 * Author: huchunhua
 * Time: 20:59
 * Package: com.ffo.ipiker.utils
 * Project: IPiker
 * Mail: 742295818@qq.com
 * Describe: 一句话描述
 */

public class NetWorkUtil {

    public static final int HOMEPAGE_LOADING_MORE_OPERATION = 1001;//加载更多
    public static final int HOMEPAGE_PULL_DOWN_REFRESH_OPERATION = 1002;//下拉刷新

    private static boolean netWork = false;//终端是否联网


    public static boolean isNetWork() {
        return netWork;
    }

    public static void setNetWork(boolean netWork) {
        NetWorkUtil.netWork = netWork;
    }
}
