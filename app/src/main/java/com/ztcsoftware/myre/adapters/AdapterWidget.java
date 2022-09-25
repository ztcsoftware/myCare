package com.ztcsoftware.myre.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cursoradapter.widget.CursorAdapter;

import com.ztcsoftware.myre.R;

public class AdapterWidget extends CursorAdapter {

    public AdapterWidget(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_show_widget_list,parent,false);
    }

    @SuppressLint("Range")
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView textPn = view.findViewById(R.id.pN);
        TextView textuFo = view.findViewById(R.id.uFo);

        String pN = cursor.getString(cursor.getColumnIndex("pN"));
        String uFo = cursor.getString(cursor.getColumnIndex("uFo"));

        textPn.setText(pN);
        textuFo.setText(uFo);

    }
}
