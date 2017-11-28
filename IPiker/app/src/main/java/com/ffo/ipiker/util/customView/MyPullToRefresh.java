package com.ffo.ipiker.util.customView;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.ffo.ipiker.R;
import com.ffo.ipiker.util.LogUtil;


/**
 * Author: huchunhua
 * Time: 20:14
 * Package: com.ffo.ipiker.utils.customView
 * Project: IPiker
 * Mail: 742295818@qq.com
 * Describe: 一句话描述
 */

public class MyPullToRefresh extends ViewGroup {

    private String TAG = MyPullToRefresh.class.getSimpleName();

    // 布局填充器对象
    private LayoutInflater mInflater;
    // 头视图(即上拉刷新时显示的部分)
    private RelativeLayout mLayoutHeader;

    // 尾视图(即下拉加载时显示的部分)
    private RelativeLayout mLayoutFooter;

    // 最后一个content-child-view的index
    private int lastChildIndex;


    // ViewGroup的内容高度(不包括header与footer的高度)
    private int mLayoutContentHeight;
    // 用于平滑滑动的Scroller对象
    private Scroller mLayoutScroller;
    // Scroller的滑动速度
    private static final int SCROLL_SPEED = 650;
    // 当滚动到内容最底部时Y轴所需要滑动的举例
    private int mReachBottomScroll;
    // 最小有效滑动距离(滑动超过该距离才视作一次有效的滑动刷新/加载操作)
    private static int mEffectiveScroll;

    private int mEffectiveHeaderHeight;
    private int mEffictiveFooterHeight;

    // 是否允许下拉刷新
    private boolean mEnablePullDown = true;
    // 是否允许上拉加载
    private boolean mEnablePullUp = true;
    private TextView header_textview;
    private TextView footer_textview;


    public MyPullToRefresh(Context context) {
        super(context);
    }

    public MyPullToRefresh(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 实例化布局填充器
        mInflater = LayoutInflater.from(context);
        // 实例化Scroller
        mLayoutScroller = new Scroller(context);
        // 计算最小有效滑动距离
        mEffectiveScroll = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        lastChildIndex = getChildCount() - 1;

        // 添加下拉加载部分
//        if (mEnablePullDown)
        addLayoutHeader();

//        // 添加上拉刷新部分
////        if (mEnablePullUp)
//        addLayoutFooter();
    }

    /**
     * 添加上拉刷新布局作为header
     */
    private void addLayoutHeader() {
        // 通过LayoutInflater获取从布局文件中获取header的view对象
        mLayoutHeader = (RelativeLayout) mInflater.inflate(R.layout.pulltorefesh_header, null);

        // 获取上拉刷新的文字描述
        header_textview = (TextView) mLayoutHeader.findViewById(R.id.header_hint_textview);
        // 设置布局参数(宽度为MATCH_PARENT,高度为MATCH_PARENT)
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams
                (RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams
                        .WRAP_CONTENT);
        // 将Header添加进Layout当中
        addView(mLayoutHeader, params);
    }

    /**
     * 添加下拉加载布局作为footer
     */
    private void addLayoutFooter() {
        // 通过LayoutInflater获取从布局文件中获取footer的view对象
        mLayoutFooter = (RelativeLayout) mInflater.inflate(R.layout.recyclerview_footer_loading_more, null);

        footer_textview = (TextView) mLayoutFooter.findViewById(R.id.footer_hint_textview);
        // 设置布局参数(宽度为MATCH_PARENT,高度为MATCH_PARENT)
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams
                (RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams
                        .MATCH_PARENT);
        // 将footer添加进Layout当中
        addView(mLayoutFooter, params);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 遍历进行子视图的测量工作
        for (int i = 0; i < getChildCount(); i++) {
            // 通知子视图进行测量
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
        }
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // 重置(避免重复累加)
        mLayoutContentHeight = 0;

        // 遍历进行子视图的置位工作
        for (int index = 0; index < getChildCount(); index++) {
            View child = getChildAt(index);
            if (child == mLayoutHeader) { // 头视图隐藏在ViewGroup的顶端
                child.layout(0, 0 - child.getMeasuredHeight(), child.getMeasuredWidth(), 0);
                mEffectiveHeaderHeight = child.getHeight();
                LogUtil.v(TAG, "mLayoutHeader Height:" + mLayoutHeader.getHeight() + " Width :" +
                        mLayoutHeader
                                .getWidth());
            } else if (child == mLayoutFooter) { // 尾视图隐藏在ViewGroup所有内容视图之后
                child.layout(0, mLayoutContentHeight, child.getMeasuredWidth(),
                        mLayoutContentHeight + getMeasuredHeight());
                mEffictiveFooterHeight = child.getHeight();
                LogUtil.v(TAG, "mLayoutHeader Height:" + mLayoutHeader.getHeight() + " Width :" +
                        mLayoutHeader
                                .getWidth());
            } else { // 内容视图根据定义(插入)顺序,按由上到下的顺序在垂直方向进行排列
                child.layout(0, mLayoutContentHeight, child.getMeasuredWidth(),
                        mLayoutContentHeight + child.getMeasuredHeight());
                if (index <= lastChildIndex) {
                    if (child instanceof NestedScrollView) {
                        mLayoutContentHeight += getMeasuredHeight();
                        continue;
                    }
                    mLayoutContentHeight += child.getMeasuredHeight();
                }
            }
        }
        // 计算到达内容最底部时ViewGroup的滑动距离
        mReachBottomScroll = mLayoutContentHeight - getMeasuredHeight();
    }

