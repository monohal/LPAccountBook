package com.example.hal.lpaccountbook;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by HAL on 2016/04/04.
 */
public class SectionManage extends Application{
    Context context;
    int section;
    SharedPreferences preference;
    SharedPreferences.Editor editor;

    //TODO 日付をさかのぼったとき元に戻せない（現時点では仕様）

    public SectionManage(Context context){

        this.context = context;
        preference = this.context.getSharedPreferences(Data.SETTING_FILE, MODE_PRIVATE);
        editor = preference.edit();

        if(preference.contains(Data.KEY_SECTION) == false) {
            editor.putInt(Data.KEY_SECTION, 1);
            editor.commit();
        }
        section =   preference.getInt(Data.KEY_SECTION, 1);
    }

    public int getNowSection(){
        return this.section;
    }

    public void changeSection(){
        section += 1;
        editor.putInt(Data.KEY_SECTION, section);
        editor.commit();
        Log.d("OUTPUT", "ChangeSection_" + section);
    }
}
