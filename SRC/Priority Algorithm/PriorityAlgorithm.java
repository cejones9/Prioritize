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
        String[] a = getPriorityDateandTime(4, 10, 2017, 3, 30,5);
        System.out.println(a[0]);
        System.out.println(a[1]);
        System.out.println(a[2]);
        System.out.println(a[3]);
        System.out.println(a[4]);
        
       
        
        //System.our.println(daysUntilReminder(df));
        // TODO code application logic here
    }
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
                    currentYear +=1;
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



    /*
    setPriorityDate sets the alert for the users event.

    It is split into two methods, one that calculates a specific time and one that
    does not

    The one below calculates the date with a specific time in mind.
    It calls daysUntilReminder with hours and minutes in mind.
    It uses the ARRAY of length 3 returned by the method.
    */
    public static String[] getPriorityDateandTime(int month, int day, int year, int hour, int minute, double priority) {
        Calendar cal = Calendar.getInstance();
        String[] dateAndTime = new String[5];
       

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
        double priotiyPercentResult = 110 - (priority*15);
        double daysToHours = daysUntil*24;
        double totalHours =daysToHours + hoursUntil;
        double totalHoursToMinutes = totalHours * 60;
        double totalMinutes = totalHoursToMinutes + minutesUntil;
        double totalMinutesUntilAlert = (totalMinutes * (priotiyPercentResult/100));

        double minutesToHours = totalMinutesUntilAlert/60;

        double wholeHoursUntilAlert = Math.floor(minutesToHours);

        double minutesUntilAlert = Math.floor((minutesToHours - wholeHoursUntilAlert)*60);

        double hoursToDays = wholeHoursUntilAlert/24;
        double wholeDaysUntilAlert = Math.floor(hoursToDays);
        double hoursUntilAlert = Math.floor((hoursToDays-wholeDaysUntilAlert)*24);


        dateAndTime = daysToDate(wholeDaysUntilAlert, hoursUntilAlert, minutesUntilAlert);
        System.out.println(minutesUntilAlert + "hi");







        return dateAndTime;


    }
    public static String[] daysToDate(double days, double hours, double minutes){
        Calendar cal = Calendar.getInstance();
        int currentMonth = cal.get(Calendar.MONTH) + 1;
        int currentDayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        int currentYear = cal.get(Calendar.YEAR);
        int currentHour = cal.get(Calendar.HOUR_OF_DAY);
        int currentMinute = cal.get(Calendar.MINUTE);
        while(days>=364){
            if(isLeapYear(currentYear)==false){
                currentYear+=1;
                days-=364;
            }
            if(isLeapYear(currentYear)==true){
                if(days>=365){
                    currentYear+=1;
                    days-=365;
                }

                else
                    break;
            }

        }
        while(days>=daysInMonth(currentMonth)){
            days-=daysInMonth(currentMonth);
            if (currentMonth==12){
                currentMonth=1;
            }
            else
                currentMonth+=1;
        }
        while(days!=0){
            currentDayOfMonth+=1;
            days-=1;
        }
        while(hours!=0){

            currentHour +=1;

            hours-=1;
            if (currentHour == 24)
                currentHour =0;

        }
        while(minutes!=0){
            currentMinute +=1;
            minutes-=1;
            if (currentMinute == 60)
                currentMinute = 0;

        }
        String m = "";
        if (currentMonth <= 9)
            m = "0" + Integer.toString(currentMonth);
        else
            m = Integer.toString(currentMonth);
        String d = "";
        if (currentDayOfMonth <= 9){
            d = "0" + Integer.toString(currentDayOfMonth);
        }
        else
            d = Integer.toString(currentDayOfMonth);
        String y = Integer.toString(currentYear);
        String h = "";
        if (currentHour <= 9){
            h = "0" + Integer.toString(currentHour);

        }
        else
            h = Integer.toString(currentHour);
        String min = "";
        if (currentMinute <= 9){
            min = "0" + Integer.toString(currentMinute);
        }
        else
            min = Integer.toString (currentMinute);
        String[] stringArray = new String[5];
        stringArray[0] = m;
        stringArray[1] = d;
        stringArray[2] = y;
        stringArray[3] = h;
        stringArray[4] = min;
        return stringArray;









    }
    public static boolean isLeapYear(int year){
        if (year == 2020||year==2024||year==2028||year==2032||year==2036||year==2040)
            return true;
        else
            return false;
    }
}

