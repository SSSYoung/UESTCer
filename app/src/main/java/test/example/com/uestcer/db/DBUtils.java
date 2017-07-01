package test.example.com.uestcer.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fullcircle on 2017/1/3.
 */

public class DBUtils {

    private static Context context = null;

    /**
     * 调用之前一定 要初始化
     * @param context
     */
    public static void initDBUtils(Context context){
        DBUtils.context = context.getApplicationContext();
    }

    /**
     *  初始化联系人，在数据库里面先去取
     * @param username 登录的用户名
     * @return
     */
    public static List<String> initContact(String username){
        List<String> result=new ArrayList<>();
        //上下文为空，抛出异常,初始化在使用
        if (context==null){
            throw new RuntimeException("先初始化后再调用");
        }else {
            //创建sqliteopenhelper对象
            ContactOpenHelper openHelper = new ContactOpenHelper(context);
            SQLiteDatabase db = openHelper.getReadableDatabase();
            Cursor cursor = db.query("contact_info",
                    new String[]{"contact"},
                    "username = ?",
                    new String[]{username},
                    null, null,null
                    );
            while (cursor.moveToNext()){
                String name = cursor.getString(0);
                result.add(name);
            }
            cursor.close();
            db.close();
        }
        return result;
    }

    /**
     * 吧从环信取得的联系人数据保存到Android手机的数据库中
     * @param username 当前的用户名
     * @param contacts  该用户从环信请求到的联系人的集合
     */
    public static void updateContactFromEMServer(String username,List<String> contacts){
        if (context==null){
            throw new RuntimeException("先初始化后再调用");
        }else {
            ContactOpenHelper openHelper = new ContactOpenHelper(context);
            SQLiteDatabase db = openHelper.getReadableDatabase();
            db.beginTransaction();
            //db如果没有标记为成功，就会回滚
            try{
                //先把所有的联系人都删除
                db.delete("contact_info","username=?",new String[]{username});
                //再把环信返回的联系人都保存到数据库中
                ContentValues values = new ContentValues();
                values.put("username",username);
                for (String contact:contacts){
                    values.put("contact",contact);
                    db.insert("contact_info",null,values);
                }
                //标记为成功
                db.setTransactionSuccessful();
            }finally {
                //结束，没有成功更改，数据会回滚
                db.endTransaction();
            }

        }
    }

}
