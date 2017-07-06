package test.example.com.uestcer.event;

/**
 * Created by oo on 2017/7/6.
 */

public class ContactChangeEvent {
    //联系人发生改变的用户
    public String username;
    //是添加了好友还是删除了
    public boolean isAdded;

    /**
     *
     * @param username
     * @param isAdded true添加好友，false删除好友
     */
    public ContactChangeEvent(String username, boolean isAdded) {
        this.username = username;
        this.isAdded = isAdded;
    }
}
