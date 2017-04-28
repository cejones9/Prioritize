package com.seniorproject.prioritize;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
//import android.icu.util.Calendar;
import java.util.Calendar;
import android.os.ParcelFileDescriptor;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;

import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.SearchableField;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class notificationActions extends IntentService  {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private GoogleApiClient mGoogleApiClient;
    public static final String ACTION1 = "ACTION1";
    public static final String ACTION2 = "ACTION2";
    public static final String ACTION3 = "ACTION3";
    public static String RID ="";
    public static int alarmID = 0;
    public static String reminderDescription= "";
    int currentDate;
    int repeatNumber;
    int numberOfDates;
    int onTime;
    int alarm;
    int priority;


    public notificationActions() {
        super("notificationActions");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
 

    @Override
    protected void onHandleIntent(Intent intent) {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Drive.API)
                .addScope(Drive.SCOPE_APPFOLDER)
                .build();
        mGoogleApiClient.connect();
        ConnectionResult connectionResult = mGoogleApiClient.blockingConnect(10, TimeUnit.SECONDS);
        //just to make sure we connect
        RID = intent.getStringExtra("rid");
        alarmID = intent.getIntExtra("alarmID", 0);
        numberOfDates = intent.getIntExtra("numberOfDates", 1);
        currentDate = intent.getIntExtra("currentDate", 1);
        repeatNumber = intent.getIntExtra("repeatNumber", 0);
        reminderDescription = intent.getStringExtra("description");
        alarm = intent.getIntExtra("alarm", 0);
        onTime = intent.getIntExtra("onTime", 0);
        priority = intent.getIntExtra("priority", 1);


        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION1.equals(action)) {
               //snooze
                snooze(RID, alarmID);
                
                
            } else if (ACTION2.equals(action)) {
              //remind me later


                remindMeLater(RID, alarmID);
              
            } else if (ACTION3.equals(action)){
               endReminder(RID, alarmID, currentDate, repeatNumber, numberOfDates);
            }
            
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleRepeat(){
        FeedReaderDBHelper mDBHelper = new FeedReaderDBHelper(getApplicationContext());
        SQLiteDatabase reminderDB = mDBHelper.getReadableDatabase();
        Cursor c = reminderDB.rawQuery("SELECT * FROM Reminder WHERE RID = '" + RID + "'", null);
        c.moveToFirst();
        Calendar cal = Calendar.getInstance();
        Calendar current = Calendar.getInstance();
        String month = "" + c.getString(c.getColumnIndex("DueDate1")).charAt(4) + c.getString(c.getColumnIndex("DueDate1")).charAt(5);
        String day =  "" + c.getString(c.getColumnIndex("DueDate1")).charAt(6) + c.getString(c.getColumnIndex("DueDate1")).charAt(7);
        String year = "" + c.getString(c.getColumnIndex("DueDate1")).charAt(0) + c.getString(c.getColumnIndex("DueDate1")).charAt(1) +
                c.getString(c.getColumnIndex("DueDate1")).charAt(2) + c.getString(c.getColumnIndex("DueDate1")).charAt(3);
        String hour = "" + c.getString(c.getColumnIndex("DueTime1")).charAt(0) + c.getString(c.getColumnIndex("DueTime1")).charAt(1);
        String minute = "" + c.getString(c.getColumnIndex("DueTime1")).charAt(2) + c.getString(c.getColumnIndex("DueTime1")).charAt(3);
        Intent alarmIntent = new Intent(getApplicationContext(), alarmReceiver.class);
        alarmIntent.putExtra("RID", RID);
        alarmIntent.putExtra("description", reminderDescription);
        alarmIntent.putExtra("alarmID", alarmID);
        alarmIntent.putExtra("alarm", alarm);
        alarmIntent.putExtra("onTime", onTime);
        alarmIntent.putExtra("currentDate", currentDate);
        alarmIntent.putExtra("repeatNumber", repeatNumber);
        alarmIntent.putExtra("numberOfDates", numberOfDates);
        alarmIntent.putExtra("priority", priority);
        if (repeatNumber == 1){
            if (onTime == 1){

                current.add(Calendar.DATE, 1);
                current.set(current.get(Calendar.YEAR), current.get(Calendar.MONTH), current.get(Calendar.DAY_OF_MONTH), Integer.parseInt(hour), Integer.parseInt(minute));
                long time = current.getTimeInMillis();
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), alarmID, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                AlarmManager alarmMan = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmMan.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
                updateCalc(current.get(Calendar.YEAR), current.get(Calendar.MONTH) + 1, current.get(Calendar.DAY_OF_MONTH), current.get(Calendar.HOUR), current.get(Calendar.MINUTE));
                updateDueDate(current.get(Calendar.YEAR), current.get(Calendar.MONTH) + 1, current.get(Calendar.DAY_OF_MONTH), current.get(Calendar.HOUR), current.get(Calendar.MINUTE));

            }
            if (onTime == 0){
                current.add(Calendar.DATE, 1);
                current.set(current.get(Calendar.YEAR), current.get(Calendar.MONTH), current.get(Calendar.DAY_OF_MONTH), Integer.parseInt(hour), Integer.parseInt(minute));
                if (priority ==1){
                    current.add(Calendar.HOUR, -1);
                }
                if (priority ==2){
                    current.add(Calendar.HOUR, -2);
                }
                if (priority ==3){
                    current.add(Calendar.HOUR, -3);
                }
                if (priority ==4){
                    current.add(Calendar.HOUR, -4);
                }
                if (priority ==5){
                    current.add(Calendar.HOUR, -5);
                }
                long time = current.getTimeInMillis();
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), alarmID, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                AlarmManager alarmMan = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmMan.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
                updateCalc(current.get(Calendar.YEAR), current.get(Calendar.MONTH) + 1, current.get(Calendar.DAY_OF_MONTH), current.get(Calendar.HOUR), current.get(Calendar.MINUTE));
                updateDueDate(current.get(Calendar.YEAR), current.get(Calendar.MONTH) + 1, current.get(Calendar.DAY_OF_MONTH), current.get(Calendar.HOUR), current.get(Calendar.MINUTE));
                /*
                current.add(Calendar.DATE, 1);
                current.set(current.get(Calendar.YEAR), current.get(Calendar.MONTH), current.get(Calendar.DAY_OF_MONTH), Integer.parseInt(hour), Integer.parseInt(minute));
                String [] calc = priorityAlgorithm.getPriorityDateandTime(current.get(Calendar.MONTH) +1, current.get(Calendar.DAY_OF_MONTH), current.get(Calendar.YEAR), current.get(Calendar.HOUR), current.get(Calendar.MINUTE), priority);
                cal.set(Integer.parseInt(calc[2]), Integer.parseInt(calc[0]) - 1, Integer.parseInt(calc[1]), Integer.parseInt(calc[3]), Integer.parseInt(calc[4]));
                long time = cal.getTimeInMillis();
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), alarmID, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                AlarmManager alarmMan = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmMan.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
                updateCalc(Integer.parseInt(calc[2]), Integer.parseInt(calc[0]), Integer.parseInt(calc[1]), Integer.parseInt(calc[3]), Integer.parseInt(calc[4]));
                updateDueDate(current.get(Calendar.YEAR), current.get(Calendar.MONTH) + 1, current.get(Calendar.DAY_OF_MONTH), current.get(Calendar.HOUR), current.get(Calendar.MINUTE));*/
            }
            sqlToJSON(RID);
        }
        else if (repeatNumber == 2){
            cal.set(Integer.parseInt(year), Integer.parseInt(month)-1, Integer.parseInt(day), Integer.parseInt(hour), Integer.parseInt(minute));
            while (cal.compareTo(current) < 0)
                cal.add(Calendar.DATE, 7);
            if (onTime == 1){
                long time = cal.getTimeInMillis();
                updateCalc(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE));
                updateDueDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE));
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), alarmID, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                AlarmManager alarmMan = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmMan.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
            }
            if (onTime == 0){
                String [] calc = priorityAlgorithm.getPriorityDateandTime(cal.get(Calendar.MONTH) +1, cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.YEAR), cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE), priority);
                current.set(Integer.parseInt(calc[2]), Integer.parseInt(calc[0]) - 1, Integer.parseInt(calc[1]), Integer.parseInt(calc[3]), Integer.parseInt(calc[4]));
                long time = current.getTimeInMillis();
                updateCalc(current.get(Calendar.YEAR), current.get(Calendar.MONTH) + 1, current.get(Calendar.DAY_OF_MONTH), current.get(Calendar.HOUR), current.get(Calendar.MINUTE));
                updateDueDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE));
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), alarmID, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                AlarmManager alarmMan = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmMan.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
            }
            sqlToJSON(RID);
        }
        else if (repeatNumber == 3){//month
            cal.set(Integer.parseInt(year), Integer.parseInt(month)-1, Integer.parseInt(day), Integer.parseInt(hour), Integer.parseInt(minute));
            while (cal.compareTo(current) < 0){
                cal.add(Calendar.MONTH, 1);
            }
            if (onTime == 1){
                long time = cal.getTimeInMillis();
                updateCalc(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE));
                updateDueDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE));
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), alarmID, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                AlarmManager alarmMan = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmMan.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
            }
            if (onTime == 0){
                String [] calc = priorityAlgorithm.getPriorityDateandTime(cal.get(Calendar.MONTH) +1, cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.YEAR), cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE), priority);
                current.set(Integer.parseInt(calc[2]), Integer.parseInt(calc[0]) - 1, Integer.parseInt(calc[1]), Integer.parseInt(calc[3]), Integer.parseInt(calc[4]));
                long time = current.getTimeInMillis();
                updateCalc(current.get(Calendar.YEAR), current.get(Calendar.MONTH) + 1, current.get(Calendar.DAY_OF_MONTH), current.get(Calendar.HOUR), current.get(Calendar.MINUTE));
                updateDueDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE));
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), alarmID, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                AlarmManager alarmMan = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmMan.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
            }
            sqlToJSON(RID);
        }
        else if (repeatNumber == 4){ // year, we're just gonna increment year, no point in preventing several notifications.
            cal.set(Integer.parseInt(year)+1, Integer.parseInt(month)-1, Integer.parseInt(day), Integer.parseInt(hour), Integer.parseInt(minute));
            if (onTime == 1){
                long time = cal.getTimeInMillis();
                updateCalc(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE));
                updateDueDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE));
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), alarmID, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                AlarmManager alarmMan = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmMan.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
            }
            if (onTime == 0){
                String [] calc = priorityAlgorithm.getPriorityDateandTime(cal.get(Calendar.MONTH) +1, cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.YEAR), cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE), priority);
                current.set(Integer.parseInt(calc[2]), Integer.parseInt(calc[0]) - 1, Integer.parseInt(calc[1]), Integer.parseInt(calc[3]), Integer.parseInt(calc[4]));
                long time = current.getTimeInMillis();
                updateCalc(current.get(Calendar.YEAR), current.get(Calendar.MONTH) + 1, current.get(Calendar.DAY_OF_MONTH), current.get(Calendar.HOUR), current.get(Calendar.MINUTE));
                updateDueDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE));
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), alarmID, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                AlarmManager alarmMan = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmMan.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
            }
            sqlToJSON(RID);
        }
    }
    public void deleteReminderFromDrive(final String RID){
        final DriveFolder appFolder = Drive.DriveApi.getAppFolder(mGoogleApiClient);
        Query query2 = new Query.Builder()
                .addFilter(Filters.eq(SearchableField.TITLE, "REMINDERS"))
                .build();
        appFolder.queryChildren(mGoogleApiClient, query2).setResultCallback(new ResultCallback<DriveApi.MetadataBufferResult>() {
            @Override
            public void onResult(@NonNull DriveApi.MetadataBufferResult metadataBufferResult) {
                DriveFolder reminderFolder = Drive.DriveApi.getFolder(mGoogleApiClient, metadataBufferResult.getMetadataBuffer().iterator().next().getDriveId());
                Query query3 = new Query.Builder()
                        .addFilter(Filters.eq(SearchableField.TITLE, RID))
                        .build();
                reminderFolder.queryChildren(mGoogleApiClient, query3).setResultCallback(new ResultCallback<DriveApi.MetadataBufferResult>() {
                    @Override
                    public void onResult(@NonNull DriveApi.MetadataBufferResult metadataBufferResult) {
                        DriveFile file = Drive.DriveApi.getFile(mGoogleApiClient, metadataBufferResult.getMetadataBuffer().iterator().next().getDriveId());
                        file.delete(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                            @Override
                            public void onResult(@NonNull Status status) {
                            }
                        });
                    }
                });
                metadataBufferResult.release();
            }
        });
    }
    public void deleteReminderFromSQLite(String RID){
        FeedReaderDBHelper mDBHelper = new FeedReaderDBHelper(getApplicationContext());
        SQLiteDatabase reminderDB = mDBHelper.getReadableDatabase();
        Cursor c = reminderDB.rawQuery("Select CalculatedDate, CalculateTime, ALarmSchedulerID FROM Reminder WHERE RID = '" + RID + "'", null);
        c.moveToFirst();
        String month = "" + c.getString(0).charAt(0) + c.getString(0).charAt(1);
        String day =  "" + c.getString(0).charAt(2) + c.getString(0).charAt(3);
        String year = "" + c.getString(0).charAt(4) + c.getString(0).charAt(5) + c.getString(0).charAt(6) + c.getString(0).charAt(7);
        String hour = "" + c.getString(1).charAt(0) + c.getString(1).charAt(1);
        String minute = "" + c.getString(1).charAt(2) + c.getString(1).charAt(3);
        int alarmID = Integer.parseInt(c.getString(2));
        Calendar cal = Calendar.getInstance();
        cal.set(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day), Integer.parseInt(hour), Integer.parseInt(minute));
        long alertTime = cal.getTimeInMillis();
        Intent alarmIntent = new Intent(getApplicationContext(), alarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), alarmID, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmMan = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmMan.cancel(pendingIntent);
        pendingIntent.cancel();
        c.close();
        reminderDB.close();
        reminderDB = mDBHelper.getWritableDatabase();
        Cursor b = reminderDB.rawQuery("DELETE FROM Reminder WHERE RID = '" + RID + "'", null);
        b.moveToFirst();
        reminderDB.close();
    }
    public void handleMultiple(){

        if (currentDate < numberOfDates){
            currentDate += 1;
        }
        else {
            deleteReminderFromSQLite(RID);
            deleteReminderFromDrive(RID);
        }
        FeedReaderDBHelper mDBHelper = new FeedReaderDBHelper(getApplicationContext());
        SQLiteDatabase reminderDB = mDBHelper.getReadableDatabase();
        Cursor c = reminderDB.rawQuery("SELECT * FROM Reminder WHERE RID = '" + RID + "'", null);
        c.moveToFirst();
        Calendar cal = Calendar.getInstance();
        Calendar alertDate = Calendar.getInstance();
        Intent alarmIntent = new Intent(getApplicationContext(), alarmReceiver.class);
        alarmIntent.putExtra("RID", RID);
        alarmIntent.putExtra("description", reminderDescription);
        alarmIntent.putExtra("alarmID", alarmID);
        alarmIntent.putExtra("alarm", alarm);
        alarmIntent.putExtra("onTime", onTime);
        alarmIntent.putExtra("currentDate", currentDate);
        alarmIntent.putExtra("repeatNumber", repeatNumber);
        alarmIntent.putExtra("numberOfDates", numberOfDates);
        alarmIntent.putExtra("priority", priority);
        if (currentDate == 2){
            String month = "" + c.getString(c.getColumnIndex("DueDate2")).charAt(4) + c.getString(c.getColumnIndex("DueDate2")).charAt(5);
            String day =  "" + c.getString(c.getColumnIndex("DueDate2")).charAt(6) + c.getString(c.getColumnIndex("DueDate2")).charAt(7);
            String year = "" + c.getString(c.getColumnIndex("DueDate2")).charAt(0) + c.getString(c.getColumnIndex("DueDate2")).charAt(1) +
                    c.getString(c.getColumnIndex("DueDate2")).charAt(2) + c.getString(c.getColumnIndex("DueDate2")).charAt(3);
            String hour = "" + c.getString(c.getColumnIndex("DueTime2")).charAt(0) + c.getString(c.getColumnIndex("DueTime2")).charAt(1);
            String minute = "" + c.getString(c.getColumnIndex("DueTime2")).charAt(2) + c.getString(c.getColumnIndex("DueTime2")).charAt(3);


            if (onTime == 1){
                cal.set(Integer.parseInt(year), Integer.parseInt(month)-1, Integer.parseInt(day), Integer.parseInt(hour), Integer.parseInt(minute));
                long time = cal.getTimeInMillis();
                updateCalc(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1, cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE));
               // updateDueDate(cal.YEAR, cal.MONTH + 1, cal.DAY_OF_MONTH, cal.HOUR, cal.MINUTE);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), alarmID, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                AlarmManager alarmMan = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmMan.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
            }
            else{
                cal.set(Integer.parseInt(year), Integer.parseInt(month)-1, Integer.parseInt(day), Integer.parseInt(hour), Integer.parseInt(minute));

                String [] calc = priorityAlgorithm.getPriorityDateandTime(cal.get(Calendar.MONTH) +1, cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.YEAR),
                        cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE), priority);
                alertDate.set(Integer.parseInt(calc[2]), Integer.parseInt(calc[0]) - 1, Integer.parseInt(calc[1]), Integer.parseInt(calc[3]), Integer.parseInt(calc[4]));
                long time = alertDate.getTimeInMillis();
                updateCalc(alertDate.get(Calendar.YEAR), alertDate.get(Calendar.MONTH)+1, alertDate.get(Calendar.DAY_OF_MONTH), alertDate.get(Calendar.HOUR), alertDate.get(Calendar.MINUTE));
               // updateDueDate(cal.YEAR, cal.MONTH + 1, cal.DAY_OF_MONTH, cal.HOUR, cal.MINUTE);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), alarmID, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);

                AlarmManager alarmMan = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmMan.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
            }
            sqlToJSON(RID);
        }
        if (currentDate == 3){
            String month = "" + c.getString(c.getColumnIndex("DueDate3")).charAt(4) + c.getString(c.getColumnIndex("DueDate3")).charAt(5);
            String day =  "" + c.getString(c.getColumnIndex("DueDate3")).charAt(6) + c.getString(c.getColumnIndex("DueDate3")).charAt(7);
            String year = "" + c.getString(c.getColumnIndex("DueDate3")).charAt(0) + c.getString(c.getColumnIndex("DueDate3")).charAt(1) +
                    c.getString(c.getColumnIndex("DueDate3")).charAt(2) + c.getString(c.getColumnIndex("DueDate3")).charAt(3);
            String hour = "" + c.getString(c.getColumnIndex("DueTime3")).charAt(0) + c.getString(c.getColumnIndex("DueTime3")).charAt(1);
            String minute = "" + c.getString(c.getColumnIndex("DueTime3")).charAt(2) + c.getString(c.getColumnIndex("DueTime3")).charAt(3);
            if (onTime == 1){
                cal.set(Integer.parseInt(year), Integer.parseInt(month)-1, Integer.parseInt(day), Integer.parseInt(hour), Integer.parseInt(minute));
                long time = cal.getTimeInMillis();
                updateCalc(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR), cal.get(Calendar.DAY_OF_MONTH));
               // updateDueDate(cal.YEAR, cal.MONTH + 1, cal.DAY_OF_MONTH, cal.HOUR, cal.MINUTE);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), alarmID, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                AlarmManager alarmMan = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmMan.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
            }
            else{
                cal.set(Integer.parseInt(year), Integer.parseInt(month)-1, Integer.parseInt(day), Integer.parseInt(hour), Integer.parseInt(minute));

                String [] calc = priorityAlgorithm.getPriorityDateandTime(cal.get(Calendar.MONTH) +1, cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.YEAR),
                        cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE), priority);
                alertDate.set(Integer.parseInt(calc[2]), Integer.parseInt(calc[0]) - 1, Integer.parseInt(calc[1]), Integer.parseInt(calc[3]), Integer.parseInt(calc[4]));
                long time = alertDate.getTimeInMillis();
                updateCalc(alertDate.get(Calendar.YEAR), alertDate.get(Calendar.MONTH) + 1, alertDate.get(Calendar.DAY_OF_MONTH), alertDate.get(Calendar.HOUR), alertDate.get(Calendar.MINUTE));
              //  updateDueDate(cal.YEAR, cal.MONTH + 1, cal.DAY_OF_MONTH, cal.HOUR, cal.MINUTE);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), alarmID, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                AlarmManager alarmMan = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmMan.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
            }
            sqlToJSON(RID);
        }
        if (currentDate == 4){
            String month = "" + c.getString(c.getColumnIndex("DueDate4")).charAt(4) + c.getString(c.getColumnIndex("DueDate4")).charAt(5);
            String day =  "" + c.getString(c.getColumnIndex("DueDate4")).charAt(6) + c.getString(c.getColumnIndex("DueDate4")).charAt(7);
            String year = "" + c.getString(c.getColumnIndex("DueDate4")).charAt(0) + c.getString(c.getColumnIndex("DueDate4")).charAt(1) +
                    c.getString(c.getColumnIndex("DueDate4")).charAt(2) + c.getString(c.getColumnIndex("DueDate4")).charAt(3);
            String hour = "" + c.getString(c.getColumnIndex("DueTime4")).charAt(0) + c.getString(c.getColumnIndex("DueTime4")).charAt(1);
            String minute = "" + c.getString(c.getColumnIndex("DueTime4")).charAt(2) + c.getString(c.getColumnIndex("DueTime4")).charAt(3);
            if (onTime == 1){
                cal.set(Integer.parseInt(year), Integer.parseInt(month)-1, Integer.parseInt(day), Integer.parseInt(hour), Integer.parseInt(minute));
                long time = cal.getTimeInMillis();
                updateCalc(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR), cal.get(Calendar.DAY_OF_MONTH));
               // updateDueDate(cal.YEAR, cal.MONTH + 1, cal.DAY_OF_MONTH, cal.HOUR, cal.MINUTE);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), alarmID, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                AlarmManager alarmMan = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmMan.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
            }
            else{
                cal.set(Integer.parseInt(year), Integer.parseInt(month)-1, Integer.parseInt(day), Integer.parseInt(hour), Integer.parseInt(minute));

                String [] calc = priorityAlgorithm.getPriorityDateandTime(cal.get(Calendar.MONTH) +1, cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.YEAR),
                        cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE), priority);                alertDate.set(Integer.parseInt(calc[2]), Integer.parseInt(calc[0]) - 1, Integer.parseInt(calc[1]), Integer.parseInt(calc[3]), Integer.parseInt(calc[4]));
                long time = alertDate.getTimeInMillis();
                updateCalc(alertDate.get(Calendar.YEAR), alertDate.get(Calendar.MONTH) + 1, alertDate.get(Calendar.DAY_OF_MONTH), alertDate.get(Calendar.HOUR), alertDate.get(Calendar.MINUTE));
              //  updateDueDate(cal.YEAR, cal.MONTH + 1, cal.DAY_OF_MONTH, cal.HOUR, cal.MINUTE);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), alarmID, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                AlarmManager alarmMan = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmMan.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
            }
            sqlToJSON(RID);
        }
        if (currentDate == 5){
            String month = "" + c.getString(c.getColumnIndex("DueDate5")).charAt(4) + c.getString(c.getColumnIndex("DueDate5")).charAt(5);
            String day =  "" + c.getString(c.getColumnIndex("DueDate5")).charAt(6) + c.getString(c.getColumnIndex("DueDate5")).charAt(7);
            String year = "" + c.getString(c.getColumnIndex("DueDate5")).charAt(0) + c.getString(c.getColumnIndex("DueDate5")).charAt(1) +
                    c.getString(c.getColumnIndex("DueDate5")).charAt(2) + c.getString(c.getColumnIndex("DueDate5")).charAt(3);
            String hour = "" + c.getString(c.getColumnIndex("DueTime5")).charAt(0) + c.getString(c.getColumnIndex("DueTime5")).charAt(1);
            String minute = "" + c.getString(c.getColumnIndex("DueTime5")).charAt(2) + c.getString(c.getColumnIndex("DueTime5")).charAt(3);
            if (onTime == 1){
                cal.set(Integer.parseInt(year), Integer.parseInt(month)-1, Integer.parseInt(day), Integer.parseInt(hour), Integer.parseInt(minute));
                long time = cal.getTimeInMillis();
                updateCalc(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR), cal.get(Calendar.DAY_OF_MONTH));
              //  updateDueDate(cal.YEAR, cal.MONTH + 1, cal.DAY_OF_MONTH, cal.HOUR, cal.MINUTE);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), alarmID, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                AlarmManager alarmMan = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmMan.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
            }
            else{
                cal.set(Integer.parseInt(year), Integer.parseInt(month)-1, Integer.parseInt(day), Integer.parseInt(hour), Integer.parseInt(minute));

                String [] calc = priorityAlgorithm.getPriorityDateandTime(cal.get(Calendar.MONTH) +1, cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.YEAR),
                        cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE), priority);                alertDate.set(Integer.parseInt(calc[2]), Integer.parseInt(calc[0]) - 1, Integer.parseInt(calc[1]), Integer.parseInt(calc[3]), Integer.parseInt(calc[4]));
                long time = alertDate.getTimeInMillis();
                updateCalc(alertDate.get(Calendar.YEAR), alertDate.get(Calendar.MONTH) + 1, alertDate.get(Calendar.DAY_OF_MONTH), alertDate.get(Calendar.HOUR), alertDate.get(Calendar.MINUTE));
              //  updateDueDate(cal.YEAR, cal.MONTH + 1, cal.DAY_OF_MONTH, cal.HOUR, cal.MINUTE);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), alarmID, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                AlarmManager alarmMan = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmMan.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
            }
            sqlToJSON(RID);
        }
        if (currentDate == 6){
            String month = "" + c.getString(c.getColumnIndex("DueDate6")).charAt(4) + c.getString(c.getColumnIndex("DueDate6")).charAt(5);
            String day =  "" + c.getString(c.getColumnIndex("DueDate6")).charAt(6) + c.getString(c.getColumnIndex("DueDate6")).charAt(7);
            String year = "" + c.getString(c.getColumnIndex("DueDate6")).charAt(0) + c.getString(c.getColumnIndex("DueDate6")).charAt(1) +
                    c.getString(c.getColumnIndex("DueDate6")).charAt(2) + c.getString(c.getColumnIndex("DueDate6")).charAt(3);
            String hour = "" + c.getString(c.getColumnIndex("DueTime6")).charAt(0) + c.getString(c.getColumnIndex("DueTime6")).charAt(1);
            String minute = "" + c.getString(c.getColumnIndex("DueTime6")).charAt(2) + c.getString(c.getColumnIndex("DueTime6")).charAt(3);
            if (onTime == 1){
                cal.set(Integer.parseInt(year), Integer.parseInt(month)-1, Integer.parseInt(day), Integer.parseInt(hour), Integer.parseInt(minute));
                long time = cal.getTimeInMillis();
                updateCalc(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR), cal.get(Calendar.DAY_OF_MONTH));
              //  updateDueDate(cal.YEAR, cal.MONTH + 1, cal.DAY_OF_MONTH, cal.HOUR, cal.MINUTE);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), alarmID, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                AlarmManager alarmMan = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmMan.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
            }
            else{
                cal.set(Integer.parseInt(year), Integer.parseInt(month)-1, Integer.parseInt(day), Integer.parseInt(hour), Integer.parseInt(minute));

                String [] calc = priorityAlgorithm.getPriorityDateandTime(cal.get(Calendar.MONTH) +1, cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.YEAR),
                        cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE), priority);                alertDate.set(Integer.parseInt(calc[2]), Integer.parseInt(calc[0]) - 1, Integer.parseInt(calc[1]), Integer.parseInt(calc[3]), Integer.parseInt(calc[4]));
                long time = alertDate.getTimeInMillis();
                updateCalc(alertDate.get(Calendar.YEAR), alertDate.get(Calendar.MONTH) + 1, alertDate.get(Calendar.DAY_OF_MONTH), alertDate.get(Calendar.HOUR), alertDate.get(Calendar.MINUTE));
             //   updateDueDate(cal.YEAR, cal.MONTH + 1, cal.DAY_OF_MONTH, cal.HOUR, cal.MINUTE);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), alarmID, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                AlarmManager alarmMan = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmMan.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
            }
            sqlToJSON(RID);
        }
        if (currentDate == 7){
            String month = "" + c.getString(c.getColumnIndex("DueDate7")).charAt(4) + c.getString(c.getColumnIndex("DueDate7")).charAt(5);
            String day =  "" + c.getString(c.getColumnIndex("DueDate7")).charAt(6) + c.getString(c.getColumnIndex("DueDate7")).charAt(7);
            String year = "" + c.getString(c.getColumnIndex("DueDate7")).charAt(0) + c.getString(c.getColumnIndex("DueDate7")).charAt(1) +
                    c.getString(c.getColumnIndex("DueDate7")).charAt(2) + c.getString(c.getColumnIndex("DueDate7")).charAt(3);
            String hour = "" + c.getString(c.getColumnIndex("DueTime7")).charAt(0) + c.getString(c.getColumnIndex("DueTime7")).charAt(1);
            String minute = "" + c.getString(c.getColumnIndex("DueTime7")).charAt(2) + c.getString(c.getColumnIndex("DueTime7")).charAt(3);
            if (onTime == 1){
                cal.set(Integer.parseInt(year), Integer.parseInt(month)-1, Integer.parseInt(day), Integer.parseInt(hour), Integer.parseInt(minute));
                long time = cal.getTimeInMillis();
                updateCalc(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR), cal.get(Calendar.DAY_OF_MONTH));
             //   updateDueDate(cal.YEAR, cal.MONTH + 1, cal.DAY_OF_MONTH, cal.HOUR, cal.MINUTE);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), alarmID, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                AlarmManager alarmMan = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmMan.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
            }
            else{
                cal.set(Integer.parseInt(year), Integer.parseInt(month)-1, Integer.parseInt(day), Integer.parseInt(hour), Integer.parseInt(minute));

                String [] calc = priorityAlgorithm.getPriorityDateandTime(cal.get(Calendar.MONTH) +1, cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.YEAR),
                        cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE), priority);                alertDate.set(Integer.parseInt(calc[2]), Integer.parseInt(calc[0]) - 1, Integer.parseInt(calc[1]), Integer.parseInt(calc[3]), Integer.parseInt(calc[4]));
                long time = alertDate.getTimeInMillis();
                updateCalc(alertDate.get(Calendar.YEAR), alertDate.get(Calendar.MONTH) + 1, alertDate.get(Calendar.DAY_OF_MONTH), alertDate.get(Calendar.HOUR), alertDate.get(Calendar.MINUTE));
               // updateDueDate(cal.YEAR, cal.MONTH + 1, cal.DAY_OF_MONTH, cal.HOUR, cal.MINUTE);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), alarmID, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                AlarmManager alarmMan = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmMan.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
            }
            sqlToJSON(RID);
        }


        updateCurrentDate(currentDate);

    }

    public void updateCalc(int year, int month, int day, int hour, int minute){
        String date = makeCalcString(year, month, day);
        String time = makeTimeString(hour, minute);
        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.columnCalculatedDate, date);
        values.put(FeedReaderContract.FeedEntry.columnCalculatedTime, time);
        FeedReaderDBHelper mDBHelper = new FeedReaderDBHelper(getApplicationContext());
        SQLiteDatabase reminderDB = mDBHelper.getWritableDatabase();
        reminderDB.update("Reminder", values, "RID = '" + RID + "'", null);
        reminderDB.close();
    }
    public void updateDueDate(int year, int month, int day, int hour, int minute){
        String date = makeDueDate(year, month, day);
        String time = makeTimeString(hour, minute);
        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.columnDueDate1, date);
        values.put(FeedReaderContract.FeedEntry.columnDueTime1, time);
        FeedReaderDBHelper mDBHelper = new FeedReaderDBHelper(getApplicationContext());
        SQLiteDatabase reminderDB = mDBHelper.getWritableDatabase();
        reminderDB.update("Reminder", values, "RID = '" + RID + "'", null);
        reminderDB.close();
    }
    public void updateCurrentDate(int date) {
        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.columnCurrentDate, date);
        FeedReaderDBHelper mDBHelper = new FeedReaderDBHelper(getApplicationContext());
        SQLiteDatabase reminderDB = mDBHelper.getWritableDatabase();
        reminderDB.update("Reminder", values, "RID = '" + RID + "'", null);
        reminderDB.close();
    }
    public String makeCalcString(int year, int month, int day){
        String yearS = "";
        String monthS = "";
        String dayS = "";
        yearS = Integer.toString(year);
        if (month < 10){
            monthS = "0" + Integer.toString(month);
        }
        else
            monthS = Integer.toString(month);

        if (day < 10){
            dayS = Integer.toString(day);
        }
        else
            dayS = Integer.toString(day);

        return monthS + dayS + yearS;
    }
    public String makeDueDate(int year, int month, int day){
        String yearS = Integer.toString(year);
        String monthS = Integer.toString(month);
        String dayS = Integer.toString(day);
        if (month < 10){
            monthS = "0" + monthS;
        }
        if (day < 10){
            dayS = "0" + dayS;
        }
        return yearS + monthS + dayS;
    }
    public String makeTimeString(int hour, int minute){
        String hourS = Integer.toString(hour);
        String minuteS = Integer.toString(minute);
        if (hour < 10){
            hourS = "0" + hourS;
        }
        if (minute < 10){
            minuteS = "0" + minuteS;
        }
        return hourS + minuteS;
    }
    private void endReminder(String rid, int alarmID, int currentDate, int repeatNumber, int numberOfDates) {
        // TODO: Handle action Foo
        Intent intent1 = new Intent(getApplicationContext(), notificationActions.class);
        PendingIntent pendingIntent1 = PendingIntent.getService(getApplicationContext(), alarmID+1, intent1, PendingIntent.FLAG_CANCEL_CURRENT);
        pendingIntent1.cancel();
        Intent intent2 = new Intent(getApplicationContext(), notificationActions.class);
        PendingIntent pendingIntent2 = PendingIntent.getService(getApplicationContext(), alarmID+2, intent2, PendingIntent.FLAG_CANCEL_CURRENT);
        pendingIntent2.cancel();
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getApplicationContext());
        managerCompat.cancel(alarmID);

        if (repeatNumber != 0){
            handleRepeat();
        }
        else if (numberOfDates > 1){
            handleMultiple();
        }
        else{
            try {
                deleteReminderFromDrive(RID);
                deleteReminderFromSQLite(RID);
            } catch (Exception e){

            }
        }

    }
    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void remindMeLater(String rid, int alarmID) {
        Intent intent1 = new Intent(getApplicationContext(), notificationActions.class);
        PendingIntent pendingIntent1 = PendingIntent.getService(getApplicationContext(), alarmID+1, intent1, PendingIntent.FLAG_CANCEL_CURRENT);
        pendingIntent1.cancel();
        Intent intent3 = new Intent(getApplicationContext(), notificationActions.class);
        PendingIntent pendingIntent3 = PendingIntent.getService(getApplicationContext(), alarmID+3, intent3, PendingIntent.FLAG_CANCEL_CURRENT);
        pendingIntent3.cancel();
        Calendar cal = Calendar.getInstance();
        FeedReaderDBHelper mDBHelper = new FeedReaderDBHelper(getApplicationContext());
        SQLiteDatabase reminderDB = mDBHelper.getReadableDatabase();
        Cursor c = reminderDB.rawQuery("SELECT DueDate1, DueTime1, Priority FROM Reminder WHERE RID = '" + rid + "'", null);
        c.moveToFirst();
        if (numberOfDates > 1) {
            if (currentDate == 2) {
                c = reminderDB.rawQuery("SELECT DueDate2, DueTime2, Priority FROM Reminder WHERE RID = '" + rid + "'", null);
                c.moveToFirst();
            } else if (currentDate == 3) {
                c = reminderDB.rawQuery("SELECT DueDate3, DueTime3, Priority FROM Reminder WHERE RID = '" + rid + "'", null);
                c.moveToFirst();
            } else if (currentDate == 4) {
                c = reminderDB.rawQuery("SELECT DueDate4, DueTime4, Priority FROM Reminder WHERE RID = '" + rid + "'", null);
                c.moveToFirst();
            } else if (currentDate == 5) {
                c = reminderDB.rawQuery("SELECT DueDate5, DueTime5, Priority FROM Reminder WHERE RID = '" + rid + "'", null);
                c.moveToFirst();
            } else if (currentDate == 6) {
                c = reminderDB.rawQuery("SELECT DueDate6, DueTime6, Priority FROM Reminder WHERE RID = '" + rid + "'", null);
                c.moveToFirst();
            } else if (currentDate == 7) {
                c = reminderDB.rawQuery("SELECT DueDate7, DueTime7, Priority FROM Reminder WHERE RID = '" + rid + "'", null);
                c.moveToFirst();
            }
        }
        String month = "" + c.getString(0).charAt(4) + c.getString(0).charAt(5);
        String day =  "" + c.getString(0).charAt(6) + c.getString(0).charAt(7);
        String year = "" + c.getString(0).charAt(0) + c.getString(0).charAt(1) + c.getString(0).charAt(2) + c.getString(0).charAt(3);
        String hour = "" + c.getString(1).charAt(0) + c.getString(1).charAt(1);
        String minute = "" + c.getString(1).charAt(2) + c.getString(1).charAt(3);
        String[] calc = priorityAlgorithm.getPriorityDateandTime(Integer.parseInt(month), Integer.parseInt(day), Integer.parseInt(year), Integer.parseInt(hour), Integer.parseInt(minute), priority);
        //String reminderDescription = c.getString(1);
        c.close();
        cal.set(Integer.parseInt(year), Integer.parseInt(month)-1, Integer.parseInt(day), Integer.parseInt(hour), Integer.parseInt(minute));
        Long d = cal.getTimeInMillis();
        Long b = System.currentTimeMillis()-1800000;
        if (d < b) {
            String calcDate = calc[0] + calc[1] + calc[2];
            String calcTime = calc[3] + calc[4];
            reminderDB.close();
            reminderDB = mDBHelper.getWritableDatabase();
            Cursor a = reminderDB.rawQuery("UPDATE Reminder SET CalculatedDate = " + calcDate + ", CalculateTime = " + calcTime + " WHERE RID = '" + rid + "'", null);
            a.moveToFirst();
            a.close();
            reminderDB.close();
            cal.set(Integer.parseInt(calc[2]), Integer.parseInt(calc[1])-1, Integer.parseInt(calc[0]), Integer.parseInt(calc[3]), Integer.parseInt(calc[4]));
            long alertTime = cal.getTimeInMillis();
            Intent alarmIntent = new Intent(getApplicationContext(), alarmReceiver.class);
            alarmIntent.putExtra("RID", RID);
            alarmIntent.putExtra("description", reminderDescription);
            alarmIntent.putExtra("alarmID", alarmID);
            alarmIntent.putExtra("alarm", alarm);
            alarmIntent.putExtra("onTime", onTime);
            alarmIntent.putExtra("currentDate", currentDate);
            alarmIntent.putExtra("repeatNumber", repeatNumber);
            alarmIntent.putExtra("numberOfDate", numberOfDates);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), alarmID, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            AlarmManager alarmMan = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmMan.set(AlarmManager.RTC_WAKEUP, alertTime, pendingIntent);
            sqlToJSON(rid);
        }
        else{
            /*
            explain to user that the deadline is either in at most half an hour or has passed.
             */
        }
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getApplicationContext());
        managerCompat.cancel(alarmID);
        // TODO: Handle action Baz
      //  throw new UnsupportedOperationException("Not yet implemented");
    }
    /*
        maybe snooze doesn't upload to the cloud? If the user hits snooze, presumably the other devices also have this notification already and wil continue to ahave it
        up until the user sees it and responds. snooze is also only an hour, why compute all this for another notification on all devices in an hour?

     */
    private void snooze(String rid, int alarmID){
        //cancel 2 and 3
        Intent intent2 = new Intent(getApplicationContext(), notificationActions.class);
        PendingIntent pendingIntent2 = PendingIntent.getService(getApplicationContext(), alarmID+2, intent2, PendingIntent.FLAG_CANCEL_CURRENT);
        pendingIntent2.cancel();
        Intent intent3 = new Intent(getApplicationContext(), notificationActions.class);
        PendingIntent pendingIntent3 = PendingIntent.getService(getApplicationContext(), alarmID+3, intent3, PendingIntent.FLAG_CANCEL_CURRENT);
        pendingIntent3.cancel();
        Long alertTime = System.currentTimeMillis() + 1800000;
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent alarmIntent = new Intent(getApplicationContext(), alarmReceiver.class);
        alarmIntent.putExtra("RID", RID);
        alarmIntent.putExtra("description", reminderDescription);
        alarmIntent.putExtra("alarmID", alarmID);
        alarmIntent.putExtra("alarm", alarm);
        alarmIntent.putExtra("onTime", onTime);
        alarmIntent.putExtra("currentDate", currentDate);
        alarmIntent.putExtra("repeatNumber", repeatNumber);
        alarmIntent.putExtra("numberOfDate", numberOfDates);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), alarmID, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmMan = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmMan.set(AlarmManager.RTC_WAKEUP, alertTime, pendingIntent);
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getApplicationContext());
        managerCompat.cancel(alarmID);
    }
    private void sqlToJSON(String rid){
        FeedReaderDBHelper mDBHelper = new FeedReaderDBHelper(getApplicationContext());
        SQLiteDatabase reminderDB = mDBHelper.getReadableDatabase();
        Cursor c = reminderDB.rawQuery("Select * FROM Reminder WHERE RID = '" + rid + "'", null);
        c.moveToFirst();
        JSON json = new JSON (c.getString(c.getColumnIndex("RID")), c.getString(c.getColumnIndex("Description")), c.getString(c.getColumnIndex("DueDate1")), c.getString(c.getColumnIndex("DueTime1")),
                c.getString(c.getColumnIndex("DueDate2")), c.getString(c.getColumnIndex("DueTime2")), c.getString(c.getColumnIndex("DueDate3")), c.getString(c.getColumnIndex("DueTime3")),
                c.getString(c.getColumnIndex("DueDate4")), c.getString(c.getColumnIndex("DueTime4")), c.getString(c.getColumnIndex("DueDate5")), c.getString(c.getColumnIndex("DueTime5")),
                c.getString(c.getColumnIndex("DueDate6")), c.getString(c.getColumnIndex("DueTime6")), c.getString(c.getColumnIndex("DueDate7")), c.getString(c.getColumnIndex("DueTime7")),
                c.getString(c.getColumnIndex("CalculatedDate")), c.getString(c.getColumnIndex("CalculateTime")), Integer.toString(c.getInt(c.getColumnIndex("Priority"))), c.getInt(c.getColumnIndex("Alarm")),
                c.getInt(c.getColumnIndex("OnTime")), c.getInt(c.getColumnIndex("RepeatEveryxDays")), c.getInt(c.getColumnIndex("NumberofDates")), c.getInt(c.getColumnIndex("CurrentDate")));
        // JSON jsonObject = new JSON(RID, reminderDescription, datePicked, timePicked, calculations[0] + calculations[1] + calculations[2], calculations[3] + calculations[4],
        //   Integer.toString(priority_Value ), alarm, onTime, 0);
         String jsonString = json.JSONString;
         saveToDrive(jsonString, rid);
    }
    public void saveToDrive(final String json, final String rid){
        final DriveFolder folder = Drive.DriveApi.getAppFolder(mGoogleApiClient);
        Query query = new Query.Builder()
                .addFilter(Filters.eq(SearchableField.TITLE, "REMINDERS"))
                .build();
        folder.queryChildren(mGoogleApiClient, query).setResultCallback(new ResultCallback<DriveApi.MetadataBufferResult>() {
            @Override
            public void onResult(@NonNull DriveApi.MetadataBufferResult metadataBufferResult) {
                //Toast.makeText(getApplicationContext(),"asdfasdf", Toast.LENGTH_SHORT).show();
                if (metadataBufferResult.getMetadataBuffer().iterator().hasNext()) {
                    DriveFolder reminderFolder = metadataBufferResult.getMetadataBuffer().iterator().next().getDriveId().asDriveFolder();

                    Query query2 = new Query.Builder()
                            .addFilter(Filters.eq(SearchableField.MIME_TYPE, "text/plain"))
                            .addFilter(Filters.eq(SearchableField.TITLE, rid))
                            .build();
                    reminderFolder.queryChildren(mGoogleApiClient, query2).setResultCallback(new ResultCallback<DriveApi.MetadataBufferResult>() {
                        @Override
                        public void onResult(@NonNull DriveApi.MetadataBufferResult metadataBufferResult) {
                            Iterator<Metadata> metadataIterator = metadataBufferResult.getMetadataBuffer().iterator();
                            if(!metadataIterator.hasNext()){
                                //   Toast.makeText(Reminders.this, "nothing here123", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            DriveFile a = metadataBufferResult.getMetadataBuffer().iterator().next().getDriveId().asDriveFile();

                            a.open(mGoogleApiClient, DriveFile.MODE_WRITE_ONLY, null).setResultCallback(new ResultCallback<DriveApi.DriveContentsResult>() {
                                @Override
                                public void onResult(@NonNull DriveApi.DriveContentsResult driveContentsResult) {
                                    if (!driveContentsResult.getStatus().isSuccess()){
                                        //driveContentsResult.getDriveContents().
                                        // Toast.makeText(getApplicationContext(),"hi", Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                    //   Toast.makeText(getApplicationContext(),"hi", Toast.LENGTH_SHORT).show();
                                    DriveContents contents = driveContentsResult.getDriveContents();
                                    ParcelFileDescriptor parcelFileDescriptor = contents.getParcelFileDescriptor();
                                    FileOutputStream fileOutputStream = new FileOutputStream(parcelFileDescriptor.getFileDescriptor());
                                    Writer writer = new OutputStreamWriter(fileOutputStream);
                                    try {writer.write(json + rid);
                                        writer.close();} catch (IOException e){ }
                                    contents.commit(mGoogleApiClient, null).setResultCallback(new ResultCallback<Status>() {
                                        @Override
                                        public void onResult(@NonNull Status status) {

                                               // Toast.makeText(getApplicationContext(), "asdf", Toast.LENGTH_SHORT).show();

                                        }
                                    });
                                }
                            });
                        }
                    });
                }
                metadataBufferResult.release();
            }
        });
    }
    public String constructString(int a){
        String b = "";
        if (a < 10) {
            b += "0" + Integer.toString(a);
        }
        else
            b = Integer.toString(a);
        return b;

    }
    public String date_Picked (int year, int month, int day) {
        String yearmonthday = "";
        String monthstring = "";
        String dayString = "";

        if (month < 10){
            monthstring = "0" + Integer.toString(month);
        }
        else
            monthstring = Integer.toString(month);
        if (day < 10){
            dayString = "0" + Integer.toString(day);
        }
        else
            dayString = Integer.toString(day);
        return yearmonthday = Integer.toString(year) + monthstring + dayString;
    }

    public String time_Picked (int hour, int minute) {
        String hourminute = "";
        String hourString = "";
        String minuteString = "";
        if (hour < 10){
            hourString = "0" + Integer.toString(hour);

        } else
            hourString = Integer.toString(hour);
        if (minute < 10){
            minuteString = "0" + Integer.toString(minute);
        } else
            minuteString = Integer.toString(minute);
        return hourminute = hourString + minuteString;
    }
}
