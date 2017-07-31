package test.example.com.uestcer;

import android.app.ActivityManager;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import com.avos.avoscloud.AVOSCloud;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.NetUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.Iterator;
import java.util.List;

import test.example.com.uestcer.db.DBUtils;
import test.example.com.uestcer.event.ContactChangeEvent;
import test.example.com.uestcer.event.ExitEvent;
import test.example.com.uestcer.utils.ThreadUtils;
import test.example.com.uestcer.view.ChatActivity;

/**
 * Created by DK on 2017/4/27.
 */

public class MyApplication extends Application {
    private int foregoundSound;
    private int backgoundSound;
    private SoundPool soundPool;
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化环信
        initEaseMobe();
        AVOSCloud.initialize(this,"N62I36x7Yc3QnnGppMbzynFY-gzGzoHsz","rlHHfn7aa33RKpKBVLkEWTtx");

        // 放在 SDK 初始化语句 AVOSCloud.initialize() 后面，只需要调用一次即可
        AVOSCloud.setDebugLogEnabled(true);
        try {
            final List<String> allContacts = EMClient.getInstance().contactManager().getAllContactsFromServer();
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
        //初始化数据库
        DBUtils.initDBUtils(this);
        //初始化即时消息的监听
        initGetMessagelistener();
        initSoundPool();
    }
    private void initGetMessagelistener(){
        EMClient.getInstance().chatManager().addMessageListener(new EMMessageListener() {
            //收到消息时
            @Override
            public void onMessageReceived(List<EMMessage> list) {

                EventBus.getDefault().post(list);
                Log.i("EventBus post", "EventBus post");

                if (isInBackgoundState()){
                    soundPool.play(backgoundSound,1,1,0,0,1);
                    //发送通知
                    sendNotification(list.get(0));
                }else {
                    soundPool.play(foregoundSound,1,1,0,0,1);
                }
            }
            //如果实在后台，发送通知

            @Override
            public void onCmdMessageReceived(List<EMMessage> list) {

            }

            @Override
            public void onMessageRead(List<EMMessage> list) {

            }

            @Override
            public void onMessageDelivered(List<EMMessage> list) {

            }

            @Override
            public void onMessageChanged(EMMessage emMessage, Object o) {

            }
        });
    }
    private void initSoundPool(){
        soundPool=new SoundPool(2, AudioManager.STREAM_MUSIC,0);
        foregoundSound = soundPool.load(getApplicationContext(), R.raw.duan, 1);
        backgoundSound = soundPool.load(getApplicationContext(),R.raw.yulu,1);
    }

    private void sendNotification(EMMessage emMessage) {
        Notification.Builder builder = new Notification.Builder(getApplicationContext());
        //点击通知后主动消失
        builder.setAutoCancel(true);
        //设置通知的小图标
        builder.setSmallIcon(R.mipmap.message);
        builder.setContentTitle("新消息");
        //消息的内容
        EMTextMessageBody body= (EMTextMessageBody) emMessage.getBody();
        //把消息的内容设置到通知的内容
        builder.setContentTitle(body.getMessage());
        //设置大图标
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.avatar3));
        builder.setContentInfo("来自"+ emMessage.getUserName());
        //创建要打开的activity对应的意图
        Intent mainActivityIntent = new Intent(getApplicationContext(), MainActivity.class);
        Intent chatActivityIntent = new Intent(getApplicationContext(), ChatActivity.class);
        chatActivityIntent.putExtra("contact",emMessage.getUserName());
        Intent[] intents = new Intent[]{mainActivityIntent, chatActivityIntent};
        //点击通知的延迟意图，用来处理通知的点击事件
        PendingIntent pendingIntent = PendingIntent.getActivities(getApplicationContext(), 1, intents, PendingIntent.FLAG_UPDATE_CURRENT);
        //设置通知点击事件
        builder.setContentIntent(pendingIntent);

        Notification notification = builder.build();
        NotificationManager manager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(1,notification);
    }

    /**
     * 判断当前的应用是否在后台状态
     * @return 后台返回true，返回false应用前台
     */
    private boolean isInBackgoundState(){
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        //通过ActivityManager 获取正在运行的 任务信息
        List<ActivityManager.RunningTaskInfo> runningTasks = manager.getRunningTasks(50);
        //获取第一个activity栈的信息
        ActivityManager.RunningTaskInfo runningTaskInfo = runningTasks.get(0);
        //获取栈中的栈顶activity  根据activity的包名判断 是否是当前应用的包名
        ComponentName componentName = runningTaskInfo.topActivity;
        if (componentName.getPackageName().equals(getPackageName())){
            return false;
        }else {
            return true;
        }
    }
    /**
     * 初始化环信，主要完了
     * （1）联系人增加或删除时消息的发送，通过EventBus
     * (2) 收到好友邀请是，直接设置为接受。
     */
    private void initEaseMobe() {
        EMOptions options = new EMOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);

        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        // 如果APP启用了远程的service，此application:onCreate会被调用2次
        // 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
        // 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回

        if (processAppName == null || !processAppName.equalsIgnoreCase(this.getPackageName())) {
            // Log.e(TAG, "enter the service process!");

            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }

        //初始化
        EMClient.getInstance().init(this, options);
        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(false);


        //联系人的监听
        EMClient.getInstance().contactManager().setContactListener(new EMContactListener() {
            @Override
            public void onContactAdded(String username) {
                EventBus.getDefault().post(new ContactChangeEvent(username,true));
            }

            @Override
            public void onContactDeleted(String username) {
                //被删除时回调方法，通过eventbus发布消息
                EventBus.getDefault().post(new ContactChangeEvent(username,false));
            }

            @Override
            public void onContactInvited(String username, String reason) {
                //收到好友邀请
                try {
                    //直接接受，不用操作
                    EMClient.getInstance().contactManager().acceptInvitation(username);

                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFriendRequestAccepted(String s) {
                //
            }

            @Override
            public void onFriendRequestDeclined(String s) {

            }
        });
    }

    /**
     *
     * @param pID
     * @return
     */
    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }
    //ConnectionListener接口
    private class Myconnectionlistener implements EMConnectionListener{
        @Override
        public void onConnected() {

        }

        @Override
        public void onDisconnected(final int error) {
            ThreadUtils.runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    if (error== EMError.USER_REMOVED){
                        //显示账号被移除
                        EventBus.getDefault().post(new ExitEvent(EMError.USER_REMOVED));
                    }else if (error==EMError.USER_LOGIN_ANOTHER_DEVICE){
                        //显示设备在其他设备登录
                        EventBus.getDefault().post(new ExitEvent(EMError.USER_LOGIN_ANOTHER_DEVICE));
                    }else {
                        if (NetUtils.hasNetwork(getApplicationContext())){
                            //连接不到聊天服务器
                        }else {
                            //当前网络不可用，请检查网络设置
                        }
                    }
                }
            });
        }
    }
}
