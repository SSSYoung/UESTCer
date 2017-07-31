package test.example.com.uestcer.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import test.example.com.uestcer.R;
import test.example.com.uestcer.utils.StringUtils;

/**
 * Created by DK on 2017/5/27.
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.MyViewHolder> {
    private List<String> contacts;

    /**
     * get,set方法
     * @return
     */
    public List<String> getContacts() {
        return contacts;
    }

    public void setContacts(List<String> contacts) {
        this.contacts = contacts;
    }

    private OnItemClickListener onItemClickListener;

    /**
     * 通过一个内部接口，吧要实现的方法暴露出来
     *
     * 自定义内部接口，抽象方法，实现点击，长按事件
     */
    public interface OnItemClickListener {
        void onclick(View v,String username);
        boolean onLongClick(View v,String username);
    }

    /**
     * 通过构造方法把所有的信息赋值给ContactAdapter里面的成员变量
     * @param contacts
     */
    public ContactAdapter(List<String> contacts){

        this.contacts=contacts;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_contact_item,parent,false);
        //把view传进去，通过构造就获取到了list_contact_item的控件
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    /**
     * 用于对recycleView子项的数据进行赋值的 ，每个子项会被滚动到屏幕内的时候执行。
     * @param holder RecycleView的每个子项
     * @param position 滚动到的位置
     */
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        //通过viewholder 给展示数据的控件设置数据
        final String contact = contacts.get(position);
        holder.tv_contact.setText(contact);
        holder.tv_section.setText(StringUtils.getFirstChar(contact));
        if(position == 0){
            holder.tv_section.setVisibility(View.VISIBLE);
        }else{
            String current = StringUtils.getFirstChar(contact);
            String last = StringUtils.getFirstChar(contacts.get(position-1));
            if(current.equals(last)){
                //当前条目的首字母跟上个条目一样 不需要展示首字母的textview
                holder.tv_section.setVisibility(View.GONE);
            }else{
                //当前条目的首字母跟上个条目不一样 需要展示首字母的textview
                holder.tv_section.setVisibility(View.VISIBLE);
            }
        }

        //绑定控件时，就设置点击事件
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(onItemClickListener!=null){
                    //具体的实现逻辑并不写出来，而是调用接口里的click和longclick方法
                    onItemClickListener.onclick(v,contact);
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onItemClickListener!=null){
                    onItemClickListener.onLongClick(v,contact);
                    return true;
                }
                return false;
            }

        });
    }

    /**
     * 设置onItemClickListener
     * @param onItemClickListener 传递一个接口，自然要实现里面的方法，所以谁调用，谁实现
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener=onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return contacts==null?0:contacts.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tv_section;
        TextView tv_contact;
        //构造方法对控件初始化
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_section = (TextView) itemView.findViewById(R.id.tv_section);
            tv_contact = (TextView) itemView.findViewById(R.id.tv_contact);
        }
    }


}
