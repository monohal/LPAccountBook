package com.example.hal.lpaccountbook;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by HAL on 2016/03/11.
 */
public class DataBaseHelper extends SQLiteOpenHelper {
    public DataBaseHelper(Context c) {
        super(c, Database.DATABASE_NAME, null, Database.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + Database.TABLE_NAME + " ( "
                        + Data._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + Data.MONEY_DATA + " INTEGER, "
                        + Data.STRING_DATA + " STRING,"
                        + Data.M_YMD_DATA + " INTEGER,"
                        + Data.S_YMD_DATA + " STRING,"
                        + Data.SECTION_DATA + " INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS " + Database.TABLE_NAME);
        onCreate(db);
    }
}
