package com.ffo.ipiker.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.ffo.ipiker.R;
import com.ffo.ipiker.model.ReportInfo;

/**
 * Author: huchunhua
 * Time: 2017/8/30 16:08
 * Package: com.ffo.ipiker.activity
 * Project: IPiker
 * Mail: 742295818@qq.com
 * Describe: 一句话描述
 */

public class ReportDetailActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_detail);

        ReportInfo reportInfo = (ReportInfo) getIntent().getSerializableExtra("reportInfo");
        if (reportInfo == null) {
            Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "value", Toast.LENGTH_SHORT).show();
        }
    }
}
