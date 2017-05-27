package test.example.com.uestcer.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import test.example.com.uestcer.Adapter.ContactAdapter;
import test.example.com.uestcer.R;
import test.example.com.uestcer.presenter.ContactPresenter;
import test.example.com.uestcer.presenter.impl.ContactPresenterImpl;
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

        return inflater.inflate(R.layout.fragment_contact,null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        contactLayout = (ContactLayout) view.findViewById(R.id.contactlayout);
        presenter = new ContactPresenterImpl(this);
        //初始化联系人
        presenter.initContact();
        super.onViewCreated(view, savedInstanceState);
    }

    /**
     * ContactView接口里面未实现的方法
     * @param contact
     */
    @Override
    public void onInitContact(List<String> contact) {
        //得到了联系人的数据，在UI中展示
        adapter = new ContactAdapter(contact);
        contact
    }

    @Override
    public void onUpdateContact(List<String> contact, boolean isUpdateSuccess) {

    }

    @Override
    public void onDeleteContact() {

    }
}
