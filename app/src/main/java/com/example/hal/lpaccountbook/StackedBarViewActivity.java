package com.example.hal.lpaccountbook;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;

/**
 * Created by HAL on 2016/04/06.
 */
public class StackedBarViewActivity extends Activity{

    BarChart sbChart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_stackedbar);

        sbChart = (BarChart) findViewById(R.id.chart1);
        sbChart.setDescription("");

        sbChart.setMaxVisibleValueCount(60);
        sbChart.setDrawGridBackground(false);
        sbChart.setDrawBarShadow(false);
        sbChart.setDrawValueAboveBar(false);

        DataBaseHelper helper = new DataBaseHelper(getApplicationContext());
        SQLiteDatabase sqdb = helper.getReadableDatabase();
        SectionManage sm = new SectionManage(this);

        int i;

        String col[] = new String[]{Data._ID, "TOTAL(" + Data.MONEY_DATA +")", Data.STRING_DATA};


        Cursor cur = sqdb.query(
                Database.TABLE_NAME,       //テーブル名
                col,            //カラム名の配列
                Data.SECTION_DATA + "=?",           //取得するレコードの条件
                new String[] { String.valueOf(sm.getNowSection())},           //取得するレコードの条件
                Data.STRING_DATA,           //GroupBy
                null,           //Having
                "TOTAL(" + Data.MONEY_DATA +")" + " Asc");          //orderBy

        String[] xVals = new String[cur.getCount()];
        float[] yVals = new float[cur.getCount()];
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        ArrayList<Integer> colors = new ArrayList<>();

        cur.moveToFirst();
        for (i=0; i< cur.getCount(); i++) {     //query結果
            Log.d("OUTPUT", String.valueOf(cur.getInt(0)));
            Log.d("OUTPUT",String.valueOf(cur.getInt(1)));
            Log.d("OUTPUT", cur.getString(2));
            Log.d("OUTPUT", "--------------------");

            yVals[i] = Float.valueOf(cur.getInt(1));
            xVals[i] = cur.getString(2);
            colors.add(Color.parseColor(Data.COLOR_DATA[i % 4]));
            cur.moveToNext();
        }

        yVals1.add(new BarEntry(yVals,0));
        BarDataSet set1 = new BarDataSet(yVals1, "Element");
        set1.setColors(colors);
        set1.setStackLabels(xVals);

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);

        BarData barData = new BarData(new String[]{"4/6~4/10"}, dataSets);
        sbChart.setData(barData);
        sbChart.invalidate();
    }
}
