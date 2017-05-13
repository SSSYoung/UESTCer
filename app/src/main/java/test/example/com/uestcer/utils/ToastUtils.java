package test.example.com.uestcer.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * toast的工具类,ToastUtils.showToast直接显示toast
 * Created by DK on 2017/4/27.
 */

public class ToastUtils {
    private static Toast toast;

    /**
     *
     * @param context
     * @param msg 要显示的toast text
     */
    public static void showToast(Context context, String msg){
        if(toast==null){
            //如果toast第一次创建 makeText创建toast对象
            toast = Toast.makeText(context.getApplicationContext(),msg,Toast.LENGTH_SHORT);
        }else{
            //如果toast存在 只需要修改文字
            toast.setText(msg);
        }
        toast.show();
    }
}
