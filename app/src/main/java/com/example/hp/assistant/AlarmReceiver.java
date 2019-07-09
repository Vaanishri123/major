package com.example.hp.assistant;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver{


    @Override
    public void onReceive(Context context, Intent intent) {
        try
        {
//            Toast.makeText(context, "Alarm", Toast.LENGTH_LONG).show();
            Log.e("RECEIVER","ALARM ALARM");

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
            mBuilder.setSmallIcon(R.drawable.cast_ic_notification_small_icon);
            mBuilder.setContentTitle("Assistant");

            if(intent.getStringExtra("message").equals("Wake-up Alarm"))
                mBuilder.setContentText("Wake-up Alarm");
            else
                mBuilder.setContentText(intent.getStringExtra("message"));

            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(1, mBuilder.build());

            try {
                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone r = RingtoneManager.getRingtone(context, notification);
                r.play();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        catch (Exception e)
        {
            Log.e("RECEIVER",e.getMessage());
        }
//        Toast.makeText(context, intent.getStringExtra("message"), Toast.LENGTH_LONG).show();
    }
}
