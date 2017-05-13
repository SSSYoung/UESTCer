package test.example.com.uestcer.presenter.impl;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SignUpCallback;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import test.example.com.uestcer.utils.ThreadUtils;
import test.example.com.uestcer.view.RegistView;

/**
 * Created by DK on 2017/4/27.
 */

public class RegistPresenterImpl implements RegistPresenter {
    //P层要持有一个View层的引用
    private RegistView registView;

    public RegistPresenterImpl(RegistView registView) {
        this.registView = registView;
    }

    @Override
    public void registUser(final String username, final String pwd) {
        //注册的逻辑
        final AVUser user = new AVUser();
        //设置用户名和密码
        user.setUsername(username);
        user.setPassword(pwd);
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(final AVException e) {
                if (e == null) {
                    //说明leanCloud注册成功了,切环信注册,需要联网， 去子线程处理
                    ThreadUtils.runOnNonUIThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                EMClient.getInstance().createAccount(username, pwd);
                                //注册成功了，返回界面的跳转,UI更新要在主线程中完成
                                ThreadUtils.runOnMainThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        registView.onGetRegistState(username, pwd, true, null);
                                    }
                                });
                            } catch (final HyphenateException e1) {
                                //环信没有注册成功,输出leanCloud中的数据
                                e1.printStackTrace();
                                try {
                                    user.delete();
                                } catch (AVException e2) {
                                    e2.printStackTrace();
                                }
                                //环信注册失败了，在UI中显示为啥失败
                                ThreadUtils.runOnMainThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        registView.onGetRegistState(username, pwd, false, e1.getDescription());
                                    }
                                });

                            }
                        }
                    });

                } else {
                    //leanCloud注册失败了,通知UI更新进行提示注册失败了
                    registView.onGetRegistState(username, pwd, false, e.getMessage());
                }
            }
        });
    }
}
