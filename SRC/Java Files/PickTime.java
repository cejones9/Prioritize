package com.seniorproject.prioritize;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class PickTime extends AppCompatActivity {

    TimePicker alarm_TimePicker1,alarm_TimePicker2,alarm_TimePicker3,
            alarm_TimePicker4, alarm_TimePicker5,alarm_TimePicker6,alarm_TimePicker7;
    TextView textView1, textView2, textView3, textView4, textView5, textView6, textView7;
    Button addTimePicker;
    Button removeTimePicker;
    int numberOfDates;
  //  int numberOfTime = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_time);
        /*
        
         */

        //center align the app title
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_layout);
        //title font7
        Typeface myfont = Typeface.createFromAsset(getAssets(), "Pacifico.ttf");
        TextView app_title = (TextView) findViewById(R.id.mytitle);
        app_title.setTypeface(myfont);
        //for alarm info
        Intent intent = getIntent();
        numberOfDates = intent.getIntExtra("numberOfDates", 1);
        final String[] date1 = intent.getStringArrayExtra("date1");
        final String[] date2 = intent.getStringArrayExtra("date2");
        final String[] date3 = intent.getStringArrayExtra("date3");
        final String[] date4 = intent.getStringArrayExtra("date4");
        final String[] date5 = intent.getStringArrayExtra("date5");
        final String[] date6 = intent.getStringArrayExtra("date6");
        final String[] date7 = intent.getStringArrayExtra("date7");
        //initialize timepickers
        alarm_TimePicker1 = (TimePicker) findViewById(R.id.timePicker1);

        alarm_TimePicker2 = (TimePicker) findViewById(R.id.timePicker2);
        alarm_TimePicker3 = (TimePicker) findViewById(R.id.timePicker3);
        alarm_TimePicker4 = (TimePicker) findViewById(R.id.timePicker4);
        alarm_TimePicker5 = (TimePicker) findViewById(R.id.timePicker5);
        alarm_TimePicker6 = (TimePicker) findViewById(R.id.timePicker6);
        alarm_TimePicker7 = (TimePicker) findViewById(R.id.timePicker7);
        textView1 = (TextView) findViewById(R.id.date1text);
        textView2 = (TextView) findViewById(R.id.date2text);
        textView3 = (TextView) findViewById(R.id.date3text);
        textView4 = (TextView) findViewById(R.id.date4text);
        textView5 = (TextView) findViewById(R.id.date5text);
        textView6 = (TextView) findViewById(R.id.date6text);
        textView7 = (TextView) findViewById(R.id.date7text);


        Button nextScreen = (Button) findViewById(R.id.thirdScreen);

        nextScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar now = Calendar.getInstance();
                Calendar deadline1 = Calendar.getInstance();
                deadline1.set(Integer.parseInt(date1[0]), Integer.parseInt(date1[1])-1, Integer.parseInt(date1[2]), alarm_TimePicker1.getHour(), alarm_TimePicker1.getMinute());
                if (deadline1.compareTo(now)< 0) {
                    Toast.makeText(PickTime.this, "You cannot set an alert for the past.", Toast.LENGTH_SHORT).show();


                }
                else {

                    Intent thirdScreenIntent = new Intent(PickTime.this, SetAlarm.class);
                    date1[3] = goToString(alarm_TimePicker1.getHour());
                    date1[4] = goToString(alarm_TimePicker1.getMinute());
                    date2[3] = goToString(alarm_TimePicker2.getHour());
                    date2[4] = goToString(alarm_TimePicker2.getMinute());
                    date3[3] = goToString(alarm_TimePicker3.getHour());
                    date3[4] = goToString(alarm_TimePicker3.getMinute());
                    date4[3] = goToString(alarm_TimePicker4.getHour());
                    date4[4] = goToString(alarm_TimePicker4.getMinute());
                    date5[3] = goToString(alarm_TimePicker5.getHour());
                    date5[4] = goToString(alarm_TimePicker5.getMinute());
                    date6[3] = goToString(alarm_TimePicker6.getHour());
                    date6[4] = goToString(alarm_TimePicker6.getMinute());
                    date7[3] = goToString(alarm_TimePicker7.getHour());
                    date7[4] = goToString(alarm_TimePicker7.getMinute());
                    thirdScreenIntent.putExtra("date1", date1);
                    thirdScreenIntent.putExtra("date2", date2);
                    thirdScreenIntent.putExtra("date3", date3);
                    thirdScreenIntent.putExtra("date4", date4);
                    thirdScreenIntent.putExtra("date5", date5);
                    thirdScreenIntent.putExtra("date6", date6);
                    thirdScreenIntent.putExtra("date7", date7);
                    thirdScreenIntent.putExtra("numberOfDates", numberOfDates);
                    startActivity(thirdScreenIntent);
                }


            }
        });

        addTimePicker = (Button) findViewById(R.id.newTimePicker);
        if (numberOfDates > 1){
            addTimePicker.setVisibility(View.VISIBLE);
        }
        addTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                textView1.setText("At what time for: " + date1[1] + "-" + date1[2] + "-" +date1[0]);
                textView2.append(date2[1] + "-" + date2[2] + "-" + date2[0]);
                textView3.append(date3[1] + "-" + date3[2] + "-" + date3[0]);
                textView4.append(date4[1] + "-" + date4[2] + "-" + date4[0]);
                textView5.append(date5[1] + "-" + date5[2] + "-" + date5[0]);
                textView6.append(date6[1] + "-" + date6[2] + "-" + date6[0]);
                textView7.append(date7[1] + "-" + date7[2] + "-" + date7[0]);
                if (numberOfDates > 1){
                    alarm_TimePicker2.setVisibility(View.VISIBLE);
                    textView2.setVisibility(View.VISIBLE);
                }
                if (numberOfDates > 2){
                    alarm_TimePicker3.setVisibility(View.VISIBLE);
                    textView3.setVisibility(View.VISIBLE);
                }
                if (numberOfDates > 3){
                    alarm_TimePicker4.setVisibility(View.VISIBLE);
                    textView4.setVisibility(View.VISIBLE);
                }
                if (numberOfDates > 4){
                    alarm_TimePicker5.setVisibility(View.VISIBLE);
                    textView5.setVisibility(View.VISIBLE);
                }
                if (numberOfDates > 5){
                    alarm_TimePicker6.setVisibility(View.VISIBLE);
                    textView6.setVisibility(View.VISIBLE);
                }
                if (numberOfDates > 6){
                    alarm_TimePicker7.setVisibility(View.VISIBLE);
                    textView7.setVisibility(View.VISIBLE);
                }
                addTimePicker.setVisibility(View.INVISIBLE);
                removeTimePicker.setVisibility(View.VISIBLE);
            }
        });
        removeTimePicker = (Button) findViewById(R.id.removeTimePicker);
        removeTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                textView1.setText("At what time?");
                textView2.setText("At what time for: ");
                textView3.setText("At what time for: ");
                textView4.setText("At what time for: ");
                textView5.setText("At what time for: ");
                textView6.setText("At what time for: ");
                textView7.setText("At what time for: ");

                if (numberOfDates > 1){
                    alarm_TimePicker2.setVisibility(View.INVISIBLE);
                    textView2.setVisibility(View.INVISIBLE);

                }
                if (numberOfDates > 2){
                    alarm_TimePicker3.setVisibility(View.INVISIBLE);
                    textView3.setVisibility(View.INVISIBLE);

                }
                if (numberOfDates > 3){
                    alarm_TimePicker4.setVisibility(View.INVISIBLE);
                    textView4.setVisibility(View.INVISIBLE);

                }
                if (numberOfDates > 4){
                    alarm_TimePicker5.setVisibility(View.INVISIBLE);
                    textView5.setVisibility(View.INVISIBLE);

                }
                if (numberOfDates > 5){
                    alarm_TimePicker6.setVisibility(View.INVISIBLE);
                    textView6.setVisibility(View.INVISIBLE);

                }
                if (numberOfDates > 6){
                    alarm_TimePicker7.setVisibility(View.INVISIBLE);
                    textView7.setVisibility(View.INVISIBLE);

                }
                addTimePicker.setVisibility(View.VISIBLE);
                removeTimePicker.setVisibility(View.INVISIBLE);

            }
        });
    }
    public String goToString(int a ){
        String b = "";
        if (a < 10){
            b = "0" + Integer.toString(a);
        }
        else
            b = Integer.toString(a);
        return b;
    }
}
