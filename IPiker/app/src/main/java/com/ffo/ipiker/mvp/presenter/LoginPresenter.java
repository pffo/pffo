package com.ffo.ipiker.mvp.presenter;

import com.ffo.ipiker.mvp.contract.LoginContract;
import com.ffo.ipiker.mvp.ui.LoginFragment;
import com.ffo.ipiker.util.StringUtil;

/**
 * Author: huchunhua
 * Time: 2017/9/29 9:53
 * Package: com.ffo.ipiker.mvp.presenter
 * Project: IPiker
 * Mail: 742295818@qq.com
 * Describe: 一句话描述
 */

public class LoginPresenter extends IPresenterImp<LoginFragment> implements LoginContract.IPresenter{
    private static int CHECK_FORMAT_RESULT_ACCOUNT = 1; //标识为账户
    private static int CHECK_FORMAT_RESULT_PASSWORD = 2; //标识为密码
    private LoginContract.IView iView;

    public LoginPresenter(LoginContract.IView iView) {
        this.iView = iView;
    }

    @Override
    public void login(String account, String password) {
        if (checkFormat(account,password)) {
            iView.loginResult(true, "login result");
        }
    }

    @Override
    public Boolean checkFormat(String account, String password) {
        if (StringUtil.isEmpty(account)) {
            //账号输入为空
            //还需要判断其账号格式是否正确（可在自定义控件里面添加）
            iView.checkFormatResult(CHECK_FORMAT_RESULT_ACCOUNT,"账户无效");
            return false;
        }
        if (StringUtil.isEmpty(password)) {
            //密码为空
            iView.checkFormatResult(CHECK_FORMAT_RESULT_PASSWORD,"密码无效");
            return false;
        }
        return true;
    }
}
