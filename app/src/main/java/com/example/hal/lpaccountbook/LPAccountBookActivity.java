package com.example.hal.lpaccountbook;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class LPAccountBookActivity extends AppCompatActivity {

    public static final String FIRST_TIME_CHECK = "FIRST_TIME_CHECK";
    private InputState inputstate = InputMoneyData.getInstance();
    SharedPreferences money_data;
    SharedPreferences string_data;

    Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lpaccount_book);
        View view;
        view = findViewById(R.id.activity_input);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FirstTime();
        inputstate.Init(this, view);
    }


    public void onClick(View v) {
        inputstate.getInputData(v);

        if(inputstate.getNowState() == InputState.STATE_STRING){
            money_data = getSharedPreferences(InputState.MONEY_FILE_NAME, Context.MODE_PRIVATE);
            string_data = getSharedPreferences(InputState.STRING_FILE_NAME, Context.MODE_PRIVATE);

            int mdata = money_data.getInt(InputState.MONEY_DATA, 0);
            String sdata = string_data.getString(InputState.STRING_DATA,"default");
            database = new Database();
            database.DBSave(mdata, sdata, this);
        }

        inputstate = inputstate.ChangeState(this);
        View view;
        view = findViewById(R.id.activity_input);
        inputstate.Init(this, view);
    }

    private void FirstTime(){
        SharedPreferences preference = getSharedPreferences(FIRST_TIME_CHECK, MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();

        if(preference.getBoolean("Launched", false)==false){
            AlertDialog.Builder alertDialog=new AlertDialog.Builder(this);

            // ダイアログの設定
            alertDialog.setTitle(R.string.dialog_firsttime_title)          //タイトル
                .setMessage(R.string.dialog_firsttime_message)      //内容
                .setIcon(android.R.drawable.ic_menu_info_details)   //アイコン設定
                .create()
                .show();

            editor.putBoolean("Launched", true);
            editor.commit();
        }
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
