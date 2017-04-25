package com.seniorproject.prioritize;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class dateAndTime extends AppCompatActivity {
    String date = "";
    String time = "";
    int number = 0;
    DatePicker datePicker;
    TimePicker timePicker;
    String RID;
    int[] datearray;
    int[] timearray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_layout);
        Typeface myfont = Typeface.createFromAsset(getAssets(), "Pacifico.ttf");
        TextView app_title = (TextView) findViewById(R.id.mytitle);
        app_title.setTypeface(myfont);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_and_time);
        Intent intent = getIntent();
        date = intent.getStringExtra("date");
        time = intent.getStringExtra("time");
        number = intent.getIntExtra("number", 1);
        RID = intent.getStringExtra("rid");


        timePicker = (TimePicker) findViewById(R.id.editTime);
        datePicker = (DatePicker) findViewById(R.id.editDate);
        Calendar now = Calendar.getInstance();
        long nowMili = now.getTimeInMillis();
        if (number == 1) {
            datePicker.setMinDate(nowMili);
        }
        if (number == 2) {
            datePicker.setMinDate(nowMili + 86400000);
        }
        if (number == 3) {
            datePicker.setMinDate(nowMili + 172800000);
        }
        if (number == 4) {
            datePicker.setMinDate(nowMili + 259200000);
        }
        if (number == 5) {
            datePicker.setMinDate(nowMili + 345600000);
        }
        if (number == 6) {
            datePicker.setMinDate(nowMili + 432000000);
        }
        if (number == 7) {
            datePicker.setMinDate(nowMili + 518400000);
        }



        Button button = (Button) findViewById(R.id.alarmSave);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String DueDate = dateString(datePicker.getYear(), datePicker.getMonth() +1, datePicker.getDayOfMonth());
                String DueTime = timeString(timePicker.getHour(), timePicker.getMinute());
                FeedReaderDBHelper mDBHelper = new FeedReaderDBHelper(getApplicationContext());
                SQLiteDatabase reminderDB = mDBHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                if (number == 1){
                    values.put(FeedReaderContract.FeedEntry.columnDueDate1, DueDate);
                    values.put(FeedReaderContract.FeedEntry.columnDueTime1, DueTime);
                }
                if (number == 2){
                    values.put(FeedReaderContract.FeedEntry.columnDueDate2, DueDate);
                    values.put(FeedReaderContract.FeedEntry.columnDueTime2, DueTime);
                }
                if (number == 3){
                    values.put(FeedReaderContract.FeedEntry.columnDueDate3, DueDate);
                    values.put(FeedReaderContract.FeedEntry.columnDueTime3, DueTime);
                }
                if (number == 4){
                    values.put(FeedReaderContract.FeedEntry.columnDueDate4, DueDate);
                    values.put(FeedReaderContract.FeedEntry.columnDueTime4, DueTime);
                }
                if (number == 5){
                    values.put(FeedReaderContract.FeedEntry.columnDueDate5, DueDate);
                    values.put(FeedReaderContract.FeedEntry.columnDueTime5, DueTime);
                }
                if (number == 6){
                    values.put(FeedReaderContract.FeedEntry.columnDueDate6, DueDate);
                    values.put(FeedReaderContract.FeedEntry.columnDueTime6, DueTime);
                }
                if (number == 7){
                    values.put(FeedReaderContract.FeedEntry.columnDueDate7, DueDate);
                    values.put(FeedReaderContract.FeedEntry.columnDueTime7, DueTime);
                }
                reminderDB.update("Reminder", values, "RID = '" + RID+ "'", null );
                reminderDB.close();
                Intent goBack = new Intent(dateAndTime.this, editReminder.class);
                goBack.putExtra("rid", RID);
                startActivity(goBack);
                finish();



                //passing information from alarm setup activity to reminders activity


            }
        });
        //datearray = getDueDate(date);
      //  Toast.makeText(this, date, Toast.LENGTH_SHORT).show();
      //  timearray = getTime(time);
      //  datePicker.updateDate(datearray[0], datearray[1] -1, datearray[2]);

        /*


        ON CLICK MUST SEND RID



         */



    }
    public String dateString(int year, int month, int day){
        String years = Integer.toString(year);
        String months = "";
        String days = "";
        if (month < 10)
            months = "0" + Integer.toString(month);
        else
            months = Integer.toString(month);
        if (day < 10) {
            days = "0" + Integer.toString(day);
        }
        else
            days = Integer.toString(day);
        return years + months + days;


    }
    public String timeString(int hour, int minute){
        String hours = "";
        String minutes = "";
        if (minute < 10)
            minutes = "0" + Integer.toString(minute);
        else
            minutes = Integer.toString(minute);
        if (hour < 10)
            hours = "0" + Integer.toString(hour);
        else
            hours = Integer.toString(hour);
        return hours + minutes;
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
}
