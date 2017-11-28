package com.ffo.ipiker.util.customView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

import com.ffo.ipiker.util.LogUtil;

/**
 * Author: huchunhua
 * Time: 14:09
 * Package: com.ffo.ipiker.utils.customView
 * Project: IPiker
 * Mail: 742295818@qq.com
 * Describe: 一句话描述
 */

public class MyScrollView extends ScrollView {
    private static final String TAG = MyScrollView.class.getSimpleName();

    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    private int mLastYIntercept;
    private int mLastXIntercept;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        boolean intercept = false;

        // 判断触摸事件类型
        switch (event.getAction()) {
            // Down事件
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                int datey = Math.abs(y - mLastYIntercept);
                int dateX = Math.abs(x - mLastXIntercept);
                if (dateX > datey) {
                    //左右滑动
                    intercept = false;
                } else {
                    // 上下滑动
                    intercept = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                intercept = false;
                break;
        }
        mLastYIntercept = y;
        mLastXIntercept = x;
        return  intercept;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        LogUtil.v(TAG,"dispatchTouchEvent");
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        LogUtil.v(TAG,"onTouchEvent");
        return super.onTouchEvent(ev);
    }
}
