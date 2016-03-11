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
    private DatabaseHelper databaseHelper;
    private static final UriMatcher uriMatcher;

    private static final int PERSONS = 1;
    private static final int PERSON_ID = 2;

    private static HashMap<String, String> personProjectionMap;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(Data.AUTHORITY, Database.TABLE_NAME, PERSONS);
        uriMatcher.addURI(Data.AUTHORITY, Database.TABLE_NAME + "/#", PERSON_ID);

        personProjectionMap = new HashMap<String, String>();
        personProjectionMap.put(Data._ID, Data._ID);
        personProjectionMap.put(Data.NAME, Data.NAME);
        personProjectionMap.put(Data.AGE, Data.AGE);
    }

    @Override
    public boolean onCreate() {
        databaseHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(Database.TABLE_NAME);

        switch (uriMatcher.match(uri)) {
            case PERSONS:
                qb.setProjectionMap(personProjectionMap);
                break;
            case PERSON_ID:
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
        if (uriMatcher.match(uri) != PERSONS) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        if (values.containsKey(Data.NAME) == false) {
            values.put(Data.NAME, "詠み人知らず");
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
            case PERSONS:
                count = db.update(Database.TABLE_NAME, values, selection,
                        selectionArgs);
                break;

            case PERSON_ID:
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
            case PERSONS:
                count = db.delete(Database.TABLE_NAME, selection,
                        selectionArgs);
                break;

            case PERSON_ID:
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
            case PERSONS:
                return Data.CONTENT_TYPE;
            case PERSON_ID:
                return Data.CONTENT_ITEM_TYPE;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }
}
