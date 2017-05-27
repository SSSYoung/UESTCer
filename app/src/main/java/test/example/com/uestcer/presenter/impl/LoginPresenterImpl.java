package test.example.com.uestcer.presenter.impl;

import com.hyphenate.chat.EMClient;


import test.example.com.uestcer.callback.MyEMCallBack;
import test.example.com.uestcer.presenter.LoginPresenter;
import test.example.com.uestcer.view.LoginView;

/**
 * Created by DK on 2017/5/2.
 */

public class LoginPresenterImpl implements LoginPresenter {
    private LoginView loginView;

    public LoginPresenterImpl(LoginView loginView) {
        this.loginView = loginView;
    }

    @Override
    public void login(final String username, String pwd) {
        /**
         * 用自己的EmCallBack。确保Success方法在主线程中执行
         */
        EMClient.getInstance().login(username, pwd, new MyEMCallBack() {
            @Override
            public void success() {
                loginView.onGetLoginState(username, true, null);

            }

            @Override
            public void Error(int i, String s) {
                loginView.onGetLoginState(username, false, s);
            }


        });
    }
}
