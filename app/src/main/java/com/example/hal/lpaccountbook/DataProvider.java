package com.example.hal.lpaccountbook;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.HashMap;

/**
 * Created by HAL on 2016/03/11.
 */
public class DataProvider extends ContentProvider {
    private DataBaseHelper databaseHelper;
    private static final UriMatcher uriMatcher;

    private static final int DATA = 1;
    private static final int DATA_ID = 2;

    private static HashMap<String, String> personProjectionMap;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(Data.AUTHORITY, Database.TABLE_NAME, DATA);
        uriMatcher.addURI(Data.AUTHORITY, Database.TABLE_NAME + "/#", DATA_ID);

        personProjectionMap = new HashMap<String, String>();
        personProjectionMap.put(Data._ID, Data._ID);
        personProjectionMap.put(Data.MONEY_DATA, Data.MONEY_DATA);
        personProjectionMap.put(Data.STRING_DATA, Data.STRING_DATA);
        personProjectionMap.put(Data.YMD_DATA, Data.YMD_DATA);
    }

    @Override
    public boolean onCreate() {
        databaseHelper = new DataBaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(Database.TABLE_NAME);

        switch (uriMatcher.match(uri)) {
            case DATA:
                qb.setProjectionMap(personProjectionMap);
                break;
            case DATA_ID:
                qb.setProjectionMap(personProjectionMap);
                qb.appendWhere(Data._ID + "=" + uri.getPathSegments().get(1));
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor c = qb.query(db, projection, selection, selectionArgs, null,
                null, sortOrder);

        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (uriMatcher.match(uri) != DATA) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        //多分いらない
        if (values.containsKey(Data.MONEY_DATA) == false) {
            values.put(Data.MONEY_DATA, "金額記入なし");
        }

        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        long rowId = db.insert(Database.TABLE_NAME, null, values);
        if (rowId > 0) {
            Uri returnUri = ContentUris.withAppendedId(Data.CONTENT_URI, rowId);
            getContext().getContentResolver().notifyChange(returnUri, null);
            return returnUri;
        }

        throw new SQLException("Failed to insert row into " + uri);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int count;
        switch (uriMatcher.match(uri)) {
            case DATA:
                count = db.update(Database.TABLE_NAME, values, selection,
                        selectionArgs);
                break;

            case DATA_ID:
                String id = uri.getPathSegments().get(1);
                count = db.update(Database.TABLE_NAME, values, Data._ID
                        + "="
                        + id
                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection
                        + ')' : ""), selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int count;
        switch (uriMatcher.match(uri)) {
            case DATA:
                count = db.delete(Database.TABLE_NAME, selection,
                        selectionArgs);
                break;

            case DATA_ID:
                String id = uri.getPathSegments().get(1);
                count = db.delete(Database.TABLE_NAME, Data._ID
                        + "="
                        + id
                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection
                        + ")" : ""), selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case DATA:
                return Data.CONTENT_TYPE;
            case DATA_ID:
                return Data.CONTENT_ITEM_TYPE;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }
}
