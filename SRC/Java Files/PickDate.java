package com.seniorproject.prioritize;

import android.content.Intent;
import android.graphics.Typeface;
import java.util.Calendar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.DatePicker;

import org.w3c.dom.Text;

public class PickDate extends AppCompatActivity {

    DatePicker alarm_DatePicker1,alarm_DatePicker2,alarm_DatePicker3,
            alarm_DatePicker4, alarm_DatePicker5,alarm_DatePicker6,alarm_DatePicker7;
    TextView textView1, textView2, textView3, textView4, textView5, textView6, textView7;
    Button addDatePicker;
    Button removeDatePicker;
    int numberOfDates = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_date);

        //center align the app title
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_layout);
        //title font7
        Typeface myfont = Typeface.createFromAsset(getAssets(), "Pacifico.ttf");
        TextView app_title = (TextView) findViewById(R.id.mytitle);
        app_title.setTypeface(myfont);

        //for alarm info


        //initialize Datepickers
        Calendar now = Calendar.getInstance();
        long nowMili = now.getTimeInMillis();

        alarm_DatePicker1 = (DatePicker) findViewById(R.id.datePicker1);
        alarm_DatePicker1.setMinDate(nowMili);

        alarm_DatePicker2 = (DatePicker) findViewById(R.id.datePicker2);
        alarm_DatePicker2.setMinDate(nowMili + 86400000);
        alarm_DatePicker3 = (DatePicker) findViewById(R.id.datePicker3);
        alarm_DatePicker3.setMinDate(nowMili + 172800000);
        alarm_DatePicker4 = (DatePicker) findViewById(R.id.datePicker4);
        alarm_DatePicker4.setMinDate(nowMili + 259200000);
        alarm_DatePicker5 = (DatePicker) findViewById(R.id.datePicker5);
        alarm_DatePicker5.setMinDate(nowMili + 345600000);
        alarm_DatePicker6 = (DatePicker) findViewById(R.id.datePicker6);
        alarm_DatePicker6.setMinDate(nowMili + 432000000);
        alarm_DatePicker7 = (DatePicker) findViewById(R.id.datePicker7);
        alarm_DatePicker7.setMinDate(nowMili + 518400000);
        textView1 = (TextView) findViewById(R.id.date1text);
        textView2 = (TextView) findViewById(R.id.date2text);
        textView3 = (TextView) findViewById(R.id.date3text);
        textView4 = (TextView) findViewById(R.id.date4text);
        textView5 = (TextView) findViewById(R.id.date5text);
        textView6 = (TextView) findViewById(R.id.date6text);
        textView7 = (TextView) findViewById(R.id.date7text);




        Button nextScreen = (Button) findViewById(R.id.timePickerScreen);

        nextScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  Intent thirdScreenIntent = new Intent (PickDate.this, PickTime.class);

                Intent alarmSetupTime = new Intent(PickDate.this, PickTime.class);
                String[] date1 = new String[5];
                String[] date2 = new String[5];
                String[] date3 = new String[5];
                String[] date4 = new String[5];
                String[] date5 = new String[5];
                String[] date6 = new String[5];
                String[] date7 = new String[5];
                date1[0] = Integer.toString(alarm_DatePicker1.getYear());
                date1[1] = goToString(alarm_DatePicker1.getMonth() + 1);
                date1[2] = goToString(alarm_DatePicker1.getDayOfMonth());
                date2[0] = Integer.toString(alarm_DatePicker2.getYear());
                date2[1] = goToString(alarm_DatePicker2.getMonth() + 1);
                date2[2] = goToString(alarm_DatePicker2.getDayOfMonth());
                date3[0] = Integer.toString(alarm_DatePicker3.getYear());
                date3[1] = goToString(alarm_DatePicker3.getMonth() + 1);
                date3[2] = goToString(alarm_DatePicker3.getDayOfMonth());
                date4[0] = Integer.toString(alarm_DatePicker4.getYear());
                date4[1] = goToString(alarm_DatePicker4.getMonth() + 1);
                date4[2] = goToString(alarm_DatePicker4.getDayOfMonth());
                date5[0] = Integer.toString(alarm_DatePicker5.getYear());
                date5[1] = goToString(alarm_DatePicker5.getMonth() + 1);
                date5[2] = goToString(alarm_DatePicker5.getDayOfMonth());
                date6[0] = Integer.toString(alarm_DatePicker6.getYear());
                date6[1] = goToString(alarm_DatePicker6.getMonth() + 1);
                date6[2] = goToString(alarm_DatePicker6.getDayOfMonth());
                date7[0] = Integer.toString(alarm_DatePicker7.getYear());
                date7[1] = goToString(alarm_DatePicker7.getMonth() + 1);
                date7[2] = goToString(alarm_DatePicker7.getDayOfMonth());
                alarmSetupTime.putExtra("date1", date1);
                alarmSetupTime.putExtra("date2", date2);
                alarmSetupTime.putExtra("date3", date3);
                alarmSetupTime.putExtra("date4", date4);
                alarmSetupTime.putExtra("date5", date5);
                alarmSetupTime.putExtra("date6", date6);
                alarmSetupTime.putExtra("date7", date7);
                alarmSetupTime.putExtra("numberOfDates", numberOfDates);


                startActivity(alarmSetupTime);
            }
        });


        addDatePicker = (Button) findViewById(R.id.newDatePicker);
        addDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (alarm_DatePicker2.getVisibility() == View.INVISIBLE) {
                    alarm_DatePicker2.setVisibility(View.VISIBLE);
                    removeDatePicker.setVisibility(View.VISIBLE);
                    textView2.setVisibility(View.VISIBLE);

                    numberOfDates +=1;
                }
                else if (alarm_DatePicker3.getVisibility() == View.INVISIBLE) {
                    alarm_DatePicker3.setVisibility(View.VISIBLE);
                    textView3.setVisibility(View.VISIBLE);

                    numberOfDates +=1;
                }
                else if (alarm_DatePicker4.getVisibility() == View.INVISIBLE) {
                    alarm_DatePicker4.setVisibility(View.VISIBLE);
                    textView4.setVisibility(View.VISIBLE);

                    numberOfDates +=1;
                }
                else if (alarm_DatePicker5.getVisibility() == View.INVISIBLE) {
                    alarm_DatePicker5.setVisibility(View.VISIBLE);
                    textView5.setVisibility(View.VISIBLE);

                    numberOfDates +=1;
                }
                else if (alarm_DatePicker6.getVisibility() == View.INVISIBLE) {
                    alarm_DatePicker6.setVisibility(View.VISIBLE);
                    textView6.setVisibility(View.VISIBLE);

                    numberOfDates +=1;
                }
                else {
                    alarm_DatePicker7.setVisibility(View.VISIBLE);
                    numberOfDates +=1;
                    textView7.setVisibility(View.VISIBLE);

                    addDatePicker.setVisibility(View.INVISIBLE);
                }
            }
        });
        removeDatePicker = (Button) findViewById(R.id.removeDatePicker);
        removeDatePicker.setOnClickListener(new View.OnClickListener(){
            public void onClick (View view){
                if (numberOfDates == 2){
                    numberOfDates -= 1;
                    alarm_DatePicker2.setVisibility(View.INVISIBLE);
                    removeDatePicker.setVisibility(View.INVISIBLE);
                    textView2.setVisibility(View.INVISIBLE);

                }
                else if (numberOfDates == 3){
                    numberOfDates -= 1;
                    alarm_DatePicker3.setVisibility(View.INVISIBLE);
                    textView3.setVisibility(View.INVISIBLE);



                }
                else if (numberOfDates == 4){
                    numberOfDates -= 1;
                    alarm_DatePicker4.setVisibility(View.INVISIBLE);
                    textView4.setVisibility(View.INVISIBLE);



                }
                else if (numberOfDates == 5){
                    numberOfDates -= 1;
                    alarm_DatePicker5.setVisibility(View.INVISIBLE);
                    textView5.setVisibility(View.INVISIBLE);



                }
                else if (numberOfDates == 6){
                    numberOfDates -= 1;
                    alarm_DatePicker6.setVisibility(View.INVISIBLE);
                    textView6.setVisibility(View.INVISIBLE);



                }
                else if (numberOfDates == 7){
                    numberOfDates -= 1;
                    alarm_DatePicker7.setVisibility(View.INVISIBLE);
                    textView7.setVisibility(View.INVISIBLE);
                    addDatePicker.setVisibility(View.VISIBLE);




                }


            }

        });
    }
    public String goToString(int a){
        String b = "";
        if (a < 10){
             b = "0" + Integer.toString(a);
        }
        else
            b = Integer.toString(a);
        return b;

    }
}