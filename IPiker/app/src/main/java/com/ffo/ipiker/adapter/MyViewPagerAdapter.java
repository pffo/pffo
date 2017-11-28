package com.ffo.ipiker.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;


import java.util.List;

/**
 * Author: huchunhua
 * Time: 11:08
 * Package: com.ffo.ipiker.adapter
 * Project: IPiker
 * Mail: 742295818@qq.com
 * Describe: 一句话描述
 */

public class MyViewPagerAdapter extends PagerAdapter {

    private Context context;
    private List<View> views;

    public MyViewPagerAdapter( List<View> views) {
        this.views = views;
    }

    public MyViewPagerAdapter(Context context, List<View> views) {
        this.context = context;
        this.views = views;
    }

    @Override
    public int getCount() {
        return views == null ? 0 : views.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = views.get(position);
        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position));
    }
}
