package com.ffo.ipiker.adapter.holder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ffo.ipiker.R;
import com.ffo.ipiker.util.DisplayScreen;
import com.ffo.ipiker.util.LogUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: huchunhua
 * Time: 2017/8/30 10:16
 * Package: com.ffo.ipiker.adapter.holder
 * Project: IPiker
 * Mail: 742295818@qq.com
 * Describe: 一句话描述
 */

public class RecommendHolder extends RecyclerView.ViewHolder {
    private static final String TAG = RecommendHolder.class.getSimpleName();
    public ViewPager vp_recommend;
    private LinearLayout ll_dots_recommend_homepage;

    private List<View> mListDataViewPage; //顶部信息推荐栏数据源

    private int dotCurrentIndex; //顶部信息推荐栏小圆点偏移量
    private ImageView[] dotImages; //顶部信息推荐栏引导小圆点
    private MyOnPageChangeListener myOnPageChangeListener;

    private Context context;

    public RecommendHolder(View itemView, List<View> mListDataViewPage) {
        super(itemView);
        context = itemView.getContext();
        this.mListDataViewPage = mListDataViewPage;

        vp_recommend = (ViewPager) itemView.findViewById(R.id.vp_recommend_homepage);
        ll_dots_recommend_homepage = (LinearLayout) itemView.findViewById(R.id
                .ll_dots_recommecd_homepage);

        //设置Viewpager的高度为屏幕高度的五分之一
        int screenHeight = DisplayScreen.getDisplayScreenHeight();
        ViewGroup.LayoutParams layoutParams = vp_recommend.getLayoutParams();
        layoutParams.height = screenHeight / 5;//屏幕宽高的五分之一
        vp_recommend.setLayoutParams(layoutParams);

        initDots();
        initDataForRecommend();

        if (myOnPageChangeListener == null) {
            myOnPageChangeListener = new MyOnPageChangeListener(vp_recommend);
        }
        vp_recommend.setOnPageChangeListener(myOnPageChangeListener);
    }


    /**
     * 初始化底部推信息推荐栏引导小圆点View
     */

    private void initDots() {
        LogUtil.v(TAG, "initDots()");
        if (dotImages == null) {
            dotImages = new ImageView[mListDataViewPage.size()];
            for (int i = 0; i < mListDataViewPage.size(); i++) {
                dotImages[i] = (ImageView) ll_dots_recommend_homepage.getChildAt(i);
                dotImages[i].setEnabled(false);
            }
            //将第一个小圆点设置为高亮
            dotImages[1].setEnabled(true);
            dotCurrentIndex = 1;
        }

    }

    private void initDataForRecommend() {
        if (mListDataViewPage == null) {
            mListDataViewPage = new ArrayList<>();
            //这里的数据作为测试用，推荐栏的数据源应该来自于服务端或者本地数据库
            // 为了实现无限循环，首位两张图片都是重复的
            int[] resource = new int[]{
                    R.drawable.test_viewpager_homepage_4, R.drawable.test_viewpager_homepage_1, R
                    .drawable.test_viewpager_homepage_2, R
                    .drawable.test_viewpager_homepage_3, R.drawable.test_viewpager_homepage_4, R
                    .drawable.test_viewpager_homepage_1,};

            for (int i = 0; i < 6; i++) {
                WeakReference<Bitmap> bitmao = new WeakReference<Bitmap>(BitmapFactory
                        .decodeResource(context.getResources(), resource[i]));
                ImageView imageView = new ImageView(context);
                imageView.setImageBitmap(bitmao.get());
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                mListDataViewPage.add(imageView);
            }
        }
    }

    private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        ViewPager mViewPager;

        public MyOnPageChangeListener(ViewPager viewpager) {
            mViewPager = viewpager;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (position < 0 || position > mListDataViewPage.size() || position ==
                    dotCurrentIndex) {
                return;
            }

            if (mListDataViewPage.size() > 1) { //多于1，才会循环跳转
                if (position < 1) { //首位之前，跳转到末尾（N）
                    position = 4;
                    mViewPager.setCurrentItem(position, false);
                } else if (position > 4) { //末位之后，跳转到首位（1）
                    position = 1;
                    mViewPager.setCurrentItem(position, false); //false:不显示跳转过程的动画

                } else {
                    dotImages[dotCurrentIndex].setEnabled(false);
                    dotImages[position].setEnabled(true);
                    dotCurrentIndex = position;
                }
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }



}
