package com.example.hal.lpaccountbook;

import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
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
        db = new Database();

        Output();
    }

    public void Output(){
        ListView listview = (ListView) findViewById(R.id.listView);

        uri = ContentUris.withAppendedId(Data.CONTENT_URI, 1);
        Cursor cur = cr.query(uri, null, null, null, null);
        String[] from = {Data._ID, Data.MONEY_DATA, Data.STRING_DATA};
        int[] to = {R.id.listlayout_tv1, R.id.listlayout_tv2, R.id.listlayout_tv3};

        adapter = new SimpleCursorAdapter(this, R.layout.listviewlayout, cur, from, to, 0);
        listview.setAdapter(adapter);
        getLoaderManager().initLoader(0, null, this);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                helper = new DatabaseHelper(getApplicationContext());
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
                Data.STRING_DATA
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
}
