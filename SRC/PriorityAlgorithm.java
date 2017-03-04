
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
    public static int daysUntilReminder(int month, int day, int year){
                Calendar cal = Calendar.getInstance();
        
        int dayCount = 0;
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
        
        return dayCount;
        
        
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
        
        int daysUntil = daysUntilReminder(month, day, year);
        
        
   
        if (daysUntil == 1){
            
            
        }
        
        if (daysUntil <= 3){
            
            
        }
        
        if (daysUntil <= 5) {

        }

        if (daysUntil <= 7) {

        }

        if (daysUntil <= 14) {

        }

        if (daysUntil <= 28) {

        }

        if (daysUntil <= 56) {

        }

        if (daysUntil <= 84) {

        }

        if (daysUntil > 84) {

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
                if (priority >= 5) {
                    //set alert for 6 hours later
                }
                if (priority < 5) {
                    //set alert for 5 hours later

                }
            }

            if (hoursUntil <= 12) {
                // make some decisions

            }
        }
        if (daysUntil <= 5) {

        }

        if (daysUntil <= 7) {

        }

        if (daysUntil <= 14) {

        }

        if (daysUntil <= 28) {

        }

        if (daysUntil <= 56) {

        }

        if (daysUntil <= 84) {

        }

        if (daysUntil > 84) {

        }

        return true;
    }
}
