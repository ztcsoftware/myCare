package com.ztcsoftware.myre;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.ztcsoftware.myre.adapters.AdapterUnitTimeCalendar;
import org.joda.time.DateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static com.ztcsoftware.myre.SQLiteDBHelper.COLUMN_ID;
import static com.ztcsoftware.myre.SQLiteDBHelper.TABLE_NAME_P;
import static com.ztcsoftware.myre.Settings.interval;
import static com.ztcsoftware.myre.Settings.duration;

public class RangePickerActivity extends AppCompatActivity {

    private final static int MAX_SCREEN_HEIGHT=1200; //pixels
    private final static double MAX_SCREEN_INCHES=5.0; //in
    private final static int ONE_DAY=86400000; //milliseconds

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String month = "month";
    public static final String year = "year";
    public static final String auto = "auto";

    SharedPreferences sharedpreferences;

    List<String> myData = new ArrayList<>();
    List<EventDay> events = new ArrayList<>();
    private SQLiteDBHelper sqLiteDBHelper;
    private AdapterUnitTimeCalendar adapterUnitTimeCalendar;
    public  TextView noEntriesText;
    public ListView mListView;
    public Cursor cursorTimeEvents;
    LinearLayout listLinearLayout;

    @SuppressLint({"WrongConstant", "Range"})
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.date_range_picker_activity);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        listLinearLayout = findViewById(R.id.btmView);
        //Hide ActionBar for devices  <=5"
        if(dim()<MAX_SCREEN_INCHES){
            Objects.requireNonNull(getSupportActionBar()).hide();
            listLinearLayout = findViewById(R.id.btmView);
            ViewGroup.LayoutParams params = listLinearLayout.getLayoutParams();
            params.height = 310; //pixels
            listLinearLayout.setLayoutParams(params);
        }

        mListView = findViewById(R.id.eventList);

        noEntriesText = findViewById(R.id.emptyListEvents);

        sqLiteDBHelper  = SQLiteDBHelper.getmInstance(getApplicationContext());

