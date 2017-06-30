package test.example.com.uestcer.utils;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by DK on 2017/4/27.
 */

public class StringUtils {
    /**
     *
     * @param username
     * @return
     */
    public static boolean checkUsername(String username){
       if (TextUtils.isEmpty(username)){
           return false;
       }else {
           return username.matches("^[a-zA-Z][0-9a-zA-Z]{4,19}$");
       }

    }

    /**
     * 校验密码合法性 数字 长度4-18位
     * @param pwd
     * @return
     */
    public static boolean Checkpwd(String pwd){
        if(TextUtils.isEmpty(pwd)){
            //如果没有输入
            return false;
        }else{
            return pwd.matches("^[0-9a-zA-Z]{4,19}$");
        }
    }
    public static String getFirstChar(String text){
        if(TextUtils.isEmpty(text)){
            return null;
        }else{
            return text.substring(0,1).toUpperCase();
        }
    }

    /**
     * 把日期格式化输出
     * @param date
     * @return
     */
    public static String getData(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(date);
    }
}
