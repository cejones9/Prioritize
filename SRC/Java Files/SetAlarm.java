package com.seniorproject.prioritize;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Typeface;
//import android.icu.util.Calendar;
import java.util.Calendar;
import android.os.ParcelFileDescriptor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.EventLogTags;
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

import com.google.android.gms.auth.TokenData;
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
import java.util.Date;
import java.util.Set;
import java.util.UUID;

public class SetAlarm extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    AlarmManager alarm_Manager;
    EditText alarm_Description;
    Spinner repeatDropdown;
    SeekBar priority_Bar;
    TextView priorityPickedText;
    CheckBox alarm_Type;
    CheckBox on_Time;
    int priority_Value;
    int repeatValue;

    String repeat;
    int hour, minute, year, month, day;
    public GoogleApiClient mGoogleApiClient;
    private static final int REQUEST_CODE_CAPTURE_IMAGE = 1;
    private static final int REQUEST_CODE_CREATOR = 2;
    private static final int REQUEST_CODE_RESOLUTION = 3;
    private static final int RESOLVE_CONNECTION_REQUEST_CODE = 4;
    private static final String TAG = "";
    private Bitmap mBitmapToSave;
    int numberOfDates;



    //Bundle extras = getIntent().getExtras();

    //String newYearString, newMonthString, newDayString, newHourString, newMinuteString;
    //int newYear, newMonth, newDay, newHour, newMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alarm);
        connect();
        Intent intent = getIntent();
        numberOfDates = intent.getIntExtra("numberOfDates", 1);
        final String[] date1 = intent.getStringArrayExtra("date1");
        final String[] date2 = intent.getStringArrayExtra("date2");
        final String[] date3 = intent.getStringArrayExtra("date3");
        final String[] date4 = intent.getStringArrayExtra("date4");
        final String[] date5 = intent.getStringArrayExtra("date5");
        final String[] date6 = intent.getStringArrayExtra("date6");
        final String[] date7 = intent.getStringArrayExtra("date7");
        //center align the app title
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_layout);
        //title font
        final Typeface myfont = Typeface.createFromAsset(getAssets(), "Pacifico.ttf");
        TextView app_title = (TextView) findViewById(R.id.mytitle);
        app_title.setTypeface(myfont);
        //initialize alarmmanager
        alarm_Manager = (AlarmManager) getSystemService(ALARM_SERVICE);

        //initialize description text box
        alarm_Description = (EditText) findViewById(R.id.descriptionText);
        //initialize priority text box
        priorityPickedText = (TextView) findViewById(R.id.priorityInfo);
        //initialize priority_Bar
        priority_Bar = (SeekBar) findViewById(R.id.priorityBar);
        priority_Bar.setMax(4);
//        priority_Bar.setMax(5);
//        priority_Value = priority_Bar.getProgress();
//        priorityPickedText.setText(Integer.toString(priority_Value));
        priority_Value = 1;

        priority_Bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int priority_Val, boolean b) {
              //  priority_Bar.setMax(5);
                priority_Value = priority_Val + 1;
               // priority_Bar.setMax(5);
                priorityPickedText.setText(Integer.toString(priority_Value));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                try {

                    String[] preview = priorityAlgorithm.getPriorityDateandTime(Integer.parseInt(date1[1]), Integer.parseInt(date1[2]), Integer.parseInt(date1[0]), Integer.parseInt(date1[3]), Integer.parseInt(date1[4]), priority_Value);
                    String a = preview[3];


                    Toast.makeText(SetAlarm.this, "You will be alerted on: " + feedback(preview), Toast.LENGTH_SHORT).show();
                } catch (Exception e){

                }

            }
        });
        repeatDropdown = (Spinner) findViewById(R.id.repeatDropDown);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.repeat, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        repeatDropdown.setAdapter(adapter2);
        repeatDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Object item = parent.getItemAtPosition(pos);
                String repeatString = String.valueOf(item);
                repeatValue = pos;
                if (numberOfDates > 1){
                    Toast.makeText(SetAlarm.this, "You can't repeat with more than 1 date.", Toast.LENGTH_SHORT).show();
                    repeatDropdown.setSelection(0);
                }

                /*
                use the position to decide what do do
                 */
                //Toast.makeText(SetAlarm.this, Integer.toString(pos), Toast.LENGTH_SHORT).show();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        //
        // .makeText(this, Integer.toString(numberOfDates), Toast.LENGTH_SHORT).show();
        //initialize alarm_type checkbox
        alarm_Type = (CheckBox) findViewById(R.id.alarmCheckBox);
        //initialize on_time checkbox
        on_Time = (CheckBox) findViewById(R.id.onTimeCheckBox);
        //create an instance of a calendar
        final Calendar calendar = Calendar.getInstance();
        //initialize button
        Button save_Alarm = (Button) findViewById(R.id.alarmSave);
        //extras
