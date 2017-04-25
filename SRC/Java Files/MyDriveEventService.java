package com.seniorproject.prioritize;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.events.ChangeEvent;
import com.google.android.gms.drive.events.DriveEventService;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.SearchableField;
import com.google.android.gms.drive.query.SortOrder;
import com.google.android.gms.drive.query.SortableField;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

public class MyDriveEventService extends DriveEventService {
    public GoogleApiClient mGoogleApiClient;

    public void onChange(ChangeEvent event){
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Drive.API)
                .addScope(Drive.SCOPE_APPFOLDER)
                .build();
        mGoogleApiClient.connect();
            syncToSQLDB();
            handleDeletes();
        }
    public void syncToSQLDB(){
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
                    SortOrder sortOrder = new SortOrder.Builder()
                            .addSortDescending(SortableField.MODIFIED_DATE).build();
                    Query query2 = new Query.Builder()
                            .addFilter(Filters.eq(SearchableField.MIME_TYPE, "text/plain"))
                            .setSortOrder(sortOrder)
                            .build();
                    reminderFolder.queryChildren(mGoogleApiClient, query2).setResultCallback(new ResultCallback<DriveApi.MetadataBufferResult>() {
                        @Override
                        public void onResult(@NonNull DriveApi.MetadataBufferResult metadataBufferResult) {
                            Iterator<Metadata> metadataIterator = metadataBufferResult.getMetadataBuffer().iterator();
                            if(!metadataIterator.hasNext()){
                                Toast.makeText(MyDriveEventService.this, "nothing here123", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            /*
                            DriveFile file = metadataBufferResult.getMetadataBuffer().iterator().next().getDriveId().asDriveFile();
                            file.open(mGoogleApiClient, DriveFile.MODE_READ_ONLY, null).await();
                            DriveResource.MetadataResult result = file.getMetadata(mGoogleApiClient).await();
                            Metadata metadata = result.getMetadata();
                            Date reminderModifiedDate = metadata.getModifiedDate();
                            Date lastSyncDate = getSyncDate();
                            */
                            Date reminderModifiedDate = metadataBufferResult.getMetadataBuffer().iterator().next().getModifiedDate();
                            // while (getSyncDate().compareTo(date)>0){
                            //  while (metadataBufferResult.getMetadataBuffer().iterator().hasNext()){
                            while(metadataIterator.hasNext()){
                                Metadata metaData = metadataIterator.next();
                                String reminderDate = metaData.getCreatedDate().toString();
                                //   Toast.makeText(getApplicationContext(), date2, Toast.LENGTH_SHORT).show();
                                DriveFile a = metaData.getDriveId().asDriveFile();
                                String b = metaData.getTitle();
                                Toast.makeText(MyDriveEventService.this, b, Toast.LENGTH_SHORT).show();
                                a.open(mGoogleApiClient, DriveFile.MODE_READ_ONLY, null).setResultCallback(new ResultCallback<DriveApi.DriveContentsResult>() {
                                    @Override
                                    public void onResult(@NonNull DriveApi.DriveContentsResult driveContentsResult) {
                                        if (!driveContentsResult.getStatus().isSuccess()){
                                            //driveContentsResult.getDriveContents().
                                            // Toast.makeText(getApplicationContext(),"hi", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        //   Toast.makeText(getApplicationContext(),"hi", Toast.LENGTH_SHORT).show();
                                        readContent(driveContentsResult.getDriveContents());
                                    }
                                });
                            }


                        }
                    });
                }
                metadataBufferResult.release();
            }

        });
    }
    public void readContent(DriveContents contents){
        BufferedReader reader = new BufferedReader((new InputStreamReader(contents.getInputStream())));

        StringBuilder builder = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } catch (IOException e){
            //    Toast.makeText(this, "could not read file", Toast.LENGTH_SHORT).show();
        }
        //String a = contents.ge
        String contentsString = builder.toString();//this is the JSON String
        // Toast.makeText(this, contentsString, Toast.LENGTH_SHORT).show();
        JSON json = JSON.convert_from_string_to_Object(contentsString);
        // Toast.makeText(this, Integer.toString(json.Repeatable), Toast.LENGTH_SHORT).show();
        FeedReaderDBHelper mDBHelper = new FeedReaderDBHelper(getApplicationContext());
        SQLiteDatabase reminderDB = mDBHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.RID, json.RID);
        values.put(FeedReaderContract.FeedEntry.columnDescription, json.Description);
        values.put(FeedReaderContract.FeedEntry.columnDueDate1, json.Due_Date);
        values.put(FeedReaderContract.FeedEntry.columnDueTime1, json.Due_Time);
        values.put(FeedReaderContract.FeedEntry.columnCalculatedDate, json.C_Remind_Date);
        values.put(FeedReaderContract.FeedEntry.columnCalculatedTime, json.C_Remind_time);
        values.put(FeedReaderContract.FeedEntry.columnPriority, json.Priority_Val);
        values.put(FeedReaderContract.FeedEntry.columnAlarm, json.Alarm);
        values.put(FeedReaderContract.FeedEntry.columnOnTime, json.On_Time);
        values.put(FeedReaderContract.FeedEntry.columnRepeatNumber, json.Repeatable);
        values.put(FeedReaderContract.FeedEntry.columnDueDate2, json.Due_Date2);
        values.put(FeedReaderContract.FeedEntry.columnDueTime2, json.Due_Time2);
        values.put(FeedReaderContract.FeedEntry.columnDueDate3, json.Due_Date3);
        values.put(FeedReaderContract.FeedEntry.columnDueTime3, json.Due_Time3);
        values.put(FeedReaderContract.FeedEntry.columnDueDate4, json.Due_Date4);
        values.put(FeedReaderContract.FeedEntry.columnDueTime4, json.Due_Time4);
        values.put(FeedReaderContract.FeedEntry.columnDueDate5, json.Due_Date5);
        values.put(FeedReaderContract.FeedEntry.columnDueTime6, json.Due_Time5);
        values.put(FeedReaderContract.FeedEntry.columnDueDate6, json.Due_Date6);
        values.put(FeedReaderContract.FeedEntry.columnDueTime6, json.Due_Time6);
        values.put(FeedReaderContract.FeedEntry.columnDueDate7, json.Due_Date7);
        values.put(FeedReaderContract.FeedEntry.columnDueTime7, json.Due_Time7);
        //values.put(FeedReaderContract.FeedEntry.columnAlarmSchedulerID, alarmID);
        Cursor c = reminderDB.rawQuery("SELECT * FROM Reminder WHERE RID = '" + json.RID + "'", null);
        //int numberOfDates = json.numDates;
         /*
                if (numberOfDates > 1){
                    values.put(FeedReaderContract.FeedEntry.columnDueDate2, datePicked2);
                    values.put(FeedReaderContract.FeedEntry.columnDueTime2, timePicked2);
                }
                if (numberOfDates > 2){
                    values.put(FeedReaderContract.FeedEntry.columnDueDate3, datePicked3);
                    values.put(FeedReaderContract.FeedEntry.columnDueTime3, timePicked3);
                }
                if (numberOfDates > 3){
                    values.put(FeedReaderContract.FeedEntry.columnDueDate4, datePicked4);
                    values.put(FeedReaderContract.FeedEntry.columnDueTime4, timePicked4);
                }
                if (numberOfDates > 4){
                    values.put(FeedReaderContract.FeedEntry.columnDueDate5, datePicked5);
                    values.put(FeedReaderContract.FeedEntry.columnDueTime6, timePicked5);
                }
                if (numberOfDates > 5){
                    values.put(FeedReaderContract.FeedEntry.columnDueDate6, datePicked6);
                    values.put(FeedReaderContract.FeedEntry.columnDueTime6, timePicked6);
                }
                if (numberOfDates > 6){
                    values.put(FeedReaderContract.FeedEntry.columnDueDate7, datePicked7);
                    values.put(FeedReaderContract.FeedEntry.columnDueTime7, timePicked7);
                }*/

        if (c.getCount()==0) { //just creating a new reminder at this point
            int alarmID = getAlarmID();

            //   Toast.makeText(this, "here", Toast.LENGTH_SHORT).show();
            values.put(FeedReaderContract.FeedEntry.columnAlarmSchedulerID, alarmID);
            String month = "" + json.C_Remind_Date.charAt(0) + json.C_Remind_Date.charAt(1);
            String day =  "" + json.C_Remind_Date.charAt(2) + json.C_Remind_Date.charAt(3);
            String year = "" + json.C_Remind_Date.charAt(4) + json.C_Remind_Date.charAt(5) + json.C_Remind_Date.charAt(6) + json.C_Remind_Date.charAt(7);
            String hour = "" + json.C_Remind_Date.charAt(0) + json.C_Remind_Date.charAt(1);
            String minute = "" + json.C_Remind_Date.charAt(2) + json.C_Remind_Date.charAt(3);
            reminderDB.close();
            c.close();
            reminderDB = mDBHelper.getWritableDatabase();
            long newRowID = reminderDB.insert(FeedReaderContract.FeedEntry.tableName, null, values);
            Calendar cal = Calendar.getInstance();
            cal.set(Integer.parseInt(year), Integer.parseInt(month)-1, Integer.parseInt(day), Integer.parseInt(hour), Integer.parseInt(minute));
            long alertTime = cal.getTimeInMillis();
            Date alertDate = new Date (alertTime);
            Date currentDate = new Date (System.currentTimeMillis());
            if (alertDate.compareTo(currentDate) > 0) {
                // NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                Intent alarmIntent = new Intent(getApplicationContext(), alarmReceiver.class);
                alarmIntent.putExtra("RID", json.RID);
                alarmIntent.putExtra("description", json.Description);
                alarmIntent.putExtra("alarmID", alarmID);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), alarmID, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                AlarmManager alarmMan = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmMan.set(AlarmManager.RTC_WAKEUP, alertTime, pendingIntent);
            }
            reminderDB.close();
        }
        else { //uupdating a previous reminder
            c.moveToFirst();
            String alarmID = c.getString(c.getColumnIndex("ALarmSchedulerID"));
            String month = "" + json.C_Remind_Date.charAt(0) + json.C_Remind_Date.charAt(1);
            String day = "" + json.C_Remind_Date.charAt(2) + json.C_Remind_Date.charAt(3);
            String year = "" + json.C_Remind_Date.charAt(4) + json.C_Remind_Date.charAt(5) + json.C_Remind_Date.charAt(6) + json.C_Remind_Date.charAt(7);
            String hour = "" + json.C_Remind_Date.charAt(0) + json.C_Remind_Date.charAt(1);
            String minute = "" + json.C_Remind_Date.charAt(2) + json.C_Remind_Date.charAt(3);
            reminderDB.close();
            c.close();
            reminderDB = mDBHelper.getWritableDatabase();
            reminderDB.update("Reminder", values, "RID = '" + json.RID + "'", null);
            Calendar cal = Calendar.getInstance();
            cal.set(Integer.parseInt(year), Integer.parseInt(month)-1, Integer.parseInt(day), Integer.parseInt(hour), Integer.parseInt(minute));
            long alertTime = cal.getTimeInMillis();
            Date alertDate = new Date(alertTime);
            Date currentDate = new Date(System.currentTimeMillis());
            if (alertDate.compareTo(currentDate) > 0) {
                Intent alarmIntent = new Intent(getApplicationContext(), alarmReceiver.class);
                alarmIntent.putExtra("RID", json.RID);
                alarmIntent.putExtra("description", json.Description);
                alarmIntent.putExtra("alarmID", alarmID);
                int alarmIDInt = Integer.parseInt(alarmID);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), alarmIDInt, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                AlarmManager alarmMan = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmMan.set(AlarmManager.RTC_WAKEUP, alertTime, pendingIntent);
            }
            reminderDB.close();
            //Toast.makeText(this, "here", Toast.LENGTH_SHORT).show();
        }
    }
    public int getAlarmID(){
        int alarmID = 1;
        try{
            File file = new File(this.getFilesDir(), "alarmID.txt");
            if(!file.exists()){
                file.createNewFile();
                file.setReadable(true);
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                writer.write("1");
                writer.close();
                return alarmID;
            }
            else {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String readIn = reader.readLine();
                reader.close();
                alarmID = Integer.parseInt(readIn);
                alarmID +=4;
                file.setWritable(true);
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                writer.write(String.valueOf(alarmID));
                writer.close();
                return alarmID;
            }
        } catch (IOException e){
            return alarmID;
        }
        //  cal.
        /*
        text file that is updated each time a sync is done
        this should be updated after handling changeSubscriptions
         */
    }
    public void handleDeletes(){
        final String deviceID = getDeviceID();
        final DriveFolder appFolder = Drive.DriveApi.getAppFolder(mGoogleApiClient);
        Query query = new Query.Builder()
                .addFilter(Filters.eq(SearchableField.TITLE, deviceID))
                .build();
        appFolder.queryChildren(mGoogleApiClient, query).setResultCallback(new ResultCallback<DriveApi.MetadataBufferResult>() {
            @Override
            public void onResult(@NonNull DriveApi.MetadataBufferResult metadataBufferResult) {
                if (!metadataBufferResult.getMetadataBuffer().iterator().hasNext()){
                    DriveFolder deviceFolder = metadataBufferResult.getMetadataBuffer().iterator().next().getDriveId().asDriveFolder();
                    deviceFolder.listChildren(mGoogleApiClient).setResultCallback(new ResultCallback<DriveApi.MetadataBufferResult>() {
                        @Override
                        public void onResult(@NonNull DriveApi.MetadataBufferResult metadataBufferResult) {
                            Iterator<Metadata> metadataIterator = metadataBufferResult.getMetadataBuffer().iterator();
                            while(metadataIterator.hasNext()){
                                Metadata metadata = metadataIterator.next();
                                DriveFile file = metadata.getDriveId().asDriveFile();
                                String rid = metadata.getTitle();
                                deleteReminderFromSQLite(rid);
                                file.delete(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                                    @Override
                                    public void onResult(@NonNull Status status) {
                                    }
                                });
                            }
                        }
                    });
                    // Toast.makeText(getApplicationContext(), "Device ID folder not there", Toast.LENGTH_LONG).show();
                }
                metadataBufferResult.release();
            }
        });
    }
    public String getDeviceID(){
        String readIn = "";
        try{
            File file = new File(this.getFilesDir(), "deviceID.txt");
            BufferedReader reader = new BufferedReader(new FileReader(file));
            readIn = reader.readLine();
            reader.close();
            return readIn;
        } catch (Exception e){
        }
        return readIn;
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





}
