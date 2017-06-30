package test.example.com.uestcer.presenter.impl;

import test.example.com.uestcer.presenter.AddFriendPresenter;
import test.example.com.uestcer.utils.ThreadUtils;
import test.example.com.uestcer.view.addFriendView;

/**
 * Created by oo on 2017/6/29.
 */

public class addFriendPresenterImpl implements AddFriendPresenter {
    private addFriendView addFriendView;
    public addFriendPresenterImpl(addFriendView addFriendView) {
        this.addFriendView=addFriendView;
    }

    @Override
    public void addFriend(String username) {
        ThreadUtils.runOnNonUIThread(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    @Override
    public void searchFriend(String keyword) {

    }
}
