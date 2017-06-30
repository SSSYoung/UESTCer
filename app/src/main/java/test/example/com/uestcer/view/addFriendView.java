package test.example.com.uestcer.view;

import com.avos.avoscloud.AVUser;

import java.util.List;

/**
 * Created by oo on 2017/6/29.
 */

public interface addFriendView {
    /**
     * 请求增加
     * @param list
     * @param users
     * @param isSuccess
     * @param errorMsg
     */
    void onQuerySuccess(List<AVUser> list,List<String> users,boolean isSuccess,String errorMsg);
    void onGetAddfriendResult(boolean isSuccess,String errorMsg);
}
