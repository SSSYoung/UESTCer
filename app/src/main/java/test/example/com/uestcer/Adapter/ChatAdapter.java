package test.example.com.uestcer.Adapter;

import android.graphics.drawable.AnimationDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.util.DateUtils;

import java.util.Date;
import java.util.List;

import test.example.com.uestcer.R;

/**
 * Created by oo on 2017/7/6.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {
    private List<EMMessage> messages;

    public void setMessages(List<EMMessage> messages) {
        this.messages = messages;
    }

    public ChatAdapter(List<EMMessage> messages) {
        this.messages = messages;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == 0) {
            Log.d("direction", String.valueOf(viewType));
            //收到的消息
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_chat_item, parent, false);
        } else {
            Log.d("direction", String.valueOf(viewType));
            //发送的消息
            view=LayoutInflater.from(parent.getContext()).inflate(R.layout.list_chat_item_send, parent, false);
        }
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        EMMessage message = messages.get(position);
        long msgTime = message.getMsgTime();

        //两个消息的时间间隔不超过30s不显示时间
        if (position == 0) {
            //如果发送的消息就是刚发送的
            if (DateUtils.isCloseEnough(msgTime, System.currentTimeMillis())) {
                holder.tv_time.setVisibility(View.GONE);
            } else {
                //用环形的工具类来格式化时间
                holder.tv_time.setText(DateUtils.getTimestampString(new Date(msgTime)));
                holder.tv_time.setVisibility(View.VISIBLE);
            }
        } else {
            //与上一条的时间相比，
            if (DateUtils.isCloseEnough(msgTime, messages.get(position - 1).getMsgTime())) {
                holder.tv_time.setVisibility(View.GONE);
            } else {
                holder.tv_time.setText(DateUtils.getTimestampString(new Date(msgTime)));
                holder.tv_time.setVisibility(View.VISIBLE);
            }
        }

        EMTextMessageBody body = (EMTextMessageBody) message.getBody();
        //要发送的信息
        String msg = body.getMessage();
        holder.tv_message.setText(msg);

        if (message.direct() == EMMessage.Direct.SEND) {
            //发送的消息，发送状态的显示
            EMMessage.Status status = message.status();
            switch (status) {
                case SUCCESS:
                    //隐藏发送状态的图标
                    holder.iv_state.setVisibility(View.GONE);
                    break;
                case FAIL:
                    holder.iv_state.setImageResource(R.mipmap.msg_error);
                    break;
                case INPROGRESS:
                    //转圈框
                    holder.iv_state.setImageResource(R.drawable.send_animation);
                    AnimationDrawable drawable = (AnimationDrawable) holder.iv_state.getDrawable();
                    drawable.start();
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 获取每条消息的方向，是发送还是接收
     *
     * @param position
     * @return 收到的消息用0标记，发送的消息用1标记
     */
    @Override
    public int getItemViewType(int position) {
        EMMessage message = messages.get(position);
        EMMessage.Direct direct = message.direct();
        return direct == EMMessage.Direct.RECEIVE ? 0 : 1;
    }

    @Override
    public int getItemCount() {
        return messages==null?0:messages.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_time;
        TextView tv_message;
        ImageView iv_state;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_message = (TextView) itemView.findViewById(R.id.tv_message);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            iv_state = (ImageView) itemView.findViewById(R.id.iv_state);


        }

    }
}