    // Layout状态
    private int status = NORMAL;
    // 普通状态
    private static final int NORMAL = 0;
    // 意图刷新
    private static final int TRY_REFRESH = 1;
    // 刷新状态
    private static final int REFRESH = 2;
    // 意图加载
    private static final int TRY_LOAD_MORE = 3;
    // 加载状态
    private static final int LOAD_MORE = 4;

    // 用于计算滑动距离的Y坐标中介
    private int mLastYMoved;
    // 用于判断是否拦截触摸事件的Y坐标中介
    private int mLastYIntercept = 0;
    private int mLastXIntercept = 0;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        boolean intercept = false;
        // 记录此次触摸事件的y坐标
        int y = (int) event.getY();
        int x = (int) event.getX();
        // 判断触摸事件类型
        switch (event.getAction()) {
            // Down事件
            case MotionEvent.ACTION_DOWN: {
                // 记录下本次系列触摸事件的起始点Y坐标
                mLastYMoved = y;
                // 不拦截ACTION_DOWN，因为当ACTION_DOWN被拦截，后续所有触摸事件都会被拦截
                intercept = false;
                break;
            }
            // Move事件
            case MotionEvent.ACTION_MOVE: {
                int datey = Math.abs(y - mLastYIntercept);
                int dateX = Math.abs(x - mLastXIntercept);
                LogUtil.v(TAG, "mLayoutHeader.getScrollY(): " + mLayoutHeader.getScrollY());
                if (dateX > datey) {
                    //左右滑动
                    intercept = false;
                } else {
                    //上下滑动
                    if (y > mLastYIntercept) { // 下滑操作
                        LogUtil.v(TAG, "status : " + status);
                        if (status == REFRESH || status == LOAD_MORE) {
                            intercept = true;
                            break;
                        }
                        View child = getChildAt(0);
                        if (child instanceof NestedScrollView) {
                            intercept = svPullDownIntercept(child);
                        }
                        if (intercept) {
                            status = TRY_REFRESH;
                        }
                    } else if (y < mLastYIntercept) { // 上拉操作
                        if (status == REFRESH || status == LOAD_MORE) {
                            intercept = true;
                            break;
                        }
                        View child = getChildAt(lastChildIndex);
                        if (child instanceof NestedScrollView) {
                            intercept = svPullUpIntercept(child);
                        }
                        if (intercept) {
                            status = TRY_LOAD_MORE;
                        }

                    } else {
                        intercept = false;
                    }
                }

                break;
            }
            // Up事件
            case MotionEvent.ACTION_UP: {
                intercept = false;
                break;
            }
        }

