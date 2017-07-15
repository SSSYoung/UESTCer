package test.example.com.uestcer.view;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import test.example.com.uestcer.Adapter.ConversationAdapter;
import test.example.com.uestcer.MainActivity;
import test.example.com.uestcer.R;
import test.example.com.uestcer.presenter.ConversationPresenter;
import test.example.com.uestcer.presenter.impl.ConversationPresenterImpl;

/**
 * Created by DK on 2017/5/5.
 */
public class ConversationFragment extends BaseFragment implements ConversationView{
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private ConversationAdapter adapter;
    private ConversationPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_conversation,null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.clearAllUnreadMark();
            }
        });
        adapter=new ConversationAdapter(null);
        //实现每个条目的具体点击事件
        adapter.setOnItemClickListener(new ConversationAdapter.onAddItemClickListener() {
            @Override
            public void onConversationClick(View v, String username) {
                //跳转到聊天ChatActivity
                Intent intent = new Intent(getContext(),ChatActivity.class);
                intent.putExtra("contact",username);
                startActivity(intent);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        presenter = new ConversationPresenterImpl(this);
        presenter.getConversations();
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onGetConversations(List<EMConversation> conversationList) {
        adapter.setEMConversations(conversationList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClearAllUnreadMark() {
        presenter.getConversations();
        MainActivity activity = (MainActivity) getActivity();
        activity.updateBadgeItem();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    void onGetMessageEvent(List<EMMessage> list){
        presenter.getConversations();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.getConversations();
    }
}
