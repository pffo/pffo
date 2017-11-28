package com.ffo.ipiker.mvp.presenter;

import android.view.View;

import com.ffo.ipiker.mvp.IPresenter;
import com.ffo.ipiker.mvp.IView;
import com.ffo.ipiker.util.LogUtil;

/**
 * Author: huchunhua
 * Time: 2017/10/9 10:19
 * Package: com.ffo.ipiker.mvp.presenter
 * Project: IPiker
 * Mail: 742295818@qq.com
 * Describe: 一句话描述
 */

public class IPresenterImp<V extends IView> implements IPresenter {
    private static String TAG = "IPresenterImp";

    private V view;

    @Override
    public void attachView(IView view) {
        LogUtil.d(TAG, "attachView>>>>>>>>>>>>>>>>>");
        if (view == null) {
            LogUtil.d(TAG, "attachView>>>>>>>>>>>>>>>>> view is null");
        }

        this.view = (V) view;
    }

    @Override
    public void destoryView() {
        LogUtil.d(TAG, "destoryView>>>>>>>>>>>>>>>>>");
        if (view != null) {
            view = null;
        }
    }

    @Override
    public V getView() {
        LogUtil.d(TAG, "getView>>>>>>>>>>>>>>>>>");
        if (view == null) {
            LogUtil.d(TAG, "getView>>>>>>>>>>>>>>>>> view is null");
        }

        return view;
    }
}
