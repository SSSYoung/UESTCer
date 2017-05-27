package test.example.com.uestcer.view;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import test.example.com.uestcer.MainActivity;
import test.example.com.uestcer.R;
import test.example.com.uestcer.presenter.LoginPresenter;
import test.example.com.uestcer.presenter.impl.LoginPresenterImpl;

/**
 * Created by fullcircle on 2016/12/31.
 */
public class LoginActivity extends BaseActivity implements LoginView{
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
    @InjectView(R.id.btn_login)
    Button btnLogin;
    @InjectView(R.id.tv_newuser)
    TextView tvNewuser;

    LoginPresenter presenter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            //有底部导航栏的手机 虚拟home键 如果加上下面一句 底部的控制栏看不清了
//            window.setNavigationBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
        //获取缓存的用户名密码
        if(getPwd()!=null&&getPwd().trim().length()>0)
            etPwd.setText(getPwd());
        if(getUsername()!=null&&getUsername().trim().length()>0)
            etUsername.setText(getUsername());

        presenter = new LoginPresenterImpl(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
       // showToast("onNewIntent");
        //获取缓存的用户名密码
        if(getPwd()!=null&&getPwd().trim().length()>0)
            etPwd.setText(getPwd());
        if(getUsername()!=null&&getUsername().trim().length()>0)
            etUsername.setText(getUsername());
    }

    @OnClick({R.id.btn_login, R.id.tv_newuser})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                login();

                break;
            case R.id.tv_newuser:
                startActivity(RegistActivity.class,false);
                break;
        }
    }

    private void login() {
        showProgressDialog("正在登录... ...");
        String username = etUsername.getText().toString().trim();
        String pwd = etPwd.getText().toString().trim();
        //判断sdk版本 如果是6.0以后
        if(Build.VERSION.SDK_INT<23){
            presenter.login(username,pwd);
        }else{
            // 如果用到隐私的权限 6.0之后 需要手动检测是否有权限
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PermissionChecker.PERMISSION_GRANTED){
                //如果有权限的话就执行登录
                presenter.login(username,pwd);
            }else{
                //如果没有权限 就动态申请
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
            }
        }
    }

    @Override
    public void onGetLoginState(String username, boolean isLogin, String errorMsg) {
        cancelProgressDialog();
        if(isLogin)
        //如果登录成功跳到主界面
        startActivity(MainActivity.class,true);
        else{
            //如果登录失败弹吐司
            showToast(errorMsg);
        }
    }

    //动态申请权限之后 会走回调
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //requestCode 区分不同的权限申请操作 注意requestCode不能<0
        //permissions 动态申请的权限数组
        //grantResults int数组 用来封装每一个权限授权的结果  PermissionChecker.PERMISSION_GRANTED 授权了
//        PermissionChecker.PERMISSION_DENIED; 拒绝了
        if(grantResults[0]== PermissionChecker.PERMISSION_GRANTED){
            //说明给权限了
            login();
        }else{
            //说明没给权限
            showToast("没授权sd卡权限 数据库保存会失败 影响使用");
        }

    }
}
