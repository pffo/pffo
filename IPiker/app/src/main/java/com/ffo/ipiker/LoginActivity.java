package com.ffo.ipiker;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.ffo.ipiker.activity.BaseActivity;
import com.ffo.ipiker.fragment.LoginFragment;
import com.ffo.ipiker.fragment.RegisterFragment;
import com.ffo.ipiker.util.LogUtil;

import java.util.HashMap;

/**
 * Author: huchunhua
 * Time: 15:10
 * Package: com.ffo.ipiker
 * Project: IPiker
 * Mail: 742295818@qq.com
 * Describe: 登陆，注册，信息修改等
 */

public class LoginActivity extends BaseActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);
        LogUtil.i(TAG, "LoginActivity create ");
        initView();

        if (savedInstanceState == null) {
            initFragmentParamers();
            switchFragment(R.string.login);
        }
    }

    /**
     * initView
     */
    private void initView() {
        initFragment();
        findView();
    }

    /**
     * findView
     */
    private void findView() {
    }

    /**
     * initFragmentParamers
     */
    public void initFragmentParamers() {
        content = R.id.content;
        initFragment();
    }

    /**
     * initFragment  LoginFragment/RegisterFragment
     */
    private void initFragment() {
        if (fragments != null) {
            fragments.clear();
            fragments = null;
        }
        fragments = new HashMap<>();
        LoginFragment loginFragment = new LoginFragment();
        fragments.put(getString(R.string.login), loginFragment);
        RegisterFragment registerFragment = new RegisterFragment();
        fragments.put(getString(R.string.register), registerFragment);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.i(TAG, "LoginActivity destroy ");
        setContentView(R.layout.clear_null);
    }


}
