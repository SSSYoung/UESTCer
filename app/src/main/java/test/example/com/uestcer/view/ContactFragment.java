package test.example.com.uestcer.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import test.example.com.uestcer.R;

/**
 * Created by DK on 2017/5/5.
 */
public class ContactFragment extends BaseFragment implements ContactView{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_contact,null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onInitContact(List<String> contact) {

    }

    @Override
    public void onUpdateContact(List<String> contact, boolean isUpdateSuccess) {

    }

    @Override
    public void onDeleteContact() {

    }
}