//        Calendar calendar = Calendar.getInstance();
//        calendar.set(2021,11,20);
//        List<Calendar> calendars = new ArrayList<>();
//        calendars.add(calendar);
//        //calendar1.add(Calendar.DAY_OF_MONTH, 1);
//        events.add(new EventDay(calendar, R.drawable.ic_action_test));

        CalendarView calendarView =  findViewById(R.id.calendarView);
        calendarView.setSelectionBackground(R.drawable.custom_selector);
        calendarView.setCalendarDayLayout(R.layout.custom_calendar_day_row);

        //load the list when user starts the RangePickerActivity
        DateTime dateTime = new DateTime();
        loadTimeEventsCalendar(dateTime.monthOfYear().getAsShortText(),String.valueOf(dateTime.getYear()));
        editor.putString(month, dateTime.monthOfYear().getAsShortText());
        editor.putString(year, String.valueOf(dateTime.getYear()));

        editor.commit();

        calendarView.setOnDayClickListener(eventDay ->{

                    DateTime dateTimeWc=new DateTime(eventDay.getCalendar().getTimeInMillis());

                    editor.putString(month, dateTimeWc.monthOfYear().getAsShortText());
                    editor.putString(year, String.valueOf(dateTimeWc.getYear()));

                    editor.commit();

                    //This method returns two dates the first and the last user picked from calendar view
                    myData.add(String.valueOf(dateTimeWc.getMillis()));

                    if(sharedpreferences.getBoolean(auto,false)){
                            //calculate intervals for auto
                            if(myData.size()>=2){
                                    if (Long.parseLong(myData.get(0)) > Long.parseLong(myData.get(1))) {

                                        calculateIntervals(myData.get(1),myData.get(0),sharedpreferences.getInt(interval,28),sharedpreferences.getInt(duration,3));
                                        //update ListView after date range saved
                                        loadTimeEventsCalendar(dateTimeWc.monthOfYear().getAsShortText(), String.valueOf(dateTimeWc.getYear()));
                                    }else {
                                        calculateIntervals(myData.get(0),myData.get(1),sharedpreferences.getInt(interval,28),sharedpreferences.getInt(duration,3));
                                        //update ListView after date range saved
                                        loadTimeEventsCalendar(dateTimeWc.monthOfYear().getAsShortText(), String.valueOf(dateTimeWc.getYear()));
                                    }

                                    myData.clear();
                            }
                    }else { //manual
                        if (myData.size() >= 2) {
                            if (Long.parseLong(myData.get(0)) > Long.parseLong(myData.get(1))) {
                                saveToDB_P_TableForManual(myData.get(1), myData.get(0),0);
                                //update ListView after date range saved
                                loadTimeEventsCalendar(dateTimeWc.monthOfYear().getAsShortText(), String.valueOf(dateTimeWc.getYear()));
                            } else {
                                saveToDB_P_TableForManual(myData.get(0), myData.get(1),0);
                                //update ListView after date range saved
                                loadTimeEventsCalendar(dateTimeWc.monthOfYear().getAsShortText(), String.valueOf(dateTimeWc.getYear()));
                            }
                            myData.clear();
                        }
                    }
//                    Toast.makeText(getApplicationContext(),
//                            eventDay.getCalendar().getTime().toString() + " "
//                                    + eventDay.isEnabled(),
//                            Toast.LENGTH_SHORT).show();

                }
        );

        //handle the calendarView when months changed from user
        calendarView.setOnForwardPageChangeListener(() -> {
            DateTime dTF = new DateTime(calendarView.getCurrentPageDate().getTime());

            loadTimeEventsCalendar(dTF.monthOfYear().getAsShortText(),String.valueOf(dTF.getYear()));

            editor.putString(month, dTF.monthOfYear().getAsShortText());
            editor.putString(year, String.valueOf(dTF.getYear()));

            editor.commit();
        });

        calendarView.setOnPreviousPageChangeListener(() ->{

            DateTime dTP = new DateTime(calendarView.getCurrentPageDate().getTimeInMillis());

            loadTimeEventsCalendar(dTP.monthOfYear().getAsShortText(),String.valueOf(dTP.getYear()));

            editor.putString(month, dTP.monthOfYear().getAsShortText());
            editor.putString(year, String.valueOf(dTP.getYear()));

            editor.commit();
        });

        calendarView.setEvents(events);

        mListView.setOnItemClickListener((parent,view,position,id) ->{
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(RangePickerActivity.this);

            View dialogView = getLayoutInflater().inflate(R.layout.remove_entry_dialog,null);

            Button deleteBtn = dialogView.findViewById(R.id.deleteBtn);
            Button deleteAllBtn = dialogView.findViewById(R.id.deleteAll);
            Button cancel = dialogView.findViewById(R.id.cancel);

            mBuilder.setView(dialogView);

            final AlertDialog dialog = mBuilder.create();
            dialog.setCanceledOnTouchOutside(false);

            deleteBtn.setOnClickListener(v ->{
                        if (cursorTimeEvents!=null){
                            delete(cursorTimeEvents.getString(cursorTimeEvents.getColumnIndex("_id")));
                            loadTimeEventsCalendar(sharedpreferences.getString(month,null),sharedpreferences.getString(year,null));
                            cursorTimeEvents.moveToNext();
                        }
                Toast.makeText(RangePickerActivity.this,getResources().getText(R.string.entry_deleted),Toast.LENGTH_LONG ).show();
                dialog.cancel();
            });

            //show delete All button when P table is not empty and it has at least one record with function = 1
            if(!P_TableIsEmpty(TABLE_NAME_P) && cursorTimeEvents!=null && cursorTimeEvents.getInt(cursorTimeEvents.getColumnIndex("function"))==1 ){
                    deleteAllBtn.setVisibility(View.VISIBLE);

                    deleteAllBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            delAllRecords(TABLE_NAME_P);
                            loadTimeEventsCalendar(sharedpreferences.getString(month,null),sharedpreferences.getString(year,null));
                            Toast.makeText(RangePickerActivity.this,getResources().getText(R.string.entries_deleted),Toast.LENGTH_LONG ).show();
                            dialog.cancel();
                        }
                    });

            }

            cancel.setOnClickListener(v -> dialog.cancel());
            dialog.show();
        });
    }

    private void saveToDB_P_Table(String sD,String eD, String fD, int fnc) {

        SQLiteDatabase database = new SQLiteDBHelper(RangePickerActivity.this).getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(SQLiteDBHelper.FIELD1_P, sD);
        values.put(SQLiteDBHelper.FIELD2_P, eD);
        DateTime dateTime = new DateTime(Long.parseLong(sD));
        values.put(SQLiteDBHelper.FIELD3_P, dateTime.monthOfYear().getAsShortText());
        values.put(SQLiteDBHelper.FIELD4_P, dateTime.getYear());
        values.put(SQLiteDBHelper.FIELD5_P, fD);
        values.put(SQLiteDBHelper.FIELD6_P, fnc ); // saved values 0 or 1 ,    1 for auto ,   0 for manual

        database.insert(SQLiteDBHelper.TABLE_NAME_P, null, values);

        Toast.makeText(this, getResources().getString(R.string.data_saved), Toast.LENGTH_LONG).show();
    }

    private void saveToDB_P_TableForManual(String sD,String eD, int fnc) {

        SQLiteDatabase database = new SQLiteDBHelper(RangePickerActivity.this).getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(SQLiteDBHelper.FIELD1_P, sD);
        values.put(SQLiteDBHelper.FIELD2_P, eD);
        DateTime dateTime = new DateTime(Long.parseLong(sD));
        values.put(SQLiteDBHelper.FIELD3_P, dateTime.monthOfYear().getAsShortText());
        values.put(SQLiteDBHelper.FIELD4_P, dateTime.getYear());
        values.put(SQLiteDBHelper.FIELD6_P, fnc ); // saved values 0 or 1 ,    1 for auto ,   0 for manual

        database.insert(SQLiteDBHelper.TABLE_NAME_P, null, values);

        Toast.makeText(this, getResources().getString(R.string.data_saved), Toast.LENGTH_LONG).show();
    }

    private double dim() {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenHeight = displayMetrics.heightPixels;
        int screenWidth = displayMetrics.widthPixels;
        double y = Math.pow(screenHeight /displayMetrics.xdpi, 2);
        double x = Math.pow(screenWidth /displayMetrics.xdpi, 2);
        double screenInches = Math.sqrt(x + y);
        screenInches = (double) Math.round(screenInches *10)/10;

        Log.i(TAG, "Device screen inches = " + screenInches);
        return screenInches;
    }

    private void loadTimeEventsCalendar(String msD,String ysD){
            cursorTimeEvents = sqLiteDBHelper.getTimeEventCalendar(msD,ysD);//Read the data from the table "p_data"

            if(cursorTimeEvents!=null){
                //load cursor to the adapter
                adapterUnitTimeCalendar = new AdapterUnitTimeCalendar(getApplicationContext(),cursorTimeEvents,0);
                //handle the adapter if it is empty or not
                if(adapterUnitTimeCalendar.isEmpty())
                    noEntriesText.setVisibility(View.VISIBLE);
                else
                    noEntriesText.setVisibility(View.INVISIBLE);

                mListView.setAdapter(adapterUnitTimeCalendar);
            }
    }

    @Override
    protected void onDestroy() {
        sqLiteDBHelper.close();
        super.onDestroy();
    }

    private void delete(String text){
        SQLiteDatabase database = new SQLiteDBHelper(RangePickerActivity.this).getWritableDatabase();
        database.delete(SQLiteDBHelper.TABLE_NAME_P, COLUMN_ID + "=?" , new String[] {text});
    }

    //for auto
    private void calculateIntervals(String sD, String eD, int iDur, int calcDur){

            //check if exist previous interval chain (if yes, will deleted and insert the new)
            if (!P_TableIsEmpty(TABLE_NAME_P)) delAllRecords(TABLE_NAME_P);

            //calculate the length of first interval
            long iS = Long.parseLong(eD) - Long.parseLong(sD);

            long space = daysToMillis(Math.round(iDur/2));
            long fD = Long.parseLong(sD) + space + iS;

            //save the first interval
            saveToDB_P_Table(sD,eD,String.valueOf(fD),1);


            //calculate the rest intervals
            int i=0;
            long newSD=Long.parseLong(sD);
            long newED,newFD;

            while (i<calcDur-1){
                    newSD = newSD + iS+daysToMillis(iDur);
                    newED = newSD + iS;
                    newFD = newSD + space + (newED-newSD);

                    saveToDB_P_Table(String.valueOf(newSD),String.valueOf(newED),String.valueOf(newFD),1);
                    i++;
            }

    }

    private void delAllRecords(String tableName) {
        SQLiteDatabase database = new SQLiteDBHelper(RangePickerActivity.this).getWritableDatabase();
        database.execSQL("delete from "+ tableName);
    }

    private Boolean P_TableIsEmpty(String TableName) {

            SQLiteDatabase database = new SQLiteDBHelper(RangePickerActivity.this).getWritableDatabase();
            long NoOfRows = DatabaseUtils.queryNumEntries(database,TableName);

        return NoOfRows == 0;
    }

    private long daysToMillis(int days){
        return (long) days * ONE_DAY;
    }
}