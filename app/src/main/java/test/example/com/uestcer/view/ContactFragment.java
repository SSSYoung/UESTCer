package test.example.com.uestcer.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import test.example.com.uestcer.Adapter.ContactAdapter;
import test.example.com.uestcer.R;
import test.example.com.uestcer.presenter.ContactPresenter;
import test.example.com.uestcer.presenter.impl.ContactPresenterImpl;
import test.example.com.uestcer.utils.ToastUtils;
import test.example.com.uestcer.widget.ContactLayout;

/**
 * Created by DK on 2017/5/5.
 */
public class ContactFragment extends BaseFragment implements ContactView{

    private ContactAdapter adapter;
    private ContactLayout contactLayout;
    private ContactPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contact, null);
    }

    /**
     * 在OncreateView后立即执行
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter=new ContactPresenterImpl(this);
        presenter.initContact();
        contactLayout= (ContactLayout) view.findViewById(R.id.contactlayout);

    }

    //View接口里的方法

    /**
     * 初始化联系人的View
     * @param contact
     */
    @Override
    public void onInitContact(List<String> contact) {
        adapter=new ContactAdapter(contact);
        //自定义控件
        contactLayout.setAdapter(adapter);
        contactLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.updateContact();
            }
        });
        adapter.setOnItemClickListener(new ContactAdapter.OnItemClickListener() {
            @Override
            //单击和该联系人聊天
            public void onclick(View v, String username) {
                Intent intent = new Intent(getContext(), ChatActivity.class);
                intent.putExtra("contact",username);
                startActivity(intent);

            }
            //长按删除此联系人
            @Override
            public boolean onLongClick(View v, final String username) {
                Snackbar.make(contactLayout,"真的要删除"+username+"吗?",Snackbar.LENGTH_LONG)
                        .setAction("确认", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                presenter.deleteContact(username);
                            }
                        }).show();
                return true;
            }
        });
    }

    @Override
    public void onUpdateContact(List<String> contact, boolean isUpdateSuccess) {
        contactLayout.setRefreshing(false);
        if (isUpdateSuccess){
            //更新adapter中的联系人数据
            adapter.setContacts(contact);
            //刷新界面
            adapter.notifyDataSetChanged();
        }else {
            ToastUtils.showToast(getActivity(),"更新联系人失败");
        }
    }

    @Override
    public void onDeleteContact(boolean isDeleteSuccess, String errorMsg) {
        if (isDeleteSuccess){
            ToastUtils.showToast(getContext(),"删除成功");
        }else {
            ToastUtils.showToast(getContext(),"删除失败"+errorMsg);
        }
    }


    //    private ContactPresenter presenter;
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//
//        return inflater.inflate(R.layout.fragment_contact,null);
//    }
//
//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        contactLayout = (ContactLayout) view.findViewById(R.id.contactlayout);
//        presenter = new ContactPresenterImpl(this);
//        //初始化联系人
//        presenter.initContact();
//        super.onViewCreated(view, savedInstanceState);
//    }
//
//    /**
//     * ContactView接口里面未实现的方法
//     * @param contact
//     */
//    @Override
//    public void onInitContact(List<String> contact) {
//        //得到了联系人的数据，在UI中展示
//        adapter = new ContactAdapter(contact);
//
//    }
//
//    @Override
//    public void onUpdateContact(List<String> contact, boolean isUpdateSuccess) {
//
//    }
//
//    @Override
//    public void onDeleteContact() {
//
//    }
}
