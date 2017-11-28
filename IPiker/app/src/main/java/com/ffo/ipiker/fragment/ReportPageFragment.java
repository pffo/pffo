package com.ffo.ipiker.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ffo.ipiker.R;
import com.ffo.ipiker.Test;
import com.ffo.ipiker.activity.EvidenceActiviry;

/**
 * Author: huchunhua
 * Time: 16:50
 * Package: com.ffo.ipiker.fragment
 * Project: IPiker
 * Mail: 742295818@qq.com
 * Describe: 一句话描述
 */

public class ReportPageFragment extends BaseFragment {
    private View view;
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
        view = inflater.inflate(R.layout.fragment_reportpage, container, false);

        //测试所用
        TextView tv_report = (TextView) view.findViewById(R.id.tv_report);
        tv_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EvidenceActiviry.class);
                context.startActivity(intent);
            }
        });

        return view;
    }

}
