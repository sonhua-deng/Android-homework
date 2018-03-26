package com.example.lab8;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Lenovo on 2017/12/10.
 */

public class Mydatabase extends SQLiteOpenHelper {
    public static final String CREATE_ITEM="create table if not exists item ("
            +"name text primary key,"
            +"gift text,"
            +"birth text )";
    private Context mcontext;
    public Mydatabase(Context context, String name, SQLiteDatabase.CursorFactory factory,int version) {
        super(context,name,factory,version);
        mcontext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ITEM);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldversion,int newversion) {

    }
}
