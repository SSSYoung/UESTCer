package test.example.com.uestcer.view;

import com.hyphenate.chat.EMMessage;

import java.util.List;

/**
 * Created by oo on 2017/7/6.
 */

public interface ChatView  {
    /**
     * 获取历史消息记录
     * @param emMessages
     */
    void getHistoryMessage(List<EMMessage> emMessages);

    //更新消息列表
    void updateList();
}
