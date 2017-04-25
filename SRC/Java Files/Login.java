package com.seniorproject.prioritize;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.icu.text.SimpleDateFormat;
import android.os.AsyncTask;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
//import android.icu.util.Calendar;
import java.util.Calendar;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.ResultCallbacks;
import com.google.android.gms.common.api.ResultTransform;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.DriveResource;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataBuffer;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.events.ChangeEvent;
import com.google.android.gms.drive.events.ChangeListener;
import com.google.android.gms.drive.events.DriveEventService;
import com.google.android.gms.drive.metadata.CustomPropertyKey;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.SearchableField;
import com.google.android.gms.drive.query.SortOrder;
import com.google.android.gms.drive.query.SortableField;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;


public class Login extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    public GoogleApiClient mGoogleApiClient;
    private static final int REQUEST_CODE_CAPTURE_IMAGE = 1;
    private static final int REQUEST_CODE_CREATOR = 2;
    private static final int REQUEST_CODE_RESOLUTION = 3;
    private static final int RESOLVE_CONNECTION_REQUEST_CODE = 4;
    private static final String TAG = "";
    private Bitmap mBitmapToSave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Drive.API)
                .addScope(Drive.SCOPE_APPFOLDER)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




    }
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this, RESOLVE_CONNECTION_REQUEST_CODE);
            } catch (IntentSender.SendIntentException e) {
                Toast.makeText(this, "Connection Falure", Toast.LENGTH_SHORT).show();
                // Unable to resolve, message user appropriately
            }
        } else {
            GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), this, 0).show();
        }
    }
    public long getDriveSyncBreak(){
        String readIn = "";
        long minutesRead = 0;
        try{
            File file = new File(this.getFilesDir(), "driveSyncBreak.txt");
            BufferedReader reader = new BufferedReader(new FileReader(file));
            readIn = reader.readLine();
            minutesRead = Long.parseLong(readIn);
        } catch ( Exception a){
            //broke
        }
        return minutesRead;
    }

    public void setDriveSyncBreak(){
        try{
            File file = new File(this.getFilesDir(), "driveSyncBreak.txt");
            file.createNewFile();
            file.setReadable(true);
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            long minutes = System.currentTimeMillis() + 300000;
            String a = Long.toString(minutes);
            writer.write(a);
            writer.close();
        } catch (IOException e){
            return;
        }
        //  cal.
        /*
        text file that is updated each time a sync is done
        this should be updated after handling changeSubscriptions
         */
    }
    public Integer syncDown(){
        Drive.DriveApi.requestSync(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                long time = getDriveSyncBreak();
                if (time==0) {
                    //Toast.makeText(Reminders.this, "can sync 1", Toast.LENGTH_SHORT).show();
                    if (status.isSuccess()) {
                     //   Toast.makeText(Login.this, "can  sync", Toast.LENGTH_SHORT).show();
                        checkDeviceID();
                        checkReminderFolder();
                    } else
                    //    Toast.makeText(Login.this, "can't sync", Toast.LENGTH_SHORT).show();
                    setDriveSyncBreak();
                }
                else if (System.currentTimeMillis() > time){
                    if (status.isSuccess()) {
                     //   Toast.makeText(Login.this, "can  sync", Toast.LENGTH_SHORT).show();
                        //  Toast.makeText(Reminders.this, "can sync2", Toast.LENGTH_SHORT).show();
                        checkDeviceID();
                        checkReminderFolder();
                    }
                    //    Toast.makeText(Login.this, "can't sync", Toast.LENGTH_SHORT).show();
                    //  Toast.makeText(Reminders.this, "can't sync", Toast.LENGTH_SHORT).show();
                }
            }

        });
        return 1;
        /*
        check the file that says to wait a while for syncing
         */
       /* Drive.DriveApi.requestSync(mGoogleApiClient)
                .then((new ResultTransform<Status, Result>() {
                    @Nullable
                    @Override
                    public PendingResult<Result> onSuccess(@NonNull Status status) {
                        Toast.makeText(Reminders.this, "asdf", Toast.LENGTH_SHORT).show();
                        checkDeviceID();
                        checkReminderFolder();
                        return null;
                    }
                    public PendingResult<Result> onFail(@NonNull Status status){
                        Toast.makeText(Reminders.this, "123", Toast.LENGTH_SHORT).show();
                        setDriveSyncBreak();
                        //set a file that says no syncing for a little while.
                        return null;
                    }
                }));*/
    }
    public void onConnected(Bundle connectionHint) {
       // checkDeviceID();
        //checkReminderFolder();
        Log.i(TAG, "API client connected.");
       // Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();

       // syncDown();
        final Intent intent = new Intent(this, Reminders.class);
        new AsyncTask<Void, Void, Integer>() {

            protected Integer doInBackground(Void... params) {
                syncDown();
                return syncToSQLDB();


            }

            protected void onPostExecute(Integer result) {
                if (result == 1) {
                    startActivity(intent);
                    finish();
                }
            }
        }.execute();





        // setDeviceIDFolderMetadata(getDeviceID());
        //  syncDown();
       // syncToSQLDB();


        //  checkReminderFolder();
        /*
        final DriveFolder folder = Drive.DriveApi.getAppFolder(mGoogleApiClient);
        Query query = new Query.Builder()
                .addFilter(Filters.eq(SearchableField.TITLE, "REMINDERS"))
                .build();
        folder.queryChildren(mGoogleApiClient, query).setResultCallback(new ResultCallback<DriveApi.MetadataBufferResult>() {
            @Override
            public void onResult(@NonNull DriveApi.MetadataBufferResult metadataBufferResult) {
                //Toast.makeText(getApplicationContext(),"asdfasdf", Toast.LENGTH_SHORT).show();
                if (metadataBufferResult.getMetadataBuffer().iterator().hasNext()) {
                    DriveFolder b = metadataBufferResult.getMetadataBuffer().iterator().next().getDriveId().asDriveFolder();
                    //  DriveFolder a = Drive.DriveApi.getFolder(mGoogleApiClient, metadataBufferResult.getMetadataBuffer().iterator().next().getDriveId());
                    b.listChildren(mGoogleApiClient).setResultCallback(new ResultCallback<DriveApi.MetadataBufferResult>() {
                        @Override
                        public void onResult(@NonNull DriveApi.MetadataBufferResult metadataBufferResult) {
                            if (!metadataBufferResult.getMetadataBuffer().iterator().hasNext()){
                                // Toast.makeText(Reminders.this, "done", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    // Drive.DriveApi.requestSync(mGoogleApiClient);
                    //Toast.makeText(Reminders.this, "here", Toast.LENGTH_SHORT).show();
                    // a.addChangeSubscription(mGoogleApiClient);
                    metadataBufferResult.release();
                }
            }
        });
        */
        // saveFileToDrive();
    }
    @Override
    public void onConnectionSuspended(int cause) {
        Log.i(TAG, "GoogleApiClient connection suspended");
    }
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        switch (requestCode) {
            case RESOLVE_CONNECTION_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    mGoogleApiClient.connect();
                }
                break;
        }
    }
    public void checkDeviceFolder(String uuid) {
        final String deviceID = uuid;
        final DriveFolder appFolder = Drive.DriveApi.getAppFolder(mGoogleApiClient);
        Query query = new Query.Builder()
                .addFilter(Filters.eq(SearchableField.TITLE, deviceID))
                .build();
        appFolder.queryChildren(mGoogleApiClient, query).setResultCallback(new ResultCallback<DriveApi.MetadataBufferResult>() {
            @Override
            public void onResult(@NonNull DriveApi.MetadataBufferResult metadataBufferResult) {
                if (!metadataBufferResult.getMetadataBuffer().iterator().hasNext()){

                    // Toast.makeText(getApplicationContext(), "Device ID folder not there", Toast.LENGTH_LONG).show();
                    MetadataChangeSet changeSet = new MetadataChangeSet.Builder().setTitle(deviceID).build();
                    appFolder.createFolder(mGoogleApiClient, changeSet).setResultCallback(new ResultCallback<DriveFolder.DriveFolderResult>() {
                        @Override
                        public void onResult(@NonNull DriveFolder.DriveFolderResult driveFolderResult) {
                            driveFolderResult.getDriveFolder().getDriveId();
                        }
                    });
                }
                metadataBufferResult.release();
            }
        });
    }
    public void checkReminderFolder(){
        final DriveFolder folder = Drive.DriveApi.getAppFolder(mGoogleApiClient);
        Query query = new Query.Builder()
                .addFilter(Filters.eq(SearchableField.TITLE, "REMINDERS"))
                .build();
        folder.queryChildren(mGoogleApiClient, query).setResultCallback(new ResultCallback<DriveApi.MetadataBufferResult>() {
            @Override
            public void onResult(@NonNull DriveApi.MetadataBufferResult metadataBufferResult) {
                if (!metadataBufferResult.getMetadataBuffer().iterator().hasNext()) {
                    Toast.makeText(Login.this, "Creating Drive Reminder Folder", Toast.LENGTH_SHORT).show();
                    //   Toast.makeText(getApplicationContext(), "Reminder Folder Not Here", Toast.LENGTH_LONG).show();
                    MetadataChangeSet changeSet = new MetadataChangeSet.Builder().setTitle("REMINDERS").build();
                    folder.createFolder(mGoogleApiClient, changeSet).setResultCallback(new ResultCallback<DriveFolder.DriveFolderResult>() {
                        @Override
                        public void onResult(@NonNull DriveFolder.DriveFolderResult driveFolderResult) {
                            driveFolderResult.getDriveFolder().getDriveId();
                        }
                    });
                }
                //  Toast.makeText(Reminders.this, metadataBufferResult.getMetadataBuffer().iterator().next().getTitle(), Toast.LENGTH_SHORT).show();
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
    public void checkDeviceID() {
        try {
            File deviceID = new File(this.getFilesDir(), "deviceID.txt");
            if (!deviceID.exists()) {
                deviceID.createNewFile();
                BufferedWriter writer = new BufferedWriter(new FileWriter(deviceID));
                final String uuid = UUID.randomUUID().toString();
                writer.write(uuid);
                writer.close();
                checkDeviceFolder(uuid);
                setDeviceIDFolderMetadata(uuid);
            }
        } catch (IOException e) {
            //    Toast.makeText(this, "Could not create deviceID.txt", Toast.LENGTH_SHORT).show();
        }
    }
    public void setDeviceIDFolderMetadata(String uuid){
        final String deviceID = uuid;
        final DriveFolder appFolder = Drive.DriveApi.getAppFolder(mGoogleApiClient);
        Query query = new Query.Builder()
                .addFilter(Filters.eq(SearchableField.TITLE, deviceID))
                .build();
        appFolder.queryChildren(mGoogleApiClient, query).setResultCallback(new ResultCallback<DriveApi.MetadataBufferResult>() {
            @Override
            public void onResult(@NonNull DriveApi.MetadataBufferResult metadataBufferResult) {
                if (metadataBufferResult.getMetadataBuffer().iterator().hasNext()){
                   // Toast.makeText(Login.this, "hereeee", Toast.LENGTH_SHORT).show();
                    DriveFolder a = Drive.DriveApi.getFolder(mGoogleApiClient, metadataBufferResult.getMetadataBuffer().iterator().next().getDriveId());
                    final CustomPropertyKey deviceFolder = new CustomPropertyKey("DeviceFolder", CustomPropertyKey.PUBLIC);
                    MetadataChangeSet.Builder changeSetBuilder = new MetadataChangeSet.Builder();
                    changeSetBuilder.setCustomProperty(deviceFolder, "True");
                    a.updateMetadata(mGoogleApiClient, changeSetBuilder.build()).setResultCallback(new ResultCallback<DriveResource.MetadataResult>() {
                        @Override
                        public void onResult(@NonNull DriveResource.MetadataResult metadataResult) {
                        }
                    });
                }
                metadataBufferResult.release();
            }
        });
    }
    public Integer syncToSQLDB(){
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
                              //  Toast.makeText(Login.this, "nothing here123", Toast.LENGTH_SHORT).show();
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
                            Date lastSyncDate = getSyncDate();
                            Date reminderModifiedDate = metadataBufferResult.getMetadataBuffer().iterator().next().getModifiedDate();
                            // while (getSyncDate().compareTo(date)>0){
                            //  while (metadataBufferResult.getMetadataBuffer().iterator().hasNext()){
                            while(metadataIterator.hasNext()){
                                Metadata metaData = metadataIterator.next();
                                String reminderDate = metaData.getCreatedDate().toString();
                                //   Toast.makeText(getApplicationContext(), date2, Toast.LENGTH_SHORT).show();
                                DriveFile a = metaData.getDriveId().asDriveFile();
                                String b = metaData.getTitle();
                               // Toast.makeText(Login.this, b, Toast.LENGTH_SHORT).show();
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
                            setSyncDate();

                        }
                    });
                }
                metadataBufferResult.release();
            }

        });
        return 1;
    }
    public void setSyncDate(){
        Calendar cal = Calendar.getInstance();
        try{
            File file = new File(this.getFilesDir(), "syncDate.txt");
            file.createNewFile();
            file.setReadable(true);
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            Date a = new Date(System.currentTimeMillis());
            String b = a.toString();
            Date c = new Date(b);
            String d = c.toString();
            //
            //  Toast.makeText(this, d, Toast.LENGTH_SHORT).show();
            writer.write(b);
            writer.close();
        } catch (IOException e){
            return;
        }
        //  cal.
        /*
        text file that is updated each time a sync is done
        this should be updated after handling changeSubscriptions
         */
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
        values.put(FeedReaderContract.FeedEntry.columnCurrentDate, json.currday);
        values.put(FeedReaderContract.FeedEntry.columnNumberOfDates, json.NumDates);


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
                alarmIntent.putExtra("alarm", json.Alarm);
                alarmIntent.putExtra("numberOfDates", json.NumDates);
                alarmIntent.putExtra("currentDate", json.currday);
                alarmIntent.putExtra("repeatNumber", json.Repeatable);
                alarmIntent.putExtra("onTime", json.On_Time);
                alarmIntent.putExtra("priority", json.Priority_Val);
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
                alarmIntent.putExtra("alarm", json.Alarm);
                alarmIntent.putExtra("numberOfDates", json.NumDates);
                alarmIntent.putExtra("currentDate", json.currday);
                alarmIntent.putExtra("repeatNumber", json.Repeatable);
                alarmIntent.putExtra("onTime", json.On_Time);
                alarmIntent.putExtra("priority", json.Priority_Val);
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
    public Date getSyncDate(){
        String readIn = "";
        Date date = new Date();
        try{
            File file = new File(this.getFilesDir(), "syncDate.txt");
            if (!file.exists()){
                Calendar cal = Calendar.getInstance();
                cal.set(1960, 2, 15);
                Date dummy = new Date(cal.getTimeInMillis());
                return dummy;
            }
            BufferedReader reader = new BufferedReader(new FileReader(file));
            readIn = reader.readLine();
            date = new Date(readIn);
        } catch (IOException e){
            //broke
        }
        return date;
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

}
