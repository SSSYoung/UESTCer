package test.example.com.uestcer.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by oo on 2017/6/28.
 */

public class ContactOpenHelper extends SQLiteOpenHelper {
    /**
     * 初始化创建一个名为contact.db的数据库
     * @param context
     */
    public ContactOpenHelper(Context context){
        super(context,"contact.db",null,1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table contact_info(_id integer primary key autoincrement,username varchar(20),contact varchar(20))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
