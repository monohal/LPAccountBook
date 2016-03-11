package com.example.hal.lpaccountbook;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by HAL on 2016/03/11.
 */
public class Database {
    public static final String DATABASE_NAME = "TestDataBase";
    public static final String TABLE_NAME = "TestDataBaseTable";
    public static final Integer DATABASE_VERSION = 1;

    ContentValues values;
    ContentResolver cr;
    Uri uri;

    /**
     * 引数のname,ageをgetContextResolverのinsertを用いて保存
     * @param name
     * @param age
     * @param context getContentResolver() のために必要
     */
    public void DBSave(String name, int age, Context context) {
        cr = context.getContentResolver();
        uri = Data.CONTENT_URI;

        values = new ContentValues();
        values.put(Data.NAME, name);
        values.put(Data.AGE, age);

        cr.insert(uri, values);
    }

    /**
     * id指定が無い場合全データの削除
     * @param context
     */
    public void DBDelete(Context context) {
        uri = Data.CONTENT_URI;
        cr = context.getContentResolver();
        cr.delete(uri, null, null);
    }

    /**
     * idで指定したデータの削除
     * @param id 消したいデータのid
     * @param context
     */
    public void DBDelete(int id, Context context) {
        uri = ContentUris.withAppendedId(Data.CONTENT_URI, id);
        cr = context.getContentResolver();
        cr.delete(uri, null, null);
    }

    /**
     * idが一番大きい (一番最後に挿入された)データを削除
     * @param context
     */
    public void DBUndo(Context context) {
        DBDelete(getLastid(context), context);
    }


    /**
     * 一番大きいidを探す
     * @param context
     * @return idの最大値
     */
    public int getLastid(Context context){
        int lastid = 0;

        Cursor cur = context.getContentResolver().query(Data.CONTENT_URI, null, null, null, null);

        cur.moveToFirst();
        do{
            lastid = cur.getInt(cur.getColumnIndex(Data._ID));
        }while(cur.moveToNext());
        cur.close();
        return lastid;
    }
}
