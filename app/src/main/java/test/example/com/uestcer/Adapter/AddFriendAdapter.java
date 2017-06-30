package test.example.com.uestcer.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;

import java.util.ArrayList;
import java.util.List;

import test.example.com.uestcer.R;
import test.example.com.uestcer.utils.StringUtils;

/**
 * addFriends显示条目的适配器
 * Created by oo on 2017/6/29.
 */

public class AddFriendAdapter extends RecyclerView.Adapter<AddFriendAdapter.MyViewHolder> {

    private List<AVUser> users;
    private List<String> contacts = new ArrayList<>();
    //成员接口，添加好友的接口
    private onAddFriendClickListener onAddFriendClickListener;


    public void setUsers(List<AVUser> users) {
        this.users = users;
    }

    public void setContacts(List<String> contacts) {
        this.contacts = contacts;
    }

    public void setOnAddFriendClickListener(AddFriendAdapter.onAddFriendClickListener onAddFriendClickListener) {
        this.onAddFriendClickListener = onAddFriendClickListener;
    }

    /**
     * 初始化内部的成员变量
     *
     * @param users    搜索到的联系人
     * @param contacts 已经是好友的联系人
     */
    public AddFriendAdapter(List<AVUser> users, List<String> contacts) {
        this.users = users;
        this.contacts = contacts;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_addfriend, parent, false);
        //用得到的充气view构造MyViewHolder
        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    //在每一个子项滚动到屏幕内的时候执行
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        //滚动到相应 得到相应位置的AVUser对象
        final AVUser avUser = users.get(position);
        //设置用户名
        holder.tv_username.setText(avUser.getUsername());
        //设置注册时间
        holder.tv_regist_time.setText(StringUtils.getData(avUser.getCreatedAt()));
        //判断是不是已经是好友，来设置添加好友button的状态
        if (contacts.contains(avUser.getUsername())) {
            //已经是好友了
            holder.bt_add.setText("已经是好友了");
            holder.bt_add.setEnabled(false);
        } else {
            //不是好友
            holder.bt_add.setText("添加好友");
            holder.bt_add.setEnabled(true);
            //添加按键监听
            holder.bt_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //MVP的设计模式，添加好友的逻辑操作不写出来，用接口的方式暴露出来，谁调用谁实现
                    if (onAddFriendClickListener != null) {
                        onAddFriendClickListener.onAddFriendClick(v, avUser.getUsername());
                    }
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return users == null ? 0 : users.size();
    }

    /**
     * 内部ViewHolder类
     */
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_username;
        TextView tv_regist_time;
        Button bt_add;

        public MyViewHolder(View itemView) {
            super(itemView);
            TextView tv_username = (TextView) itemView.findViewById(R.id.tv_username);
            TextView tv_regist_time = (TextView) itemView.findViewById(R.id.tv_regist_time);
            Button bt_add = (Button) itemView.findViewById(R.id.btn_add);
        }
    }

    //
    public interface onAddFriendClickListener {
        /**
         * 添加好友逻辑的接口
         *
         * @param v        点击控件的View
         * @param username 所属条目的添加好友的username
         */
        void onAddFriendClick(View v, String username);
    }
}
