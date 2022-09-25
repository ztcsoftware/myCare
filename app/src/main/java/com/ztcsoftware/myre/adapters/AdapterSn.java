package com.ztcsoftware.myre.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import com.ztcsoftware.myre.R;

public class AdapterSn extends CursorAdapter {

    public AdapterSn(Context context, Cursor c,int flag) {
        super(context, c,0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_show_sn_list,parent,false);
    }

    @SuppressLint("Range")
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView textPn = view.findViewById(R.id.pN);
        TextView textuFo = view.findViewById(R.id.uFo);
        TextView text_tZ = view.findViewById(R.id.tZ);
        TextView textdY = view.findViewById(R.id.dY);

        String pN = cursor.getString(cursor.getColumnIndex("pN"));
        String uFo = cursor.getString(cursor.getColumnIndex("uFo"));
        String tZ = cursor.getString(cursor.getColumnIndex("tZ"));
        String dY = cursor.getString(cursor.getColumnIndex("dY"));

        textPn.setText(pN);
        textuFo.setText(uFo);
        text_tZ.setText(timeZoneToText(tZ,context.getApplicationContext()));
        textdY.setText(dayCodeToText(dY,context.getApplicationContext()));
    }

    private String dayCodeToText(String data,Context context){
        String day;
        switch(data){
                  case  "1" : day = context.getResources().getString(R.string.monday);
                           break;
                    case  "2" : day = context.getResources().getString(R.string.tuesday);
                        break;
                    case  "3" : day = context.getResources().getString(R.string.wednesday);
                        break;
                    case  "4" : day = context.getResources().getString(R.string.thursday);
                        break;
                    case  "5" : day = context.getResources().getString(R.string.friday);
                        break;
                    case  "6" : day = context.getResources().getString(R.string.saturday);
                        break;
                    case  "7" : day = context.getResources().getString(R.string.sunday);
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + data);
                    }

        return day;
    }

    private String timeZoneToText(String data, Context context){
        String timeZone;
        switch (data){
                        case "Text_A": timeZone = context.getResources().getString(R.string.text_a);
                                        break;
                        case "Text_B": timeZone = context.getResources().getString(R.string.text_b);
                                        break;
                        case "Text_C": timeZone = context.getResources().getString(R.string.text_c);
                                        break;
                        case "Text_D": timeZone = context.getResources().getString(R.string.text_d);
                                        break;
                            default:
                                throw new IllegalStateException("Unexpected value: " + data);
                }
        return timeZone;
    }
}
