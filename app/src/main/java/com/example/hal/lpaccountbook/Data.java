package com.example.hal.lpaccountbook;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by HAL on 2016/03/11.
 */
public class Data implements BaseColumns{
    public static final String AUTHORITY = "com.example.hal.databasetest";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + Database.TABLE_NAME);
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.hal.persons";
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.hal.persons";

    public static final String NAME = "name";
    public static final String AGE = "age";
}