        mLastYIntercept = y;
        mLastXIntercept = x;
        return intercept;
    }


    private boolean svPullDownIntercept(View child) {
        boolean intercept = false;
        if (child.getScrollY() <= 0) {
            LogUtil.v(TAG, "child.getScrollY() :" + child.getScrollY());
            intercept = true;
        }
        return intercept;
    }

    private boolean svPullUpIntercept(View child) {
        boolean intercept = false;
        NestedScrollView scrollView = (NestedScrollView) child;

        View scrollChild = scrollView.getChildAt(scrollView.getChildCount() - 1);
        int d = scrollChild.getBottom();
        LogUtil.v(TAG, "scrollChild.getBottom() :" + scrollChild.getBottom());
        LogUtil.v(TAG, "scrollView.getScrollY() :" + scrollView.getScrollY());
        LogUtil.v(TAG, "scrollView.getChildCount() :" + scrollView.getChildCount());
        LogUtil.v(TAG, "scrollView.getHeight() :" + scrollView.getHeight());
        LogUtil.v(TAG, "scrollView.getChildAt(1).getHeight() :" + scrollView.getChildAt(0)
                .getHeight());

        if (scrollView.getScrollY() + scrollView.getHeight() - scrollView.getPaddingTop() -
                scrollView.getPaddingBottom() < getChildAt(0)
                .getHeight()) {
            intercept = false;
        }
        return intercept;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        LogUtil.v(TAG, "onTouchEvent");
        int y = (int) event.getY();


        switch (event.getAction()) {

            case MotionEvent.ACTION_MOVE: {
                // 计算本次滑动的Y轴增量(距离)
                LogUtil.v(TAG, "getScrollY() : " + getScrollY());
                int dy = mLastYMoved - y;
                // 如果滑动增量小于0，即下拉操作
                if (dy < 0 && getScrollY() < 0) {
                    if (mEnablePullDown) {
                        if (status == TRY_REFRESH) {
                            scrollBy(0, dy / 3);
                        } else {
                            scrollBy(0, dy / 8);
                        }
                    }
                } else if (dy > 0 && getScrollY() > 0) {
                    if (mEnablePullUp) {
                        if (status == TRY_LOAD_MORE) {
                            scrollBy(0, dy / 8);
                        } else {
                            scrollBy(0, dy / 3);
                        }
                    }
                } else {
                    scrollBy(0, dy / 3);
                }

                beforeRefreshing();
                beforeLoadMore();
                // 记录y坐标
                mLastYMoved = y;
                break;
            }

            case MotionEvent.ACTION_UP: {
                // 判断本次触摸系列事件结束时,Layout的状态
                if (getScrollY() <= -mEffectiveHeaderHeight) {
                    releaseWithStateRefresh();
                } else if (getScrollY() >= mEffictiveFooterHeight) {
                    releaseWithStateLoadMore();
                } else {
                    releaseWithStatusTryRefresh();
                    releaseWithStatusTryLoadMore();
                }
            }
        }
        mLastYIntercept = 0;
        postInvalidate();
        return true;
    }

    /**
     * header 信息状态更新
     */
    private void beforeRefreshing() {
        if (getScrollY() >= -mEffectiveHeaderHeight) {
            // 下拉刷新
            header_textview.setText(getContext().getString(R.string.header_hint_normal));
        } else {
            // 松开刷新
            header_textview.setText(getContext().getString(R.string.header_hint_ready));
            if (getScrollY() <= -3 * mEffectiveHeaderHeight) {
                header_textview.setText(getContext().getString(R.string.header_hint_refreshing));
                status = REFRESH;
            }
        }
    }

    /**
     * footer 信息状态更新
     */
    private void beforeLoadMore() {
        if (getScrollY() >= mEffictiveFooterHeight) {
            // 放开加载
            footer_textview.setText(getContext().getString(R.string.footer_hint_release));
        } else {
            // 上拉加载
            footer_textview.setText(getContext().getString(R.string.footer_hint_click));
        }
    }

    /**
     * 松开手指,正在刷新
     */
    private void releaseWithStateRefresh() {
        scrollTo(0, -mEffectiveHeaderHeight);
        scrollTo(0, 0);
        header_textview.setText(getContext().getString(R.string.header_hint_refreshing));
    }

    /**
     * 松开手指,正在加载
     */
    private void releaseWithStateLoadMore() {
        scrollTo(0, mEffictiveFooterHeight);
        footer_textview.setText(getContext().getString(R.string.header_hint_loading));
    }

    private void releaseWithStatusTryRefresh() {
        scrollBy(0, -getScrollY());
        header_textview.setText("下拉刷新");
        status = NORMAL;
    }

    private void releaseWithStatusTryLoadMore() {
        scrollBy(0, -getScrollY());
        footer_textview.setText("上拉加载更多");
        status = NORMAL;
    }


}
