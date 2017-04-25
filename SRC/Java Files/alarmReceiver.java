package com.seniorproject.prioritize;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;

import com.google.android.gms.drive.query.Query;

public class alarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        FeedReaderDBHelper mDBHelper = new FeedReaderDBHelper(context);
        SQLiteDatabase reminderDB = mDBHelper.getReadableDatabase();
        String reminderDescription = intent.getStringExtra("description");
        int alarmID = intent.getIntExtra("alarmID", 0);
        String RID = intent.getStringExtra("RID");
        int alarm = intent.getIntExtra("alarm", 0);
        int numberOfDates = intent.getIntExtra("numberOfDates", 1);
        int currentDate = intent.getIntExtra("currentDate", 1);
        int repeat = intent.getIntExtra("repeatNumber", 0);
        int onTime = intent.getIntExtra("onTime", 0);
        int priority = intent.getIntExtra("priority", 1);
        Intent intent1 = new Intent(context, notificationActions.class);
        Intent intent2 = new Intent(context, notificationActions.class);
        Intent intent3 = new Intent(context, notificationActions.class);
        intent1.putExtra("rid", RID);
        intent1.putExtra("description", reminderDescription);
        intent1.putExtra("alarmID", alarmID);
        intent1.putExtra("numberOfDates", numberOfDates);
        intent1.putExtra("currentDate", currentDate);
        intent1.putExtra("onTime", onTime);
        intent1.putExtra("repeatNumber", repeat);
        intent1.putExtra("alarm", alarm);
        intent1.putExtra("priority", priority);

        intent2.putExtra("rid", RID);
        intent2.putExtra("description", reminderDescription);
        intent2.putExtra("alarmID", alarmID);
        intent2.putExtra("numberOfDates", numberOfDates);
        intent2.putExtra("currentDate", currentDate);
        intent2.putExtra("onTime", onTime);
        intent2.putExtra("repeatNumber", repeat);
        intent2.putExtra("alarm", alarm);
        intent2.putExtra("priority", priority);

        intent3.putExtra("rid", RID);
        intent3.putExtra("description", reminderDescription);
        intent3.putExtra("alarmID", alarmID);
        intent3.putExtra("numberOfDates", numberOfDates);
        intent3.putExtra("currentDate", currentDate);
        intent3.putExtra("onTime", onTime);
        intent3.putExtra("repeatNumber", repeat);
        intent3.putExtra("alarm", alarm);
        intent3.putExtra("priority", priority);


        intent1.setAction(notificationActions.ACTION1);
        intent2.setAction(notificationActions.ACTION2);
        intent3.setAction(notificationActions.ACTION3);
        PendingIntent pendingIntent1 = PendingIntent.getService(context, alarmID+1, intent1, PendingIntent.FLAG_CANCEL_CURRENT);
        PendingIntent pendingIntent2 = PendingIntent.getService(context, alarmID+2, intent2, PendingIntent.FLAG_CANCEL_CURRENT);
        PendingIntent pendingIntent3 = PendingIntent.getService(context, alarmID+3, intent3, PendingIntent.FLAG_CANCEL_CURRENT);
        Uri alarmSound = null;

        if (alarm==0) {
            alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        else {
            alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        }


        Cursor cursor = reminderDB.rawQuery("Select DueDate" + Integer.toString(currentDate) + ", DueTime" + Integer.toString(currentDate) +
                                        " from Reminder WHERE RID = '" + RID + "'", null);
        cursor.moveToFirst();
        String daymonthyear = "" + cursor.getString(0).charAt(4) + cursor.getString(0).charAt(5) + "-" + cursor.getString(0).charAt(6) + cursor.getString(0).charAt(7)
                + "-" + cursor.getString(0).charAt(0) + cursor.getString(0).charAt(1) + cursor.getString(0).charAt(2) + cursor.getString(0).charAt(3);


        Notification noti = new Notification.Builder(context)
               // .setContentTitle("Reminder: ")
                //.setContentText(reminderDescription)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setAutoCancel(false)
                .setOngoing(true)
                .setSound(alarmSound)
                .setStyle(new Notification.BigTextStyle()
                .bigText("Deadline: "+ daymonthyear)
                .setBigContentTitle("Reminder: " + reminderDescription))
                .addAction(0, "End", pendingIntent3)
                .addAction(0, "Snooze", pendingIntent1)
                .addAction(0, "Remind Me Later", pendingIntent2)

                .setPriority(Notification.PRIORITY_HIGH)
                .build();
        if (alarm!=0) {

            noti.flags = Notification.FLAG_INSISTENT;
       //     noti.flags = Notification.FLAG_NO_CLEAR;
        }

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(alarmID, noti);





        // an Intent broadcast.
      //  throw new UnsupportedOperationException("Not yet implemented");
    }
}
