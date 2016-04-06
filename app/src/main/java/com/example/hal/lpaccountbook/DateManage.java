package com.example.hal.lpaccountbook;

import android.content.SharedPreferences;
import android.widget.DatePicker;

/**
 * Created by HAL on 2016/04/05.
 */
public class DateManage {
    public static int getDate(DatePicker datePicker) {
        int year = datePicker.getYear();//年を取得
        int month = datePicker.getMonth() + 1;//月を取得
        int day = datePicker.getDayOfMonth();//日を取得

        int ymd = year * 10000 + month * 100 + day;
        return ymd;
    }

    public static String getStringDate(DatePicker datePicker) {
        return convertStringDate(getDate(datePicker));
    }

    public static String convertStringDate(int ymd){
        int day = ymd % 100;
        int month = (ymd % 10000 - day) / 100;
        int year = ymd / 10000;
        return year + "/" + month +"/"+ day;
    }

    public static void resetCenterDate(SharedPreferences.Editor editor){
        editor.remove(Data.KEY_DATE);
        editor.commit();
    }
}
