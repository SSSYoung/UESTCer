package test.example.com.uestcer.callback;

import com.hyphenate.EMCallBack;

import test.example.com.uestcer.utils.ThreadUtils;

/**
 * Created by DK on 2017/5/2.
 * 自定义一个EMCallback接口，确保success后在主线程中执行
 *
 * 定义为一个抽象类，继承EMCallBack接口，success()方法写为抽象
 *
 */
public abstract class MyEMCallBack implements EMCallBack {

    public abstract void success();
    public abstract void Error(int i, String s);
    @Override
    public void onSuccess() {
        ThreadUtils.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                success();
            }
        });
    }

    @Override
    public void onError(final int i, final String s) {
        ThreadUtils.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                Error(i, s);
            }
        });
    }

    @Override
    public void onProgress(int i, String s) {
        //在加载的过程中
    }
}
