package com.example.hal.lpaccountbook;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.echo.holographlibrary.PieGraph;
import com.echo.holographlibrary.PieSlice;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.security.KeyStore;
import java.util.ArrayList;

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
        database = new Database();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FirstTime();
        //DrawGraph();

        createPieChart();
        inputstate.Init(this, view);
    }


    public void onClick(View v) {
        inputstate.getInputData(v);

        if(inputstate.getNowState() == InputState.STATE_STRING){
            money_data = getSharedPreferences(InputState.MONEY_FILE_NAME, Context.MODE_PRIVATE);
            string_data = getSharedPreferences(InputState.STRING_FILE_NAME, Context.MODE_PRIVATE);

            int mdata = money_data.getInt(InputState.MONEY_DATA, 0);
            String sdata = string_data.getString(InputState.STRING_DATA,"default");

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

            case R.id.action_alldelete:
                database.DBDelete(this);
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    private void createPieChart() {
        PieChart pieChart = (PieChart) findViewById(R.id.pie_chart);

        pieChart.setDrawHoleEnabled(true); // 真ん中に穴を空けるかどうか
        pieChart.setHoleRadius(50f);       // 真ん中の穴の大きさ(%指定)
        //pieChart.setHoleColorTransparent(true);
        pieChart.setTransparentCircleRadius(55f);
        pieChart.setRotationAngle(270);          // 開始位置の調整
        pieChart.setRotationEnabled(true);       // 回転可能かどうか
        pieChart.getLegend().setEnabled(true);   //
        pieChart.setDescription("PieChart 説明");
        pieChart.setData(createPieChartData());

        // 更新
        pieChart.invalidate();
        // アニメーション
        pieChart.animateXY(2000, 2000); // 表示アニメーション
    }

    // pieChartのデータ設定
    private PieData createPieChartData() {
        ArrayList<Entry> yVals = new ArrayList<>();
        ArrayList<String> xVals = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();

        DatabaseHelper helper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase sqdb = helper.getReadableDatabase();

        String col[] = new String[]{Data._ID, "TOTAL(" + Data.MONEY_DATA +")", Data.STRING_DATA};

        Cursor cur = sqdb.query(
                Database.TABLE_NAME,       //テーブル名
                col,            //カラム名の配列
                null,           //取得するレコードの条件
                null,           //取得するレコードの条件
                Data.STRING_DATA,           //GroupBy
                null,           //Having
                null);          //orderBy

        int i;
        String[] color = {"#99CC00", "#FFBB33", "#AA66CC", "#FF7F7F"};

        cur.moveToFirst();
        for (i=0; i< cur.getCount(); i++) {     //query結果
            Log.d("OUTPUT",String.valueOf(cur.getInt(0)));
            Log.d("OUTPUT",String.valueOf(cur.getInt(1)));
            Log.d("OUTPUT", cur.getString(2));
            Log.d("OUTPUT","--------------------");

            xVals.add(cur.getString(2));
            yVals.add(new Entry(cur.getInt(1),i));
            colors.add(Color.parseColor(color[i % 4]));


            /*
            PieGraph graph = (PieGraph) findViewById(R.id.piegraph);
            PieSlice slice = new PieSlice();
            slice.setColor(Color.parseColor(color[i %3]));
            slice.setValue(cur.getInt(1));
            graph.addSlice(slice);
            */

            cur.moveToNext();
        }

        /*
        xVals.add("A");
        xVals.add("B");
        xVals.add("C");
        */

        /*
        yVals.add(new Entry(20, 0));
        yVals.add(new Entry(30, 1));
        yVals.add(new Entry(50, 2));
        */

        PieDataSet dataSet = new PieDataSet(yVals, "Data");
        dataSet.setSliceSpace(5f);
        dataSet.setSelectionShift(1f);

        // 色の設定
        /*
        colors.add(ColorTemplate.COLORFUL_COLORS[0]);
        colors.add(ColorTemplate.COLORFUL_COLORS[1]);
        colors.add(ColorTemplate.COLORFUL_COLORS[2]);
        */
        dataSet.setColors(colors);
        dataSet.setDrawValues(true);

        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new DefaultValueFormatter(0));

        // テキストの設定
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.WHITE);
        return data;
    }

    /*
    public void DrawGraph(){
        DatabaseHelper helper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase sqdb = helper.getReadableDatabase();

        String col[] = new String[]{Data._ID, "TOTAL(" + Data.MONEY_DATA +")", Data.STRING_DATA};

        Cursor cur = sqdb.query(
                Database.TABLE_NAME,       //テーブル名
                col,            //カラム名の配列
                null,           //取得するレコードの条件
                null,           //取得するレコードの条件
                Data.STRING_DATA,           //GroupBy
                null,           //Having
                null);          //orderBy

        int i;
        String[] color = {"#99CC00","#FFBB33","#AA66CC"};

        cur.moveToFirst();
        for (i=0; i< cur.getCount(); i++) {     //query結果
            Log.d("OUTPUT",String.valueOf(cur.getInt(0)));
            Log.d("OUTPUT",String.valueOf(cur.getInt(1)));
            Log.d("OUTPUT", cur.getString(2));
            Log.d("OUTPUT","--------------------");

            PieGraph graph = (PieGraph) findViewById(R.id.piegraph);
            PieSlice slice = new PieSlice();
            slice.setColor(Color.parseColor(color[i %3]));
            slice.setValue(cur.getInt(1));
            graph.addSlice(slice);

            cur.moveToNext();
        }

        cur.close();
    }
    */

}
