package com.seniorproject.prioritize;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.icu.util.Calendar;
import android.media.AudioManager;
import android.os.ParcelFileDescriptor;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.DriveResource;
import com.google.android.gms.drive.ExecutionOptions;
import com.google.android.gms.drive.MetadataBuffer;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.events.ChangeListener;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.SearchableField;
import com.google.android.gms.drive.zzc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class SetAlarm extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    AlarmManager alarm_Manager;
    TimePicker alarm_TimePicker;
    DatePicker alarm_DatePicker;
    EditText alarm_Description;
    Spinner priority_DropDown;
    CheckBox alarm_Type;
    CheckBox on_Time;
    double priority_Value;
    int hour, minute, year, month, day;
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
        setContentView(R.layout.activity_set_alarm);
        connect();





        //center align the app title
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_layout);

        //title font
        final Typeface myfont = Typeface.createFromAsset(getAssets(), "Pacifico.ttf");
        TextView app_title = (TextView) findViewById(R.id.mytitle);
        app_title.setTypeface(myfont);

        //initialize alarmmanager
        alarm_Manager = (AlarmManager) getSystemService(ALARM_SERVICE);

        //initialize timepicker
        alarm_TimePicker = (TimePicker) findViewById(R.id.timePicker);

        //initialize datepicker
        alarm_DatePicker = (DatePicker) findViewById(R.id.datePicker);

        //initialize description text box
        alarm_Description = (EditText) findViewById(R.id.descriptionText);

        //initialize priority_Bar
        priority_DropDown = (Spinner) findViewById(R.id.priorityDropDown);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.priority, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        priority_DropDown.setAdapter(adapter);
        priority_DropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Object item = parent.getItemAtPosition(pos);
                String itemTest = String.valueOf(item);
                priority_Value = Integer.valueOf(itemTest);
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //initialize alarm_type checkbox
        alarm_Type = (CheckBox) findViewById(R.id.alarmCheckBox);

        //initialize on_time checkbox
        on_Time = (CheckBox) findViewById(R.id.onTimeCheckBox);

        //create an instance of a calendar
        final Calendar calendar = Calendar.getInstance();

        //initialize button
        Button save_Alarm = (Button) findViewById(R.id.alarmSave);

        //onClick method for saving the alarm
        save_Alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                calendar.set(alarm_DatePicker.getYear(),alarm_DatePicker.getMonth(),alarm_DatePicker.getDayOfMonth(),
                        alarm_TimePicker.getHour(),alarm_TimePicker.getMinute());


                if (alarm_Type.isChecked()) {//ringtone

                    calendar.set (Calendar.SECOND,0);

                    Intent intent = new Intent(SetAlarm.this, PlayAudio.class);
                    PendingIntent penInt = PendingIntent.getService(SetAlarm.this.getApplicationContext(), getAlarmID(), intent, 0);
                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTime().getTime(), penInt);

                    Toast.makeText(SetAlarm.this, "Alarm is set!" + calendar.getTime(), Toast.LENGTH_LONG).show();
                }
                else   {
                    calendar.set (Calendar.SECOND,0);
                    Intent push = new Intent(SetAlarm.this, Reminders.class);
                    PendingIntent pi = PendingIntent.getActivity(SetAlarm.this, getAlarmID(), push,
                            PendingIntent.FLAG_ONE_SHOT);

                    Context context = SetAlarm.this;
                    NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.ic_launcher)
                            .setContentTitle("Reminder")
                            .setContentText(alarm_Description.getText())
                            .setDefaults(Notification.DEFAULT_ALL)
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setAutoCancel(true)
                            .setWhen(calendar.getTime().getTime())
                            .setTicker("Reminder")
                            .setContentIntent(pi);
                    notificationManager.notify(getAlarmID(), builder.build());

