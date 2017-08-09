package test.example.com.uestcer.presenter.impl;

import com.hyphenate.chat.EMClient;

import test.example.com.uestcer.callback.MyEMCallBack;
import test.example.com.uestcer.presenter.LogoutPresenter;
import test.example.com.uestcer.view.PluginView;

/**
 * Created by oo on 2017/7/14.
 */

public class LogoutPresenterImpl implements LogoutPresenter {
    private PluginView pluginView;
    public LogoutPresenterImpl(PluginView pluginView){
        this.pluginView=pluginView;
    }
    @Override
    public void logout() {
        EMClient.getInstance().logout(true, new MyEMCallBack() {
            @Override
            public void success() {
                pluginView.onlogout(true,null);
            }

            @Override
            public void Error(int i, String s) {
                pluginView.onlogout(false,s);
            }
        });
    }
}
