package com.ffo.ipiker.adapter.holder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ffo.ipiker.R;
import com.ffo.ipiker.activity.ReportDetailActivity;
import com.ffo.ipiker.model.ReportInfo;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Author: huchunhua
 * Time: 2017/8/30 10:17
 * Package: com.ffo.ipiker.adapter.holder
 * Project: IPiker
 * Mail: 742295818@qq.com
 * Describe: 一句话描述
 */

public class NormalHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private LinearLayout recyclerview_holder_normal;
    public CircleImageView user_circleimage_normal_item;
    public ImageView iv_report_thumbnail;
    public TextView user_name_normal_item;
    public TextView report_time_normal_item;
    public TextView tv_report_describe;
    public TextView tv_location_item;
    private Context context;

    public ReportInfo reportInfo;//此item的reportInfo;

    public NormalHolder(View itemView) {
        super(itemView);
        context = itemView.getContext();
        findView(itemView);
        setListener();

    }

    /**
     * findView
     *
     * @param itemView
     */
    private void findView(View itemView) {
        recyclerview_holder_normal = (LinearLayout) itemView.findViewById(R.id
                .recyclerview_holder_normal);
        user_circleimage_normal_item = (CircleImageView) itemView.findViewById(R.id
                .user_circleimage_normal_item);
        iv_report_thumbnail = (ImageView) itemView.findViewById(R.id.iv_report_thumbnail);
        user_name_normal_item = (TextView) itemView.findViewById(R.id.user_name_normal_item);
        report_time_normal_item = (TextView) itemView.findViewById(R.id.report_time_normal_item);
        tv_report_describe = (TextView) itemView.findViewById(R.id.tv_report_describe);
        tv_location_item = (TextView) itemView.findViewById(R.id.tv_location_item);
    }

    /**
     * setListener
     */
    private void setListener() {
        recyclerview_holder_normal.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.recyclerview_holder_normal:
                if (reportInfo != null) {
                    Intent intent = new Intent(context, ReportDetailActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("reportInfo", reportInfo);
                    intent.putExtra("reportInfo", reportInfo);
                    context.startActivity(intent);
                }
                break;
            default:
                break;
        }
    }

    public void setReportInfo(ReportInfo reportInfo) {
        this.reportInfo = reportInfo;
    }
}
