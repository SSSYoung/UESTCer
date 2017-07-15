package test.example.com.uestcer.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.util.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import test.example.com.uestcer.R;

/**
 * Created by oo on 2017/7/10.
 */

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.MyViewHolder> {

    private List<EMConversation> conversations = new ArrayList<>();



    public void setEMConversations(List<EMConversation> conversations) {
        this.conversations = conversations;
    }

    public ConversationAdapter(List<EMConversation> conversations) {
        this.conversations = conversations;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //  View view = View.inflate(parent.getContext(), R.layout.list_item_addfriend, null);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_conversation,parent,false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        //获取一个会话
        EMConversation emConversation = conversations.get(position);
//        获取最近一条消息
        EMMessage lastMessage = emConversation.getLastMessage();
        //聊天的对象
        final String userName = lastMessage.getUserName();
        EMTextMessageBody textMessageBody = (EMTextMessageBody) lastMessage.getBody();
        //获取消息内容
        String message = textMessageBody.getMessage();
        //获取未读消息数量
        int unreadMsgCount = emConversation.getUnreadMsgCount();
        //设置每个item
        holder.tv_username.setText(userName);
        holder.tv_msg.setText(message);
        holder.tv_time.setText(DateUtils.getTimestampString(new Date(lastMessage.getMsgTime())));

        if(unreadMsgCount==0){
            //没有未读消息，悬浮按钮隐藏
            holder.tv_unread.setVisibility(View.GONE);
        }else if(unreadMsgCount>99){
            holder.tv_unread.setText(String.valueOf(99));
            holder.tv_unread.setVisibility(View.VISIBLE);
        }else{
            holder.tv_unread.setText(String.valueOf(unreadMsgCount));
            holder.tv_unread.setVisibility(View.VISIBLE);
        }
        //初始化每个item的点击监听响应
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.onConversationClick(v,userName);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return conversations == null? 0:conversations.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tv_username;
        TextView tv_time;
        TextView tv_msg;
        TextView tv_unread;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_username = (TextView) itemView.findViewById(R.id.tv_username);
            tv_msg = (TextView) itemView.findViewById(R.id.tv_message);
            tv_unread = (TextView) itemView.findViewById(R.id.tv_unread);

        }
    }
    //点击条目的监听响应接口，不实现
    public interface onAddItemClickListener{
        void onConversationClick(View v, String username);
    }
    //item点击的监听类
    private onAddItemClickListener listener;

    public void setOnItemClickListener(onAddItemClickListener listener){
        this.listener = listener;
    }
}
