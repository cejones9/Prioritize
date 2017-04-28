package com.seniorproject.prioritize;

import java.util.Calendar;

/**
 * Created by Cody on 3/13/2017.
 */

public class priorityAlgorithm {
    public static void main(String[] args){
        String a[] = getPriorityDateandTime(5, 11, 2017, 3, 30, 1);
        System.out.println(a[0]);
        System.out.println(a[1]);
        System.out.println(a[2]);
        System.out.println(a[3]);
        System.out.println(a[4]);
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

    /*
    As of late March I've come to realize that the way I decided to go about getting the nubmer of
    days between and getting a eprcentage of them is ridiculous.

    I did not realize (until I took over working on the Notifications and setting the alarm managers
    as progress wasn't moving at all on them), that you could simply set a calendar to a specific date,
    add, subtract, etc. It would have been much simpler to save the dates as system millisecond longs instead
    of strings as well. I'd love to change it but it's too deep in the code to change all in time and finish
    the rest of the project. Anyway this is for future me to come back to in a few weeks or months
    for when I decide to add the geolocation stuff myself and maybe tidy up the code to make
    it into a more professional app.
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
        cal.set(Integer.parseInt(dateAndTime[2]), Integer.parseInt(dateAndTime[0]), Integer.parseInt(dateAndTime[1]), Integer.parseInt(dateAndTime[3]), Integer.parseInt(dateAndTime[4]));
        if (cal.get(Calendar.HOUR)>=22){
            dateAndTime[3] = "09";
        }
        else if(cal.get(Calendar.HOUR) < 7 ){
            dateAndTime[3] = "07";
        }
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
                currentYear +=1;
            }
            else
                currentMonth+=1;
        }
        while(days!=0){
            currentDayOfMonth+=1;
            days-=1;
            if(currentDayOfMonth == daysInMonth(currentMonth)){
                currentMonth+=1;
                currentDayOfMonth =1;
            }
        }
        while(hours!=0){
            currentHour +=1;
            hours-=1;
            if (currentHour == 24){
                currentDayOfMonth +=1;
                if (currentDayOfMonth > daysInMonth(currentMonth)){
                    currentDayOfMonth=1;
                    currentMonth +=1;
                    if (currentMonth ==13)
                        currentMonth =1;
                }
                currentHour =0;
            }
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
        System.out.println(d + "mark");
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
