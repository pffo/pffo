package com.ffo.ipiker.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ffo.ipiker.LoginActivity;
import com.ffo.ipiker.R;

/**
 * Author: huchunhua
 * Time: 16:51
 * Package: com.ffo.ipiker.fragment
 * Project: IPiker
 * Mail: 742295818@qq.com
 * Describe: 一句话描述
 */

public class PersonalPageFragment extends BaseFragment {
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
        view = inflater.inflate(R.layout.fragment_personalpage, container, false);

        //测试所用
        TextView login = (TextView) view.findViewById(R.id.btn_login_personal);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, LoginActivity.class);
                context.startActivity(intent);
            }
        });
        return view;
    }
}
