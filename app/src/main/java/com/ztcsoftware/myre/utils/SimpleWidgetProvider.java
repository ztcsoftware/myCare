package com.ztcsoftware.myre.utils;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RemoteViews;

import com.ztcsoftware.myre.MainActivity;
import com.ztcsoftware.myre.R;
import com.ztcsoftware.myre.SQLiteDBHelper;
import com.ztcsoftware.myre.adapters.AdapterSn;
import com.ztcsoftware.myre.adapters.AdapterWidget;
import com.ztcsoftware.myre.model.Item;

import org.joda.time.DateTime;

public class SimpleWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        // Perform this loop procedure for each widget that belongs to this
        // provider.
        for (int i=0; i < appWidgetIds.length; i++) {

            int appWidgetId = appWidgetIds[i];
            // Create an Intent to launch MainActivity
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(
                    /* context = */ context,
                    /* requestCode = */ 0,
                    /* intent = */ intent,
                    /* flags = */ PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );

            // Get the layout for the widget and attach an on-click listener
            // to the button.
            @SuppressLint("RemoteViewLayout")
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            views.setOnClickPendingIntent(R.id.widget_layout, pendingIntent);
            //load the list
//            Intent intentList = new Intent(context, MyWidgetRemoteViewsService.class);
//            views.setRemoteAdapter(R.id.listViewWidget, intentList);

            // Tell the AppWidgetManager to perform an update on the current app widget.
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }

        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(
                    context.getPackageName(),
                    R.layout.widget_layout
            );
            Intent intent = new Intent(context, MyWidgetRemoteViewsService.class);
            views.setRemoteAdapter(R.id.listViewWidget, intent);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
}

}
