package test.example.com.uestcer.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import test.example.com.uestcer.R;

/**
 * Created by DK on 2017/5/27.
 */

public class ContactLayout extends RelativeLayout{
    private RecyclerView recyclerView;
    private TextView tv_float;
    private Slidebar slidebar;
    private SwipeRefreshLayout swipeRefreshLayout;

    public ContactLayout(Context context) {
        this(context,null);
    }

    public ContactLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        //初始化contactLayout各子控件的布局
        initView();

    }

    private void initView() {
        //加载xml文件为view对象
        View.inflate(getContext(), R.layout.contact_layout, this);
        //找到列表
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        //找到中间显示首字母的textView
        tv_float = (TextView) findViewById(R.id.tv_float);
        //找到侧边快速滑动的菜单条
        slidebar = (Slidebar) findViewById(R.id.slidebar);
        //下拉刷新的控件
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ContactLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public ContactLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context,attrs);
    }

    /**
     * 给联系人recyclerView设置适配器
     * @param adapter
     */
    public void setAdapter(RecyclerView.Adapter adapter){
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    /**
     * 设置下拉刷新listener
     * @param listener
     */
    public void setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener listener){
        swipeRefreshLayout.setOnRefreshListener(listener);
    }

    /**
     *
     * @param isRefreshing
     */
    public void setRefreshing(boolean isRefreshing){
        swipeRefreshLayout.setRefreshing(isRefreshing);
    }
}
