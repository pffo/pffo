package com.ffo.ipiker.mvp;

/**
 * Author: huchunhua
 * Time: 2017/9/29 9:51
 * Package: com.ffo.ipiker.mvp
 * Project: IPiker
 * Mail: 742295818@qq.com
 * Describe: 一句话描述
 */

public interface IPresenter<V extends IView> {
    void attachView(V view);//绑定View

    void destoryView();//销毁View，防止内存泄露

    IView getView();
}
