package com.ztcsoftware.myre.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.ztcsoftware.myre.R;
import com.ztcsoftware.myre.SQLiteDBHelper;
import com.ztcsoftware.myre.ShowTheList;

public class Adapter extends CursorAdapter {
    public SQLiteDBHelper sqLiteDBHelper;
    public  Cursor cursorTZ,cursorDays;

    public Adapter(Context context, Cursor c,int flag) {
        super(context, c,0);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_showthelist,parent,false);
    }

    @SuppressLint("Range")
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        sqLiteDBHelper = SQLiteDBHelper.getmInstance(context.getApplicationContext());

        TextView textPn = view.findViewById(R.id.pN);
        TextView textuFo = view.findViewById(R.id.uFo);
        TextView textlastSub = view.findViewById(R.id.lastSub);

        ImageView iconLn = view.findViewById(R.id.iconLn);
        ImageView iconM = view.findViewById(R.id.iconM);
        ImageView iconA = view.findViewById(R.id.iconA);
        ImageView iconN = view.findViewById(R.id.iconN);

        TextView monday = view.findViewById(R.id.mon);
        TextView tuesday = view.findViewById(R.id.tue);
        TextView wednesday = view.findViewById(R.id.wed);
        TextView thursday = view.findViewById(R.id.thu);
        TextView friday = view.findViewById(R.id.fri);
        TextView saturday = view.findViewById(R.id.sat);
        TextView sunday = view.findViewById(R.id.sun);

        @SuppressLint("Range") String pN = cursor.getString(cursor.getColumnIndex("pN"));
        @SuppressLint("Range") String uFo = cursor.getString(cursor.getColumnIndex("uFo"));
        @SuppressLint("Range") String lastSub = cursor.getString(cursor.getColumnIndex("lastSubmission"));

        cursorTZ = sqLiteDBHelper.getListTZ(pN);

        if(cursorTZ.moveToFirst()){
                    do {
                        Log.i("cursorTZ",cursorTZ.getString(cursorTZ.getColumnIndex("tZ")));

                        if (cursorTZ.getString(cursorTZ.getColumnIndex("tZ")).equals("Text_A")){
                            iconLn.setImageResource(R.drawable.ic_action_bed_white);

                        }
                        if (cursorTZ.getString(cursorTZ.getColumnIndex("tZ")).equals("Text_B")){
                            iconM.setImageResource(R.drawable.ic_action_alarm_white);

                        }
                        if (cursorTZ.getString(cursorTZ.getColumnIndex("tZ")).equals("Text_C")){
                            iconA.setImageResource(R.drawable.ic_action_sun_white);
                        }
                        if (cursorTZ.getString(cursorTZ.getColumnIndex("tZ")).equals("Text_D")){
                            iconN.setImageResource(R.drawable.ic_action_moon_white);
                        }

                    }while (cursorTZ.moveToNext());
                }

        cursorDays = sqLiteDBHelper.getListDays(pN);
        cursorDays.getCount();

        if(cursorDays.moveToFirst()){
            do {
                Log.i("cursorDays",cursorDays.getString(cursorDays.getColumnIndex("dY")));

                if (cursorDays.getString(cursorDays.getColumnIndex("dY")).equals("1")){
                    monday.setText(R.string.cb1);
                }
                if (cursorDays.getString(cursorDays.getColumnIndex("dY")).equals("2")){
                    tuesday.setText(R.string.cb2);
                }
                if (cursorDays.getString(cursorDays.getColumnIndex("dY")).equals("3")){
                    wednesday.setText(R.string.cb3);
                }
                if (cursorDays.getString(cursorDays.getColumnIndex("dY")).equals("4")){
                    thursday.setText(R.string.cb4);
                }
                if (cursorDays.getString(cursorDays.getColumnIndex("dY")).equals("5")){
                    friday.setText(R.string.cb5);
                }
                if (cursorDays.getString(cursorDays.getColumnIndex("dY")).equals("6")){
                    saturday.setText(R.string.cb6);
                }
                if (cursorDays.getString(cursorDays.getColumnIndex("dY")).equals("7")){
                    sunday.setText(R.string.cb7);
                }
            }while (cursorDays.moveToNext());
        }

        textPn.setText(pN);
        textuFo.setText(uFo);
        textlastSub.setText(lastSub);

    }
}
