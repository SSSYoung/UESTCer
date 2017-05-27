package test.example.com.uestcer.view;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import butterknife.ButterKnife;
import butterknife.InjectView;

import butterknife.OnClick;
import test.example.com.uestcer.R;
import test.example.com.uestcer.presenter.RegistPresenter;
import test.example.com.uestcer.presenter.impl.RegistPresenterImpl;
import test.example.com.uestcer.utils.StringUtils;

/**
 * Created by DK on 2017/4/27.
 */

public class RegistActivity extends BaseActivity implements RegistView {
    @InjectView(R.id.iv_avatar)
    ImageView ivAvatar;
    @InjectView(R.id.et_username)
    EditText etUsername;
    @InjectView(R.id.til_username)
    TextInputLayout tilUsername;
    @InjectView(R.id.et_pwd)
    EditText etPwd;
    @InjectView(R.id.til_pwd)
    TextInputLayout tilPwd;
    @InjectView(R.id.btn_regist)
    Button btnRegist;
    //要持有一个present层
    RegistPresenter registPresenter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            //有的手机没有实体按键 是通过虚拟的底部导航实现的 home 返回的效果
//            window.setNavigationBarColor(Color.TRANSPARENT);
        }
        ButterKnife.inject(this);
        registPresenter=new RegistPresenterImpl(this);

    }
    @OnClick(R.id.btn_regist)
    public void onClick(){
        //获取用户名和密码
        String username=etUsername.getText().toString().trim();
        String pwd=etUsername.getText().toString().trim();
        //校验用户名输入是否合法
        if (!StringUtils.checkUsername(username)){
            tilUsername.setErrorEnabled(true);
            tilUsername.setError("用户名不合法");
            return;
        }else {
            tilUsername.setErrorEnabled(false);
        }

        //校验密码输入是否合法
        //校验输入是否合法
        if( !StringUtils.Checkpwd(pwd)){
            tilPwd.setErrorEnabled(true);
            tilPwd.setError("密码不合法");
            return;
        }else{
            tilPwd.setErrorEnabled(false);
        }
        showProgressDialog("正在注册");
        /**
         * 当showProgressDialog时实现注册业务层的逻辑
         */
        registPresenter.registUser(username,pwd);

    }
    @Override
    public void onGetRegistState(String username, String pwd, boolean isSuccess, String errorMsg) {
        //到这儿了说明已经得到了注册的结果，无论是否成功，然后根据结果做UI变化
        cancelProgressDialog();
        if (isSuccess)
        {
            showToast("注册成功");
            //吧用户名和密码存入sp
            saveUsernamePwd(username,pwd);
            //跳入登录界面
            startActivity(LoginActivity.class,true);
        }else {
            showToast("注册失败:"+errorMsg);
        }
    }
}
