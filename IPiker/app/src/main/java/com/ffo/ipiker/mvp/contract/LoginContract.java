package com.ffo.ipiker.mvp.contract;

/**
 * Author: huchunhua
 * Time: 2017/10/9 11:09
 * Package: com.ffo.ipiker.mvp.protocol
 * Project: IPiker
 * Mail: 742295818@qq.com
 * Describe: 一句话描述
 */

public interface LoginContract {
    interface IView {
        /**
         * 登陆结果
         *
         * @param result
         * @param msg
         */
        void loginResult(Boolean result, String msg);

        /**
         * 账户和密码校验，为空校验结果
         *
         * @param type 1-表示账户，2-表示密码
         * @param msg
         */
        void checkFormatResult(int type, String msg);
    }

    interface IPresenter {

        void login(String account, String password);

        /**
         * 账户和密码校验，为空校验
         *
         * @param account
         * @param password
         * @return
         */
        Boolean checkFormat(String account, String password);
    }

}
