package com.ffo.ipiker.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ffo.ipiker.MainActivity;
import com.ffo.ipiker.R;
import com.ffo.ipiker.util.LogUtil;
import com.ffo.ipiker.util.StringUtil;
import com.ffo.ipiker.util.ViewUtils;

/**
 * Author: huchunhua
 * Time: 15:14
 * Package: com.ffo.ipiker.fragment
 * Project: IPiker
 * Mail: 742295818@qq.com
 * Describe: 一句话描述
 */

public class LoginFragment extends BaseFragment implements View.OnClickListener {

    private String TAG = this.getClass().getSimpleName();

    private Context context;

    private View view;
    private Button btn_login;
    private TextView tx_forgetPassword;
    private TextView tx_register;
    private EditText et_account;
    private EditText et_password;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);
        LogUtil.i("hch", "LoginFragment create" + this.getId());
        initView();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.i("hch", "LoginFragment Destroy" + this.getId());
        View rootView = getActivity().getWindow().getDecorView().findViewById(android.R.id.content);
        ViewUtils.unbindDrawables(rootView);
    }

    /**
     * 初始化View
     */
    private void initView() {

        findView();
    }

    /**
     * 初始化控件
     */
    private void findView() {
        btn_login = (Button) view.findViewById(R.id.btn_login);
        tx_forgetPassword = (TextView) view.findViewById(R.id.tx_forget_password);
        tx_register = (TextView) view.findViewById(R.id.tx_register);
        et_account = (EditText) view.findViewById(R.id.et_account_login);
        et_password = (EditText) view.findViewById(R.id.et_password_login);

        btn_login.setOnClickListener(this);
        tx_register.setOnClickListener(this);
        tx_forgetPassword.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                loginOperation();
                //登录
                break;
            case R.id.tx_forget_password:
                //忘记密码
                break;
            case R.id.tx_register:
                //注册
                registerOperation();
                break;
            default:
                break;
        }
    }

    /**
     * register operatoin
     */
    private void registerOperation() {
        ((CallFragmentValue) context).showFragment(getString(R.string.register), null);
    }

    /**
     * login operation
     */
    private void loginOperation() {
        String accoutStr = et_account.getText().toString();
        String passwordStr = et_password.getText().toString();
        if (StringUtil.isEmpty(accoutStr)) {
            //账号输入为空
            //还需要判断其账号格式是否正确（可在自定义控件里面添加）
            return;
        }
        if (StringUtil.isEmpty(passwordStr)) {
            //密码为空
            return;
        }

        Intent intent = new Intent(context, MainActivity.class);
        startActivity(intent);
        ((Activity) context).finish();

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };


}
