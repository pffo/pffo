package com.ffo.ipiker.activity;

import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.ffo.ipiker.receiver.NetWorkChangeBroadcastReceiver;
import com.ffo.ipiker.util.StringUtil;

import java.util.HashMap;

/**
 * @Author: huchunhua
 * @Time: 2017/7/18 14:16
 * @Package: com.ffo.ipiker
 * @Project: IPiker
 * @Mail: 742296818@qq.com
 * @Describe: 一句话描述
 */
public class BaseActivity extends AppCompatActivity {
    private IntentFilter intentFilter;
    private NetWorkChangeBroadcastReceiver netWorkChangeBroadcastReceiver;
    /**
     * 切换Fragment时显示的Fragment
     */
    protected String currentFragmeng;
    /**
     * Fragments
     */
    protected HashMap<String, Fragment> fragments;
    /**
     * fragment切换view
     */
    protected int content;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        registerNetWorkChangeBroadcast();
         // 添加到activity的管理队列
        ActivityManager.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (fragments != null) {
            fragments.clear();
            fragments = null;
        }
        unEegisterNetWorkChangeBroadcast();
        //从activity的管理队列移除
        ActivityManager.removeActivity(this);
    }

    /**
     * 注册网络变化监听广播
     */
    private void registerNetWorkChangeBroadcast() {
        intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        netWorkChangeBroadcastReceiver = new NetWorkChangeBroadcastReceiver();
        registerReceiver(netWorkChangeBroadcastReceiver, intentFilter);
    }

    /**
     * 取消网络变化监听广播
     */
    private void unEegisterNetWorkChangeBroadcast() {
        unregisterReceiver(netWorkChangeBroadcastReceiver);
        intentFilter = null;
        netWorkChangeBroadcastReceiver = null;
    }

    /**
     * Fragment切换
     *
     * @param intTag 即将显示的Fragment在bottomFragments中的Key值
     */
    protected void switchFragment(int intTag) {
        String tag = getString(intTag);
        if (StringUtil.isEmpty(tag)) {
            return;
        }
        if (tag == currentFragmeng) {
            return;
        }
        FragmentTransaction mFragementTransaction = getSupportFragmentManager().beginTransaction();
        if (currentFragmeng == null) {
            mFragementTransaction.add(content, fragments.get(tag), tag);
        } else {
            Fragment show = fragments.get(tag);
            Fragment old = fragments.get(currentFragmeng);
            if (show != null && old != null) {
                if (show.isAdded()) {
                    mFragementTransaction.hide(old).show(show);
                } else {
                    mFragementTransaction.hide(old).add(content, show, tag);
                    mFragementTransaction.addToBackStack(tag);
                }
            }
        }
        mFragementTransaction.commit();
//        showToolBarTitle(getString(intTag));
        currentFragmeng = tag;

    }


    /**
     * 子类必须重写此方法
     */
    public void initFragmentParamers() {

    }
}

