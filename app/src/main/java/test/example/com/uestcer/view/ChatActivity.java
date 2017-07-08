package test.example.com.uestcer.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hyphenate.chat.EMMessage;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import test.example.com.uestcer.Adapter.ChatAdapter;
import test.example.com.uestcer.R;
import test.example.com.uestcer.presenter.ChatPresenter;
import test.example.com.uestcer.presenter.impl.ChatPresenterImpl;

/**
 * Created by oo on 2017/6/27.
 */

public class ChatActivity extends BaseActivity implements ChatView {
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.tb_toolbar)
    Toolbar tbToolbar;
    @InjectView(R.id.rv_chat)
    RecyclerView rvChat;
    @InjectView(R.id.et_message)
    EditText etMessage;
    @InjectView(R.id.btn_send)
    Button btnSend;


    private String contact;    //當前正在聊天的聯繫人
    private ChatPresenter presenter;
    private ChatAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvitity_chat);
        ButterKnife.inject(this);
        initToolbar();
        //getIntent(), Return the intent that started this activity.
        contact = getIntent().getStringExtra("contact");
        tvTitle.setText("与"+contact+"聊天中");

        etMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            //空消息不能发送
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()==0){
                    btnSend.setEnabled(false);
                }else {
                    btnSend.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //先把适配界面设为空
        presenter=new ChatPresenterImpl(this);
        //初始化recycleview
        rvChat.setLayoutManager(new LinearLayoutManager(this));
        //先把会话设为空的
        adapter =new ChatAdapter(null);
        rvChat.setAdapter(adapter);
        //获取历史消息，更新界面

        presenter.getChatHistoryMessage(contact);

    }

    private void initToolbar() {
        tbToolbar.setTitle("");
        setSupportActionBar(tbToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    //发送的点击事件
    @OnClick(R.id.btn_send)
    public void onClick(){
        String msgText = etMessage.getText().toString();
        //发送的逻辑由presenter处理
        presenter.sendMessage(msgText,contact);
        //清空editetxt
        etMessage.setText("");
    }

    //ChatView的方法
    @Override
    public void getHistoryMessage(List<EMMessage> emMessages) {
        Log.i("View层emMessages", emMessages.toString());

        //给适配器设置数据
        adapter.setMessages(emMessages);
//        刷新界面

        adapter.notifyDataSetChanged();
        if(adapter.getItemCount()>0){
            rvChat.smoothScrollToPosition(adapter.getItemCount()-1);
        }
    }

    @Override
    public void updateList() {
        // 刷新界面
        adapter.notifyDataSetChanged();
        if(adapter.getItemCount()>0){
            rvChat.smoothScrollToPosition(adapter.getItemCount()-1);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    void onGetMessageEvent(List<EMMessage> messages){
        presenter.getChatHistoryMessage(contact);
    }
}
