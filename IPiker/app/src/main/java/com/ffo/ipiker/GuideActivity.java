package com.ffo.ipiker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ffo.ipiker.activity.BaseActivity;
import com.ffo.ipiker.adapter.MyViewPagerAdapter;
import com.ffo.ipiker.util.LogUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: huchunhua
 * Time: 20:52
 * Package: com.ffo.ipiker
 * Project: IPiker
 * Mail: 742295818@qq.com
 * Describe: 引导界面，做一些软件初始化工作
 */

public class GuideActivity extends BaseActivity implements View.OnClickListener {

    private ViewPager mViewPager;
    private TextView btn_jump;//引导界面结束跳转按钮

    private MyViewPagerAdapter mAdapter;
    private mOnPageChangeListener mOnPagerChangeListener;

    private LinearLayout ll_dots; //引导小圆点Layout
    private RelativeLayout rl_guide; //guide_activity.xml最外层layout

    private int GUIDE_NUMBER = 4; //引导页面的数量
    private int[] guideImages;//引导界面图片数组
    private List<View> guideViews; //引导页面的View
    private ImageView[] dotImages; //引导小圆点
    private int dotCurrentIndex; //小圆点偏移量


    private SharedPreferences mSharePreferences;
    private String SHAREPREFERENCES_NAME = "FIRST_START_APPLICATION";
    private Boolean guide = true; //是否进入引导界面

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        //判断是否进入引导页面
        guide = guide();

        initView();

    }

    @Override
    protected void onDestroy() {
        dotCurrentIndex = 0;
        dotImages = null;
        ll_dots = null;
        btn_jump = null;

        if (mViewPager != null) {
            mViewPager.setAdapter(null);
            mViewPager.addOnPageChangeListener(null);
            mViewPager = null;
            mAdapter = null;
            mOnPagerChangeListener = null;
        }
        guideImages = null;
        if (guideViews != null) {
            guideViews.clear();
            guideViews = null;
        }

        setContentView(R.layout.clear_null);
        super.onDestroy();
    }

    /**
     * 跳转主界面
     */
    private void jumpMainActivity() {
        if (guide) {
            //  记录已不是第一次进入软件
            SharedPreferences.Editor editor = mSharePreferences.edit();
            editor.putBoolean("guide", false);
            editor.commit();
        }

        Intent intent = new Intent(GuideActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

    }

    /**
     * 是否进入引导界面
     */
    private Boolean guide() {
        Boolean isGuide = null;
        mSharePreferences = getSharedPreferences(SHAREPREFERENCES_NAME, Context.MODE_PRIVATE);
        isGuide = mSharePreferences.getBoolean("guide", true);

        return isGuide;
    }


    /**
     * 初始化View，initialze some view
     */
    private void initView() {
        findLayout();

        if (guide) {
            initPagerViewData();
            initDotsData();
        }

        findView();
        setListener();
    }

    /**
     * 初始化引导页原始数据
     */
    private void initPagerViewData() {
        guideImages = new int[]{R.drawable.guide_one, R.drawable.guide_two, R.drawable
                .guide_three, R.drawable.guide_four};
        guideViews = new ArrayList<>();
        for (int i = 0; i < GUIDE_NUMBER; i++) {
            ImageView view = new ImageView(this);
            WeakReference<Bitmap> bitmao = new WeakReference<Bitmap>(BitmapFactory.decodeResource
                    (getResources(), guideImages[i]));
            view.setImageBitmap(bitmao.get());
            view.setScaleType(ImageView.ScaleType.FIT_XY);
            guideViews.add(view);
        }
    }

    /**
     * 初始化引导小圆点View
     */
    private void initDotsData() {
        dotImages = new ImageView[guideViews.size()];
        for (int i = 0; i < guideViews.size(); i++) {
            dotImages[i] = (ImageView) ll_dots.getChildAt(i);
            LogUtil.i("image", (dotImages[1] == null) + "");
            dotImages[i].setEnabled(false);
        }
        //将第一个小圆点设置为高亮
        dotImages[0].setEnabled(true);
        dotCurrentIndex = 0;
    }

    /**
     * 初始化Layout
     */
    private void findLayout() {
        ll_dots = (LinearLayout) findViewById(R.id.ll_dots);
        rl_guide = (RelativeLayout) findViewById(R.id.rl_guide);
        if (!guide) {
            ll_dots.setVisibility(View.INVISIBLE);// 如果不进入引导界面则不需要显示提小圆点
            rl_guide.setBackgroundResource(R.drawable.guide_one); //设置进入界面背景图

        }

    }

    /**
     * findView
     */
    private void findView() {
        btn_jump = (TextView) findViewById(R.id.tv_jump);
        btn_jump.setOnClickListener(this);

        if (guide) {
            btn_jump.setVisibility(View.INVISIBLE);

            mViewPager = (ViewPager) findViewById(R.id.viewPager);
            mOnPagerChangeListener = new mOnPageChangeListener();
            mAdapter = new MyViewPagerAdapter(guideViews);
            mViewPager.setAdapter(mAdapter);
            mViewPager.addOnPageChangeListener(mOnPagerChangeListener);
        } else {
            btn_jump.setVisibility(View.VISIBLE);
        }

    }

    /**
     * 添加响应事件
     */
    private void setListener() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_jump:
                jumpMainActivity();
                break;
            default:
                break;
        }
    }

    private class mOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (position < 0 || position > dotImages.length || position == dotCurrentIndex) {
                return;
            }

            dotImages[dotCurrentIndex].setEnabled(false);
            dotImages[position].setEnabled(true);
            dotCurrentIndex = position;

            //判断是否到达引导页的最后一页，是否显示跳转按钮
            if (dotCurrentIndex == dotImages.length - 1) {
                btn_jump.setVisibility(View.VISIBLE);
            } else {
                btn_jump.setVisibility(View.INVISIBLE);
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
