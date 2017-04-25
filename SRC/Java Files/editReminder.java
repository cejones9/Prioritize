package com.seniorproject.prioritize;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.ParcelFileDescriptor;
import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


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
import com.google.android.gms.drive.query.SortOrder;
import com.google.android.gms.drive.query.SortableField;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public class editReminder extends AppCompatActivity {
    private GoogleApiClient mGoogleApiClient;
    ListView timesList;
    ArrayList<String> arrList;
    ArrayAdapter<String> arrAdapter;
    EditText reminderDescription;
    SeekBar priority_Bar;
    TextView priorityPickedText;
    int priority_Value;
    String date1 ="";
    String time1="";
    String date2="";
    String time2="";
    String date3="";
    String time3="";
    String date4="";
    String time4="";
    String date5="";
    String time5="";
    String date6="";
    String time6="";
    String date7="";
    String time7="";
    String[] alertDateandTime;
    int[] alertDate;
    int[] alertTime;
    int alarmId;
    int number;
    int alarm;
    int ontime;
    String rid = "";
    CheckBox alarm_Type;
    CheckBox on_Time;
    int currentDate;
    Button save;
    Spinner repeatDropdown;
    int repeatValue;
    String[] calc = new String[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Drive.API)
                .addScope(Drive.SCOPE_APPFOLDER)
                .build();
        mGoogleApiClient.connect();
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_layout);
        Typeface myfont = Typeface.createFromAsset(getAssets(), "Pacifico.ttf");
        TextView app_title = (TextView) findViewById(R.id.mytitle);
        app_title.setTypeface(myfont);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_reminder);
        alarm_Type = (CheckBox) findViewById(R.id.alarmCheckBox);
        on_Time = (CheckBox) findViewById(R.id.onTimeCheckBox);
        save = (Button) findViewById(R.id.alarmSave);
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
                /*
                use the position to decide what do do
                 */
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        FeedReaderDBHelper mDBHelper = new FeedReaderDBHelper(getApplicationContext());
        SQLiteDatabase reminderDB = mDBHelper.getReadableDatabase();
        Intent intent = getIntent();
        rid = intent.getStringExtra("rid");
        Cursor c = reminderDB.rawQuery("Select * FROM Reminder WHERE RID = '" + rid + "'", null);
        c.moveToFirst();
       // Toast.makeText(this, c.getString(c.getColumnIndex("DueTime1")), Toast.LENGTH_SHORT).show();
        alarmId = c.getInt(c.getColumnIndex("ALarmSchedulerID"));
        repeatDropdown.setSelection(c.getInt(c.getColumnIndex("RepeatEveryxDays")));
        currentDate = c.getInt(c.getColumnIndex("CurrentDate"));
        reminderDescription = (EditText) findViewById(R.id.descriptionText);
        reminderDescription.setText(c.getString(c.getColumnIndex("Description")));
        priority_Value = c.getInt(c.getColumnIndex("Priority"));
        priorityPickedText = (TextView) findViewById(R.id.priorityInfo);
        //initialize priority_Bar
        priority_Bar = (SeekBar) findViewById(R.id.priorityBar);
        priority_Bar.setMax(4);
        priority_Bar.setProgress(priority_Value-1);
        priorityPickedText.setText(Integer.toString(priority_Value));
        priority_Bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int priority_Val, boolean b) {
                //  priority_Bar.setMax(5);
                priority_Value = priority_Val +1;
                // priority_Bar.setMax(5);
                priorityPickedText.setText(Integer.toString(priority_Value));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //String[] preview = priorityAlgorithm.getPriorityDateandTime(Integer.parseInt(date1[1]), Integer.parseInt(date1[2]), Integer.parseInt(date1[0]), Integer.parseInt(date1[3]), Integer.parseInt(date1[4]), priority_Value);
               // Toast.makeText(editReminder.this, "You will be alerted on: " + preview[0] + preview[1] + preview[2] + ", at " + preview[3] + preview[4], Toast.LENGTH_SHORT).show();
            }
        });

        if (c.getInt(c.getColumnIndex("OnTime")) == 1){
            on_Time.setChecked(true);

        }
        if (c.getInt(c.getColumnIndex("Alarm")) == 1){
            alarm_Type.setChecked(true);
        }
        Toast.makeText(this, c.getString(c.getColumnIndex("CalculatedDate")), Toast.LENGTH_SHORT).show();

        String dateAndTime = "";
        timesList = (ListView) findViewById(R.id.reminderDates);
        arrList = new ArrayList<String>();
        final int numberOfDates = c.getInt(c.getColumnIndex("NumberofDates"));
        final int currentDate = c.getInt(c.getColumnIndex("CurrentDate"));
        int count = 1;
        while(count <= numberOfDates){
            arrAdapter = new ArrayAdapter<String>(editReminder.this, android.R.layout.simple_selectable_list_item, arrList);
          //  dateAndTime = "asdf";
            dateAndTime = "" + dueDate(c.getString(c.getColumnIndex("DueDate" + Integer.toString(count))), count) +
            dueTime(c.getString(c.getColumnIndex("DueTime" + Integer.toString(count))), count);
            arrList.add(dateAndTime);
            timesList.setAdapter(arrAdapter);
            arrAdapter.notifyDataSetChanged();
            count++;
        }
      // Toast.makeText(this, time1, Toast.LENGTH_SHORT).show();
        final Intent editDate = new Intent(this, dateAndTime.class);
        editDate.putExtra("rid", rid);
        timesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                if (position == 0){
                    editDate.putExtra("date", date1);
                    editDate.putExtra("time", time1);
                    editDate.putExtra("number", 1);
                    if (currentDate <= 1)
                        startActivity(editDate);
                }
                if (position == 1){
                    editDate.putExtra("date", date2);
                    editDate.putExtra("time", time2);
                    editDate.putExtra("number", 2);
                    if (currentDate <= 2)
                        startActivity(editDate);
                }
                if (position == 2){
                    editDate.putExtra("date", date3);
                    editDate.putExtra("time", time3);
                    editDate.putExtra("number", 3);
                    if (currentDate <= 3)
                        startActivity(editDate);
                }
                if (position == 3){
                    editDate.putExtra("date", date4);
                    editDate.putExtra("time", time4);
                    editDate.putExtra("number", 4);
                    if (currentDate <= 4)
                        startActivity(editDate);
                }
                if (position == 4){
                    editDate.putExtra("date", date5);
                    editDate.putExtra("time", time5);
                    editDate.putExtra("number", 5);
                    if (currentDate <= 5)
                        startActivity(editDate);
                }
                if (position == 5){
                    editDate.putExtra("date", date6);
                    editDate.putExtra("time", time6);
                    editDate.putExtra("number", 6);
                    if (currentDate <= 6)
                        startActivity(editDate);
                }
                if (position == 6){
                    editDate.putExtra("date", date7);
                    editDate.putExtra("time", time7);
                    editDate.putExtra("number", 7);
                    if (currentDate <= 8)
                        startActivity(editDate);
                }
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] dateAndTime;
                FeedReaderDBHelper mDBHelper = new FeedReaderDBHelper(getApplicationContext());
                SQLiteDatabase reminderDB = mDBHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(FeedReaderContract.FeedEntry.columnDescription, reminderDescription.getText().toString());
                if (on_Time.isChecked()) {
                    ontime =1;
                    values.put(FeedReaderContract.FeedEntry.columnOnTime, 1);
                    if (currentDate == 1) {
                        values.put(FeedReaderContract.FeedEntry.columnCalculatedDate, dueToCalc(date1));
                        values.put(FeedReaderContract.FeedEntry.columnCalculatedTime, time1);
                        alertDate = getDueDate(date1);
                        alertTime = getTime(time1);
                    }
                    if (currentDate == 2) {
                        values.put(FeedReaderContract.FeedEntry.columnCalculatedDate, dueToCalc(date2));
                        values.put(FeedReaderContract.FeedEntry.columnCalculatedTime, time2);
                        alertDate = getDueDate(date2);
                        alertTime = getTime(time2);
                    }
                    if (currentDate == 3) {
                        values.put(FeedReaderContract.FeedEntry.columnCalculatedDate, dueToCalc(date3));
                        values.put(FeedReaderContract.FeedEntry.columnCalculatedTime, time3);
                        alertDate = getDueDate(date3);
                        alertTime = getTime(time3);
                    }
                    if (currentDate == 4) {
                        values.put(FeedReaderContract.FeedEntry.columnCalculatedDate, dueToCalc(date4));
                        values.put(FeedReaderContract.FeedEntry.columnCalculatedTime, time4);
                        alertDate = getDueDate(date4);
                        alertTime = getTime(time4);
                    }
                    if (currentDate == 5) {
                        values.put(FeedReaderContract.FeedEntry.columnCalculatedDate, dueToCalc(date5));
                        values.put(FeedReaderContract.FeedEntry.columnCalculatedTime, time5);
                        alertDate = getDueDate(date5);
                        alertTime = getTime(time5);
                    }
                    if (currentDate == 6) {
                        values.put(FeedReaderContract.FeedEntry.columnCalculatedDate, dueToCalc(date6));
                        values.put(FeedReaderContract.FeedEntry.columnCalculatedTime, time6);
                        alertDate = getDueDate(date6);
                        alertTime = getTime(time6);
                    }
                    if (currentDate == 7) {
                        values.put(FeedReaderContract.FeedEntry.columnCalculatedDate, dueToCalc(date7));
                        values.put(FeedReaderContract.FeedEntry.columnCalculatedTime, time7);
                        alertDate = getDueDate(date7);
                        alertTime = getTime(time7);
                    }
                }
                else {
                    ontime = 0;
                    values.put(FeedReaderContract.FeedEntry.columnOnTime, 0);
                    if (currentDate == 1){
                        alertDate = getDueDate(date1);
                        alertTime = getTime(time1);
                        calc = priorityAlgorithm.getPriorityDateandTime(alertDate[1], alertDate[2], alertDate[0], alertTime[0], alertTime[1], priority_Value);
                        alertDate[0] = Integer.parseInt(calc[2]);
                        alertDate[1] = Integer.parseInt(calc[0]);
                        alertDate[2] = Integer.parseInt(calc[1]);
                        alertTime[0] = Integer.parseInt(calc[3]);
                        alertTime[1] = Integer.parseInt(calc[4]);
                        values.put(FeedReaderContract.FeedEntry.columnCalculatedDate, calc[0] + calc[1] + calc[2]);
                        values.put(FeedReaderContract.FeedEntry.columnCalculatedTime, calc[3] + calc[4]);
                    }
                    if (currentDate == 2){
                        alertDate = getDueDate(date2);
                        alertTime = getTime(time2);
                        calc = priorityAlgorithm.getPriorityDateandTime(alertDate[1], alertDate[2], alertDate[0], alertTime[0], alertTime[1], priority_Value);
                        alertDate[0] = Integer.parseInt(calc[2]);
                        alertDate[1] = Integer.parseInt(calc[0]);
                        alertDate[2] = Integer.parseInt(calc[1]);
                        alertTime[0] = Integer.parseInt(calc[3]);
                        alertTime[1] = Integer.parseInt(calc[4]);
                        values.put(FeedReaderContract.FeedEntry.columnCalculatedDate, calc[0] + calc[1] + calc[2]);
                        values.put(FeedReaderContract.FeedEntry.columnCalculatedTime, calc[3] + calc[4]);
                    }
                    if (currentDate == 3){
                        alertDate = getDueDate(date3);
                        alertTime = getTime(time3);
                        calc = priorityAlgorithm.getPriorityDateandTime(alertDate[1], alertDate[2], alertDate[0], alertTime[0], alertTime[1], priority_Value);
                        alertDate[0] = Integer.parseInt(calc[2]);
                        alertDate[1] = Integer.parseInt(calc[0]);
                        alertDate[2] = Integer.parseInt(calc[1]);
                        alertTime[0] = Integer.parseInt(calc[3]);
                        alertTime[1] = Integer.parseInt(calc[4]);
                        values.put(FeedReaderContract.FeedEntry.columnCalculatedDate, calc[0] + calc[1] + calc[2]);
                        values.put(FeedReaderContract.FeedEntry.columnCalculatedTime, calc[3] + calc[4]);
                    }
                    if (currentDate == 4){
                        alertDate = getDueDate(date4);
                        alertTime = getTime(time4);
                        calc = priorityAlgorithm.getPriorityDateandTime(alertDate[1], alertDate[2], alertDate[0], alertTime[0], alertTime[1], priority_Value);
                        alertDate[0] = Integer.parseInt(calc[2]);
                        alertDate[1] = Integer.parseInt(calc[0]);
                        alertDate[2] = Integer.parseInt(calc[1]);
                        alertTime[0] = Integer.parseInt(calc[3]);
                        alertTime[1] = Integer.parseInt(calc[4]);
                        values.put(FeedReaderContract.FeedEntry.columnCalculatedDate, calc[0] + calc[1] + calc[2]);
                        values.put(FeedReaderContract.FeedEntry.columnCalculatedTime, calc[3] + calc[4]);
                    }
                    if (currentDate == 5){
                        alertDate = getDueDate(date5);
                        alertTime = getTime(time5);
                        calc = priorityAlgorithm.getPriorityDateandTime(alertDate[1], alertDate[2], alertDate[0], alertTime[0], alertTime[1], priority_Value);
                        alertDate[0] = Integer.parseInt(calc[2]);
                        alertDate[1] = Integer.parseInt(calc[0]);
                        alertDate[2] = Integer.parseInt(calc[1]);
                        alertTime[0] = Integer.parseInt(calc[3]);
                        alertTime[1] = Integer.parseInt(calc[4]);
                        values.put(FeedReaderContract.FeedEntry.columnCalculatedDate, calc[0] + calc[1] + calc[2]);
                        values.put(FeedReaderContract.FeedEntry.columnCalculatedTime, calc[3] + calc[4]);
                    }
                    if (currentDate == 6){
                        alertDate = getDueDate(date6);
                        alertTime = getTime(time6);
                        calc = priorityAlgorithm.getPriorityDateandTime(alertDate[1], alertDate[2], alertDate[0], alertTime[0], alertTime[1], priority_Value);
                        alertDate[0] = Integer.parseInt(calc[2]);
                        alertDate[1] = Integer.parseInt(calc[0]);
                        alertDate[2] = Integer.parseInt(calc[1]);
                        alertTime[0] = Integer.parseInt(calc[3]);
                        alertTime[1] = Integer.parseInt(calc[4]);
                        values.put(FeedReaderContract.FeedEntry.columnCalculatedDate, calc[0] + calc[1] + calc[2]);
                        values.put(FeedReaderContract.FeedEntry.columnCalculatedTime, calc[3] + calc[4]);
                    }
                    if (currentDate == 7){
                        alertDate = getDueDate(date7);
                        alertTime = getTime(time7);
                        calc = priorityAlgorithm.getPriorityDateandTime(alertDate[1], alertDate[2], alertDate[0], alertTime[0], alertTime[1], priority_Value);
                        alertDate[0] = Integer.parseInt(calc[2]);
                        alertDate[1] = Integer.parseInt(calc[0]);
                        alertDate[2] = Integer.parseInt(calc[1]);
                        alertTime[0] = Integer.parseInt(calc[3]);
                        alertTime[1] = Integer.parseInt(calc[4]);
                        values.put(FeedReaderContract.FeedEntry.columnCalculatedDate, calc[0] + calc[1] + calc[2]);
                        values.put(FeedReaderContract.FeedEntry.columnCalculatedTime, calc[3] + calc[4]);
                    }
                }
                if (alarm_Type.isChecked()){
                    values.put(FeedReaderContract.FeedEntry.columnAlarm, 1);
                    alarm=1;
                }
                else {
                    values.put(FeedReaderContract.FeedEntry.columnAlarm, 0);
                    alarm = 0;
                }
                values.put(FeedReaderContract.FeedEntry.columnRepeatNumber, repeatValue);
                values.put(FeedReaderContract.FeedEntry.columnCurrentDate, currentDate);

                //values.put(FeedReaderContract.FeedEntry.RID, rid);
                values.put(FeedReaderContract.FeedEntry.columnPriority, priority_Value);

                reminderDB.update("Reminder", values, "RID = '" + rid + "'", null );
                reminderDB.close();
                Calendar cal = Calendar.getInstance();
                dateAndTime = getAlertStrings(alertDate, alertTime);
               // Toast.makeText(editReminder.this, Integer.toString(alertTime[1]), Toast.LENGTH_SHORT).show();
                cal.set(alertDate[0], alertDate[1] -1, alertDate[2], alertTime[0], alertTime[1]);

                long alertTime = cal.getTimeInMillis();
                Intent alarmIntent = new Intent(getApplicationContext(), alarmReceiver.class);
                alarmIntent.putExtra("RID", rid);
                alarmIntent.putExtra("description", reminderDescription.getText().toString());
                alarmIntent.putExtra("alarmID", alarmId);
                alarmIntent.putExtra("alarm", alarm);
                alarmIntent.putExtra("numberOfDates", numberOfDates);
                alarmIntent.putExtra("currentDate", currentDate);
                alarmIntent.putExtra("repeatNumber", repeatValue);
                alarmIntent.putExtra("onTime", ontime);
                alarmIntent.putExtra("priority", priority_Value);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), alarmId, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                AlarmManager alarmMan = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmMan.set(AlarmManager.RTC_WAKEUP, alertTime, pendingIntent);

                JSON jsonObject = new JSON(rid, reminderDescription.getText().toString(), date1, time1, date2, time2, date3, time3,
                        date4, time4, date5, time5, date6, time6, date7, time7,
                        dateAndTime[0], dateAndTime[1], Integer.toString(priority_Value), alarm, ontime, repeatValue, numberOfDates, currentDate);
                String jsonString = jsonObject.JSONString;
                Toast.makeText(editReminder.this, jsonObject.Due_Date, Toast.LENGTH_SHORT).show();
                saveToDrive(jsonString, rid);

                Intent setNewAlarm = new Intent (getApplicationContext(), Reminders.class);
                Intent goBack = new Intent(editReminder.this, Reminders.class);
                startActivity(goBack);
                finish();
                //passing information from alarm setup activity to reminders activity
            }
        });
    }
    public String[] getAlertStrings(int[] date, int[] time){
        String year = Integer.toString(date[0]);
        String month;
        String day;
        String hour;
        String minute;
        if (date[1] < 10){
            month = "0" + Integer.toString(date[1]);
        }
        else
            month = Integer.toString(date[1]);
        if (date[2] < 10){
            day = "0" + Integer.toString(date[2]);
        }
        else
            day = Integer.toString(date[2]);
        if (time[0] < 10){
            hour = "0" + Integer.toString(time[0]);
        }
        else
            hour = Integer.toString(time[0]);
        if (time[1] < 10){
            minute = "0" + Integer.toString(time[1]);
        }
        else
            minute = Integer.toString(time[1]);
        String[] dateandtime = new String[2];
        dateandtime[0] = "" + year + month + day;
        dateandtime[1] = "" + hour + minute;
        return dateandtime;


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

                                          //  Toast.makeText(editReminder.this, "asdf", Toast.LENGTH_SHORT).show();

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

    public String dueToCalc(String s){
        return "" + s.charAt(4) + s.charAt(5) + s.charAt(6) + s.charAt(7) + s.charAt(0) + s.charAt(1) + s.charAt(2) + s.charAt(3);
    }

    public int[] getDueDate(String s){
        int[] yearmonthday = new int[3];
        String year = "" + s.charAt(0) + s.charAt(1) + s.charAt(2) + s.charAt(3);
        String month = "" + s.charAt(4) + s.charAt(5);
        String day = "" + s.charAt(6) + s.charAt(7);
        yearmonthday[0] = Integer.parseInt(year);
        yearmonthday[1] = Integer.parseInt(month);
        yearmonthday[2] = Integer.parseInt(day);
        return yearmonthday;
    }
    public int[] getTime (String s){
        int[] hourminute = new int[2];
        String hour = "" + s.charAt(0) + s.charAt(1);
        String min = "" + s.charAt(2) + s.charAt(3);
        hourminute[0] = Integer.parseInt(hour);
        hourminute[1] = Integer.parseInt(min);
        return hourminute;
    }
    public String dueDate(String s, int count){String a = "";
        a = "" + s.charAt(4) + s.charAt(5) + "-" + s.charAt(6) + s.charAt(7) + "-"
                + s.charAt(0) + s.charAt(1) + s.charAt(2) + s.charAt(3);
        String b = "" + s.charAt(4) + s.charAt(5) + s.charAt(6) + s.charAt(7)
                + s.charAt(0) + s.charAt(1) + s.charAt(2) + s.charAt(3);
        if (count == 1){
            date1 += s;
        }
        if (count == 2){
            date2 += s;
        }
        if (count == 3){
            date3 += s;
        }
        if (count == 4){
            date4 += s;

        }
        if (count == 5){
            date5 += s;
        }
        if (count == 6){
            date6 += s;
        }
        if (count == 7){
            date7 += s;
        }
        return  a;
    }
    public String dueTime(String s, int count){
        String hour = "" + s.charAt(0) + s.charAt(1);
        int hourint = Integer.parseInt(hour);
        if (hourint > 12)
            hourint -= 12;
        if (hourint ==0)
            hourint +=1;
        if (count == 1){
            time1 += s;
        }
        if (count == 2){
            time2 += s;
        }
        if (count == 3){
            time3 +=s;
        }
        if (count == 4){
            time4 += s;
        }
        if (count == 5){
            time5 += s;
        }
        if (count == 6){
            time6 += s;
        }if (count == 7){
            time7 += s;
        }

        return ", at " + Integer.toString(hourint) + ":" + s.charAt(2) + s.charAt(3);

    }
}
