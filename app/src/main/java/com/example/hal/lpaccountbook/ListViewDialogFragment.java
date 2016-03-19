package com.example.hal.lpaccountbook;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by HAL on 2016/03/19.
 */
public class ListViewDialogFragment extends DialogFragment {
    Database db;

    @Override
    public Dialog onCreateDialog(Bundle saveInstanceState) {
        AlertDialog.Builder builder;
        Bundle bundle = getArguments();

        final int intId = bundle.getInt(Data._ID);
        int intMdata = bundle.getInt(Data.MONEY_DATA);
        String strSdata = bundle.getString(Data.STRING_DATA);
        String message = intMdata + "：" + strSdata +"\nデータを削除します。 よろしいですか？";

        builder = new AlertDialog.Builder(getActivity())
                .setTitle("Warning")
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db = new Database();
                        db.DBDelete(intId, getActivity());
                    }
                })
                .setNegativeButton("Cancel", null);

        return builder.create();
    }
}
