package com.ztcsoftware.myre.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.ztcsoftware.myre.R;

import org.joda.time.DateTime;

public class AdapterUnitTimeCalendar extends CursorAdapter {

    public AdapterUnitTimeCalendar(Context context, Cursor c, int flags) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.custom_event_row,parent,false);
    }

    @SuppressLint({"Range", "SetTextI18n"})
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView textsD = view.findViewById(R.id.sD);
        TextView texteD = view.findViewById(R.id.eD);
        TextView textfD = view.findViewById(R.id.fD);

        DateTime dateTimesD = new DateTime(Long.parseLong(cursor.getString(cursor.getColumnIndex("sD"))));
        DateTime dateTimeeD = new DateTime(Long.parseLong(cursor.getString(cursor.getColumnIndex("eD"))));

        if(cursor.getString(cursor.getColumnIndex("fD"))!=null) {
            DateTime dateTimefD = new DateTime(Long.parseLong(cursor.getString(cursor.getColumnIndex("fD"))));
            textfD.setText(dateTimefD.dayOfWeek().getAsShortText()+" "+dateTimefD.dayOfMonth().getAsShortText());
        }else {
            textfD.setText(context.getResources().getString(R.string.fert_msg));
        }

        textsD.setText(dateTimesD.dayOfWeek().getAsShortText()+" "+dateTimesD.dayOfMonth().getAsShortText());
        texteD.setText(dateTimeeD.dayOfWeek().getAsShortText()+" "+ dateTimeeD.dayOfMonth().getAsShortText());

    }
}
