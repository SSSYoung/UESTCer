package test.example.com.uestcer.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import test.example.com.uestcer.Constant;
import test.example.com.uestcer.utils.ToastUtils;

/**
 * Created by DK on 2017/4/27.
 */

public class BaseActivity extends AppCompatActivity {
    private SharedPreferences sp;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp=getSharedPreferences("config",MODE_PRIVATE);
    }

    /**
     *  自定义的一个开启activity 的方法，
     * @param class1 要开启的activity
     * @param isFinish 为真时，一开启另一个activity就finish本activity
     */
    protected void startActivity(Class class1, boolean isFinish){
        startActivity(new Intent(getApplicationContext(),class1));
        if (isFinish){
            finish();
        }
    }

    /**
     * baseActivity的showToast方法，内部有了utilsToast
     * @param msg
     */
    protected void showToast(String msg){
        ToastUtils.showToast(getApplicationContext(),msg);
    }

    /**
     * 把用户名和密码放入sharedPerference的方法
     * @param username
     * @param pwd
     */
    protected void saveUsernamePwd(String username,String pwd){
        sp.edit().putString(Constant.SP_KEY_USERNAME,username).putString(Constant.SP_KEY_PASSWORD,pwd).commit();
    }
    protected String getUsername(){
        return sp.getString(Constant.SP_KEY_USERNAME,"");
    }
    protected String getPwd(){
        return sp.getString(Constant.SP_KEY_PASSWORD,"");
    }

    /**
     * @param msg
     */
    protected void showProgressDialog(String msg){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(msg);
        progressDialog.show();
    }
    /**
     * 取消进度条对话框
     */
    protected void cancelProgressDialog(){

        if( progressDialog!=null){
            progressDialog.dismiss();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if( progressDialog!=null){
            progressDialog.dismiss();
        }

    }
}
