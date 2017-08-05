package test.example.com.uestcer.presenter.impl;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

import test.example.com.uestcer.db.DBUtils;
import test.example.com.uestcer.presenter.AddFriendPresenter;
import test.example.com.uestcer.utils.ThreadUtils;
import test.example.com.uestcer.view.addFriendView;

/**
 * Created by oo on 2017/6/29.
 */

public class AddFriendPresenterImpl implements AddFriendPresenter {
    private addFriendView addFriendView;

    public AddFriendPresenterImpl(addFriendView addFriendView) {
        this.addFriendView = addFriendView;
    }

    @Override
    public void addFriend(final String username) {
        ThreadUtils.runOnNonUIThread(new Runnable() {
            @Override
            public void run() {
                try {
                    //默认好友请求是自动同意的，如果要手动同意需要在初始化SDK时调用 opptions.setAcceptInvitationAlways(false); 。
                    EMClient.getInstance().contactManager().addContact(username, "申请添加好友");
                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            addFriendView.onGetAddfriendResult(true, null);
                        }
                    });
                } catch (final HyphenateException e) {
                    e.printStackTrace();
                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            addFriendView.onGetAddfriendResult(false, e.toString());
                        }
                    });
                }

            }
        });
    }

    @Override
    public void searchFriend(String keyword) {
        final String currentUser = EMClient.getInstance().getCurrentUser();
        //AVQuery到LeanCloud服务端查询数据
        AVQuery<AVUser> query = new AVQuery<>("_User");
        //只查前几个字母一样的所有,第一个参数username代表查找"username"的键值
        query.whereStartsWith("username", keyword)
                //去掉自己
                .whereNotEqualTo("username", currentUser)
                .findInBackground(new FindCallback<AVUser>() {
                    @Override
                    public void done(List<AVUser> list, AVException e) {
                        //异常为空，，返回的查找列表不为空
                        if (e == null && list != null && list.size() > 0) {
                            List<String> users = DBUtils.initContact(currentUser);
                            addFriendView.onQuerySuccess(list, users, true, null);
                            for (AVUser username : list) {
                                Log.e("test", username.getUsername());
                            }
                        }else {
                            if (e==null){
                                //查询成功但没有符合的结果
                                addFriendView.onQuerySuccess(null,null,true,"没有满足条件的用户");
                            }else {
                                addFriendView.onQuerySuccess(null,null,false,e.getMessage());
                            }

                        }
                    }

                });
    }
}
