package test.example.com.uestcer.presenter.impl;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;

import java.util.List;

import test.example.com.uestcer.presenter.ChatPresenter;
import test.example.com.uestcer.view.ChatView;

/**
 * Created by oo on 2017/7/6.
 */

public class ChatPresenterImpl implements ChatPresenter{
    private ChatView chatView;
    public ChatPresenterImpl(ChatView chatView){
        this.chatView=chatView;
    }
    @Override
    public List<EMMessage> getChatHistoryMessage(String username) {
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(username);
        if (conversation!=null){
            //设置所有消息为已读
            conversation.markAllMessagesAsRead();
            //
        }
        return null;
    }

    @Override
    public void sendMessage(String msgText, String contact) {

    }
}
