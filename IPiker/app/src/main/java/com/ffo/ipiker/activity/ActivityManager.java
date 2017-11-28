package com.ffo.ipiker.activity;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: huchunhua
 * Time: 2017/7/18 14:19
 * Package: com.ffo.ipiker.Activity
 * Project: IPiker
 * Mail: 742296818@qq.com
 * Describe: activity的管理
 */

public class ActivityManager {
    private static List<Activity> activities = new ArrayList<>();

    /**
     * add activity to activity queue
     * @param activity
     */
    public static void addActivity(Activity activity) {

        activities.add(activity);
    }

    /**
     * remove activity from this activity queue
     * @param activity
     */
    public static void removeActivity(Activity activity) {

        activities.remove(activity);
    }

    /**
     * remove all acitivitis and finish
     */
    public static void finishAll() {

        for (Activity activity : activities) {

            if (!activity.isFinishing()) {

                activity.finish();
            }
        }
    }

}
