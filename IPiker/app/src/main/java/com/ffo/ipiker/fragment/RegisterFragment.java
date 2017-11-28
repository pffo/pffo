package com.ffo.ipiker.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ffo.ipiker.R;
import com.ffo.ipiker.util.LogUtil;

/**
 * Author: huchunhua
 * Time: 15:17
 * Package: com.ffo.ipiker.fragment
 * Project: IPiker
 * Mail: 742295818@qq.com
 * Describe: 一句话描述
 */

public class RegisterFragment extends BaseFragment {
    private String TGA = this.getClass().getSimpleName();

    private Context context;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogUtil.i("hch","RegisterFragment create "+this.getId());
        return inflater.inflate(R.layout.fragment_register,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Button button = (Button) getActivity().findViewById(R.id.registerbutton_register);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CallFragmentValue)getActivity()).showFragment(getString(R.string.login), null);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.i("hch","RegisterFragment destroy "+this.getId());
    }
}
