package test.example.com.uestcer.presenter;

import com.hyphenate.chat.EMMessage;

import java.util.List;

/**
 * Created by oo on 2017/7/6.
 */

public interface ChatPresenter {
    /**
     * 获取聊天记录
     * @param username 使用者的username
     * @return
     */
    List<EMMessage> getChatHistoryMessage(String username);

    /**
     * 发送消息
     * @param msgText
     * @param contact
     */
    void sendMessage(String msgText,String contact);
}
