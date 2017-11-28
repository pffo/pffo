package com.ffo.ipiker.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.ffo.ipiker.R;
import com.ffo.ipiker.adapter.RecyclerViewAdapter;
import com.ffo.ipiker.adapter.SpaceItemDecoration;
import com.ffo.ipiker.model.ReportInfo;
import com.ffo.ipiker.util.LogUtil;
import com.ffo.ipiker.util.NetWorkUtil;
import com.ffo.ipiker.util.customView.LoadingMoreOnScrollListener;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: huchunhua
 * Time: 16:50
 * Package: com.ffo.ipiker.fragment
 * Project: IPiker
 * Mail: 742295818@qq.com
 * Describe: 一句话描述
 */

public class HomePageFragment extends BaseFragment {

    private String TGA = HomePageFragment.class.getSimpleName();

    private View view;
    //    private ViewPager mViewPager;//顶部信息推荐栏
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerViewAdapter mRecyclerViewAdapter;
    private List<View> mListDataViewPage; //顶部信息推荐栏数据源
    private SpaceItemDecoration mSpaceItemDecoration; // item间的间距

    private List<ReportInfo> reportInfos; // 基本数据源


    private Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frament_homepage, container, false);

        initData();
        initView();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtil.v(TGA, "onResume");

    }

    /**
     * 初始化一些基础数据
     */
    private void initData() {
        if (mListDataViewPage != null) {
            mListDataViewPage.clear();
            mListDataViewPage = null;
        } else {
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
                        .decodeResource(getResources(), resource[i]));
                ImageView imageView = new ImageView(context);
                imageView.setImageBitmap(bitmao.get());
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                mListDataViewPage.add(imageView);
            }

        }
        initDataReportInfo();

    }

    /**
     * RecyclerView 显示数据的初始化
     */
    private void initDataReportInfo() {
        if (reportInfos != null) {
            reportInfos.clear();
            reportInfos = null;
        }
        reportInfos = new ArrayList<>();
        //测试模拟数据
        for (int i = 0; i < 50; i++) {
            ReportInfo reportInfo = new ReportInfo();
            reportInfos.add(reportInfo);
        }

        //从本地数据库拿数据
        //从缓存中拿数据
        //从网络拿数据
    }


    /**
     * initView
     */
    private void initView() {
        findLayout();
        findView();
    }

    /**
     * findLayout
     */
    private void findLayout() {

    }

    /**
     * 初始化控件
     */
    private void findView() {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_homepage);
        initRecyclerView();
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.srl_homepage);
        initSwipeRefreshLayout();


    }

    /**
     * RecylcerView Operation
     */
    private void initRecyclerView() {
        mSpaceItemDecoration = new SpaceItemDecoration(30);
        mRecyclerViewAdapter = new RecyclerViewAdapter(context, reportInfos, mListDataViewPage);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setAutoMeasureEnabled(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(mSpaceItemDecoration);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setOnScrollListener(new LoadingMoreOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                //实现加载更多数据的方法
                hander.sendEmptyMessage(NetWorkUtil.HOMEPAGE_LOADING_MORE_OPERATION);
            }
        });
    }

    /**
     * SwipeRefreshLayout Operation
     */
    private void initSwipeRefreshLayout() {
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.green_28a57d); //
        // 设置下拉进度的背景颜色，默认就是白色的
        mSwipeRefreshLayout.setColorSchemeResources(R.color.green_00FF00, R.color.red_FF0000,
                R.color.blue_0000FF);//设置下拉主题颜色
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 开始刷新，设置当前为刷新状态
                //mSwipeRefreshLayout.setRefreshing(true);

                hander.sendEmptyMessage(NetWorkUtil.HOMEPAGE_PULL_DOWN_REFRESH_OPERATION);
                //模拟网络操作
                hander.postDelayed(new Runnable() {
                    @Override
                    public void run() {


                        Toast.makeText(context, "刷新了一条数据", Toast.LENGTH_SHORT).show();

                        // 加载完数据设置为不刷新状态，将下拉进度收起来
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 1200);

            }
        });
    }

    /**
     * 下拉刷新
     */
    private void doPullDownRefresh() {

        //完成后收起
        mSwipeRefreshLayout.setRefreshing(false);
    }

    /**
     * 加载更多
     */
    private void doLoadingMore() {
        reportInfos.add(new ReportInfo());
        mRecyclerViewAdapter.notifyDataSetChanged();
    }


    Handler hander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case NetWorkUtil.HOMEPAGE_LOADING_MORE_OPERATION:
                    doLoadingMore();
                    break;
                case NetWorkUtil.HOMEPAGE_PULL_DOWN_REFRESH_OPERATION:
                    doPullDownRefresh();
                    break;
                default:
                    break;
            }
        }
    };

}