//                    if (calendar.getTimeInMillis()==System.currentTimeMillis()) {
//                        //Intent addAlarmView = new Intent(SetAlarm.this, MyBroadcastReceiver.class);
//                        Vibrator v = (Vibrator) SetAlarm.this.getSystemService(Context.VIBRATOR_SERVICE);
//                        // Vibrate for 500 milliseconds
//                        v.vibrate(500);
//                    }
                }

                //get int values
                year = alarm_DatePicker.getYear();
                month = alarm_DatePicker.getMonth() + 1;
                day = alarm_DatePicker.getDayOfMonth();
                hour = alarm_TimePicker.getHour();
                minute = alarm_TimePicker.getMinute();

                //convert to string
                String year_String = String.valueOf(year);
                String month_String = String.valueOf(month);
                String day_String = String.valueOf(day);
                String hour_string = String.valueOf(hour);
                String minute_string = String.valueOf(minute);

                Intent alarmData = new Intent();
                //---set the data to pass back---
                alarmData.putExtra("year", year_String);
                alarmData.putExtra("month", month_String);
                alarmData.putExtra("day", day_String);
                alarmData.putExtra("hour", hour_string);
                alarmData.putExtra("minute", minute_string);
                setResult(RESULT_OK, alarmData);
                //---close the activity---
                finish();
//                Intent alarmset = new Intent(SetAlarm.this, Reminders.class);
//                startActivity(alarmset);



                /*
                STILL NEED TO CHECK DRIVE FOLDER FOR FILES WITH THE SAME UUID.
                CONFLICT PROBABLY WON'T HAPPEN, BUT PREVENTING IT IS NECESSARY.
                 */

