package com.seniorproject.prioritize;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.support.annotation.NonNull;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.events.ChangeEvent;
import com.google.android.gms.drive.events.DriveEventService;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.SearchableField;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Calendar;
import java.util.Iterator;

public class deleteEventService extends DriveEventService {

    public GoogleApiClient mGoogleApiClient;


    public void onChange(ChangeEvent event){
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Drive.API)
                .addScope(Drive.SCOPE_APPFOLDER)
                .build();
        mGoogleApiClient.connect();
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
