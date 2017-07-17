# UESTCer
即时通信APP
一款类似于QQ的简易即时通信APP，使用环形即时通信第三方SDK,实现具有文本聊天功能的Android客户端。
## 采用了MVP的设计模式。界面的设计采用了Material Design风格，控件也大量使用了android5.0之后出现新控件（RecyclerView,CardView,ToolBar,SearchView,Snackbar,TextinputLayout，FloatingActionButton等）使用了GITHub上优秀的开源项目，包括ButterKnife，EventBus，BottomNavigationBar

## 注册界面和登录界面UI一致
![image](https://github.com/genjiyang/UESTCer/blob/master/README/LoginActivity.png)
![image](https://github.com/genjiyang/UESTCer/blob/master/README/RegisterActivity.png)

## 主界面由“消息”，“联系人”，和“动态”三个Fragment切换组成。

![image](https://github.com/genjiyang/UESTCer/blob/master/README/ConversationFragment.jpg)

## 1."消息"Fragment，可以展示多条于不同联系人的会话。

![image](https://github.com/genjiyang/UESTCer/blob/master/README/ConversationFragment.jpg)

## 点击每个会话Item会跳转至于该联系人的ChatActivity。
![image](https://github.com/genjiyang/UESTCer/blob/master/README/ChatActivty.png)

## 2.“联系人”Fragment点击ToolBar中的OptionMenu可以搜索好友（默认被添加用户登录时自动通过验证）,联系人列表按照首字母排列。还可以实现单击联系人条目跳转到ChatActivity，长按删除联系人，下拉刷新联系人。联系人列表发送变化时，通过EvenBus完成事件的分发，即时更新联系人列表

![image](https://github.com/genjiyang/UESTCer/blob/master/README/AddFriend.png)
![image](https://github.com/genjiyang/UESTCer/blob/master/README/ContactFragment.png)

## 3.“动态”Fragment目前暂时没有实现，用一个登出的功能代替。点击登出按钮跳转到LoginActivity
![image](https://github.com/genjiyang/UESTCer/blob/master/README/logoutFragment.png)

