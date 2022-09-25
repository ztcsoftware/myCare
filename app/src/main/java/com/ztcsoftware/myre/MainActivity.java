package com.ztcsoftware.myre;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ztcsoftware.myre.adapters.AdapterSn;
import com.ztcsoftware.myre.model.Item;
import com.ztcsoftware.myre.utils.BottomSheetFragment;
import com.ztcsoftware.myre.utils.BroadcastReceiver;
import com.ztcsoftware.myre.utils.SimpleWidgetProvider;

import org.joda.time.DateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String Zone_LN = "Text_A"; // Early Morning
    private static final String Zone_M = "Text_B";  // Morning
    private static final String Zone_A = "Text_C";  // Afternoon
    private static final String Zone_N = "Text_D";  // Night

    private final static String CHANNEL_ID = "notification #1";

    private final static int NOTIFICATION_A = 0;
    private final static int NOTIFICATION_B = 6;
    private final static int NOTIFICATION_C = 12;
    private final static int NOTIFICATION_D = 18;

    RecyclerView.LayoutManager layoutManager;
    public SQLiteDBHelper sqLiteDBHelper;
    private AdapterSn adapterSn;
    private ListView listViewSn;
    private final List<Item> item = new ArrayList<>();
    private CustomAdapter adapterCur;
    PendingIntent pendingIntent;
    RecyclerView recyclerView;
    TextView notificationText;

    //Read the db when return to MainActivity
    @Override
    protected void onResume() {
        super.onResume();

        ImageView imageEmptySnList = findViewById(R.id.emptyListSn);
        if (getSnList() != null) {
            adapterSn = new AdapterSn(getApplicationContext(), getSnList(), 0);
            if (adapterSn.isEmpty()) imageEmptySnList.setVisibility(View.VISIBLE);
            listViewSn.setAdapter(adapterSn);
        }
        updateWidget();
    }

    @Override
    protected void onStop() {
        super.onStop();
        alarmSet(NOTIFICATION_A,1);
        alarmSet(NOTIFICATION_B,2);
        alarmSet(NOTIFICATION_C,3);
        alarmSet(NOTIFICATION_D,4);
        updateWidget();
    }

    @Override
    protected void onPause() {
        super.onPause();
        alarmSet(NOTIFICATION_A,1);
        alarmSet(NOTIFICATION_B,2);
        alarmSet(NOTIFICATION_C,3);
        alarmSet(NOTIFICATION_D,4);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sqLiteDBHelper = SQLiteDBHelper.getmInstance(getApplicationContext());

        createNotificationChannel();

        alarmSet(NOTIFICATION_A,1);
        alarmSet(NOTIFICATION_B,2);
        alarmSet(NOTIFICATION_C,3);
        alarmSet(NOTIFICATION_D,4);

        recyclerView = findViewById(R.id.recyclerView);

        notificationText = findViewById(R.id.notificationText);
        ImageButton buttonCalendar = findViewById(R.id.button1);
        ImageButton buttonList = findViewById(R.id.button2);
        listViewSn = findViewById(R.id.listViewSn);
        ImageView imageEmptySnList = findViewById(R.id.emptyListSn);

        FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton);

        if (sqLiteDBHelper.initFlags(getCoding()))
                Log.i("flags","Values updated");

        layoutManager = new LinearLayoutManager(getApplication(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        //Load curList
        adapterCur = new CustomAdapter(getApplication(), getCurList());
        recyclerView.setAdapter(adapterCur);
        if (adapterCur.getItemCount() == 0) notificationText.setVisibility(View.VISIBLE);

        //Load snList
        if (getSnList() != null) {
            adapterSn = new AdapterSn(getApplicationContext(), getSnList(), 0);
            if (adapterSn.isEmpty()) imageEmptySnList.setVisibility(View.VISIBLE);
            listViewSn.setAdapter(adapterSn);
        }

        buttonCalendar.setOnClickListener(v -> {
            Intent intentC = new Intent(MainActivity.this, RangePickerActivity.class);
            startActivity(intentC);
        });

        buttonList.setOnClickListener(v -> {
            Intent intentL = new Intent(MainActivity.this, ShowTheList.class);
            startActivity(intentL);
        });

        floatingActionButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Insert.class);
            startActivity(intent);
        });

        //when new entries inserted from app widget load the new data
        updateWidget();

    }

    public String getCoding() {
        String code = null;
        DateTime dateTime = new DateTime();
        if (dateTime.getHourOfDay() >= 0 && dateTime.getHourOfDay() < 6) code = "Text_A";
        if (dateTime.getHourOfDay() >= 6 && dateTime.getHourOfDay() < 12) code = "Text_B";
        if (dateTime.getHourOfDay() >= 12 && dateTime.getHourOfDay() < 18) code = "Text_C";
        if (dateTime.getHourOfDay() >= 18 && dateTime.getHourOfDay() < 24) code = "Text_D";

        return code;
    }

    public List<Item> getCurList() {
        Cursor cursorCur;
        DateTime dateTime = new DateTime();

        switch (getCoding()) {
            case Zone_LN:
                if (dateTime.getDayOfWeek() == 1 || dateTime.getDayOfWeek() == 2 || dateTime.getDayOfWeek() == 3 || dateTime.getDayOfWeek() == 4
                        || dateTime.getDayOfWeek() == 5 || dateTime.getDayOfWeek() == 6 || dateTime.getDayOfWeek() == 7) {
                    cursorCur = sqLiteDBHelper.getSnList(Zone_LN, String.valueOf(dateTime.getDayOfWeek()));
                    if (cursorCur.moveToFirst()) {
                        do {
                            //item.add(new Item(cursorCur.getString(1)));
                            item.add(new Item(cursorCur.getString(1), cursorCur.getString(3), cursorCur.getString(4)));
                        } while (cursorCur.moveToNext());
                    }
                } else {
                    cursorCur = sqLiteDBHelper.getSnList(Zone_LN, "cb8");
                    item.add(new Item(cursorCur.getString(1)));
                }

                break;
            case Zone_M:
                if (dateTime.getDayOfWeek() == 1 || dateTime.getDayOfWeek() == 2 || dateTime.getDayOfWeek() == 3 || dateTime.getDayOfWeek() == 4
                        || dateTime.getDayOfWeek() == 5 || dateTime.getDayOfWeek() == 6 || dateTime.getDayOfWeek() == 7) {
                    cursorCur = sqLiteDBHelper.getSnList(Zone_M, String.valueOf(dateTime.getDayOfWeek()));

                    if (cursorCur.moveToFirst()) {
                        do {
                            // item.add(new Item(cursorCur.getString(1)));
                            item.add(new Item(cursorCur.getString(1), cursorCur.getString(3), cursorCur.getString(4)));
                        } while (cursorCur.moveToNext());
                    }
                } else {
                    cursorCur = sqLiteDBHelper.getSnList(Zone_M, "cb8");

                    item.add(new Item(cursorCur.getString(1)));
                }

                break;
            case Zone_A:
                if (dateTime.getDayOfWeek() == 1 || dateTime.getDayOfWeek() == 2 || dateTime.getDayOfWeek() == 3 || dateTime.getDayOfWeek() == 4
                        || dateTime.getDayOfWeek() == 5 || dateTime.getDayOfWeek() == 6 || dateTime.getDayOfWeek() == 7) {

                    cursorCur = sqLiteDBHelper.getSnList(Zone_A, String.valueOf(dateTime.getDayOfWeek()));
                    Log.i("cursor return value ", String.valueOf(cursorCur));
                    if (cursorCur.moveToFirst()) {
                        do {
                            Log.i("cursor --> ", cursorCur.getString(1));
                            //item.add(new Item(cursorCur.getString(1)));
                            item.add(new Item(cursorCur.getString(1), cursorCur.getString(3), cursorCur.getString(4)));
                            Log.i("list content --> ", String.valueOf(item.size()));
                        } while (cursorCur.moveToNext());
                    }

                } else {
                    cursorCur = sqLiteDBHelper.getSnList(Zone_A, "cb8");
                    item.add(new Item(cursorCur.getString(1)));
                }

                break;
            case Zone_N:
                if (dateTime.getDayOfWeek() == 1 || dateTime.getDayOfWeek() == 2 || dateTime.getDayOfWeek() == 3 || dateTime.getDayOfWeek() == 4
                        || dateTime.getDayOfWeek() == 5 || dateTime.getDayOfWeek() == 6 || dateTime.getDayOfWeek() == 7) {
                    cursorCur = sqLiteDBHelper.getSnList(Zone_N, String.valueOf(dateTime.getDayOfWeek()));
                    if (cursorCur.moveToFirst()) {
                        do {
                            // item.add(new Item(cursorCur.getString(1)));
                            item.add(new Item(cursorCur.getString(1), cursorCur.getString(3), cursorCur.getString(4)));
                        } while (cursorCur.moveToNext());
                    }
                } else {
                    cursorCur = sqLiteDBHelper.getSnList(Zone_N, "cb8");
                    item.add(new Item(cursorCur.getString(1)));
                }

                break;
        }

        return item;


    }

    private Cursor getSnList() {
        Cursor cursorSn = null;
        DateTime dateTime = new DateTime();
        switch (getCoding()) {
            case Zone_LN:
                if (dateTime.getDayOfWeek() == 1 || dateTime.getDayOfWeek() == 2 || dateTime.getDayOfWeek() == 3 || dateTime.getDayOfWeek() == 4 || dateTime.getDayOfWeek() == 5 || dateTime.getDayOfWeek() == 6 || dateTime.getDayOfWeek() == 7)
                    cursorSn = sqLiteDBHelper.getSnList(Zone_A, String.valueOf(dateTime.getDayOfWeek()));
                else
                    cursorSn = sqLiteDBHelper.getSnList(Zone_A, "cb8");
                break;
            case Zone_M:
                if (dateTime.getDayOfWeek() == 1 || dateTime.getDayOfWeek() == 2 || dateTime.getDayOfWeek() == 3 || dateTime.getDayOfWeek() == 4 || dateTime.getDayOfWeek() == 5 || dateTime.getDayOfWeek() == 6 || dateTime.getDayOfWeek() == 7)
                    cursorSn = sqLiteDBHelper.getSnList(Zone_N, String.valueOf(dateTime.getDayOfWeek()));
                else
                    cursorSn = sqLiteDBHelper.getSnList(Zone_N, "cb8");
                break;
            case Zone_A:
                if (dateTime.getDayOfWeek() == 1 || dateTime.getDayOfWeek() == 2 || dateTime.getDayOfWeek() == 3 || dateTime.getDayOfWeek() == 4 || dateTime.getDayOfWeek() == 5 || dateTime.getDayOfWeek() == 6)

                    cursorSn = sqLiteDBHelper.getSnList(Zone_LN, String.valueOf(dateTime.getDayOfWeek() + 1));
                else if (dateTime.getDayOfWeek() == 7)
                    cursorSn = sqLiteDBHelper.getSnList(Zone_LN, "1");
                else
                    cursorSn = sqLiteDBHelper.getSnList(Zone_LN, "cb8");
                break;
            case Zone_N:
                if (dateTime.getDayOfWeek() == 1 || dateTime.getDayOfWeek() == 2 || dateTime.getDayOfWeek() == 3 || dateTime.getDayOfWeek() == 4 || dateTime.getDayOfWeek() == 5 || dateTime.getDayOfWeek() == 6)
                    cursorSn = sqLiteDBHelper.getSnList(Zone_M, String.valueOf(dateTime.getDayOfWeek() + 1));
                else if (dateTime.getDayOfWeek() == 7)
                    cursorSn = sqLiteDBHelper.getSnList(Zone_M, "1");
                else
                    cursorSn = sqLiteDBHelper.getSnList(Zone_M, "cb8");
                break;
        }
        return cursorSn;
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    public void alarmSet(int hourOfDay, int requestCode) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        // calendar.set(Calendar.MINUTE,40);

        Intent intent = new Intent(this, BroadcastReceiver.class);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), requestCode, intent, PendingIntent.FLAG_IMMUTABLE);
        } else {
            pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), requestCode, intent, 0);
        }
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        //Wakes up the device to fire the pending intent at the specified time RTC_WAKEUP
        //  alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 10000, pendingIntent);

        // Invoke an alarm at a nearly precise time in the future, even if battery-saving measures are in effect.
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        updateWidget();
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = getString(R.string.channel_name);
                String description = getString(R.string.channel_description);
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
                channel.setDescription(description);
                // Register the channel with the system; you can't change the importance
                // or other notification behaviors after this
                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
        }
    }

    private void updateWidget() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(getApplicationContext(), SimpleWidgetProvider.class));
        if (appWidgetIds.length > 0) {
            // Tell the widgets that the list items should be invalidated and refreshed!
            // Will call onDatasetChanged in ListWidgetService, doing a new requery
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.listViewWidget);
        }
    }

    public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

        private Context context;
        private List<Item> dataSet;

        public CustomAdapter(Context context, List<Item> data) {
            this.context = context;
            this.dataSet = data;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView textTop;
            TextView textMiddle;
            TextView textBottom;
            ImageView imagePro;
            RelativeLayout relativeLayout;
            Button buttonCnl;
            Button buttonSbmt;

            public MyViewHolder(View itemView) {
                super(itemView);
                this.textTop = itemView.findViewById(R.id.textTop);
                this.textMiddle = itemView.findViewById(R.id.textMiddle);
                this.textBottom = itemView.findViewById(R.id.textBottom);
                this.imagePro = itemView.findViewById(R.id.imagePro);
                this.relativeLayout = itemView.findViewById(R.id.view);
                this.buttonCnl = itemView.findViewById(R.id.cancelBtn);
                this.buttonSbmt = itemView.findViewById(R.id.confirmBtn);

            }
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_show_cur_list, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

            TextView textMiddle = holder.textMiddle;
            Button buttonSbmt = holder.buttonSbmt;

            textMiddle.setText(dataSet.get(position).getpN());

            buttonSbmt.setOnClickListener(view -> {

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);

                View dialogView = getLayoutInflater().inflate(R.layout.confirm_dialog, null);

                Button confirmBtn = dialogView.findViewById(R.id.confirmBtn);
                Button cancel = dialogView.findViewById(R.id.cancel);

                mBuilder.setView(dialogView);

                final AlertDialog dialog = mBuilder.create();
                dialog.setCanceledOnTouchOutside(false);

                confirmBtn.setOnClickListener(v -> {
                        DateTime dateTime=new DateTime();

                        String timeStamp =   getResources().getString(R.string.hour) +" ["+dateTime.getHourOfDay()+":"+dateTime.getMinuteOfHour()+"], "+dateTime.dayOfWeek().getAsText() +" "+dateTime.dayOfMonth().getAsText()+"-"+dateTime.monthOfYear().getAsText()+"-"+dateTime.getYear();
                        String timeStampMillis = String.valueOf(dateTime.toDateTime().getMillis());

                        if (sqLiteDBHelper.updateList(dataSet.get(position).getpN(), dataSet.get(position).gettZ(), dataSet.get(position).getdY(),timeStamp,timeStampMillis) != 0) {
                            Toast.makeText(MainActivity.this, getResources().getText(R.string.action_confirm), Toast.LENGTH_LONG).show();
                            //Load curList
                            dataSet.clear();
                            adapterCur = new CustomAdapter(getApplication(), getCurList());
                            recyclerView.setAdapter(adapterCur);
                            if (adapterCur.getItemCount() == 0)
                                notificationText.setVisibility(View.VISIBLE);
                        }
                        //when user confirm submission update the listview on widget view
                        updateWidget();

                        dialog.cancel();

                });
                cancel.setOnClickListener(v -> dialog.cancel());
                dialog.show();
            });


        }

        @Override
        public int getItemCount() {
            return dataSet.size();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.information:
                BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
                bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
                break;
            case R.id.settings:
                Intent intent = new Intent(this,Settings.class);
                startActivity(intent);
                break;
            case R.id.about:
                Intent intentAbout = new Intent(this,About.class);
                startActivity(intentAbout);
                break;
            default:
                break;
        }

        return true;
    }

}