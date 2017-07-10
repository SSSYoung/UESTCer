package test.example.com.uestcer.view;

import com.hyphenate.chat.EMConversation;

import java.util.List;

/**
 * Created by oo on 2017/7/10.
 */

public interface ConversationView {
    void onGetConversations(List<EMConversation> conversationList);

    void onClearAllUnreadMark();
}
