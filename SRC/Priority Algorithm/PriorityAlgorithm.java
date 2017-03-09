/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package priority.algorithm;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Cody
 */
public class PriorityAlgorithm {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyy");
        String formattedDate = df.format(c.getTime());
        int i = c.get(Calendar.HOUR_OF_DAY);
        System.out.println(daysUntilReminder(3, 9, 2017)[2]);
        String a = "12345";
        System.out.println(a.charAt(3));
        
        //System.our.println(daysUntilReminder(df));
        // TODO code application logic here
    }
    
    /*
    To avoid several loops of increments  in other methods, this method returns
    days in the month
    */
    public static int daysInMonth(int month) {
        Calendar calendar = Calendar.getInstance();

        if (month == 1) {
            return 31;
        }
        if (month == 2) {
            return 28;
        }
        //still need to account for leap  years
        if (month == 3) {
            return 31;
        }
        if (month == 4) {
            return 30;
        }
        if (month == 5) {
            return 31;
        }
        if (month == 6) {
            return 30;
        }
        if (month == 7) {
            return 31;
        }
        if (month == 8) {
            return 31;
        }
        if (month == 9) {
            return 30;
        }
        if (month == 10) {
            return 31;
        }
        if (month == 11) {
            return 30;
        }
        if (month == 12) {
            return 31;
        }
        return 0;
    }
    /*
    In the case that the user designates a specific time, the number of hours
    and minutes must be included in the calculation. This method does the calculation
    
    If the the minutes are negative, that means it must decrement an hours, and
    add that negative value to 60 minutes to calculate the difference.
    */
    public static int[] timeUntilReminder(int hour, int minute) {
        Calendar cal = Calendar.getInstance();
        int[] timeCount = new int[2];
        int hourCount = 0;
        int minuteCount = 0;
        int currentHour = cal.get(Calendar.HOUR_OF_DAY);
        int currentMinute = cal.get(Calendar.MINUTE);
        while (currentHour < hour) {
            currentHour += 1;
            hourCount += 1;
        }
        while (currentHour > hour) {
            currentHour -= 1;
            hourCount -= 1;
        }
        while (currentMinute < minute) {
            currentMinute += 1;
            minuteCount += 1;
        }
        while (currentMinute > minute) {
            currentMinute -= 1;
            minuteCount -= 1;
        }
        if (minuteCount < 0) {
            hourCount -= 1;
            minuteCount = (60 + minuteCount);
        }
        timeCount[0] = hourCount;
        timeCount[1] = minuteCount;

        return timeCount;

    }
    /*
    daysUntilReminder calculates the number of days there are between the current date
    and the deadline of the user's event.
    
    It is split into two methods, one where the user picked a specific time and
    one where the user did not.
    
    The one below simply calculates days
    
    */
    public static int[] daysUntilReminder(int month, int day, int year){
                Calendar cal = Calendar.getInstance();
        
        int dayCount = 0;
        int[] timeUntil = new int[3];
        int currentMonth = cal.get(Calendar.MONTH) + 1;
        int currentDayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        int currentYear = cal.get(Calendar.YEAR);
        int[] timeCount = timeUntilReminder(0, 0);

        if (month == currentMonth) {
            while (year != currentYear) {
                dayCount += 365;
                currentYear += 1;
            }
        } else {
            while (month != currentMonth) {
                dayCount += daysInMonth(currentMonth);
                if (currentMonth == 12) {
                    currentMonth = 1;
                } else {
                    currentMonth += 1;
                }

            }
        }

        while (day > currentDayOfMonth) {
            dayCount += 1;
            currentDayOfMonth++;
        }

        while (day < currentDayOfMonth) {
            dayCount -= 1;
            currentDayOfMonth--;
        }
        if (timeCount[0] < 0) {
            dayCount -= 1;
            timeCount[0] = (24 + timeCount[0]);

        }
        timeUntil[0] = dayCount;
        timeUntil[1] = timeCount[0];
        timeUntil[2] = timeCount[1];
        return timeUntil;
        
        
    }
    
    /*
    daysUntilReminder calculates the number of days there are between the current date
    and the deadline of the user's event.
    
    It is split into two methods, one where the user picked a specific time and
    one where the user did not.
    
    The one below includes time into the calculation, overrides the other
    one when the user selects a time. 
    It returns an ARRAY of INT's. 
        int[0] contains the number of full days
        int[1] contains the number of full hours
        int[2] contains the exact number of minutes
    */
    public static int[] daysUntilReminder(int month, int day, int year, int hour, int minute) {
        Calendar cal = Calendar.getInstance();
        int counts[] = new int[3];
        int dayCount = 0;
        int[] timeCount = timeUntilReminder(hour, minute);
        int currentMonth = cal.get(Calendar.MONTH) + 1;
        int currentDayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        int currentYear = cal.get(Calendar.YEAR);

        if (month == currentMonth) {
            while (year != currentYear) {
                dayCount += 365;
                currentYear += 1;
            }
        } else {
            while (month != currentMonth) {
                dayCount += daysInMonth(currentMonth);
                if (currentMonth == 12) {
                    currentMonth = 1;
                } else {
                    currentMonth += 1;
                }

            }
        }

        while (day > currentDayOfMonth) {
            dayCount += 1;
            currentDayOfMonth++;
        }

        while (day < currentDayOfMonth) {
            dayCount -= 1;
            currentDayOfMonth--;
        }
        if (timeCount[0] < 0) {
            dayCount -= 1;
            timeCount[0] = (24 + timeCount[0]);

        }

        counts[0] = dayCount;
        counts[1] = timeCount[0];
        counts[2] = timeCount[1];
        return counts;

    }
    
    /*
    setPriorityDate sets the alert for the users event. 
    
    It is split into two methods, one that calculates a specific time and one that
    does not
    
    The one below does not calculate time, it calls daysUntilReminder to get
    information.
    */
    public boolean setPriorityDate(int month, int day, int year, double priority){
        Calendar cal = Calendar.getInstance();
        int currentMonth = cal.get(Calendar.MONTH) + 1;
        int currentDayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        int currentHour = cal.get(Calendar.HOUR_OF_DAY);
        int currentMinute = cal.get(Calendar.MINUTE);
       
        int reminderMonth;
        int reminderDay;
        int reminderHour;
        int reminderMinute;
        
        int[] daysUntil = daysUntilReminder(month, day, year);
        
        
   
        if (daysUntil[0] < 1){
            if (cal.get(Calendar.HOUR_OF_DAY)<9){
                if (priority<5){
                    
                }
                else{
                    
                }
                    
            }
            if (cal.get(Calendar.HOUR_OF_DAY)<12){
                if (priority<5){
                    
                }
                else{
                    
                }
            }
            if (cal.get(Calendar.HOUR_OF_DAY)<15){
                if (priority<5){
                    
                }
                else{
                    
                }
            }
            if (cal.get(Calendar.HOUR_OF_DAY)<20){
                if (priority<5){
                    
                }
                else{
                    
                }
            }
            if (cal.get(Calendar.HOUR_OF_DAY)<24){
                if (priority<5){
                    
                }
                else{
                    
                }
            }
                
            
            
        }
        
        /*
        may be more useful to simply multiply the priority percentage by 24 
        in a way that produces reasonable times for the 10 values.
        */
        if (daysUntil[0] == 1){
            if (priority == 1){
                
            }
            if (priority == 2){
                
            }
            if (priority == 3){
                
            }
            if (priority == 4){
                
            }
            if (priority == 5){
                
            }
            if (priority == 6){
                
            }
            if (priority == 7){
                
            }
            if (priority == 8){
                
            }
            if (priority == 9){
                
            }
            if (priority == 10){
                
            }
            
            
            
        }
        
        if (daysUntil[0] <= 3){
            
            
        }
        
        if (daysUntil[0] <= 5) {

        }

        if (daysUntil[0] <= 7) {

        }

        if (daysUntil[0] <= 14) {

        }

        if (daysUntil[0] <= 28) {

        }

        if (daysUntil[0] <= 56) {

        }

        if (daysUntil[0] <= 84) {

        }

        if (daysUntil[0] > 84) {

        }
        
        return true;
    }

    
    /*
    setPriorityDate sets the alert for the users event. 
    
    It is split into two methods, one that calculates a specific time and one that
    does not
    
    The one below calculates the date with a specific time in mind.
    It calls daysUntilReminder with hours and minutes in mind.
    It uses the ARRAY of length 3 returned by the method.
    */
    public boolean setPriorityDate(int month, int day, int year, int hour, int minute, double priority) {
        Calendar cal = Calendar.getInstance();

        int currentMonth = cal.get(Calendar.MONTH) + 1;
        int currentDayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        int currentHour = cal.get(Calendar.HOUR_OF_DAY);
        int currentMinute = cal.get(Calendar.MINUTE);

        int reminderMonth;
        int reminderDay;
        int reminderHour;
        int reminderMinute;

        int[] until = daysUntilReminder(month, day, year, hour, minute);
        int daysUntil = until[0];
        int hoursUntil = until[1];
        int minutesUntil = until[2];

        double priorityPercent = priority / 100;
        

        if (daysUntil == 0) {
            if (hoursUntil > 5 && hoursUntil < 8) {
                if (priority >= 8) {
                    //set alert for  5 hours later
                }
                else if (priority >= 5) {
                    //set alert for 6 hours later
                }
                else (priority < 5) {
                    //set alert for 5 hours later

                }
            }

            if (hoursUntil <= 12) {
                if (priority >= 8) {
                   // four hours later
                }
                else if (priority >= 5) {
                    //set alert for 6 hours later
                }
                else (priority < 5) {
                    //set alert for 8 hours later
                }
       
        }
       else if (daysUntil <= 5) {
             if (priority >= 8) {
                    //1.5 day later
                }
                else if (priority >= 5) {
                    //3 days
                }
                else (priority < 5) {
                   //4 days
            
            

        }
        }
       else if (daysUntil <= 7) {
            if (priority >= 8) {
                   //3 days
                }
                else if (priority >= 5) {
                    //5 days later
                }
                else (priority < 5) {
                   //6 days later
                }
        }

       else if (daysUntil <= 14) {
            if (priority >= 8) {
                   //80%
                }
                else if (priority >= 5) {
                   //50%
                }
                else (priority < 5) {
                  //30%
                }
        }

        else if (daysUntil <= 28) {
             if (priority >= 8) {
                   //70%
                }
                else if (priority >= 5) {
                    //50%
                }
                else (priority < 5) {
                    //20%
                }
        }
       else if (daysUntil <= 56) {
             if (priority >= 8) {
                    //80%
                }
                else if (priority >= 5) {
                    //50%
                }
                else (priority < 5) {
                    //30%
                }
        }

        else if (daysUntil <= 84) {
            if (priority >= 8) {
                    //60%$
                }
                else if (priority >= 5) {
                    //40%
                }
                else (priority < 5) {
                    //20%
                }
        }

        else (daysUntil > 84) {
            if (priority >= 8) {
                    //50%
                }
                else if (priority >= 5) {
                    //35%
                }
                else (priority < 5) {
                    //15%
                }
        }

        return true;

    
    }
        
        public String Days_Until_into_Date(int days){
         String date;
            //do stuff
            
            
            
            
            
            
         return date;   
        }
        
        
        
        
        
}



