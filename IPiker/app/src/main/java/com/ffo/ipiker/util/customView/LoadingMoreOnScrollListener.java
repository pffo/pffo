package com.ffo.ipiker.util.customView;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ffo.ipiker.util.LogUtil;

/**
 * Author: huchunhua
 * Time: 19:53
 * Package: com.ffo.ipiker.utils.customView
 * Project: IPiker
 * Mail: 742295818@qq.com
 * Describe: 一句话描述
 */

public abstract class LoadingMoreOnScrollListener extends RecyclerView.OnScrollListener {
    private static String TAG = LoadingMoreOnScrollListener.class.getSimpleName();

    //声明一个LinearLayoutManager
    private LinearLayoutManager mLinearLayoutManager;

    //当前页，从0开始
    private int currentPage = 0;
    //已经加载出来的Item的数量
    private int totalItemCount;

    //主要用来存储上一个totalItemCount
    private int previousTotal = 0;

    //在屏幕上可见的item数量
    private int visibleItemCount;

    //在屏幕可见的Item中的第一个
    private int firstVisibleItem;

    //是否正在上拉数据
    private boolean loading = true;

    public LoadingMoreOnScrollListener(LinearLayoutManager linearLayoutManager) {
        this.mLinearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = mLinearLayoutManager.getItemCount();
        firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();
        if (loading) {
            LogUtil.v(TAG, "firstVisibleItem: " + firstVisibleItem);
            LogUtil.v(TAG, "totalPageCount:" + totalItemCount);
            LogUtil.v(TAG, "visibleItemCount:" + visibleItemCount);
            if (totalItemCount > previousTotal) {
                //说明数据已经加载结束
                loading = false;
                previousTotal = totalItemCount;
            }
        }
        //这里需要好好理解
        if (!loading && totalItemCount - visibleItemCount <= firstVisibleItem) {
            currentPage++;
            onLoadMore(currentPage);
            loading = true;
        }
    }

    /**
     * 提供一个抽象方法，在Activity中监听到这个LoadingMoreOnScrollListener
     * 并且实现这个方法
     */
    public abstract void onLoadMore(int currentPage);
}