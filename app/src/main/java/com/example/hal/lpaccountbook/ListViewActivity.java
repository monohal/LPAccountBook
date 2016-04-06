package com.example.hal.lpaccountbook;

import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

/**
 * Created by HAL on 2016/03/19.
 */
public class ListViewActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>{

    Context context;
    Database db;
    SQLiteOpenHelper helper;
    Uri uri;
    ContentResolver cr;
    SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);

        context = getApplicationContext();
        cr = getContentResolver();
        db = new Database(this);

        Output();
    }

    public void Output(){
        ListView listview = (ListView) findViewById(R.id.listView);

        uri = ContentUris.withAppendedId(Data.CONTENT_URI, 1);
        Cursor cur = cr.query(uri, null, null, null, null);

        String[] from = {Data.MONEY_DATA, Data.STRING_DATA, Data.S_YMD_DATA, Data.SECTION_DATA};
        int[] to = {R.id.listlayout_tv1, R.id.listlayout_tv2, R.id.listlayout_tv3, R.id.listlayout_tv4};
        adapter = new SimpleCursorAdapter(this, R.layout.listviewlayout, cur, from, to, 0);
        listview.setAdapter(adapter);
        getLoaderManager().initLoader(0, null, this);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                helper = new DataBaseHelper(getApplicationContext());
                SQLiteDatabase sqdb = helper.getReadableDatabase();

                Cursor cur = sqdb.query(Database.TABLE_NAME,
                        new String[]{Data._ID, Data.MONEY_DATA, Data.STRING_DATA},
                        null, null, null, null, null);

                if (cur.moveToPosition(pos)) {
                    // 検索結果をCursorから取り出す
                    Bundle bundle = new Bundle();
                    bundle.putInt(Data._ID, cur.getInt(0));
                    bundle.putInt(Data.MONEY_DATA, cur.getInt(1));
                    bundle.putString(Data.STRING_DATA, cur.getString(2));

                    ListViewDialogFragment dialog = new ListViewDialogFragment();
                    dialog.setArguments(bundle);
                    dialog.show(getFragmentManager(), "test");
                }
                cur.close();
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                Data._ID,
                Data.MONEY_DATA,
                Data.STRING_DATA,
                Data.M_YMD_DATA,
                Data.S_YMD_DATA,
                Data.SECTION_DATA
        };

        return new CursorLoader(
                this,
                Data.CONTENT_URI,
                projection,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
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
