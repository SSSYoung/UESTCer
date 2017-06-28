package test.example.com.uestcer.view;

import java.util.List;

/**
 * 初始化，更新，删除联系人
 * Created by DK on 2017/5/27.
 */

public interface ContactView {
    void onInitContact(List<String> contact);
    void onUpdateContact(List<String> contact,boolean isUpdateSuccess);
    void onDeleteContact(boolean isDeleteSuccess,String errorMsg);
}
