package com.ffo.ipiker.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ffo.ipiker.R;
import com.ffo.ipiker.adapter.holder.LoadingMore;
import com.ffo.ipiker.adapter.holder.NormalHolder;
import com.ffo.ipiker.adapter.holder.RecommendHolder;
import com.ffo.ipiker.model.ReportInfo;
import com.ffo.ipiker.util.NetWorkUtil;

import java.util.List;

/**
 * Author: huchunhua
 * Time: 19:07
 * Package: com.ffo.ipiker.adapter
 * Project: IPiker
 * Mail: 742295818@qq.com
 * Describe: 一句话描述
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = RecyclerViewAdapter.class.getSimpleName();

    //item type
    private static final int RECYCLERVIEW_ITEM_TYPE_NORMAL = 1000; //normal
    private static final int RECYCLERVIEW_ITEM_TYPE_RECOMMEND = 1001; //信息推荐栏
    private static final int RECYCLERVIEW_ITEM_TYPE_LOADING_MORE = 1002; //加载更多

    private Context context;

    private List<ReportInfo> reportInfos;
    private List<View> mListDataViewPage; //顶部信息推荐栏数据源

    private LayoutInflater layoutInflater;
    private MyViewPagerAdapter mViewPagerAdapter;

    private int headerCount = 1;

    public RecyclerViewAdapter(Context context, List<ReportInfo> reportInfos, List<View>
            mListDataViewPage) {
        this.reportInfos = reportInfos;
        this.mListDataViewPage = mListDataViewPage;
        this.context = context;

        if (context != null) {
            layoutInflater = LayoutInflater.from(context);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        if (layoutInflater != null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        switch (viewType) {
            case RECYCLERVIEW_ITEM_TYPE_RECOMMEND:
                viewHolder = new RecommendHolder(layoutInflater.inflate(R.layout
                        .recyclerview_holder_commend, parent, false), mListDataViewPage);
                break;
            case RECYCLERVIEW_ITEM_TYPE_LOADING_MORE:
                viewHolder = new LoadingMore(layoutInflater.inflate(R.layout
                        .recyclerview_footer_loading_more, parent, false));
                break;
            default:
                viewHolder = new NormalHolder(layoutInflater.inflate(R.layout
                        .recyclerview_holder_normal, parent, false));
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NormalHolder) {
            doWithNormalHolder((NormalHolder) holder, position);
        } else if (holder instanceof RecommendHolder) {
            doWithRecommendHolder((RecommendHolder) holder);
        } else if (holder instanceof LoadingMore) {
            doWithLoadingMoreHolder((LoadingMore) holder);
        }
    }


    @Override
    public int getItemCount() {
        return reportInfos == null ? 0 + 1 : reportInfos.size() + headerCount;
    }

    @Override
    public int getItemViewType(int position) {
        int type;
        switch (position) {
            case 0:
                type = RECYCLERVIEW_ITEM_TYPE_RECOMMEND;
                break;
            default:
                type = RECYCLERVIEW_ITEM_TYPE_NORMAL;
        }

        if (position >= (getItemCount() - 1)) {
//            LogUtil.v(TAG, "getItemCount() + headerCount ：" + getItemCount());
//            LogUtil.v(TAG, "position ：" + position);
            type = RECYCLERVIEW_ITEM_TYPE_LOADING_MORE;
        }

        return type;
    }

    /**
     * 主页的推荐栏 Viewpager
     *
     * @param holder
     */
    private void doWithRecommendHolder(RecommendHolder holder) {
        if (mViewPagerAdapter == null) {
            mViewPagerAdapter = new MyViewPagerAdapter(mListDataViewPage);
            holder.vp_recommend.setAdapter(mViewPagerAdapter);
        }
        holder.vp_recommend.setCurrentItem(1, false); //默认选中第二张图片

    }

    /**
     * RecyclerView 普通item
     *
     * @param holder
     */
    private void doWithNormalHolder(NormalHolder holder, int postion) {
        ReportInfo reportInfo = reportInfos.get(postion);
        if (reportInfo == null) {
            return;
        }
        if (reportInfo.getUrl_img() == null && reportInfo.getUrl_vid() == null) {
            // 如果只有文字信息
//            holder.iv_report_thumbnail.setVisibility(View.GONE);
        }

        holder.setReportInfo(reportInfo);
    }

    /**
     * footer view
     *
     * @param holder
     */
    private void doWithLoadingMoreHolder(LoadingMore holder) {
        NetWorkUtil.isNetWork();//返回终端是否联网。以判断加载更多的显示信息/没有网络信息

    }
}
