package com.example.hal.lpaccountbook;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * Created by HAL on 2016/03/06.
 * State:String
 */
public class InputStringData implements InputState {
    private static final String NOW_STATE = "MONEY";
    private static InputStringData singleton = new InputStringData();

    SharedPreferences.Editor money_editor;
    SharedPreferences.Editor string_editor;

    Button btnUL;
    Button btnUR;
    Button btnLL;
    Button btnLR;

    /**
     * コンストラクタ
     */
    private InputStringData(){}

    /**
     * singletonパターン
     */
    public static InputState getInstance(){
        return singleton;
    }

    @Override
    public void Init(Context context, View v){
        Log.d("OUTPUT", "ISD");
        SharedPreferences money_data = context.getSharedPreferences(MONEY_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences string_data = context.getSharedPreferences(STRING_FILE_NAME, Context.MODE_PRIVATE);

        money_editor = money_data.edit();
        string_editor = string_data.edit();

        setStringData(string_data, v);
    }

    @Override
    public void getInputData(View v){
        getStringData(v);
        Log.d("OUTPUT", getStringData(v));
        string_editor.apply();
    }

    @Override
    public InputState ChangeState(Context context){
        return InputMoneyData.getInstance();
    }

    @Override
    public String getNowState(){
        return NOW_STATE;
    }

    public void setStringData(SharedPreferences data, View v){
        btnUL = (Button) v.findViewById(R.id.button_UL);
        btnUR = (Button) v.findViewById(R.id.button_UR);
        btnLL = (Button) v.findViewById(R.id.button_LL);
        btnLR = (Button) v.findViewById(R.id.button_LR);

        btnUL.setText(data.getString(UL, "食費"));
        btnUR.setText(data.getString(UR, "生活費"));
        btnLL.setText(data.getString(LL, "交際費"));
        btnLR.setText(data.getString(LR, "雑費"));
    }

    public String getStringData(View v){
        switch (v.getId()){
            case R.id.button_UL:
                return btnUL.getText().toString();
            case R.id.button_UR:
                return btnUR.getText().toString();
            case R.id.button_LL:
                return btnLL.getText().toString();
            case R.id.button_LR:
                return btnLR.getText().toString();
        }
        return null;
    }
}
