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
    public static final String DATABASE_NAME = "LPAccountBook";
    public static final String TABLE_NAME = "AccountBook";
    public static final Integer DATABASE_VERSION = 1;

    Context context;
    ContentValues values;
    ContentResolver cr;
    Uri uri;

    public Database(Context context){
        this.context = context;
    }
    /**
     * 引数のmoney_data,stringをgetContextResolverのinsertを用いて保存
     * @param money_data 金額
     * @param string_data 区分
     * @param ymd_data 年月日
     */
    public void DBSave(int money_data, String string_data, int ymd_data) {
        cr = context.getContentResolver();
        uri = Data.CONTENT_URI;

        values = new ContentValues();
        values.put(Data.MONEY_DATA, money_data);
        values.put(Data.STRING_DATA, string_data);
        values.put(Data.M_YMD_DATA, ymd_data);
        values.put(Data.S_YMD_DATA, DateManage.convertStringDate(ymd_data));

        SectionManage sm = new SectionManage(context);
        values.put(Data.SECTION_DATA, sm.getNowSection());
        cr.insert(uri, values);
    }

    /**
     * id指定が無い場合全データの削除
     */
    public void DBDelete() {
        uri = Data.CONTENT_URI;
        cr = context.getContentResolver();
        cr.delete(uri, null, null);
    }

    /**
     * idで指定したデータの削除
     * @param id 消したいデータのid
     */
    public void DBDelete(int id) {
        uri = ContentUris.withAppendedId(Data.CONTENT_URI, id);
        cr = context.getContentResolver();
        cr.delete(uri, null, null);
    }

    /**
     * idが一番大きい (一番最後に挿入された)データを削除
     */
    public void DBUndo() {
        DBDelete(getLastid());
    }


    /**
     * 一番大きいidを探す
     * @return idの最大値
     */
    public int getLastid(){
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
