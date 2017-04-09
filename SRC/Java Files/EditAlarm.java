package com.seniorproject.prioritize;

import android.app.AlarmManager;
import android.content.IntentSender;
import android.graphics.Typeface;
import android.icu.util.Calendar;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;

public class EditAlarm extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

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
    private static final int RESOLVE_CONNECTION_REQUEST_CODE = 4;
    private static final String TAG = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_alarm);


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
}
