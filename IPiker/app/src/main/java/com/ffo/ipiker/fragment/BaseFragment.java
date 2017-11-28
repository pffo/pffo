package com.ffo.ipiker.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ffo.ipiker.util.LogUtil;

/**
 * Author: huchunhua
 * Time: 15:12
 * Package: com.ffo.ipiker.fragment
 * Project: IPiker
 * Mail: 742295818@qq.com
 * Describe: 一句话描述
 */

public class BaseFragment extends Fragment {

    private static String STATE_SAVE_IS_HIDDEN = "STATE_SAVE_IS_HIDDEN";
    private static String TAG = BaseFragment.class.getSimpleName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            boolean isSupporHidden = savedInstanceState.getBoolean(STATE_SAVE_IS_HIDDEN);
            judgeState(isSupporHidden);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogUtil.i(TAG, "BaseFragment create" + this.getId());

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        LogUtil.i(TAG, "BaseFragment destroy" + this.getId());
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        LogUtil.i(TAG, "BaseFragment onSaveInstanceState（）---内存重启，保存fragment显示状态");
        outState.putBoolean(STATE_SAVE_IS_HIDDEN, isHidden());//fragment内存异常回收时，保存其显示状态
    }

    /**
     * 判断fragment被意外回收后。也就是内存重启后，是否需要重新显示
     *
     * @param isSupporHidden true-内存重启前改Fragment是hide状态，反之是显示状态
     */
    private void judgeState(boolean isSupporHidden) {
        FragmentTransaction mFragmentTransaction = getFragmentManager().beginTransaction();
        if (isSupporHidden) {
            mFragmentTransaction.hide(this);
        } else {
            mFragmentTransaction.show(this);
        }
        mFragmentTransaction.commit();
    }
}
