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
        public static final String columnDueDate = "DueDate";
        public static final String columnDueTime = "DueTime";
        public static final String columnCalculatedDate = "CalculatedDate";
        public static final String columnCalculatedTime = "CalculateTime";
        public static final String columnPriority = "Priority";
        public static final String columnAlarm = "Alarm";
        public static final String columnOnTime = "OnTime";
        public static final String columnRepeatNumber = "RepeatEveryxDays";
        public static final String columnAlarmSchedulerID = "ALarmSchedulerID";



    }

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedEntry.tableName + " ("+
                    FeedEntry.RID + " TEXT NOT NULL PRIMARY KEY, " +
                    FeedEntry.columnDescription + " TEXT, " +
                    FeedEntry.columnDueDate + " TEXT, "+
                    FeedEntry.columnDueTime + " TEXT NOT NULL, "+
                    FeedEntry.columnPriority + " DOUBLE NOT NULL, "+
                    FeedEntry.columnCalculatedDate + " TEXT, " +
                    FeedEntry.columnCalculatedTime + " TEXT, " +
                    FeedEntry.columnAlarm + " INT, " +
                    FeedEntry.columnOnTime + " INT, " +
                    FeedEntry.columnRepeatNumber + " TEXT, " +
                    FeedEntry.columnAlarmSchedulerID + " INT)";

}
