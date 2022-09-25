package com.ztcsoftware.myre.utils;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.ztcsoftware.myre.MainActivity;
import com.ztcsoftware.myre.R;
import com.ztcsoftware.myre.SQLiteDBHelper;
import com.ztcsoftware.myre.model.Item;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import static androidx.core.app.NotificationCompat.EXTRA_NOTIFICATION_ID;

public class BroadcastReceiver extends android.content.BroadcastReceiver {

    private static final String Zone_LN = "Text_A";
    private static final String Zone_M  = "Text_B";
    private static final String Zone_A  = "Text_C";
    private static final String Zone_N  = "Text_D";

    private final static String CHANNEL_ID="notification #1";

    public int idCounter=0;

    public SQLiteDBHelper sqLiteDBHelper;
    private final List<Item> item = new ArrayList<>();

    @Override
    public void onReceive(Context context, Intent intent) {
        sqLiteDBHelper  = SQLiteDBHelper.getmInstance(context.getApplicationContext());
        if ( intent.getAction()!=null && intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Log.i("Time after boot","time");
        }

        if(getCurList().size()!=0 )
            notification(context.getApplicationContext() , context.getResources().getString(R.string.ntf_msg) );

    }
    public void notification(Context context,String message){

        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(context,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        Intent snoozeIntent = new Intent(context, BroadcastReceiverFromNotification.class);
        snoozeIntent.setAction("snooze");
        snoozeIntent.putExtra("extra id", 0);
        PendingIntent snoozePendingIntent =
                PendingIntent.getBroadcast(context, 0, snoozeIntent, 0);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.appicon)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setAllowSystemGeneratedContextualActions(false)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .addAction(R.drawable.ic_action_alarm, context.getString(R.string.submit),
                        snoozePendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(idCounter++, builder.build());

    }
    public List<Item> getCurList(){
        Cursor cursorCur = null;
        DateTime dateTime = new DateTime();

        switch (getCoding()){
            case Zone_LN: if (dateTime.getDayOfWeek()==1||dateTime.getDayOfWeek()==2 || dateTime.getDayOfWeek()==3 || dateTime.getDayOfWeek()==4
                    || dateTime.getDayOfWeek()==5 || dateTime.getDayOfWeek()==6 || dateTime.getDayOfWeek()==7 )
            {
                cursorCur = sqLiteDBHelper.getSnList(Zone_LN, String.valueOf(dateTime.getDayOfWeek()));
                if(cursorCur.moveToFirst()){
                    do {
                        item.add(new Item(cursorCur.getString(1)));
                    }while(cursorCur.moveToNext());
                }
            }
            else
            {
                cursorCur = sqLiteDBHelper.getSnList(Zone_LN,"cb8");
                item.add(new Item(cursorCur.getString(1)));
            }

                break;
            case Zone_M:
                if (dateTime.getDayOfWeek()==1||dateTime.getDayOfWeek()==2 || dateTime.getDayOfWeek()==3 || dateTime.getDayOfWeek()==4
                        || dateTime.getDayOfWeek()==5 || dateTime.getDayOfWeek()==6 || dateTime.getDayOfWeek()==7 )
                {
                    cursorCur = sqLiteDBHelper.getSnList(Zone_M,String.valueOf(dateTime.getDayOfWeek()));
                    if(cursorCur.moveToFirst()){
                        do {
                            item.add(new Item(cursorCur.getString(1)));
                        }while(cursorCur.moveToNext());
                    }
                }
                else{
                    cursorCur = sqLiteDBHelper.getSnList(Zone_M,"cb8");
                    item.add(new Item(cursorCur.getString(1)));
                }

                break;
            case Zone_A:
                if (dateTime.getDayOfWeek()==1||dateTime.getDayOfWeek()==2 || dateTime.getDayOfWeek()==3 || dateTime.getDayOfWeek()==4
                        || dateTime.getDayOfWeek()==5 || dateTime.getDayOfWeek()==6 || dateTime.getDayOfWeek()==7 )
                {

                    cursorCur = sqLiteDBHelper.getSnList(Zone_A,String.valueOf(dateTime.getDayOfWeek()));
                    Log.i("cursor return value ", String.valueOf(cursorCur));
                    if (cursorCur.moveToFirst()){
                        do{
                            Log.i("cursor --> ", cursorCur.getString(1));
                            item.add(new Item(cursorCur.getString(1)));

                            Log.i("list content --> ", String.valueOf(item.size()));
                        }while (cursorCur.moveToNext());
                    }

                }
                else{
                    cursorCur = sqLiteDBHelper.getSnList(Zone_A,"cb8");
                    item.add(new Item(cursorCur.getString(1)));
                }

                break;
            case Zone_N:
                if (dateTime.getDayOfWeek()==1||dateTime.getDayOfWeek()==2 || dateTime.getDayOfWeek()==3 || dateTime.getDayOfWeek()==4
                        || dateTime.getDayOfWeek()==5 || dateTime.getDayOfWeek()==6 || dateTime.getDayOfWeek()==7 )
                {
                    cursorCur = sqLiteDBHelper.getSnList(Zone_N,String.valueOf(dateTime.getDayOfWeek()));
                    if(cursorCur.moveToFirst()){
                        do {
                            item.add(new Item(cursorCur.getString(1)));
                        }while(cursorCur.moveToNext());
                    }
                }
                else{
                    cursorCur = sqLiteDBHelper.getSnList(Zone_N,"cb8");
                    item.add(new Item(cursorCur.getString(1)));
                }

                break;
        }

        return item;


    }
    public String getCoding(){
        String code=null;
        DateTime dateTime = new DateTime();
        if (dateTime.getHourOfDay()>=0 && dateTime.getHourOfDay()<6) code="Text_A";
        if (dateTime.getHourOfDay()>=6 && dateTime.getHourOfDay()<12) code="Text_B";
        if (dateTime.getHourOfDay()>=12 && dateTime.getHourOfDay()<18) code="Text_C";
        if (dateTime.getHourOfDay()>=18 && dateTime.getHourOfDay()<24) code="Text_D";

        return code;
    }
}
