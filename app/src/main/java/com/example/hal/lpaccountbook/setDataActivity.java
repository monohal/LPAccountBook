package com.example.hal.lpaccountbook;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

/**
 * Created by HAL on 2016/03/20.
 */
public class setDataActivity extends AppCompatActivity {

    SharedPreferences money_data;
    SharedPreferences.Editor money_editor;
    SharedPreferences string_data;
    SharedPreferences.Editor string_editor;

    EditText etMoneyUL;
    EditText etMoneyUR;
    EditText etMoneyLL;
    EditText etMoneyLR;

    EditText etStringUL;
    EditText etStringUR;
    EditText etStringLL;
    EditText etStringLR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setdata);

        string_data = getSharedPreferences(InputState.STRING_FILE_NAME, MODE_PRIVATE);
        money_data = getSharedPreferences(InputState.MONEY_FILE_NAME, MODE_PRIVATE);
        setData(money_data, string_data);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveData();

                Snackbar.make(view, "Saved" , Snackbar.LENGTH_LONG)
                        .show();
            }
        });

    }

    public void SaveData(){
        money_editor = money_data.edit();
        money_editor.putInt(InputState.UL, Integer.parseInt(etMoneyUL.getText().toString()));
        money_editor.putInt(InputState.UR, Integer.parseInt(etMoneyUR.getText().toString()));
        money_editor.putInt(InputState.LL, Integer.parseInt(etMoneyLL.getText().toString()));
        money_editor.putInt(InputState.LR, Integer.parseInt(etMoneyLR.getText().toString()));
        money_editor.commit();

        string_editor = string_data.edit();
        string_editor.putString(InputState.UL, etStringUL.getText().toString());
        string_editor.putString(InputState.UR, etStringUR.getText().toString());
        string_editor.putString(InputState.LL, etStringLL.getText().toString());
        string_editor.putString(InputState.LR, etStringLR.getText().toString());
        string_editor.commit();

    }

    public void setData(SharedPreferences mdata, SharedPreferences sdata){
        etMoneyUL = (EditText) findViewById(R.id.et_money_UL);
        etMoneyUR = (EditText) findViewById(R.id.et_money_UR);
        etMoneyLL = (EditText) findViewById(R.id.et_money_LL);
        etMoneyLR = (EditText) findViewById(R.id.et_money_LR);

        etMoneyUL.setText(String.valueOf(mdata.getInt(InputState.UL, 200)));
        etMoneyUR.setText(String.valueOf(mdata.getInt(InputState.UR, 500)));
        etMoneyLL.setText(String.valueOf(mdata.getInt(InputState.LL, 1000)));
        etMoneyLR.setText(String.valueOf(mdata.getInt(InputState.LR, 3000)));

        etStringUL = (EditText) findViewById(R.id.et_class_UL);
        etStringUR = (EditText) findViewById(R.id.et_class_UR);
        etStringLL = (EditText) findViewById(R.id.et_class_LL);
        etStringLR = (EditText) findViewById(R.id.et_class_LR);

        etStringUL.setText(sdata.getString(InputState.UL, "食費"));
        etStringUR.setText(sdata.getString(InputState.UR, "生活費"));
        etStringLL.setText(sdata.getString(InputState.LL, "交際費"));
        etStringLR.setText(sdata.getString(InputState.LR, "雑費"));
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lpaccount_book, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up btnLL, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent intent;

        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.action_input:
                intent = new Intent(this, LPAccountBookActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_settings:
                intent = new Intent(this, setDataActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_listview:
                intent = new Intent(this, ListViewActivity.class);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
