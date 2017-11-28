package com.ffo.ipiker.mvp.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.ffo.ipiker.activity.BaseActivity;
import com.ffo.ipiker.mvp.IView;
import com.ffo.ipiker.mvp.presenter.IPresenterImp;
import com.ffo.ipiker.util.LogUtil;

/**
 * Author: huchunhua
 * Time: 2017/10/9 10:32
 * Package: com.ffo.ipiker.mvp.ui
 * Project: IPiker
 * Mail: 742295818@qq.com
 * Describe: 一句话描述
 */

public class BasePresenterActivity<P extends IPresenterImp> extends BaseActivity implements IView {
    private static String TAG = "BasePresenterActivity";

    private P mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initPresenter();
    }

    /**
     * 初始化presenter,绑定View
     */
    private void initPresenter() {
        LogUtil.d(TAG, "initPresenter>>>>>>>>>>>>>>>>");
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
    }

    @Override
    protected void onDestroy() {
        LogUtil.d(TAG, "onDestroy>>>>>>>>>>>>>>>>");
        if (mPresenter != null) {
            mPresenter.destoryView();
            mPresenter = null;
        }
        super.onDestroy();
    }

}
