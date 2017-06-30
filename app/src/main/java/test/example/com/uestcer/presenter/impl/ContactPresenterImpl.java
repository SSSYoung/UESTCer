package test.example.com.uestcer.presenter.impl;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import test.example.com.uestcer.db.DBUtils;
import test.example.com.uestcer.presenter.ContactPresenter;
import test.example.com.uestcer.utils.ThreadUtils;
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
        //根据目前客户端的用户名去本地数据库取出联系人的列表
        List<String> contacts = DBUtils.initContact(EMClient.getInstance().getCurrentUser());
        contactView.onInitContact(contacts);
        getContactsFromServer();
    }



    @Override
    public void updateContact() {
        getContactsFromServer();
    }



    @Override
    public void deleteContact(final String contactName) {
        ThreadUtils.runOnNonUIThread(new Runnable() {
            @Override
            public void run() {
                //去环信中删除该联系人
                try {
                    EMClient.getInstance().contactManager().deleteContact(contactName);
                    //删除成功了更新View
                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            contactView.onDeleteContact(true,null);
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    contactView.onDeleteContact(false,e.toString());
                }
            }
        });
    }
    /**
     * 去EM请求联系人，成功的话就写入本地的数据库，并更新UI，如果失败的话，本地数据库不进行操作，
     */
    private void getContactsFromServer() {
        //在子线程中去EM拿联系人list
        ThreadUtils.runOnNonUIThread(new Runnable() {
            @Override
            public void run() {
                try {
                    final List<String> contactList = EMClient.getInstance().contactManager().getAllContactsFromServer();
                    //对联系人进行排序
                    Collections.sort(contactList, new Comparator<String>() {
                        @Override
                        public int compare(String o1, String o2) {
                            return o1.compareTo(o2);
                        }
                    });
                    //更新到本地数据库
                    DBUtils.updateContactFromEMServer(EMClient.getInstance().getCurrentUser(),contactList);
                    //在UI中展示
                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            contactView.onUpdateContact(contactList,true,null);

                        }
                    });
                } catch (final HyphenateException e) {
                    e.printStackTrace();
                    //更新失败的UI展示
                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            contactView.onUpdateContact(null,false,e.getDescription());
                        }
                    });
                }

            }
        });
    }
}
