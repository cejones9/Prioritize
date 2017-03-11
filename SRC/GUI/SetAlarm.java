package com.seniorproject.prioritize;

import android.app.AlarmManager;
import android.content.Intent;
import android.graphics.Typeface;
import android.icu.util.Calendar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;

public class SetAlarm extends AppCompatActivity {

    AlarmManager alarm_Manager;
    TimePicker alarm_TimePicker;
    DatePicker alarm_DatePicker;
    EditText alarm_Description;
    SeekBar priority_Bar;
    CheckBox alarm_Type;
    CheckBox on_Time;
    int priority_Value;
    int hour, minute, year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alarm);

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
        priority_Bar = (SeekBar) findViewById(R.id.priorityBar);

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




                priority_Value = priority_Bar.getProgress()/10;

                //passing information from alarm setup activity to reminders activity
                String datePicked = date_Picked();
                String timePicked = time_Picked();
                String alarmText = "Alarm set to: " + "\n" + timePicked + "\n" + datePicked + "\n"
                        + alarm_Description.getText().toString() + "\n Priority Value = " + priority_Value;
                Intent setNewAlarm = new Intent (getApplicationContext(), Reminders.class);
                setNewAlarm.putExtra("alarmDescription", alarmText);
                startActivity(setNewAlarm);
            }
        });

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

        /*
        //change from 24hr format to 12hr format
        if (hour > 12)
            hour = Integer.valueOf(hour) - 12;
        if (hour == 0)
            hour = Integer.valueOf(hour) + 12;
            */

        //check for AM or PM
        String am_pm = "";
        if (alarm_TimePicker.getHour() < 12)
            am_pm = "AM";
        else
            am_pm = "PM";

        //convert int values to strings
        String hour_string = String.valueOf(hour);
        String minute_string = String.valueOf(minute);

        if (hour > 0 && hour < 10)
            hour_string = "0" + String.valueOf(hour);
        else
            hour_string = String.valueOf(hour);
        if (minute > 0 && minute < 10)
            minute_string = "0" + String.valueOf(minute);
        else
            minute_string = String.valueOf(minute);

        String minute_Format = hour_string + minute_string;
        return minute_Format;
    }
}
