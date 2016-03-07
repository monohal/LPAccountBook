package com.example.hal.lpaccountbook;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

public class LPAccountBookActivity extends AppCompatActivity {

    public static final String FIRST_TIME_CHECK = "FIRST_TIME_CHECK";
    private InputState inputstate = InputMoneyData.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lpaccount_book);
        ViewGroup viewGroup;
        viewGroup = (ViewGroup)findViewById(R.id.activity_input);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FirstTime();
        inputstate.Init(this, viewGroup);
    }

    public void onClick(View v) {


    }

    private void FirstTime(){
        SharedPreferences preference = getSharedPreferences(FIRST_TIME_CHECK, MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();

        if(preference.getBoolean("Launched", false)==false){
            AlertDialog.Builder alertDialog=new AlertDialog.Builder(this);

            // ダイアログの設定
            alertDialog.setTitle("ヒント");          //タイトル
            alertDialog.setMessage("「画面右上 メニューキー → Help」\nでヘルプが読めます。");      //内容
            alertDialog.setIcon(android.R.drawable.ic_menu_info_details);   //アイコン設定
            alertDialog.create();
            alertDialog.show();

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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
