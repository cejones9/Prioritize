package com.seniorproject.prioritize;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import java.util.Calendar;

import static android.content.Context.NOTIFICATION_SERVICE;

public class onBoot extends BroadcastReceiver {
    private Context mcontext;

    @Override
    public void onReceive(Context context, Intent intent) {
        mcontext = context;
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        FeedReaderDBHelper mDBHelper = new FeedReaderDBHelper(context);
        SQLiteDatabase reminderDB = mDBHelper.getReadableDatabase();
        Cursor c = reminderDB.rawQuery("SELECT RID, CalculatedDate, CalculatedTime, Description, ALarmSchedulerID, CurrentDate, NumberofDates, Alarm, OnTime, RepeatEveryxDays FROM Reminder", null);
        while(c.moveToNext()){
            Calendar cal = Calendar.getInstance();
            String month = "" + c.getString(0).charAt(0) + c.getString(0).charAt(1);
            String day =  "" + c.getString(0).charAt(2) + c.getString(0).charAt(3);
            String year = "" + c.getString(0).charAt(4) + c.getString(0).charAt(5) + c.getString(0).charAt(6) + c.getString(0).charAt(7);
            String hour = "" + c.getString(1).charAt(0) + c.getString(1).charAt(1);
            String minute = "" + c.getString(1).charAt(2) + c.getString(1).charAt(3);
            // Toast.makeText(SetAlarm.this, calculations[0], Toast.LENGTH_SHORT).show()
            cal.set(Integer.parseInt(year), Integer.parseInt(month)-1, Integer.parseInt(day), Integer.parseInt(hour), Integer.parseInt(minute));
            long alertTime = cal.getTimeInMillis();
        //    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            Intent alarmIntent = new Intent(context, alarmReceiver.class);
            alarmIntent.putExtra("RID", c.getString(0));
            alarmIntent.putExtra("description", c.getString(3));
            alarmIntent.putExtra("alarmID", c.getInt(4));
            alarmIntent.putExtra("alarm", c.getInt(7));
            alarmIntent.putExtra("numberOfDates", c.getInt(6));
            alarmIntent.putExtra("currentDate", c.getInt(5));
            alarmIntent.putExtra("repeatNumber", c.getInt(9));
            alarmIntent.putExtra("onTime", c.getInt(8));
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, c.getInt(4), alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            AlarmManager alarmMan = (AlarmManager) mcontext.getSystemService(Context.ALARM_SERVICE);
            alarmMan.set(AlarmManager.RTC_WAKEUP, alertTime, pendingIntent);

        }

        //throw new UnsupportedOperationException("Not yet implemented");
    }
}
