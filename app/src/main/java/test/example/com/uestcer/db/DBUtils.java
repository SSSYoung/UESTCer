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

    public static void initDBUtils(Context context){
        DBUtils.context = context.getApplicationContext();
    }


}
