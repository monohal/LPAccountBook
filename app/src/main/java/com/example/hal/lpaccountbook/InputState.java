package com.example.hal.lpaccountbook;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by HAL on 2016/03/06.
 * 金額を入力する状態 or 理由を入力する状態 どちらかを示す
 */
public interface InputState {
    String MONEY_FILE_NAME = "MONEY_DATA";
    String STRING_FILE_NAME = "STRING_DATA";
    String STATE_MONEY = "MONEY";
    String STATE_STRING = "STRING";

    String MONEY_DATA = Data.MONEY_DATA;
    String STRING_DATA = Data.STRING_DATA;

    String UL = "upper_left";
    String UR = "upper_right";
    String LL = "lower_left";
    String LR = "lower_right";

    /**
     *  ボタンにsettextする
     */
    void Init(Context context,View v);

    /**
     *  ボタンにsettextする
     * @param v ボタンをまとめたViewGroupを持ってきてます
     */
    void getInputData(View v);

    /**
     *  現在の状態から次の状態へ移行
     * @param context コンテキスト
     * @return 次の状態
     */
    InputState ChangeState(Context context);

    /**
     *  現在の状態を返す
     * @return 現在の状態
     */
    String getNowState();
}
