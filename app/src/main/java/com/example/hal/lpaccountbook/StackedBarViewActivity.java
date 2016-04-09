package com.example.hal.lpaccountbook;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;

/**
 * Created by HAL on 2016/04/06.
 */
public class StackedBarViewActivity extends Activity{

    HorizontalBarChart sbChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_stackedbar);

        DataBaseHelper helper = new DataBaseHelper(getApplicationContext());
        SQLiteDatabase sqdb = helper.getReadableDatabase();
        SectionManage sm = new SectionManage(this);

        sbChart = (HorizontalBarChart) findViewById(R.id.chart1);

        sbChart.setDescription("");
        sbChart.setMaxVisibleValueCount(60);
        sbChart.setDrawGridBackground(false);
        sbChart.setDrawBarShadow(false);
        sbChart.setDrawValueAboveBar(false);

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        ArrayList<Integer> colors = new ArrayList<>();

        int mDateMin = Integer.MAX_VALUE;
        int mDateMax = Integer.MIN_VALUE;
        String sDateMin = null;
        String sDateMax = null;

        ArrayList<String> xVals = new ArrayList<String>();
        float[] fDataArray;
        String[] sDataArray = null;

        String col[] = new String[]{Data._ID, "TOTAL(" + Data.MONEY_DATA +")"
                , Data.STRING_DATA, Data.M_YMD_DATA, Data.S_YMD_DATA};

        for(int j = 0; j < sm.getNowSection(); j++ ) {
            Cursor cur = sqdb.query(
                    Database.TABLE_NAME,        //テーブル名
                    col,                        //カラム名の配列
                    Data.SECTION_DATA + "=?",   //取得するレコードの条件
                    new String[]{String.valueOf(sm.getNowSection() - j)},       //取得するレコードの条件
                    Data.STRING_DATA,           //GroupBy
                    null,                       //Having
                    "TOTAL(" + Data.MONEY_DATA + ")" + " Asc");          //orderBy

            fDataArray = new float[cur.getCount()];
            sDataArray = new String[cur.getCount()];


            cur.moveToFirst();
            for (int i = 0; i < cur.getCount(); i++) {    //query結果
                Log.d("OUTPUT", String.valueOf(cur.getInt(0)));
                Log.d("OUTPUT", String.valueOf(cur.getInt(1)));
                Log.d("OUTPUT", cur.getString(2));
                Log.d("OUTPUT", String.valueOf(cur.getInt(3)));
                Log.d("OUTPUT", cur.getString(4));
                Log.d("OUTPUT", "-------");

                fDataArray[i] = Float.valueOf(cur.getInt(1));
                sDataArray[i] = cur.getString(2);
                cur.getString(2);
                colors.add(Color.parseColor(Data.COLOR_DATA[i % 4]));

                //日付最小値と最大値を得る
                if (cur.getInt(3) < mDateMin) {
                    sDateMin = cur.getString(4);
                }
                if (cur.getInt(3) > mDateMax) {
                    sDateMax = cur.getString(4);
                }
                cur.moveToNext();
            }

            yVals1.add(new BarEntry(fDataArray, j));
            xVals.add(sDateMin +"～"+ sDateMax);
            Log.d("OUTPUT", "--------------------");
        }

        BarDataSet set1 = new BarDataSet(yVals1, "");
        set1.setColors(colors);
        set1.setStackLabels(sDataArray);

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(xVals, dataSets);
        data.setValueFormatter(new MyValueFormatter());

        sbChart.setData(data);
        sbChart.invalidate();
    }
}
