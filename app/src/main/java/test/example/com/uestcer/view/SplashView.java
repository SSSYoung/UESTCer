package test.example.com.uestcer.view;

/**
 * Created by DK on 2017/4/26.
 */

public interface SplashView {
    /**
     * 根据现在是否已经登录  处理要跳转的逻辑
     * @param isLogin 是否已经登录
     */
    void onGetLoginState(boolean isLogin);
}
