package test.example.com.uestcer.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import test.example.com.uestcer.Adapter.AddFriendAdapter;
import test.example.com.uestcer.R;
import test.example.com.uestcer.presenter.AddFriendPresenter;
import test.example.com.uestcer.presenter.impl.addFriendPresenterImpl;

/**
 * Created by DK on 2017/5/6.
 */

public class AddFriendActivity extends BaseActivity implements addFriendView{
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.tb_toolbar)
    Toolbar tbToolbar;
    @InjectView(R.id.rv_addfriend)
    RecyclerView rvAddfriend;
    @InjectView(R.id.iv_nodata)
    ImageView ivNodata;
    private SearchView searchView;
    private AddFriendAdapter adapter;
    private AddFriendPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addfriend);
        ButterKnife.inject(this);
        initToolbar();
        presenter=new addFriendPresenterImpl(this);
    }

    private void initToolbar() {
        tbToolbar.setTitle("");
        //设置一个Toolbar代替Actionbar的功能
        setSupportActionBar(tbToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //把menu弄成我自定义的样子
        getMenuInflater().inflate(R.menu.menu_add_friend,menu);
        MenuItem menuItem = menu.findItem(R.id.menu_search);
        //把得到的ActionView强转为searchView
        searchView=(SearchView)menuItem.getActionView();
        //设置搜索框的提示
        searchView.setQueryHint("搜索好友");
        //添加文字变化监听
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            //query：提交的搜索文本
            @Override
            public boolean onQueryTextSubmit(String query) {
                //点击搜索，就创建适配器，把搜索到的好友展示出来
                //适配器为空时，就创建适配器
                if (adapter==null){
                    adapter=new AddFriendAdapter(null,null);
                    //设置适配器的布局
                    rvAddfriend.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    rvAddfriend.setAdapter(adapter);
                    //设置adapter的成员接口
                    adapter.setOnAddFriendClickListener(new AddFriendAdapter.onAddFriendClickListener() {
                        @Override
                        public void onAddFriendClick(View v, String username) {
                            presenter.addFriend(username);
                        }
                    });
                }
                presenter.searchFriend(query);
                //提交了隐藏软键盘
                InputMethodManager inputMethodManager= (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(rvAddfriend.getWindowToken(),0);

                return true;
            }

            //输入框中的文本改变时触发
            @Override
            public boolean onQueryTextChange(String newText) {
                //搜索框的文本变化用toast显示
                if (!TextUtils.isEmpty(newText)){
                    showToast(newText);
                }
               return true;
            }
        });

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public void onQuerySuccess(List<AVUser> list, List<String> users, boolean isSuccess, String errorMsg) {
        if (isSuccess){
            adapter.setContacts(users);
            adapter.setUsers(list);
            adapter.notifyDataSetChanged();
            //隐藏中间的图片
            ivNodata.setVisibility(View.GONE);
            rvAddfriend.setVisibility(View.VISIBLE);
        }else {
            showToast(errorMsg);
            ivNodata.setVisibility(View.VISIBLE);
            rvAddfriend.setVisibility(View.GONE);
        }
    }

    @Override
    public void onGetAddfriendResult(boolean isSuccess, String errorMsg) {
        if (isSuccess){
            showToast("添加好友成功");
        }else {
            showToast(errorMsg);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                finish();

            break;
            default:
            break;
        }
        return super.onOptionsItemSelected(item);
    }

}
