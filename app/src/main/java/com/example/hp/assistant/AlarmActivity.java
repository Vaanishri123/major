package com.example.hp.assistant;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import java.sql.Time;
import java.util.Calendar;

public class AlarmActivity {

    private AlarmManager am;
    Context cont;
    int option;
    Calendar cal;
    int hr=0,min=0;


    public AlarmActivity(Context con,String text)
    {
        try {
            this.option=option;
            int i=0,j=0,f=0;
            cont = con;
            Calendar tempcal=Calendar.getInstance();
            cal = Calendar.getInstance();
            cal.setTimeInMillis(System.currentTimeMillis());
            tempcal.setTimeInMillis(System.currentTimeMillis());

            f=0;
            for (i = 1; i <= 12; i++) {
                for (j = 59; j >= 1; j--) {
                    if (RK.match(" "+i + ":" + j+" ", text,13) || RK.match(i + ":" + j, text,13) || RK.match(" "+i + " " + j+" ", text,13)) {
                        hr = i;
                        min = j;
                        f=1;
                        Log.e("TIME1",hr+":"+min);
                        break;
                    }
                }
            }
            for(i = 1;i<=12 && f!=1;i++)
            if (RK.match(" "+i + " ", text,13) && f!=1) {
                hr = i;
                min = 0;
                Log.e("TIME2",hr+":"+min);
                break;
            }

            if (RK.match(" a.m.", text,13) || RK.match(" a m", text,13) || RK.match(" a.m", text,13)) {
                cal.set(Calendar.HOUR_OF_DAY, hr);
            } else if (RK.match(" p.m.", text,13) || RK.match(" p m", text,13) || RK.match(" p.m", text,13)) {
                cal.set(Calendar.HOUR_OF_DAY, hr + 12);
            }
            cal.set(Calendar.MINUTE, min);
            cal.set(Calendar.SECOND, 0);

            if(cal.compareTo(tempcal)<=0)
            {
                cal.add(Calendar.DATE,1);
            }

        }
        catch (Exception e)
        {
            Log.e("ALARM1",e.getMessage());
            Toast.makeText(cont, "AlarmAct Ecxp1", Toast.LENGTH_SHORT).show();
        }

    }

    public boolean setAlarm()
    {
        try {

            if(hr==0 && min==0)
                return false;

            Intent in = new Intent(cont, AlarmReceiver.class);
            in.putExtra("message","Wake-up Alarm");
            PendingIntent pi = PendingIntent.getBroadcast(cont,1, in, PendingIntent.FLAG_UPDATE_CURRENT);

            am = (AlarmManager) cont.getSystemService(Context.ALARM_SERVICE);
            am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);
            Toast.makeText(cont, "Setting alarm for "+cal.getTime().toString(), Toast.LENGTH_SHORT).show();

            return true;
        }
        catch (Exception e)
        {
            Toast.makeText(cont, "AlarmAct Ecxp2+", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public boolean setReminder(String message)
    {
        try {
            Log.e("REMINDER","hr="+hr+" min="+min);

            if(hr==0 && min==0)
                return false;

            Intent in = new Intent(cont, AlarmReceiver.class);
            in.putExtra("message",message);
            PendingIntent pi = PendingIntent.getBroadcast(cont,1, in, PendingIntent.FLAG_UPDATE_CURRENT);

            am = (AlarmManager) cont.getSystemService(Context.ALARM_SERVICE);
            am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);
            Toast.makeText(cont, "Setting reminder for "+cal.getTime().toString(), Toast.LENGTH_SHORT).show();
            return true;
        }
        catch (Exception e)
        {
            Toast.makeText(cont, "AlarmAct Ecxp2+", Toast.LENGTH_SHORT).show();
            Log.e("REMINDER",e.getMessage());
            return false;
        }
    }

}
