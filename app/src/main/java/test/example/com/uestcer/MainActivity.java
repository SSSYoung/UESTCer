package test.example.com.uestcer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.ashokvarma.bottomnavigation.BadgeItem;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import test.example.com.uestcer.utils.FragmentFactory;
import test.example.com.uestcer.view.AddFriendActivity;
import test.example.com.uestcer.view.BaseActivity;
import test.example.com.uestcer.view.BaseFragment;


public class MainActivity extends BaseActivity {

    private Toolbar toolbar;
    private TextView tv_title;
    private BottomNavigationBar bottomNavigationBar;
    private String[] titles = {"消息", "联系人", "动态"};
    private BadgeItem badgeItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.tb_toolbar);
        tv_title = (TextView) findViewById(R.id.tv_title);
        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        //初始化菜单栏
        initToolbar();
        //初始化底部的导航
        initBottomNavigationBar();
        //初始化第一个fragment
        initFirstFragment();
    }

    private void initBottomNavigationBar() {
        BottomNavigationItem conversationItem = new BottomNavigationItem(R.mipmap.conversation_selected_2, "消息");
        //BadgeItem  底部导航图标  右上角的圆圈文字
        badgeItem=new BadgeItem();
        updateBadgeItem();
        //显示右上角的圆圈文字
        conversationItem.setBadgeItem(badgeItem);
        bottomNavigationBar.addItem(new BottomNavigationItem(R.mipmap.conversation_selected_2, "消息"))
                .addItem(new BottomNavigationItem(R.mipmap.contact_selected_2, "联系人"))
                .addItem(new BottomNavigationItem(R.mipmap.plugin_selected_2, "动态"));
        //设置选中时的颜色
        bottomNavigationBar.setActiveColor(R.color.btn_normal);

        // 设置没被选中时的颜色
        bottomNavigationBar.setInActiveColor(R.color.inactive);

        //可以修改第一次加载时被选中的tab的 位置
        //  bottomNavigationBar.setFirstSelectedPosition(1);
        //初始化
        bottomNavigationBar.initialise();

        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                //通过position拿到不同的fragment
                android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                BaseFragment baseFragment = FragmentFactory.getFragment(position);
                if (baseFragment.isAdded()) {
                    //如果fragment已经被add了，就显示
                    transaction.show(baseFragment).commit();
                } else {
                    //如果fragment没有被add，就add到主界面
                    transaction.add(R.id.fl_container, baseFragment, position + "").commit();

                }
                tv_title.setText(titles[position]);

            }

            @Override
            public void onTabUnselected(int position) {
                //没有被选中的，隐藏起来
                getSupportFragmentManager().beginTransaction().hide(FragmentFactory.getFragment(position)).commit();
            }

            @Override
            public void onTabReselected(int position) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuBuilder build = (MenuBuilder) menu;
        //设置图标可见
        build.setOptionalIconsVisible(true);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_addfriend:
                startActivity(AddFriendActivity.class, false);
                break;
            case R.id.menu_item_scan:
                showToast("扫一扫");
                break;
            case R.id.menu_item_about:
                showToast("关于");
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initToolbar() {
        //不把appname显示在toolBar上
        toolbar.setTitle("");

        setSupportActionBar(toolbar);
        //如果不设置 左边的返回图标就消失了
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initFirstFragment() {
        //当应用长期处于后台 有可能会被系统回收掉 再次启动起来 系统会帮助恢复之前的状态
        //有可能会导致有之前的fragment的缓存 处理缓存 先获取所有的fragment的集合
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (fragments != null && fragments.size() > 0) {
            for (int i = 0; i < fragments.size(); i++) {
                transaction.remove(fragments.get(i));
            }
            transaction.commit();
        }
        BaseFragment fragment = FragmentFactory.getFragment(0);
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fl_container, fragment).commit();

    }

    /**
     * 更新未读消息的BadgeItem的标识
     */
    public void updateBadgeItem() {
        //获取所有的未读条目数量
        int unreadMessageCount = EMClient.getInstance().chatManager().getUnreadMessageCount();
        //根据未读消息
        if (unreadMessageCount == 0) {
            badgeItem.hide(true);
        } else if (unreadMessageCount > 99) {
            badgeItem.show(true);
            badgeItem.setText(String.valueOf(99));
        } else {
            badgeItem.show(true);
            badgeItem.setText(String.valueOf(unreadMessageCount));
        }

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    void onGetMessageEvent(List<EMMessage>messages){
        //更新未读消息的图标
        updateBadgeItem();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateBadgeItem();
    }
}
