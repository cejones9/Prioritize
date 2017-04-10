//package com.seniorproject.prioritize;

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
        private int Alarm;
        private int On_Time;
        private int Repeatable;


        //public String that represents the JSON representation
        public String JSONString;
        public String insertIntoSQLite;
        //static SetAlarm newAlarm_Information = new SetAlarm();

        //call the variables

//        public JSON(){
//            set_RID(this);
//            set_Description(this);
//            set_Due_Date(this);
//            set_Due_Time(this);
//            set_C_Remind_Date(this);
//            set_C_Remind_Time(this);
//            set_Priority_Val(this);
//            set_Alarm(this);
//            set_On_Time(this);
//            set_Repeatable(this);
//
//            JSONString = ("{" + "\"RID\"" + ":" + this.RID + ","
//                    + "\"Description\"" + ":" + this.Description + ","
//                    + "\"Due_Date\"" + ":" + this.Due_Date + ","
//                    + "\"Due_Time\"" + ":" + this.Due_Time + ","
//                    + "\"C_Remind_Date\"" + ":" + this.C_Remind_Date + ","
//                    + "\"C_Remind_Time\"" + ":" + this.C_Remind_time + ","
//                    + "\"Priority_Val\"" + ":"+ this.Priority_Val + ","
//                    + "\"Alarm\"" + ":" + this.Alarm + ","
//                    + "\"On_Time\"" + ":" + this.On_Time + ","
//                    + "\"Repeatable\"" + ":" + this.Repeatable + ","
//                    + "}");
//

           /* insertIntoSQLite = ("INSERT into Reminder (" + this.RID + ", " + this.Description + ", "
                    + this.Due_date + ", " + this.Due_time + ", " + this.C_Remind_Date + ", " + this.C_Remind_Time +
                    ", " + this.Priority_Val + ", " + this.Alarm.toString() + ", " + this.On_Time.toString() + ", " + this.Repeatable + ")");
                String insertIntoSQLite = "INSERT into Reminder (" +this.RID+", "
                   + this.Description + ", " + ....
                */






     //   }
        
        public JSON(String RID, String Description, String datepicked, String timepicked, String calcdate, String calcTime, 
                String priorityval, int alarm, int ontime, int repeat){
            this.Alarm = alarm;
            this.C_Remind_Date = calcdate;
            this.C_Remind_time = calcTime;
            this.Description = Description;
            this.Due_Date = datepicked;
            this.Due_Time = timepicked;
            this.On_Time = ontime;
            this.Priority_Val = priorityval;
            this.RID = RID;
            this.Repeatable = repeat;
            this.JSONString = ("{" + "\"RID\"" + ":" + this.RID + ","
                    + "\"Description\"" + ":" + this.Description + ","
                    + "\"Due_Date\"" + ":" + this.Due_Date + ","
                    + "\"Due_Time\"" + ":" + this.Due_Time + ","
                    + "\"C_Remind_Date\"" + ":" + this.C_Remind_Date + ","
                    + "\"C_Remind_Time\"" + ":" + this.C_Remind_time + ","
                    + "\"Priority_Val\"" + ":"+ this.Priority_Val + ","
                    + "\"Alarm\"" + ":" + this.Alarm + ","
                    + "\"On_Time\"" + ":" + this.On_Time + ","
                    + "\"Repeatable\"" + ":" + this.Repeatable + ","
                    + "}");
        }
        
        public JSON(int i){
            this.Alarm = 0;
            this.C_Remind_Date = null;
            this.C_Remind_time = null;
            this.Description = null;
            this.Due_Date = null;
            this.Due_Time = null;
            this.On_Time = 0;
            this.Priority_Val = null;
            this.RID = null;
            this.Repeatable = i;
            this.JSONString = ("{" + "\"RID\"" + ":" + this.RID + ","
                    + "\"Description\"" + ":" + this.Description + ","
                    + "\"Due_Date\"" + ":" + this.Due_Date + ","
                    + "\"Due_Time\"" + ":" + this.Due_Time + ","
                    + "\"C_Remind_Date\"" + ":" + this.C_Remind_Date + ","
                    + "\"C_Remind_Time\"" + ":" + this.C_Remind_time + ","
                    + "\"Priority_Val\"" + ":"+ this.Priority_Val + ","
                    + "\"Alarm\"" + ":" + this.Alarm + ","
                    + "\"On_Time\"" + ":" + this.On_Time + ","
                    + "\"Repeatable\"" + ":" + this.Repeatable + ","
                    + "}");
        }
        
        public static JSON convert_from_string_to_Object(String JSONstring){
           JSON new_JSON = new JSON(1);
            
                
           
                int RIDcolon = JSONstring.indexOf(":");
                JSONstring = JSONstring.substring(RIDcolon+1);
                int RIDcomma = JSONstring.indexOf(",");
                new_JSON.RID = JSONstring.substring(0, RIDcomma);
                
                int DescColon = JSONstring.indexOf(":");
                JSONstring = JSONstring.substring(DescColon+1);
                int DescComma = JSONstring.indexOf(",");
                new_JSON.Description = JSONstring.substring(0, DescComma);
                
                int DueDateColon = JSONstring.indexOf(":");
                JSONstring = JSONstring.substring(DueDateColon+1);
                int DueDateComma = JSONstring.indexOf(",");
                new_JSON.Due_Date = JSONstring.substring(0, DueDateComma);
                
                int DueTimeColon = JSONstring.indexOf(":");
                JSONstring = JSONstring.substring(DueTimeColon+1);
                int DueTimeComma = JSONstring.indexOf(",");
                new_JSON.Due_Time = JSONstring.substring(0, DueTimeComma);
                
                int C_DateColon = JSONstring.indexOf(":");
                JSONstring = JSONstring.substring(C_DateColon+1);
                int C_DateComma = JSONstring.indexOf(",");
                new_JSON.C_Remind_Date = JSONstring.substring(0, C_DateComma);
                
                int C_TimeColon = JSONstring.indexOf(":");
                JSONstring = JSONstring.substring(C_TimeColon+1);
                int C_TimeComma = JSONstring.indexOf(",");
                new_JSON.C_Remind_time = JSONstring.substring(0, C_TimeComma);
                
                int priorityColon = JSONstring.indexOf(":");
                JSONstring = JSONstring.substring(priorityColon+1);
                int priorityComma = JSONstring.indexOf(",");
                new_JSON.Priority_Val = JSONstring.substring(0, priorityComma);
                
                int alarmColon = JSONstring.indexOf(":");
                JSONstring = JSONstring.substring(alarmColon+1);
                int alarmComma = JSONstring.indexOf(",");
                
                if (JSONstring.substring(0, alarmComma).equalsIgnoreCase("1")){
                    new_JSON.Alarm = 1;
                } else{
                    new_JSON.Alarm = 0;
                }
                
                int on_TimeColon = JSONstring.indexOf(":");
                JSONstring = JSONstring.substring(on_TimeColon+1);
                int on_TimeComma = JSONstring.indexOf(",");
                
                if(JSONstring.substring(0, on_TimeComma).equalsIgnoreCase("1")){
                    new_JSON.On_Time = 1;
                }else{
                    new_JSON.On_Time = 0;
                }
                
                int repeatColon = JSONstring.indexOf(":");
                JSONstring = JSONstring.substring(repeatColon+1);
                int repeatComma = JSONstring.indexOf(",");
                
                new_JSON.Repeatable = Integer.parseInt(JSONstring.substring(0, repeatComma));
                
                new_JSON.JSONString = ("{" + "\"RID\"" + ":" + new_JSON.RID + ","
                    + "\"Description\"" + ":" + new_JSON.Description + ","
                    + "\"Due_Date\"" + ":" + new_JSON.Due_Date + ","
                    + "\"Due_Time\"" + ":" + new_JSON.Due_Time + ","
                    + "\"C_Remind_Date\"" + ":" + new_JSON.C_Remind_Date + ","
                    + "\"C_Remind_Time\"" + ":" + new_JSON.C_Remind_time + ","
                    + "\"Priority_Val\"" + ":"+ new_JSON.Priority_Val + ","
                    + "\"Alarm\"" + ":" + new_JSON.Alarm + ","
                    + "\"On_Time\"" + ":" + new_JSON.On_Time + ","
                    + "\"Repeatable\"" + ":" + new_JSON.Repeatable + ","
                    + "}");
                
                
                
                
                
            
            
            
            
            return new_JSON;
            
        }


    public  String get_RID(){
        //will get the value of RID
        return this.RID;
    }

    public void set_RID(String _RID){
        this.RID = _RID;

    }

    public  String get_Description(){

        //will get the value of description
        
        return this.Description;
    }

    public void set_Description(String _Description){
        this.Description = _Description;

    }
    public  String get_Due_Date(){

        //will get the value of Due Date
       
        return this.Due_Date;
    }

    public void set_Due_Date(String _Due_Date){
        this.Due_Date = _Due_Date;

    }

    public  String get_Due_Time(){

        //will get the value of Due Time
        
        return Due_Time;
    }

    public void set_Due_Time(String _Due_Time){
        this.Due_Time = _Due_Time;

    }
    public  String get_C_Remind_Date(){
        //will get the value of Calculated Remind Date
        return this.C_Remind_Date;
    }

    public void set_C_Remind_Date(String _C){
        this.C_Remind_Date = _C;

    }
    public  String get_C_Remind_Time(){
        //will get the value of calculated Remind Time
        return this.C_Remind_time;
    }

    private  void set_C_Remind_Time(String _R){
        this.C_Remind_time = _R;

    }

    public  String get_Priority_Val(){

        //will get the value of priority value
        return this.Priority_Val;
    }

    private  void set_Priority_Val(String _Prio){
        this.Priority_Val = _Prio;
    }

    public  int get_Alarm(){
        //will get the value of alarm
        
        return this.Alarm;
    }

    public  void set_Alarm(int alarm){
        this.Alarm = alarm;

    }

    public  int get_On_Time(){
        //will get the value of On_time
        
            return this.On_Time;
    }

    public  void set_On_Time(int time){
        this.On_Time = time;

    }

    public  int get_Repeatable(){
        //will get the value of description
        return this.Repeatable;
    }

    public  void set_Repeatable(int _r){
        this.Repeatable = _r;

    }
    
    public String get_JSONSTRING(){
        return this.JSONString;
    }



}