package test.example.com.uestcer.presenter.impl;

import com.hyphenate.chat.EMClient;

import test.example.com.uestcer.view.SplashView;

/**
 * Created by DK on 2017/4/26.
 */

public class SplashPresenterImpl implements SplashPresenter {
    /**
     *  View层的接口
     */
    private SplashView splashView;

    /**
     *  构造方法，构造的时候传入view接口的具体实现  通过这个实现 调用view 层的业务逻辑
     * @param splashView
     */
    public SplashPresenterImpl(SplashView splashView) {
        this.splashView = splashView;
    }

    @Override
    public void checkLogin() {
        //假如已经登录并在线
        if (EMClient.getInstance().isLoggedInBefore()){
            if (EMClient.getInstance().isConnected()){
                //调用splashview里面的方法，方法的实现在activity中完成
                splashView.onGetLoginState(true);
            }
        }else {
            splashView.onGetLoginState(false);
        }
    }
}
