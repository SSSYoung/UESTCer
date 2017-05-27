package test.example.com.uestcer.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import test.example.com.uestcer.MainActivity;
import test.example.com.uestcer.R;
import test.example.com.uestcer.presenter.SplashPresenter;
import test.example.com.uestcer.presenter.impl.SplashPresenterImpl;

/**
 * Created by DK on 2017/4/26.
 */

public class SplashActivity extends BaseActivity implements SplashView {
    private SplashPresenter splashPresenter;
    @InjectView(R.id.iv_splash)
    ImageView ivSplash;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        Log.e("SplashActivity", "onCreate: splash");
        //用butterKnife找控件
        ButterKnife.inject(this);
        //view层要持有一个presenter的引用,splashPresenter是SplashPresenterImpl的一个实现类
        splashPresenter = new SplashPresenterImpl(this);
        //检测登录状态在splashPresenter中完成
        splashPresenter.checkLogin();
    }

    @Override
    public void onGetLoginState(boolean isLogin) {
        if (isLogin){
            startActivity(MainActivity.class, true);

        }
        else {
            ObjectAnimator alpha = ObjectAnimator.ofFloat(ivSplash, "alpha", 0, 1);
            alpha.start();
            alpha.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    startActivity(LoginActivity.class, true);
                }
            });
        }
    }
}
