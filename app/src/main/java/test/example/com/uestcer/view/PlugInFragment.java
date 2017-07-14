package test.example.com.uestcer.view;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.hyphenate.chat.EMClient;

import test.example.com.uestcer.MainActivity;
import test.example.com.uestcer.R;
import test.example.com.uestcer.presenter.LogoutPresenter;
import test.example.com.uestcer.presenter.impl.LogoutPresenterImpl;
import test.example.com.uestcer.utils.ToastUtils;

/**
 * Created by DK on 2017/5/5.
 */
public class PlugInFragment extends BaseFragment implements PluginView{
    private ProgressDialog mProgressDialog=null;
    private LogoutPresenter presenter=null;
    private Button btn_logput;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mProgressDialog=new ProgressDialog(getActivity());
        mProgressDialog.setCancelable(false);
        presenter=new LogoutPresenterImpl(this);


        return inflater.inflate(R.layout.fragment_plugin,null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        btn_logput= (Button) view.findViewById(R.id.btn_logout);
        btn_logput.setText("退出当前账号"+ EMClient.getInstance().getCurrentUser());
        btn_logput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressDialog.setMessage("正在退出....");
                presenter.logout();
            }
        });
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onlogout(boolean isLogout, String msg) {
        mProgressDialog.hide();
        if (isLogout){
            MainActivity activity= (MainActivity) getActivity();
            activity.startActivity(LoginActivity.class,true);
        }else {
            ToastUtils.showToast(getActivity(),"退出失败");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mProgressDialog!=null){
            mProgressDialog.dismiss();
        }
    }
}