//        newYearString = extras.getStringExtra("year");
//        newMonthString = extras.getStringExtra("month");
//        newDayString = extras.getStringExtra("day");
//        newHourString = extras.getStringExtra("hour");
//        newMinuteString = extras.getStringExtra("minute");
//
//
//        newYear = Integer.valueOf(newYearString);
//        newMonth = Integer.valueOf(newMonthString);
//        newDay = Integer.valueOf(newDayString);
//        newHour = Integer.valueOf(newHourString);
//        newMinute = Integer.valueOf(newMinuteString);

        //onClick method for saving the alarm
        save_Alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //passing information from alarm setup activity to reminders activity


                String datePicked = date_Picked(date1);
                String timePicked = time_Picked(date1);
                String reminderDescription = alarm_Description.getText().toString();
                int onTime = 0;
                int alarm = 0;
                if (on_Time.isChecked())
                    onTime = 1;
                if (alarm_Type.isChecked()){
                    alarm = 1;
                }
                /*
                STILL NEED TO CHECK DRIVE FOLDER FOR FILES WITH THE SAME UUID.
                CONFLICT PROBABLY WON'T HAPPEN, BUT PREVENTING IT IS NECESSARY.
                 */
                String[] calculations = new String[5];
                if (onTime==0) {
                    calculations = priorityAlgorithm.getPriorityDateandTime(Integer.valueOf(date1[1]), Integer.valueOf(date1[2]),
                            Integer.valueOf(date1[0]), Integer.valueOf(date1[3]), Integer.valueOf(date1[4]), priority_Value);
                }
                else {
                    calculations[0] = date1[1];
                    calculations[1] = date1[2];
                    calculations[2] = date1[0];
                    calculations[3] = date1[3];
                    calculations[4] = date1[4];
                }
                int alarmID = getAlarmID();
                String RID = createRID();
                ContentValues values = new ContentValues();
                values.put(FeedReaderContract.FeedEntry.RID, RID);
                values.put(FeedReaderContract.FeedEntry.columnDescription, reminderDescription);
                values.put(FeedReaderContract.FeedEntry.columnDueDate1, datePicked);
                values.put(FeedReaderContract.FeedEntry.columnDueTime1, timePicked);
                values.put(FeedReaderContract.FeedEntry.columnCalculatedDate, calculations[0]+calculations[1]+calculations[2]);
                values.put(FeedReaderContract.FeedEntry.columnCalculatedTime, calculations[3] + calculations[4]);
                values.put(FeedReaderContract.FeedEntry.columnPriority, priority_Value);
                values.put(FeedReaderContract.FeedEntry.columnAlarm, alarm);
                values.put(FeedReaderContract.FeedEntry.columnOnTime, onTime);
                values.put(FeedReaderContract.FeedEntry.columnRepeatNumber, repeatValue);
                values.put(FeedReaderContract.FeedEntry.columnAlarmSchedulerID, alarmID);
                values.put(FeedReaderContract.FeedEntry.columnDueDate2, date_Picked(date2));
                values.put(FeedReaderContract.FeedEntry.columnDueTime2, time_Picked(date2));
                values.put(FeedReaderContract.FeedEntry.columnDueDate3, date_Picked(date3));
                values.put(FeedReaderContract.FeedEntry.columnDueTime3, time_Picked(date3));
                values.put(FeedReaderContract.FeedEntry.columnDueDate4, date_Picked(date4));
                values.put(FeedReaderContract.FeedEntry.columnDueTime4, time_Picked(date4));
                values.put(FeedReaderContract.FeedEntry.columnDueDate5, date_Picked(date5));
                values.put(FeedReaderContract.FeedEntry.columnDueTime5, time_Picked(date5));
                values.put(FeedReaderContract.FeedEntry.columnDueDate6, date_Picked(date6));
                values.put(FeedReaderContract.FeedEntry.columnDueTime6, time_Picked(date6));
                values.put(FeedReaderContract.FeedEntry.columnDueDate7, date_Picked(date7));
                values.put(FeedReaderContract.FeedEntry.columnDueTime7, time_Picked(date7));
                values.put(FeedReaderContract.FeedEntry.columnNumberOfDates, numberOfDates);
                values.put(FeedReaderContract.FeedEntry.columnCurrentDate, 1);


                Calendar cal = Calendar.getInstance();
                // Toast.makeText(SetAlarm.this, calculations[0], Toast.LENGTH_SHORT).show()
                cal.set(Integer.parseInt(calculations[2]), Integer.parseInt(calculations[0])-1, Integer.parseInt(calculations[1]), Integer.parseInt(calculations[3]), Integer.parseInt(calculations[4]));



                long alertTime = cal.getTimeInMillis();
                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                Intent alarmIntent = new Intent(getApplicationContext(), alarmReceiver.class);
                alarmIntent.putExtra("RID", RID);
                alarmIntent.putExtra("description", reminderDescription);
                alarmIntent.putExtra("alarmID", alarmID);
                alarmIntent.putExtra("alarm", alarm);
                alarmIntent.putExtra("numberOfDates", numberOfDates);
                alarmIntent.putExtra("currentDate", 1);
                alarmIntent.putExtra("repeatNumber", repeatValue);
                alarmIntent.putExtra("onTime", onTime);
                alarmIntent.putExtra("priority", priority_Value);


                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), alarmID, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                AlarmManager alarmMan = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmMan.set(AlarmManager.RTC_WAKEUP, alertTime, pendingIntent);
                SQLiteInsertion(values);
                JSON jsonObject = new JSON(RID, reminderDescription, date_Picked(date1), time_Picked(date1), date_Picked(date2), time_Picked(date2), date_Picked(date3), time_Picked(date3),
                        date_Picked(date4), time_Picked(date4), date_Picked(date5), time_Picked(date5), date_Picked(date6), time_Picked(date6), date_Picked(date7), time_Picked(date7),
                        calculations[0] + calculations[1] + calculations[2], calculations[3] + calculations[4], Integer.toString(priority_Value), alarm, onTime, repeatValue, numberOfDates, 1);
               String jsonString = jsonObject.JSONString;
                  saveToDrive(jsonString, RID);
              //  JSON asdf = JSON.convert_from_string_to_Object(jsonString);
              //  JSON asdf = JSON.convert_from_string_to_Object(jsonString);
               // Toast.makeText(SetAlarm.this, asdf.get_Due_Date6(), Toast.LENGTH_SHORT).show();
                // Toast.makeText(SetAlarm.this, jsonString, Toast.LENGTH_SHORT).show();
                //saveToDrive("jsonStringHEre", RID);
                //Toast.makeText(SetAlarm.this, asdf.C_Remind_time, Toast.LENGTH_SHORT).show();
                String alarmText = "Alert set for " + feedback(calculations);
                Intent setNewAlarm = new Intent (getApplicationContext(), Reminders.class);
                 Toast.makeText(SetAlarm.this, alarmText, Toast.LENGTH_SHORT).show();
                startActivity(setNewAlarm);
                finish();
            }
        });
    }
    public String feedback(String[] cals){
        int hour = Integer.parseInt(cals[3]);
        if (hour > 12)
            hour -= 12;
        if (hour == 0)
            hour +=1;
        return cals[0] + "-" + cals[1] + "-"+ cals[2] + " at, " + Integer.toString(hour) + ":" + cals[4];
    }
    public String date_Picked (String[] date) {
        String yearmonthday = date[0] + date[1] + date[2];
        return yearmonthday;
    }

    public String time_Picked (String[] date) {
        String hourminute = date[3] + date[4];
        return hourminute;
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
    public boolean SQLiteInsertion(ContentValues values){
        FeedReaderDBHelper mDBHelper = new FeedReaderDBHelper(getApplicationContext());
        SQLiteDatabase reminderDB = mDBHelper.getWritableDatabase();
        long newRowID = reminderDB.insert(FeedReaderContract.FeedEntry.tableName, null, values);
        reminderDB.close();
        // Toast.makeText(this, "asdfasdf", Toast.LENGTH_SHORT).show()
        return true;
    }
    public void saveToDrive(final String JSON, String ridArg) {
        final String RID = ridArg;
        Drive.DriveApi.newDriveContents(mGoogleApiClient).setResultCallback(new ResultCallback<DriveApi.DriveContentsResult>() {
            @Override
            public void onResult(@NonNull DriveApi.DriveContentsResult result) {
                if (!result.getStatus().isSuccess()) {
                    // Toast.makeText(getApplicationContext(), "Could not Create DriveContents", Toast.LENGTH_SHORT).show();
                    // Handle error
                    return;
                }
                DriveContents contents = result.getDriveContents();
                ParcelFileDescriptor parcelFileDescriptor = contents.getParcelFileDescriptor();
                FileOutputStream fileOutputStream = new FileOutputStream(parcelFileDescriptor.getFileDescriptor());
                Writer writer = new OutputStreamWriter(fileOutputStream);
                try {writer.write(JSON + RID);
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
                        reminderFolder.addChangeSubscription(mGoogleApiClient);
                    }
                });
            }
        });
    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this, RESOLVE_CONNECTION_REQUEST_CODE);
            } catch (IntentSender.SendIntentException e) {
                // Toast.makeText(this, "Connection Falure", Toast.LENGTH_SHORT).show();
                // Unable to resolve, message user appropriately
            }
        } else {
            GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), this, 0).show();
        }
    }
    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i(TAG, "API client connected.");
        // Toast.makeText(this, "Connection Success", Toast.LENGTH_LONG).show();
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
                    //  Toast.makeText(getApplicationContext(), "Reminder Folder Not Here", Toast.LENGTH_LONG).show();
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

