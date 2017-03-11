package com.seniorproject.prioritize;

/**
 * Created by shahrukh on 3/11/2017.
 */

public class JSON{

        private String RID;
        private String Description;
        private String Due_Date;
        private String Due_Time;
        private String C_Remind_Date;
        private String C_Remind_time;
        private String Priority_Val;
        private Boolean Alarm;
        private Boolean On_Time;
        private int Repeatable;


        //public String that represents the JSON representation
        public String JSONString;
        public String insertIntoSQLite;
        static SetAlarm newAlarm_Information = new SetAlarm();

        //call the variables

        public JSON(){
            set_RID(this);
            set_Description(this);
            set_Due_Date(this);
            set_Due_Time(this);
            set_C_Remind_Date(this);
            set_C_Remind_Time(this);
            set_Priority_Val(this);
            set_Alarm(this);
            set_On_Time(this);
            set_Repeatable(this);

            JSONString = ("{" + "\"RID\"" + ":" + this.RID + ","
                    + "\"Description\"" + ":" + this.Description + ","
                    + "\"Due_Date\"" + ":" + this.Due_Date + ","
                    + "\"Due_Time\"" + ":" + this.Due_Time + ","
                    + "\"C_Remind_Date\"" + ":" + this.C_Remind_Date + ","
                    + "\"C_Remind_Time\"" + ":" + this.C_Remind_time + ","
                    + "\"Priority_Val\"" + ":"+ this.Priority_Val + ","
                    + "\"Alarm\"" + ":" + this.Alarm.toString() + ","
                    + "\"On_Time\"" + ":" + this.On_Time.toString() + ","
                    + "\"Repeatable\"" + ":" + this.Repeatable + ","
                    + "}");


           /* insertIntoSQLite = ("INSERT into Reminder (" + this.RID + ", " + this.Description + ", "
                    + this.Due_date + ", " + this.Due_time + ", " + this.C_Remind_Date + ", " + this.C_Remind_Time +
                    ", " + this.Priority_Val + ", " + this.Alarm.toString() + ", " + this.On_Time.toString() + ", " + this.Repeatable + ")");


                String insertIntoSQLite = "INSERT into Reminder (" +this.RID+", "
                   + this.Description + ", " + ....
                */






        }


    public static String get_RID(){
        //will get the value of RID
        return null;
    }

    private static void set_RID(JSON a){
        a.RID = get_RID();

    }

    public static String get_Description(){

        //will get the value of description
        String description = newAlarm_Information.alarm_Description.getText().toString();
        return description;
    }

    private static void set_Description(JSON a){
        a.Description = get_Description();

    }
    public static String get_Due_Date(){

        //will get the value of Due Date
        String due_Date = newAlarm_Information.date_Picked();
        return due_Date;
    }

    private static void set_Due_Date(JSON a){
        a.Due_Date = get_Due_Date();

    }

    public static String get_Due_Time(){

        //will get the value of Due Time
        String due_Time = newAlarm_Information.time_Picked();
        return due_Time;
    }

    private static void set_Due_Time(JSON a){
        a.Due_Time = get_Due_Time();

    }
    public static String get_C_Remind_Date(){
        //will get the value of Calculated Remind Date
        return null;
    }

    private static void set_C_Remind_Date(JSON a){
        a.C_Remind_Date = get_C_Remind_Date();

    }
    public static String get_C_Remind_Time(){
        //will get the value of calculated Remind Time
        return null;
    }

    private static void set_C_Remind_Time(JSON a){
        a.C_Remind_time = get_C_Remind_Time();

    }

    public static String get_Priority_Val(){

        //will get the value of priority value
        int priority_Val = newAlarm_Information.priority_Value;
        String p_Val =  String.valueOf(priority_Val);
        return p_Val;
    }

    private static void set_Priority_Val(JSON a){
        a.Priority_Val = get_Priority_Val();
    }

    public static boolean get_Alarm(){
        //will get the value of alarm
        boolean alarm_Type = false;
        if (newAlarm_Information.alarm_Type.isChecked() == true) {
            return alarm_Type = true;
        } else
        return alarm_Type;
    }

    private static void set_Alarm(JSON a){
        a.Alarm = get_Alarm();

    }

    public static boolean get_On_Time(){
        //will get the value of On_time
        boolean on_Time = false;
        if (newAlarm_Information.on_Time.isChecked() == true) {
            return on_Time = true;
        } else
            return on_Time;
    }

    private static void set_On_Time(JSON a){
        a.On_Time = get_On_Time();

    }

    public static int get_Repeatable(){
        //will get the value of description
        return 0;
    }

    private static void set_Repeatable(JSON a){
        a.Repeatable = get_Repeatable();

    }



}