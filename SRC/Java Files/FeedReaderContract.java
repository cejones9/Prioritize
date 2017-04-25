package com.seniorproject.prioritize;

import android.provider.BaseColumns;

/**
 * Created by Cody on 3/7/2017.
 */

public final class FeedReaderContract {
    private FeedReaderContract(){};
    public static class FeedEntry implements BaseColumns {
        public static final String tableName = "Reminder";
        public static final String RID = "RID";
        public static final String columnDescription = "Description";
        public static final String columnDueDate1 = "DueDate1";
        public static final String columnDueTime1 = "DueTime1";
        public static final String columnCalculatedDate = "CalculatedDate";
        public static final String columnCalculatedTime = "CalculateTime";
        public static final String columnPriority = "Priority";
        public static final String columnAlarm = "Alarm";
        public static final String columnOnTime = "OnTime";
        public static final String columnRepeatNumber = "RepeatEveryxDays";
        public static final String columnAlarmSchedulerID = "ALarmSchedulerID";
        public static final String columnNumberOfDates = "NumberofDates";
        public static final String columnCurrentDate = "CurrentDate";
        public static final String columnDueDate2 = "DueDate2";
        public static final String columnDueTime2 = "DueTime2";
        public static final String columnDueDate3 = "DueDate3";
        public static final String columnDueTime3 = "DueTime3";
        public static final String columnDueDate4 = "DueDate4";
        public static final String columnDueTime4 = "DueTime4";
        public static final String columnDueDate5 = "DueDate5";
        public static final String columnDueTime5 = "DueTime5";
        public static final String columnDueDate6 = "DueDate6";
        public static final String columnDueTime6 = "DueTime6";
        public static final String columnDueDate7 = "DueDate7";
        public static final String columnDueTime7 = "DueTime7";
    }

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedEntry.tableName + " ("+
                    FeedEntry.RID + " TEXT NOT NULL PRIMARY KEY, " +
                    FeedEntry.columnDescription + " TEXT, " +
                    FeedEntry.columnDueDate1 + " TEXT, "+
                    FeedEntry.columnDueTime1 + " TEXT, "+
                    FeedEntry.columnPriority + " INT NOT NULL, "+
                    FeedEntry.columnCalculatedDate + " TEXT, " +
                    FeedEntry.columnCalculatedTime + " TEXT, " +
                    FeedEntry.columnAlarm + " INT, " +
                    FeedEntry.columnOnTime + " INT, " +
                    FeedEntry.columnRepeatNumber + " INT, " +
                    FeedEntry.columnAlarmSchedulerID + " INT, " +
                    FeedEntry.columnNumberOfDates + " INT, " +
                    FeedEntry.columnCurrentDate + " INT, " +
                    FeedEntry.columnDueDate2 + " TEXT, "+
                    FeedEntry.columnDueTime2 + " TEXT, "+
                    FeedEntry.columnDueDate3 + " TEXT, "+
                    FeedEntry.columnDueTime3 + " TEXT, "+
                    FeedEntry.columnDueDate4 + " TEXT, "+
                    FeedEntry.columnDueTime4 + " TEXT, "+
                    FeedEntry.columnDueDate5 + " TEXT, "+
                    FeedEntry.columnDueTime5 + " TEXT, "+
                    FeedEntry.columnDueDate6 + " TEXT, "+
                    FeedEntry.columnDueTime6 + " TEXT, "+
                    FeedEntry.columnDueDate7 + " TEXT, "+
                    FeedEntry.columnDueTime7 + " TEXT)";
}