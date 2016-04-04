package com.example.hal.lpaccountbook;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;

import java.util.ArrayList;

public class LPAccountBookActivity extends AppCompatActivity {

<<<<<<< HEAD
    //test
    public static final String FIRST_TIME_CHECK = "FIRST_TIME_CHECK";
=======
    
>>>>>>> 2662b9b... section追加
    private InputState inputstate = InputMoneyData.getInstance();
    SharedPreferences money_data;
    SharedPreferences string_data;

    Database database;
    PieChart pieChart;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lpaccount_book);
        view = findViewById(R.id.activity_input);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        database = new Database();

        setSupportActionBar(toolbar);
        FirstTime();
        createPieChart();
        inputstate.Init(this, view);
    }


    public void onClick(View v) {
        inputstate.getInputData(v);

        if(inputstate.getNowState() == InputState.STATE_STRING){
            money_data = getSharedPreferences(InputState.MONEY_FILE_NAME, Context.MODE_PRIVATE);
            string_data = getSharedPreferences(InputState.STRING_FILE_NAME, Context.MODE_PRIVATE);

            int mdata = money_data.getInt(InputState.MONEY_DATA, 0);
<<<<<<< HEAD
            String sdata = string_data.getString(InputState.STRING_DATA,"default");
=======
            String sdata = string_data.getString(InputState.STRING_DATA, "default");

            DatePicker datePicker = (DatePicker)findViewById(R.id.datePicker);

            SharedPreferences preference = getSharedPreferences(Data.SETTING_FILE, MODE_PRIVATE);
            SharedPreferences.Editor editor = preference.edit();

            if(preference.contains(Data.KEY_DATE) == false) {
                editor.putString(Data.KEY_DATE, DateManage.getStringDate(datePicker) + "～");
                editor.commit();
            }
>>>>>>> 2662b9b... section追加

            database.DBSave(mdata, sdata, getDate(),this);
            PieChartRefresh();

            Snackbar.make(view, mdata + ":" + sdata + " Saved", Snackbar.LENGTH_LONG)
                    .setAction("Undo", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            database.DBUndo(getBaseContext());
                            PieChartRefresh();
                        }
                    })
                    .show();
        }
        inputstate = inputstate.ChangeState(this);
        inputstate.Init(this, view);
    }

    private void FirstTime(){
<<<<<<< HEAD
        SharedPreferences preference = getSharedPreferences(FIRST_TIME_CHECK, MODE_PRIVATE);
=======
        SharedPreferences preference = getSharedPreferences(Data.SETTING_FILE, MODE_PRIVATE);
>>>>>>> 2662b9b... section追加
        SharedPreferences.Editor editor = preference.edit();

        if(preference.getBoolean("Launched", false)==false){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

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

            case R.id.action_alldelete:
<<<<<<< HEAD
                database.DBDelete(this);
=======
                database.DBDelete();

                //オールデリートなのでセンターに表示される日付も初期化
                SharedPreferences preference = getSharedPreferences(Data.SETTING_FILE, MODE_PRIVATE);
                SharedPreferences.Editor editor = preference.edit();
                editor.remove(Data.KEY_DATE);
                editor.commit();
>>>>>>> 2662b9b... section追加
                return true;

            case R.id.action_changesection:
                SectionManage sm = new SectionManage(this);
                sm.changeSection();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void createPieChart() {
        pieChart = (PieChart) findViewById(R.id.pie_chart);

        pieChart.setDrawHoleEnabled(false); // 真ん中に穴を空けるかどうか
        //pieChart.setHoleRadius(50f);       // 真ん中の穴の大きさ(%指定)
        //pieChart.setHoleColorTransparent(true);
        pieChart.setTransparentCircleRadius(55f);
        pieChart.setRotationAngle(270);          // 開始位置の調整
        pieChart.setRotationEnabled(false);       // 回転可能かどうか
        pieChart.getLegend().setEnabled(false);   //
        pieChart.setDescription(null);
        pieChart.setData(createPieChartData());

        // 更新
        pieChart.invalidate();
        // アニメーション
        pieChart.animateXY(2000, 2000); // 表示アニメーション
    }

    public void PieChartRefresh(){
        pieChart.setData(createPieChartData());
        pieChart.invalidate();
    }

    // pieChartのデータ設定
    private PieData createPieChartData() {
        ArrayList<Entry> yVals = new ArrayList<>();
        ArrayList<String> xVals = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();

        DataBaseHelper helper = new DataBaseHelper(getApplicationContext());
        SQLiteDatabase sqdb = helper.getReadableDatabase();

        int i;

        String col[] = new String[]{Data._ID, "TOTAL(" + Data.MONEY_DATA +")", Data.STRING_DATA};

        Cursor cur = sqdb.query(
                Database.TABLE_NAME,       //テーブル名
                col,            //カラム名の配列
                null,           //取得するレコードの条件
                null,           //取得するレコードの条件
                Data.STRING_DATA,           //GroupBy
                null,           //Having
                "TOTAL(" + Data.MONEY_DATA +")" + " Asc");          //orderBy

        cur.moveToFirst();
        for (i=0; i< cur.getCount(); i++) {     //query結果
            Log.d("OUTPUT",String.valueOf(cur.getInt(0)));
            Log.d("OUTPUT",String.valueOf(cur.getInt(1)));
            Log.d("OUTPUT", cur.getString(2));
            Log.d("OUTPUT","--------------------");

            xVals.add(cur.getString(2));
            yVals.add(new Entry(cur.getInt(1),i));
            colors.add(Color.parseColor(Data.COLOR_DATA[i % 4]));
            cur.moveToNext();
        }

        /*
        test
        xVals.add("未使用");
        yVals.add(new Entry(70000, i+1));
        colors.add(Color.parseColor("#999999"));
        */

        PieDataSet dataSet = new PieDataSet(yVals, "Data");

        if(i != 1){
            dataSet.setSliceSpace(5f);
        }
        dataSet.setSelectionShift(1f);
        dataSet.setColors(colors);
        dataSet.setDrawValues(true);

        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new DefaultValueFormatter(0));

        // テキストの設定
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.WHITE);
        return data;
    }

    public int getDate(){
        DatePicker datePicker = (DatePicker)findViewById(R.id.datePicker);

<<<<<<< HEAD
        int year = datePicker.getYear();//年を取得
        int month = datePicker.getMonth() + 1;//月を取得
        int day = datePicker.getDayOfMonth();//日を取得
        int ymd = year * 10000 + month * 100 + day;
=======
        SharedPreferences date = getSharedPreferences(Data.SETTING_FILE, MODE_PRIVATE);
        String sDate = date.getString(Data.KEY_DATE, "");
        SpannableString s = new SpannableString(sDate);
>>>>>>> 2662b9b... section追加

        Log.d("OUTPUT",String.valueOf(ymd));

        return ymd;
    }


}
