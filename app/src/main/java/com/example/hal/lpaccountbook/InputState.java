package com.example.hal.lpaccountbook;

import android.content.Context;
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

    String MONEY_DATA = "MONEY";
    String STRING_DATA = "STRING";


    String UL = "hl_button";
    String UR = "hr_button";
    String LL = "ll_button";
    String LR = "lr_button";

    /**
     *  ボタンにsettextする
     */
    void Init(Context context,ViewGroup vg);

    /**
     *  ボタンにsettextする
     * @param v ボタンをまとめたViewGroupを持ってきてます
     */
    void setInputData(View v);

    /**
     *  現在の状態から次の状態へ移行
     * @param context
     * @return 次の状態
     */
    InputState ChangeState(Context context);

    /**
     *  現在の状態を返す
     * @return 現在の状態
     */
    String getNowState();
}
