package com.ffo.ipiker.util;

import android.content.res.Resources;
import android.graphics.Point;
import android.util.DisplayMetrics;

/**
 * Author: huchunhua
 * Time: 13:54
 * Package: com.ffo.ipiker.utils
 * Project: IPiker
 * Mail: 742295818@qq.com
 * Describe: 获取屏幕参数
 */

public class DisplayScreen {

    /**
     * 获取屏幕的宽度
     *
     * @return
     */
    public static int getDisplayScreenWidth() {
        Resources resources = MyApplication.getContext().getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        float density = dm.density;
        int width = dm.widthPixels;
        return width;
    }

    /**
     * 获取屏幕的高度
     *
     * @return
     */
    public static int getDisplayScreenHeight() {
        Resources resources = MyApplication.getContext().getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        float density = dm.density;
        int height = dm.heightPixels;
        return height;
    }

    /**
     * 获取屏幕的宽高
     *
     * @return Point 屏幕右下角的坐标
     */
    public static Point getDisplayScreen() {
        Resources resources = MyApplication.getContext().getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        float density = dm.density;
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        Point point = new Point(width, height);
        return point;


//        // 通过WindowManager获取
//        DisplayMetrics dm = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(dm);
//        System.out.println("heigth : " + dm.heightPixels);
//        System.out.println("width : " + dm.widthPixels);
//        // 获取屏幕的默认分辨率
//        Display display = getWindowManager().getDefaultDisplay();
//        System.out.println("width-display :" + display.getWidth());
//        System.out.println("heigth-display :" + display.getHeight());
//        因为getWindowManager()
// 这个方法是在类Activity中的，如果你自己编写的类不是继承于类Activity，那么必然在这个类中书写代码就不能用到getWindowManager()
// 这个方法。所以当你自己编写的类不是继承于类Activity,就用getResources()
    }
}
