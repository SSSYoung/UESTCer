package test.example.com.uestcer.view;

/**
 * Created by DK on 2017/4/27.
 */


public interface RegistView {
    /**
     * 当获取到注册的状态之后 做进一步的操作 如果注册成功 跳转到登陆页面 如果注册失败 弹toast
     *
     * @param username
     * @param psw
     * @param isSuccess 是否注册成功
     * @param errorMsg  错误信息
     */
    void onGetRegistState(String username, String psw, boolean isSuccess, String errorMsg);
}
