package com.example.hal.lpaccountbook;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by HAL on 2016/03/11.
 */
public class Data implements BaseColumns{
    public static final String AUTHORITY = "com.example.hal.lpaccountbook";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + Database.TABLE_NAME);
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.hal.persons";
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.hal.persons";

    public static final String SETTING_FILE = "SETTING_FILE";
    public static final String KEY_DATE = "KEY_DATE";
    public static final String KEY_SECTION = "KEY_SECTION";

    public static final String MONEY_DATA = "money_data";
    public static final String STRING_DATA = "string_data";
    public static final String YMD_DATA = "ymd_data";
    public static final String M_YMD_DATA = "m_ymd_data";
    public static final String S_YMD_DATA = "s_ymd_data";
    public static final String SECTION_DATA = "section_data";

    public static final String[] COLOR_DATA = {"#99CC00", "#FFBB33", "#AA66CC", "#FF7F7F"};

}
