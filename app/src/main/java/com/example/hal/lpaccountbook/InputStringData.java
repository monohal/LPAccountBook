package com.example.hal.lpaccountbook;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by HAL on 2016/03/06.
 */
public class InputStringData implements InputState{
    private static final String NOW_STATE = "String";
    private static InputStringData singleton = new InputStringData();

    SharedPreferences.Editor editor;
    Button btnUL;
    Button btnUR;
    Button btnLL;
    Button btnLR;

    private InputStringData(){}


    public static InputState getInstance(){
        return singleton;
    }

    @Override
    public void Init(Context context, ViewGroup vg){
        SharedPreferences data = context.getSharedPreferences(STRING_FILE_NAME, Context.MODE_PRIVATE);
        editor = data.edit();
        setStringData(data, vg);
    }

    @Override
    public void setInputData(View v){
        getStringData(v);

    }

    @Override
    public InputState ChangeState(Context context){
        return InputMoneyData.getInstance();
    }

    @Override
    public String getNowState(){
        return NOW_STATE;
    }

    public void setStringData(SharedPreferences data, ViewGroup vg){
        btnUL = (Button)vg.findViewById(R.id.button_UL);
        btnUR = (Button)vg.findViewById(R.id.button_UR);
        btnLL = (Button)vg.findViewById(R.id.button_LL);
        btnLR = (Button)vg.findViewById(R.id.button_LR);

        btnUL.setText(String.valueOf(data.getInt(UL, 200)));
        btnUR.setText(String.valueOf(data.getInt(UR, 500)));
        btnLL.setText(String.valueOf(data.getInt(LL, 1000)));
        btnLR.setText(String.valueOf(data.getInt(LR, 3000)));
    }

    public int getStringData(View v){
        switch (v.getId()){
            case R.id.button_UL:
                return Integer.parseInt(btnUL.getText().toString());
            case R.id.button_UR:
                return Integer.parseInt(btnUR.getText().toString());
            case R.id.button_LL:
                return Integer.parseInt(btnLL.getText().toString());
            case R.id.button_LR:
                return Integer.parseInt(btnLR.getText().toString());
        }
        return 0;
    }
}
