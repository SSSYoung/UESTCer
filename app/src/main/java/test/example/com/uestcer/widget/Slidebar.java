package test.example.com.uestcer.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import test.example.com.uestcer.Adapter.ContactAdapter;
import test.example.com.uestcer.R;
import test.example.com.uestcer.utils.StringUtils;

/**
 * Created by DK on 2017/5/27.
 */

class Slidebar extends View{
    private String[] sections = {"搜","A","B","C","D","E","F","G","H","I","J","K","L","M","N",
            "O","P","Q","R","S","T","U","V","W","X","Y","Z"};
    private Paint paint;
    private int x;
    private int viewHeight;
    private RecyclerView recyclerView;
    private TextView tv_float;
    private ContactAdapter adapter;

    public Slidebar(Context context) {
        this(context,null);
    }

    public Slidebar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        //创建画笔对象
        paint = new Paint();
        //开启抗锯齿效果
        paint.setAntiAlias(true);
        //让文字绘制的时候居中显示
        paint.setTextAlign(Paint.Align.CENTER);
        //设置画笔颜色
        paint.setColor(Color.GRAY);
        //设置文字的大小
        paint.setTextSize(getResources().getDimension(R.dimen.slide_text_size));
    }

    public Slidebar(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Slidebar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        //当这个View的大小发生变化的时候就会走onSizeChanged
        x = w/2;
//        获取到View的高度
        viewHeight = h;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(tv_float == null){
            //找到父容器
            ViewGroup parent = (ViewGroup) getParent();
            //在父容器中找到要操作的控件
            recyclerView = (RecyclerView) parent.findViewById(R.id.recyclerView);
            tv_float = (TextView) parent.findViewById(R.id.tv_float);
            adapter = (ContactAdapter) recyclerView.getAdapter();
        }
        String startChar;

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                //手指按下 把textView展示出来 修改背景颜色为灰色
                setBackgroundResource(R.color.background_gray);
                tv_float.setVisibility(View.VISIBLE);
                //手指移动或者按下的时候 根据当前的坐标 计算出究竟要显示那个文字
                startChar = sections[getIndex(event.getY())];
                //在中间的textview上展示这个文字
                tv_float.setText(startChar);

                scrollRecyclerView(startChar);

//                recyclerView.smoothScrollToPosition();
                break;
            case MotionEvent.ACTION_MOVE:
                //手指移动 改变textview文字的内容
                startChar = sections[getIndex(event.getY())];
                tv_float.setText(startChar);
                scrollRecyclerView(startChar);
                break;
            case MotionEvent.ACTION_UP:
                //手指抬起的时候 把textview隐藏起来 修改背景颜色为透明
                setBackgroundColor(Color.TRANSPARENT);
                tv_float.setVisibility(View.GONE);
                break;
        }
        return true;
    }

    private void scrollRecyclerView(String startChar) {
        //获取recyclerview展示的所有文字的集合
        List<String> contacts = adapter.getContacts();
        if(contacts!=null && contacts.size()>0){
            for(int i = 0;i<contacts.size();i++){
                if(StringUtils.getFirstChar(contacts.get(i)).equals(startChar)){
                    //让recyclerView 平滑滚动到这个条目上
                    recyclerView.smoothScrollToPosition(i);
                    break;
                }
            }
        }
    }

    int getIndex(float y){
        //每一个文字占多高
        int sectionHeight = viewHeight/sections.length;
        // 当前的位置/文字占高度
        int result = (int)y/sectionHeight;
        return result<0?0:result>sections.length-1?sections.length-1:result;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        for(int i = 0;i<sections.length;i++){
            //drawText 参数1要绘制的内容 参数2和3文字要绘制的位置坐标 参数4画笔
            //需要注意 x,y坐标是文字的左下角的坐标
            canvas.drawText(sections[i],x,viewHeight/sections.length *(i+1),paint);
        }

    }
}
