package com.seniorproject.prioritize;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.icu.text.SimpleDateFormat;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.icu.util.Calendar;


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


public class Reminders extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    ListView alarmList;
    ArrayList<String> arrList;
    ArrayAdapter<String> arrAdapter;
    public GoogleApiClient mGoogleApiClient;
    private static final int REQUEST_CODE_CAPTURE_IMAGE = 1;
    private static final int REQUEST_CODE_CREATOR = 2;
    private static final int REQUEST_CODE_RESOLUTION = 3;
    private static final int RESOLVE_CONNECTION_REQUEST_CODE = 4;
    private static final String TAG = "";
    private Bitmap mBitmapToSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Drive.API)
                .addScope(Drive.SCOPE_APPFOLDER)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
        /*
        Google drive android api
         */

        FeedReaderDBHelper mDBHelper = new FeedReaderDBHelper(getApplicationContext());
        SQLiteDatabase reminderDB = mDBHelper.getReadableDatabase();
        //center align the app title
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_layout);
        //title font
        Typeface myfont = Typeface.createFromAsset(getAssets(), "Pacifico.ttf");
        TextView app_title = (TextView) findViewById(R.id.mytitle);
        app_title.setTypeface(myfont);
        //for alarm info
        TextView newAlarmDescription = (TextView) findViewById(R.id.alarm_text);
        Intent intent = getIntent();
        String alarmInfo = intent.getStringExtra("alarmDescription");
        newAlarmDescription.setText(alarmInfo);
        if (newAlarmDescription.getText() == alarmInfo) {
            newAlarmDescription.setVisibility(View.VISIBLE);
            //List of alarms
            alarmList = (ListView) findViewById(R.id.alarmListView);
            arrList = new ArrayList<String>();
            arrAdapter = new ArrayAdapter<String>(Reminders.this, android.R.layout.simple_list_item_1, arrList);
            alarmList.setAdapter(arrAdapter);
            arrAdapter.add(alarmInfo);
            arrAdapter.notifyDataSetChanged();

        }

       // The way that I suggest going about queries is with rawQuery.
    
       // and just get each result row into its own little square somehow.
        


        /* 
           
            SQLiteDatabase reminderDB = mDBHelper.getReadableDatabase();
            
            Cursor c = reminderDB.rawQuery("SELECT Description, priority, dueDate, dueTime from Reminder", null);
            c.moveToFirst();
            /*
            this getString will return the value the tuple in the first result of the query that was specified as a string.
            in this query it was the Description
            String a = c.getString(0);
             getString(0) will return the priority, and so on from the order in which they are inserted in
            setAlarm.
            
            This will go to the next tuple in the result of the query. 
            c.moveToNext(); 
            You should do this until c.moveToNext() either doesn't exist or comes out to be a 
            tuple that you've already read. I'm not sure the best way to do that part of the loop
            but I'll leave you to figure that out. this link MIGHT have that secret
            https://www.tutorialspoint.com/android/android_sqlite_database.htm
            I don't actually know.
            
            If that doesn't help then try reading up on this;
            https://developer.android.com/reference/android/database/Cursor.html
            and on this
            
            https://developer.android.com/training/basics/data-storage/databases.html
            
            
            
            
            c.close();
            */
     
        
         cursor.close();
         */
        reminderDB.close();
    }
    //open the alarm setup activity
    public void addAlarm(View view) {
        Intent addAlarmView = new Intent(this, SetAlarm.class);
        startActivity(addAlarmView);
    }
    @Override
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
    public void onConnected(Bundle connectionHint) {
        Log.i(TAG, "API client connected.");

        syncDown();



        //syncToSQLDB();





        final DriveFolder folder = Drive.DriveApi.getAppFolder(mGoogleApiClient);
        Query query = new Query.Builder()
                .addFilter(Filters.eq(SearchableField.TITLE, "REMINDERS"))
                .build();

        folder.queryChildren(mGoogleApiClient, query).setResultCallback(new ResultCallback<DriveApi.MetadataBufferResult>() {
            @Override

            public void onResult(@NonNull DriveApi.MetadataBufferResult metadataBufferResult) {
                //Toast.makeText(getApplicationContext(),"asdfasdf", Toast.LENGTH_SHORT).show();
                if (metadataBufferResult.getMetadataBuffer().iterator().hasNext()) {
                    DriveFolder a = Drive.DriveApi.getFolder(mGoogleApiClient, metadataBufferResult.getMetadataBuffer().iterator().next().getDriveId());
                    a.addChangeSubscription(mGoogleApiClient);
                    metadataBufferResult.release();
                }
            }
        });
        // saveFileToDrive();
    }
    public void syncDown(){
        Drive.DriveApi.requestSync(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                long time = getDriveSyncBreak();
                if (time==0) {

                    if (status.isSuccess()) {
                        checkDeviceID();
                        checkReminderFolder();
                        syncToSQLDB();
                    } else
                        setDriveSyncBreak();
                }
                else if (System.currentTimeMillis() > time){
                    if (status.isSuccess()) {
                        checkDeviceID();
                        checkReminderFolder();
                        syncToSQLDB();
                    } else
                        setDriveSyncBreak();

                }
            }

        });


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

                    Toast.makeText(getApplicationContext(), "Device ID folder not there", Toast.LENGTH_LONG).show();
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
    public void tellDevicesToDeleteReminder(String RID){
        final String reminderID = RID;
        final DriveFolder appFolder = Drive.DriveApi.getAppFolder(mGoogleApiClient);
        CustomPropertyKey key = new CustomPropertyKey("Device Folder", CustomPropertyKey.PUBLIC);
        Query query = new Query.Builder()
                .addFilter(Filters.eq(key, "True"))
                .build();
        appFolder.queryChildren(mGoogleApiClient, query).setResultCallback(new ResultCallback<DriveApi.MetadataBufferResult>() {
            @Override
            public void onResult(@NonNull DriveApi.MetadataBufferResult metadataBufferResult) {
                String deviceID = getDeviceID();
                Iterator<Metadata> iterator = metadataBufferResult.getMetadataBuffer().iterator();
                while (iterator.hasNext()){
                    Metadata metadat = iterator.next();
                    if (!metadat.getTitle().equals(deviceID)){
                        DriveId ID = metadat.getDriveId();
                        final DriveFolder a = Drive.DriveApi.getFolder(mGoogleApiClient, ID);
                        Drive.DriveApi.newDriveContents(mGoogleApiClient).setResultCallback(new ResultCallback<DriveApi.DriveContentsResult>() {
                            @Override
                            public void onResult(@NonNull DriveApi.DriveContentsResult result) {
                                if (!result.getStatus().isSuccess()) {
                                    Toast.makeText(getApplicationContext(), "Could not Create DriveContents", Toast.LENGTH_SHORT).show();
                                    // Handle error
                                    return;
                                }
                                DriveContents contents = result.getDriveContents();
                                ParcelFileDescriptor parcelFileDescriptor = contents.getParcelFileDescriptor();
                                FileOutputStream fileOutputStream = new FileOutputStream(parcelFileDescriptor.getFileDescriptor());
                                Writer writer = new OutputStreamWriter(fileOutputStream);
                                try {writer.write(reminderID);
                                    writer.close();} catch (IOException e){ }
                                final MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                                        .setTitle(reminderID)
                                        .setMimeType("text/plain")
                                        .build();
                                a.createFile(mGoogleApiClient, changeSet, contents);
                                return;
                            }
                        });
                    }
                }
            }
        });

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
        reminderDB.execSQL("DELETE FROM Reminders WHERE RID = " + RID);
        reminderDB.close();




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
                if (!metadataBufferResult.getMetadataBuffer().iterator().hasNext()){

                        DriveFolder a = Drive.DriveApi.getFolder(mGoogleApiClient, metadataBufferResult.getMetadataBuffer().iterator().next().getDriveId());
                    CustomPropertyKey deviceFolder = new CustomPropertyKey("Device Folder", CustomPropertyKey.PUBLIC);
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
    public void checkReminderFolder(){
        final DriveFolder folder = Drive.DriveApi.getAppFolder(mGoogleApiClient);
        Query query = new Query.Builder()
                .addFilter(Filters.eq(SearchableField.TITLE, "REMINDERS"))
                .build();
        folder.queryChildren(mGoogleApiClient, query).setResultCallback(new ResultCallback<DriveApi.MetadataBufferResult>() {
            @Override
            public void onResult(@NonNull DriveApi.MetadataBufferResult metadataBufferResult) {
                if (!metadataBufferResult.getMetadataBuffer().iterator().hasNext()) {
                    Toast.makeText(getApplicationContext(), "Reminder Folder Not Here", Toast.LENGTH_LONG).show();
                    MetadataChangeSet changeSet = new MetadataChangeSet.Builder().setTitle("REMINDERS").build();
                    folder.createFolder(mGoogleApiClient, changeSet).setResultCallback(new ResultCallback<DriveFolder.DriveFolderResult>() {
                        @Override
                        public void onResult(@NonNull DriveFolder.DriveFolderResult driveFolderResult) {
                            driveFolderResult.getDriveFolder().getDriveId();
                        }
                    });
                }
                Toast.makeText(Reminders.this, metadataBufferResult.getMetadataBuffer().iterator().next().getTitle(), Toast.LENGTH_SHORT).show();
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
            }
        } catch (IOException e) {
            Toast.makeText(this, "Could not create deviceID.txt", Toast.LENGTH_SHORT).show();
        }
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
                Toast.makeText(this, d, Toast.LENGTH_SHORT).show();
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
    public Date getSyncDate(){
        String readIn = "";
        Date date = new Date();
        try{
            File file = new File(this.getFilesDir(), "syncDate.txt");
            BufferedReader reader = new BufferedReader(new FileReader(file));
            readIn = reader.readLine();
            date = new Date(readIn);
        } catch (IOException e){
            //broke
        }
        return date;
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
    public void search(String a){
        final DriveFolder folder = Drive.DriveApi.getAppFolder(mGoogleApiClient);
        Query query = new Query.Builder()
                .addFilter(Filters.eq(SearchableField.TITLE, "REMINDERS"))
                .build();
        folder.queryChildren(mGoogleApiClient, query).setResultCallback(new ResultCallback<DriveApi.MetadataBufferResult>() {
            @Override

            public void onResult(@NonNull DriveApi.MetadataBufferResult metadataBufferResult) {
                //Toast.makeText(getApplicationContext(),"asdfasdf", Toast.LENGTH_SHORT).show();
                if (metadataBufferResult.getMetadataBuffer().iterator().hasNext()) {
                    DriveFolder a = Drive.DriveApi.getFolder(mGoogleApiClient, metadataBufferResult.getMetadataBuffer().iterator().next().getDriveId());
                    String s = metadataBufferResult.getMetadataBuffer().iterator().next().getTitle();
                    Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                    a.listChildren(mGoogleApiClient).setResultCallback(new ResultCallback<DriveApi.MetadataBufferResult>() {
                        @Override
                        public void onResult(@NonNull DriveApi.MetadataBufferResult metadataBufferResult) {
                            if (!metadataBufferResult.getMetadataBuffer().iterator().hasNext()){
                                Toast.makeText(getApplicationContext(), "nothing", Toast.LENGTH_SHORT).show();
                            }
                            String s = metadataBufferResult.getMetadataBuffer().iterator().next().getTitle();
                            Toast.makeText(Reminders.this, s, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
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
                    DriveFolder a = Drive.DriveApi.getFolder(mGoogleApiClient, metadataBufferResult.getMetadataBuffer().iterator().next().getDriveId());
                    SortOrder sortOrder = new SortOrder.Builder()
                            .addSortDescending(SortableField.CREATED_DATE).build();
                    Query query2 = new Query.Builder()
                            .addFilter(Filters.eq(SearchableField.MIME_TYPE, "text/plain"))
                            .setSortOrder(sortOrder)
                            .build();
                    a.queryChildren(mGoogleApiClient, query2).setResultCallback(new ResultCallback<DriveApi.MetadataBufferResult>() {
                        @Override
                        public void onResult(@NonNull DriveApi.MetadataBufferResult metadataBufferResult) {
                            Iterator<Metadata> metadataIterator = metadataBufferResult.getMetadataBuffer().iterator();
                            if(!metadataIterator.hasNext()){
                                Toast.makeText(Reminders.this, "nothing here123", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            //Date date = metadataIterator.next().getCreatedDate();




                            // while (getSyncDate().compareTo(date)>0){
                          //  while (metadataBufferResult.getMetadataBuffer().iterator().hasNext()){
                            while(metadataIterator.hasNext()){
                                Metadata metaData = metadataIterator.next();
                                String date2 = metaData.getCreatedDate().toString();
                                Toast.makeText(getApplicationContext(), date2, Toast.LENGTH_SHORT).show();
                                DriveFile file = metaData.getDriveId().asDriveFile();
                            String a = metaData.getTitle();
                            Toast.makeText(Reminders.this, a, Toast.LENGTH_SHORT).show();
                            file.open(mGoogleApiClient, DriveFile.MODE_READ_ONLY, null).setResultCallback(new ResultCallback<DriveApi.DriveContentsResult>() {

                                @Override
                                    public void onResult(@NonNull DriveApi.DriveContentsResult driveContentsResult) {

                                    if (!driveContentsResult.getStatus().isSuccess()){
                                        //driveContentsResult.getDriveContents().
                                       // Toast.makeText(getApplicationContext(),"hi", Toast.LENGTH_SHORT).show();
                                        return;
                                        }
                                        Toast.makeText(getApplicationContext(),"hi", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this, "could not read file", Toast.LENGTH_SHORT).show();

        }
        //String a = contents.ge
        String contentsString = builder.toString();//this is the JSON String
        Toast.makeText(this, contentsString, Toast.LENGTH_SHORT).show();
      //  JSON jsonString = JSON.convert_from_string_to_Object(contentsString);

        /*
        read in the JSON file
         */


    }

    public class MyDriveEventService extends DriveEventService {
        public void onChange(ChangeEvent event){
            syncToSQLDB();
        }
    }
    public class deleteEventService extends DriveEventService{
        /*
        open the added file, obtain RID, delete locally.
         */
        public void onChange(ChangeEvent event) {}
    }

}
