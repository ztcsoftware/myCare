package com.ztcsoftware.myre.utils;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import com.ztcsoftware.myre.MainActivity;
import com.ztcsoftware.myre.R;
import com.ztcsoftware.myre.SQLiteDBHelper;
import org.joda.time.DateTime;

public class MyWidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private static final String Zone_LN = "Text_A";
    private static final String Zone_M = "Text_B";
    private static final String Zone_A = "Text_C";
    private static final String Zone_N = "Text_D";

    private Context mContext;
    public  Cursor mCursor;

    MainActivity mainActivity = new MainActivity();
    SQLiteDBHelper sqLiteDBHelper = new SQLiteDBHelper(mContext);

    public MyWidgetRemoteViewsFactory(Context applicationContext, Intent intent) {
        mContext = applicationContext;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        if (mCursor != null) {
            mCursor.close();
        }

        DateTime dateTime = new DateTime();
        switch(mainActivity.getCoding()){
            case Zone_LN:
                if (dateTime.getDayOfWeek() == 1 || dateTime.getDayOfWeek() == 2 || dateTime.getDayOfWeek() == 3 || dateTime.getDayOfWeek() == 4
                        || dateTime.getDayOfWeek() == 5 || dateTime.getDayOfWeek() == 6 || dateTime.getDayOfWeek() == 7) {
                    mCursor = sqLiteDBHelper.getWidgetList(Zone_LN, String.valueOf(dateTime.getDayOfWeek()), mContext.getApplicationContext());

                } else
                    mCursor = sqLiteDBHelper.getWidgetList(Zone_LN, "cb8", mContext.getApplicationContext());
                break;

            case Zone_M:
                if (dateTime.getDayOfWeek() == 1 || dateTime.getDayOfWeek() == 2 || dateTime.getDayOfWeek() == 3 || dateTime.getDayOfWeek() == 4
                        || dateTime.getDayOfWeek() == 5 || dateTime.getDayOfWeek() == 6 || dateTime.getDayOfWeek() == 7) {
                    mCursor = sqLiteDBHelper.getWidgetList(Zone_M, String.valueOf(dateTime.getDayOfWeek()), mContext.getApplicationContext());

                } else
                    mCursor = sqLiteDBHelper.getWidgetList(Zone_M, "cb8", mContext.getApplicationContext());
                break;
            case Zone_A:
                if (dateTime.getDayOfWeek() == 1 || dateTime.getDayOfWeek() == 2 || dateTime.getDayOfWeek() == 3 || dateTime.getDayOfWeek() == 4
                        || dateTime.getDayOfWeek() == 5 || dateTime.getDayOfWeek() == 6 || dateTime.getDayOfWeek() == 7) {

                    try{
                        mCursor=sqLiteDBHelper.getWidgetList(Zone_A, String.valueOf(dateTime.getDayOfWeek()), mContext.getApplicationContext());
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    Log.i("cursor ", String.valueOf(mCursor));
                } else
                    mCursor = sqLiteDBHelper.getWidgetList(Zone_A, "cb8", mContext.getApplicationContext());

                break;
            case Zone_N:
                if (dateTime.getDayOfWeek() == 1 || dateTime.getDayOfWeek() == 2 || dateTime.getDayOfWeek() == 3 || dateTime.getDayOfWeek() == 4
                        || dateTime.getDayOfWeek() == 5 || dateTime.getDayOfWeek() == 6 || dateTime.getDayOfWeek() == 7) {
                    mCursor = sqLiteDBHelper.getWidgetList(Zone_N, String.valueOf(dateTime.getDayOfWeek()), mContext.getApplicationContext());

                } else
                    mCursor = sqLiteDBHelper.getWidgetList(Zone_N, "cb8", mContext.getApplicationContext());
                break;

        }

    }

    @Override
    public void onDestroy() {
        if (mCursor != null) {
            mCursor.close();
        }
    }

    @Override
    public int getCount() {
        if (mCursor == null) return 0;
        return mCursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (position == AdapterView.INVALID_POSITION ||
                mCursor == null || !mCursor.moveToPosition(position)) {
            return null;
        }
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.item_show_widget_list);
        remoteViews.setTextViewText(R.id.pN_Widget, mCursor.getString(1)); //getString(1) is the second column of returned table
        remoteViews.setTextViewText(R.id.uFo_Widget, mCursor.getString(2));

        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return mCursor.moveToPosition(position) ? mCursor.getLong(0) : position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
