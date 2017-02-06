package com.example.cody.testing2;
import java.util.Date;
//import android.icu.text.DateFormat;
import java.sql.Time;
import java.text.DateFormat;
import java.util.Calendar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class addReminder extends AppCompatActivity {

    //String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
    //TextView date = (TextView) findViewById(R.id.date1);

    //Calendar c = Calendar.getInstance();
    //int cDay = c.get(Calendar.DAY_OF_MONTH);
    //int cMonth = c.get(Calendar.MONTH);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);
       // SimpleDateFormat sdf = new SimpleDateFormat("dd,MMM,YYYY hh,mm,a");
       // String strDate = sdf.format(c.getTime());
       // TextView tv = (TextView)findViewById(R.id.date1);
       // tv.setText(strDate);
        TextView tv = (TextView)findViewById(R.id.date1);
        //tv.setText(currentDateTimeString);
        //System.currentTimeMillis();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy");

        /*
        Calendar.MONTH starts the year off with 0. Meaning December month #11
         */
       // int month = c.get(Calendar.MONTH);

        String formattedDate = df.format(c.getTime());
        //Date date2 = c.getTime();



        tv.setText(formattedDate);
    }

    /*
    Date will provide the date in the format of month/day/year and and hour of the day.
    The date and hour are the times 'due date' of the reminder are set for.
    If time is not specified then we the lower method will be used.
    Time must be in the format
    */

    /*public boolean setPriorityDate(Date date, int hour, int minute){

        //this methods format may be easier to use, passing in the values seperately may prove to be easier.
        //pass in hour in military time, any time after 12 is pm, subtract 12 to get to standard format.

        return true;
    }*/

    public int daysInMonth(int month) {
        Calendar calendar = Calendar.getInstance();

        if (month == 1)
            return 31;
        if (month == 2)
            return 28;
        //still need to account for leap  years
        if (month == 3)
            return 31;
        if (month == 4)
            return 30;
        if (month == 5)
            return 31;
        if (month == 6)
            return 30;
        if (month == 7)
            return 31;
        if (month == 8)
            return 31;
        if (month == 9)
            return 30;
        if (month == 10)
            return 31;
        if (month == 11)
            return 30;
        if (month == 12)
            return 31;
        return 0;
    }

    public int daysUntilReminder(int month, int day, int year, int hour, int minute) {
        Calendar cal = Calendar.getInstance();
        int dayCount = 0;
        int currentMonth = cal.get(Calendar.MONTH) + 1;
        int currentDayOfMonth = cal.get(Calendar.DAY_OF_MONTH);


        if (month > currentMonth) {

            while (month > currentMonth) {
                dayCount += daysInMonth(currentMonth);
                currentMonth++;
            }
            //  must add 1 to Calendar.MONTH
        }
        if (day > currentDayOfMonth) {
            while (day > currentDayOfMonth) {
                dayCount += 1;
                currentDayOfMonth++;
            }
        }
        if (day < currentDayOfMonth) {
            while (day < currentDayOfMonth) {
                dayCount -= 1;
                currentDayOfMonth--;
            }
        }
        return dayCount;

    }
    public boolean setPriorityDate(int month, int day, int year, int hour, int minute, double priority){
        Calendar cal = Calendar.getInstance();
        int currentMonth = cal.get(Calendar.MONTH) + 1;
        int reminderMonth = cal.get(Calendar.MONTH) + 1;
        int currentDayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        int reminderDay = cal.get(Calendar.DAY_OF_MONTH);


        double priorityPercent = priority/100;
        double dayCount = daysUntilReminder(month, day, year, hour, minute);
        double daysUntilReminder = dayCount*priorityPercent;

        if (dayCount>daysInMonth(currentMonth)){
            while(dayCount>daysInMonth((reminderMonth))){
                dayCount -= daysInMonth(reminderMonth);
                reminderMonth +=1;
            }
        }
        /*
        Absolutely must have the dayCount be below the number of days in the current month before
        moving onto the next step. The previous if statement should take care of this.
         */
        if (dayCount>currentDayOfMonth){
            while(dayCount>reminderDay){
                reminderDay+=1;
            }
        }

        /*
        At this point the month and the day of the months for the reminder are
        reminderMonth
        reminderDay
        Next we have to take specific time into consideration.
         */


        //int hour = Calendar.getInst

       // Date date1 = new java.util.Date();



        return true;
    }

    public boolean setPriorityDateTime(Date date){


        return true;
    }
    //String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
 //   TextView tv = (TextView)findViewById(R.id.date1);


}
