package test.example.com.uestcer;

import android.app.ActivityManager;
import android.app.Application;
import android.content.pm.PackageManager;

import com.avos.avoscloud.AVOSCloud;
import com.hyphenate.EMContactListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.exceptions.HyphenateException;

import org.greenrobot.eventbus.EventBus;

import java.util.Iterator;
import java.util.List;

import test.example.com.uestcer.db.DBUtils;
import test.example.com.uestcer.event.ContactChangeEvent;

/**
 * Created by DK on 2017/4/27.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化环信
        initEaseMobe();
        AVOSCloud.initialize(this,"N62I36x7Yc3QnnGppMbzynFY-gzGzoHsz","rlHHfn7aa33RKpKBVLkEWTtx");
        // 放在 SDK 初始化语句 AVOSCloud.initialize() 后面，只需要调用一次即可
        AVOSCloud.setDebugLogEnabled(true);
        //初始化数据库
        DBUtils.initDBUtils(this);

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

}
