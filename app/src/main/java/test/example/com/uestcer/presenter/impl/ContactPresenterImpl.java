package test.example.com.uestcer.presenter.impl;

import test.example.com.uestcer.db.DBUtils;
import test.example.com.uestcer.presenter.ContactPresenter;
import test.example.com.uestcer.view.ContactView;

/**
 * Created by DK on 2017/5/27.
 */

public class ContactPresenterImpl implements ContactPresenter {
    private ContactView contactView;
    //持有一个View层的对象
    public ContactPresenterImpl(ContactView contactView){
        this.contactView=contactView;
    }
    @Override
    public void initContact() {
        DBUtils.
    }

    @Override
    public void updateContact() {

    }

    @Override
    public void deleteContact(String username) {

    }
}
