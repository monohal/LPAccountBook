package com.example.hal.lpaccountbook;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * Created by HAL on 2016/03/06.
 * State:Money
 */
public class InputMoneyData implements InputState {
    private static final String NOW_STATE = "MONEY";
    private static InputMoneyData singleton = new InputMoneyData();

    SharedPreferences.Editor money_editor;

    Button btnUL;
    Button btnUR;
    Button btnLL;
    Button btnLR;

    /**
     * コンストラクタ
     */
    private InputMoneyData(){}

    /**
     * singletonパターン
     */
    public static InputState getInstance(){
        return singleton;
    }

    @Override
    public void Init(Context context, View v){
        Log.d("OUTPUT", "IMD");
        SharedPreferences money_data = context.getSharedPreferences(MONEY_FILE_NAME, Context.MODE_PRIVATE);
        money_editor = money_data.edit();
        setMoneyData(money_data, v);
    }

    @Override
    public void getInputData(View v){
        money_editor.putInt(MONEY_DATA, getMoneyData(v));
        Log.d("OUTPUT", String.valueOf(getMoneyData(v)));
        money_editor.apply();
    }

    @Override
    public InputState ChangeState(Context context){
        return InputStringData.getInstance();
    }

    @Override
    public String getNowState(){
        return NOW_STATE;
    }

    public void setMoneyData(SharedPreferences data, View v){
        btnUL = (Button) v.findViewById(R.id.button_UL);
        btnUR = (Button) v.findViewById(R.id.button_UR);
        btnLL = (Button) v.findViewById(R.id.button_LL);
        btnLR = (Button) v.findViewById(R.id.button_LR);

        btnUL.setText(String.valueOf(data.getInt(UL, 200)));
        btnUR.setText(String.valueOf(data.getInt(UR, 500)));
        btnLL.setText(String.valueOf(data.getInt(LL, 1000)));
        btnLR.setText(String.valueOf(data.getInt(LR, 3000)));
    }

    public int getMoneyData(View v){
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
