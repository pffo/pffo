package com.ffo.ipiker.mvp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.ffo.ipiker.fragment.BaseFragment;
import com.ffo.ipiker.mvp.IPresenter;
import com.ffo.ipiker.mvp.IView;
import com.ffo.ipiker.mvp.presenter.IPresenterImp;
import com.ffo.ipiker.util.LogUtil;

/**
 * Author: huchunhua
 * Time: 2017/10/9 14:03
 * Package: com.ffo.ipiker.mvp.ui
 * Project: IPiker
 * Mail: 742295818@qq.com
 * Describe: 一句话描述
 */

public abstract class BasePresenterFragment<P extends IPresenterImp> extends BaseFragment implements IView{
    private static String TAG = "BasePresenterFragment";

    protected IPresenter[] mAllPersenters;
    public P mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initPresenter();
    }

    /**
     * 获取layout
     */
    protected abstract void iniLayout();

    /**
     * 获取子类的IPresenter，一个Fragment有可能有多个presenter，需要由子类来实现
     * @return
     */
    protected abstract IPresenter[] getPresenter();

    /**
     * 初始化presenter
     */
    protected abstract void onInitPresenter();

    /**
     * 事件监听
     */
    protected  abstract void initEvent();

    /**
     * 从intent中解析数据，由具体子类实现
     */
    protected abstract void parseArgumentsFromIntent();

    private void addAllPresenter(){
        IPresenter[] iPresenters = getPresenter();
        if (iPresenters != null){
            mAllPersenters = iPresenters;
        }
    }

    /**
     * 初始化presenter,绑定View
     */
    private void initPresenter() {
        LogUtil.d(TAG, "initPresenter>>>>>>>>>>>>>>>>");
        mPresenter = setPresenter();
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
    }

    @Override
    public void onDestroy() {
        LogUtil.d(TAG, "onDestroy>>>>>>>>>>>>>>>>");
        if (mPresenter != null) {
            mPresenter.destoryView();
            mPresenter = null;
        }
        super.onDestroy();
    }

    abstract P setPresenter();
}
