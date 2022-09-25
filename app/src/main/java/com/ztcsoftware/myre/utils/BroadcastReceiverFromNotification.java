package com.ztcsoftware.myre.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class BroadcastReceiverFromNotification extends android.content.BroadcastReceiver  {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"This is a message from notification Button",Toast.LENGTH_LONG).show();
        Log.i("BrRecFromNot ", "test text");
    }

}