//                String[] calculations = new String[5];
//                //calculations = priorityAlgorithm.getPriorityDateandTime(alarm_DatePicker.getMonth()+1, alarm_DatePicker.getDayOfMonth(), alarm_DatePicker.getYear(), alarm_TimePicker.getHour(), alarm_TimePicker.getMinute(), 1);
//                int alarmID = getAlarmID();
//                String RID = createRID();
//                ContentValues values = new ContentValues();
//                values.put(FeedReaderContract.FeedEntry.RID, RID);
//                values.put(FeedReaderContract.FeedEntry.columnDescription, alarm_Description.getText().toString());
//                values.put(FeedReaderContract.FeedEntry.columnDueDate, date_Picked());
//                values.put(FeedReaderContract.FeedEntry.columnDueTime, time_Picked());
//                values.put(FeedReaderContract.FeedEntry.columnCalculatedDate, calculations[0]+calculations[1]+calculations[2]);
//                values.put(FeedReaderContract.FeedEntry.columnCalculatedTime, calculations[3] + calculations[4]);
//                values.put(FeedReaderContract.FeedEntry.columnPriority, priority_Value);
//                values.put(FeedReaderContract.FeedEntry.columnAlarm, alarm_Type.isChecked());
//                values.put(FeedReaderContract.FeedEntry.columnOnTime, on_Time.isChecked());
//                values.put(FeedReaderContract.FeedEntry.columnRepeatNumber, "0");
//                values.put(FeedReaderContract.FeedEntry.columnAlarmSchedulerID, alarmID);


                int calc1 = alarm_DatePicker.getMonth()+1;
                int calc2 = alarm_DatePicker.getDayOfMonth();
                int calc3 = alarm_DatePicker.getYear();
                int calc4 = alarm_TimePicker.getHour();
                int calc5 = alarm_TimePicker.getMinute();

                int alarmID = getAlarmID();
                String RID = createRID();
                ContentValues values = new ContentValues();
                values.put(FeedReaderContract.FeedEntry.RID, RID);
                values.put(FeedReaderContract.FeedEntry.columnDescription, alarm_Description.getText().toString());
                values.put(FeedReaderContract.FeedEntry.columnDueDate, date_Picked());
                values.put(FeedReaderContract.FeedEntry.columnDueTime, time_Picked());
                values.put(FeedReaderContract.FeedEntry.columnCalculatedDate, calc1+calc2+calc3);
                values.put(FeedReaderContract.FeedEntry.columnCalculatedTime, calc4 + calc5);
                values.put(FeedReaderContract.FeedEntry.columnPriority, priority_Value);
                values.put(FeedReaderContract.FeedEntry.columnAlarm, alarm_Type.isChecked());
                values.put(FeedReaderContract.FeedEntry.columnOnTime, on_Time.isChecked());
                values.put(FeedReaderContract.FeedEntry.columnRepeatNumber, "0");
                values.put(FeedReaderContract.FeedEntry.columnAlarmSchedulerID, alarmID);


                SQLiteInsertion(values);
                // JSON jsonObject = new JSON(RID, reminderDescription, datePicked, timePicked, calculations[0] + calculations[1] + calculations[2], calculations[3] + calculations[4],
                //   Integer.toString(priority_Value ), alarm, onTime, 0);
                // String jsonString = jsonObject.JSONString;
                // saveToDrive(jsonString, RID);
                saveToDrive("jsonStringHEre", RID);




            }
        });

    }
    public String createRID(){
        String RID = UUID.randomUUID().toString();
        /*
        cycle through SQL database just to make sure there are no RID conflicts
        no need to check the drive.
         */


        return RID;


    }

    //date formatting
    public String date_Picked () {

        //get int values
        year = alarm_DatePicker.getYear();
        month = alarm_DatePicker.getMonth() + 1;
        day = alarm_DatePicker.getDayOfMonth();

        String month_String = String.valueOf(month);
        String day_String = String.valueOf(day);

        if (month < 10) {
            month_String = "0" + String.valueOf(month);
        }
        if (day < 10) {
            day_String = "0" + String.valueOf(day);
        }

        String date_Format = month_String + day_String + String.valueOf( year );
        return date_Format;
    }

    public String time_Picked () {

        //get the int values
        hour = alarm_TimePicker.getHour();
        minute = alarm_TimePicker.getMinute();
        String hourString = "";
        String minuteString = "";

        /*
        //change from 24hr format to 12hr format
        if (hour > 12)
            hour = Integer.valueOf(hour) - 12;
        if (hour == 0)
            hour = Integer.valueOf(hour) + 12;
            */

        if (hour <= 9){
            hourString = "0" + Integer.toString(hour);

        }
        else
            hourString = Integer.toString(hour);
        if (minute <= 9)
            minuteString = "0" + Integer.toString(minute);
        else
            minuteString = Integer.toString(minute);

        String minute_Format = hourString + minuteString;
        return minute_Format;
    }
    public boolean SQLiteInsertion(ContentValues values){
        FeedReaderDBHelper mDBHelper = new FeedReaderDBHelper(getApplicationContext());
        SQLiteDatabase reminderDB = mDBHelper.getWritableDatabase();

        long newRowID = reminderDB.insert(FeedReaderContract.FeedEntry.tableName, null, values);
        reminderDB.close();





        return true;
    }






    public void saveToDrive(String JSON, String asdf) {
        final String RID = asdf;

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
                try {writer.write("JSON String will go here" + RID);
                    writer.close();} catch (IOException e){ }


                final MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                        .setTitle(RID)
                        .setMimeType("text/plain")
                        .build();
                final DriveContents abc = contents;
                final DriveFolder folder = Drive.DriveApi.getAppFolder(mGoogleApiClient);
                Query query = new Query.Builder()
                        .addFilter(Filters.eq(SearchableField.TITLE, "REMINDERS"))
                        .build();
                folder.queryChildren(mGoogleApiClient, query).setResultCallback(new ResultCallback<DriveApi.MetadataBufferResult>() {
                    @Override
                    public void onResult(@NonNull DriveApi.MetadataBufferResult metadataBufferResult) {
                        DriveId reminderFolderID = metadataBufferResult.getMetadataBuffer().iterator().next().getDriveId();
                        DriveFolder reminderFolder = Drive.DriveApi.getFolder(mGoogleApiClient, reminderFolderID);
                        reminderFolder.removeChangeSubscription(mGoogleApiClient);
                        reminderFolder.createFile(mGoogleApiClient, changeSet, abc);
                    }
                });
                return;
            }
        });
        return;
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
    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i(TAG, "API client connected.");
        Toast.makeText(this, "Connection Success", Toast.LENGTH_LONG).show();
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
    public void connect(){
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Drive.API)
                .addScope(Drive.SCOPE_APPFOLDER)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
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
            }
        });
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
                alarmID +=1;
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