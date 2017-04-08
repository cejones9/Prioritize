package com.seniorproject.prioritize;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

public class Reminders extends AppCompatActivity {

    ListView alarmList;
    ArrayList<String> arrList;
    ArrayAdapter<String> arrAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders);

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
        if(newAlarmDescription.getText() == alarmInfo) {
            newAlarmDescription.setVisibility(View.VISIBLE);

        /*List of alarms
        alarmList = (ListView) findViewById(R.id.alarmListView);
        arrList = new ArrayList<String>();
        arrAdapter = new ArrayAdapter<String>(Reminders.this, android.R.layout.simple_list_item_1, arrList);
        alarmList.setAdapter(arrAdapter);
        arrAdapter.add(alarmInfo);
        arrAdapter.notifyDataSetChanged();*/
        }


    }

    //open the alarm setup activity
    public void addAlarm (View view)
    {
        Intent addAlarmView = new Intent(this, SetAlarm.class);
        startActivity(addAlarmView);
    }
}
