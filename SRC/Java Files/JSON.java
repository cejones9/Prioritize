package com.seniorproject.prioritize;

/**
 * Created by shahrukh on 3/11/2017.
 */

public class JSON{

    public String RID;
    public String Description;
    public String Due_Date;
    public String Due_Time;
    public String Due_Date2;
    public String Due_Time2;
    public String Due_Date3;
    public String Due_Time3;
    public String Due_Date4;
    public String Due_Time4;
    public String Due_Date5;
    public String Due_Time5;
    public String Due_Date6;
    public String Due_Time6;
    public String Due_Date7;
    public String Due_Time7;
    public String C_Remind_Date;
    public String C_Remind_time;
    public String Priority_Val;
    public int Alarm;
    public int On_Time;
    public int Repeatable;
    public int NumDates;
    public int currday;


    //public String that represents the JSON representation
    public String JSONString;

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







    //   }

    public JSON(String RID, String Description, String datepicked, String timepicked,
                String date2, String time2, String date3, String time3, String date4,
                String time4, String date5, String time5, String date6, String time6,
                String date7, String time7, String calcdate, String calcTime,
                String priorityval, int alarm, int ontime, int repeat, int dates, int currd){

        this.Alarm = alarm;
        this.C_Remind_Date = calcdate;
        this.C_Remind_time = calcTime;
        this.Description = Description;
        this.Due_Date = datepicked;
        this.Due_Time = timepicked;
        this.Due_Date2 = date2;
        this.Due_Date3 = date3;
        this.Due_Date4 = date4;
        this.Due_Date5 = date5;
        this.Due_Date6 = date6;
        this.Due_Date7 = date7;
        this.Due_Time2 = time2;
        this.Due_Time3 = time3;
        this.Due_Time4 = time4;
        this.Due_Time5 = time5;
        this.Due_Time6 = time6;
        this.Due_Time7 = time7;
        this.On_Time = ontime;
        this.Priority_Val = priorityval;
        this.RID = RID;
        this.Repeatable = repeat;
        this.NumDates = dates;
        this.currday = currd;
        this.JSONString = ("{" + "\"RID\"" + ":" + this.RID + ","
                + "\"Description\"" + ":" + this.Description + ","
                + "\"Due_Date\"" + ":" + this.Due_Date + ","
                + "\"Due_Time\"" + ":" + this.Due_Time + ","
                + "\"Due_Date2\"" + ":" + this.Due_Date2 + ","
                + "\"Due_Time2\"" + ":" + this.Due_Time2 + ","
                + "\"Due_Date3\"" + ":" + this.Due_Date3 + ","
                + "\"Due_Time3\"" + ":" + this.Due_Time3 + ","
                + "\"Due_Date4\"" + ":" + this.Due_Date4 + ","
                + "\"Due_Time4\"" + ":" + this.Due_Time4 + ","
                + "\"Due_Date5\"" + ":" + this.Due_Date5 + ","
                + "\"Due_Time5\"" + ":" + this.Due_Time5 + ","
                + "\"Due_Date6\"" + ":" + this.Due_Date6 + ","
                + "\"Due_Time6\"" + ":" + this.Due_Time6 + ","
                + "\"Due_Date7\"" + ":" + this.Due_Date7 + ","
                + "\"Due_Time7\"" + ":" + this.Due_Time7 + ","
                + "\"C_Remind_Date\"" + ":" + this.C_Remind_Date + ","
                + "\"C_Remind_Time\"" + ":" + this.C_Remind_time + ","
                + "\"Priority_Val\"" + ":"+ this.Priority_Val + ","
                + "\"Alarm\"" + ":" + this.Alarm + ","
                + "\"On_Time\"" + ":" + this.On_Time + ","
                + "\"Repeatable\"" + ":" + this.Repeatable + ","
                + "\"Num_Dates\"" + ":" + this.NumDates + ","
                + "\"CurrDay\"" + ":" + this.currday + ","
                + "}");
    }

    public JSON(int i){
        this.Alarm = 0;
        this.C_Remind_Date = null;
        this.C_Remind_time = null;
        this.Description = null;
        this.Due_Date = null;
        this.Due_Time = null;
        this.Due_Date2 = null;
        this.Due_Date3 = null;
        this.Due_Date4 = null;
        this.Due_Date5 = null;
        this.Due_Date6 = null;
        this.Due_Date7 = null;
        this.Due_Time2 = null;
        this.Due_Time3 = null;
        this.Due_Time4 = null;
        this.Due_Time5 = null;
        this.Due_Time6 = null;
        this.Due_Time7 = null;
        this.On_Time = 0;
        this.Priority_Val = null;
        this.RID = null;
        this.Repeatable = i;
        this.NumDates = 0;
        this.currday = 0;
        this.JSONString = ("{" + "\"RID\"" + ":" + this.RID + ","
                + "\"Description\"" + ":" + this.Description + ","
                + "\"Due_Date\"" + ":" + this.Due_Date + ","
                + "\"Due_Time\"" + ":" + this.Due_Time + ","
                + "\"Due_Date2\"" + ":" + this.Due_Date2 + ","
                + "\"Due_Time2\"" + ":" + this.Due_Time2 + ","
                + "\"Due_Date3\"" + ":" + this.Due_Date3 + ","
                + "\"Due_Time3\"" + ":" + this.Due_Time3 + ","
                + "\"Due_Date4\"" + ":" + this.Due_Date4 + ","
                + "\"Due_Time4\"" + ":" + this.Due_Time4 + ","
                + "\"Due_Date5\"" + ":" + this.Due_Date5 + ","
                + "\"Due_Time5\"" + ":" + this.Due_Time5 + ","
                + "\"Due_Date6\"" + ":" + this.Due_Date6 + ","
                + "\"Due_Time6\"" + ":" + this.Due_Time6 + ","
                + "\"Due_Date7\"" + ":" + this.Due_Date7 + ","
                + "\"Due_Time7\"" + ":" + this.Due_Time7 + ","
                + "\"C_Remind_Date\"" + ":" + this.C_Remind_Date + ","
                + "\"C_Remind_Time\"" + ":" + this.C_Remind_time + ","
                + "\"Priority_Val\"" + ":"+ this.Priority_Val + ","
                + "\"Alarm\"" + ":" + this.Alarm + ","
                + "\"On_Time\"" + ":" + this.On_Time + ","
                + "\"Repeatable\"" + ":" + this.Repeatable + ","
                + "\"Num_Dates\"" + ":" + this.NumDates + ","
                + "\"CurrDay\"" + ":" + this.currday + ","
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

        int DueDate2Colon = JSONstring.indexOf(":");
        JSONstring = JSONstring.substring(DueDate2Colon+1);
        int DueDate2Comma = JSONstring.indexOf(",");
        new_JSON.Due_Date2 = JSONstring.substring(0, DueDate2Comma);

        int DueTime2Colon = JSONstring.indexOf(":");
        JSONstring = JSONstring.substring(DueTime2Colon+1);
        int DueTime2Comma = JSONstring.indexOf(",");
        new_JSON.Due_Time2 = JSONstring.substring(0, DueTime2Comma);

        int DueDate3Colon = JSONstring.indexOf(":");
        JSONstring = JSONstring.substring(DueDate3Colon+1);
        int DueDate3Comma = JSONstring.indexOf(",");
        new_JSON.Due_Date3 = JSONstring.substring(0, DueDate3Comma);

        int DueTime3Colon = JSONstring.indexOf(":");
        JSONstring = JSONstring.substring(DueTime3Colon+1);
        int DueTime3Comma = JSONstring.indexOf(",");
        new_JSON.Due_Time3 = JSONstring.substring(0, DueTime3Comma);

        int DueDate4Colon = JSONstring.indexOf(":");
        JSONstring = JSONstring.substring(DueDate4Colon+1);
        int DueDate4Comma = JSONstring.indexOf(",");
        new_JSON.Due_Date4 = JSONstring.substring(0, DueDate4Comma);

        int DueTime4Colon = JSONstring.indexOf(":");
        JSONstring = JSONstring.substring(DueTime4Colon+1);
        int DueTime4Comma = JSONstring.indexOf(",");
        new_JSON.Due_Time4 = JSONstring.substring(0, DueTime4Comma);

        int DueDate5Colon = JSONstring.indexOf(":");
        JSONstring = JSONstring.substring(DueDate5Colon+1);
        int DueDate5Comma = JSONstring.indexOf(",");
        new_JSON.Due_Date5 = JSONstring.substring(0, DueDate5Comma);

        int DueTime5Colon = JSONstring.indexOf(":");
        JSONstring = JSONstring.substring(DueTime5Colon+1);
        int DueTime5Comma = JSONstring.indexOf(",");
        new_JSON.Due_Time5 = JSONstring.substring(0, DueTime5Comma);

        int DueDate6Colon = JSONstring.indexOf(":");
        JSONstring = JSONstring.substring(DueDate6Colon+1);
        int DueDate6Comma = JSONstring.indexOf(",");
        new_JSON.Due_Date6 = JSONstring.substring(0, DueDate6Comma);

        int DueTime6Colon = JSONstring.indexOf(":");
        JSONstring = JSONstring.substring(DueTime6Colon+1);
        int DueTime6Comma = JSONstring.indexOf(",");
        new_JSON.Due_Time6 = JSONstring.substring(0, DueTime6Comma);

        int DueDate7Colon = JSONstring.indexOf(":");
        JSONstring = JSONstring.substring(DueDate7Colon+1);
        int DueDate7Comma = JSONstring.indexOf(",");
        new_JSON.Due_Date7 = JSONstring.substring(0, DueDate7Comma);

        int DueTime7Colon = JSONstring.indexOf(":");
        JSONstring = JSONstring.substring(DueTime7Colon+1);
        int DueTime7Comma = JSONstring.indexOf(",");
        new_JSON.Due_Time7 = JSONstring.substring(0, DueTime7Comma);

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

        int numdatesColon = JSONstring.indexOf(":");
        JSONstring = JSONstring.substring(numdatesColon+1);
        int numdatesComma = JSONstring.indexOf(",");
        new_JSON.NumDates = Integer.parseInt(JSONstring.substring(0, numdatesComma));

        int currdayColon = JSONstring.indexOf(":");
        JSONstring = JSONstring.substring(currdayColon+1);
        int currdayComma = JSONstring.indexOf(",");
        new_JSON.currday = Integer.parseInt(JSONstring.substring(0, currdayComma));


        new_JSON.JSONString = ("{" + "\"RID\"" + ":" + new_JSON.RID + ","
                + "\"Description\"" + ":" + new_JSON.Description + ","
                + "\"Due_Date\"" + ":" + new_JSON.Due_Date + ","
                + "\"Due_Time\"" + ":" + new_JSON.Due_Time + ","
                + "\"Due_Date2\"" + ":" + new_JSON.Due_Date2 + ","
                + "\"Due_Time2\"" + ":" + new_JSON.Due_Time2 + ","
                + "\"Due_Date3\"" + ":" + new_JSON.Due_Date3 + ","
                + "\"Due_Time3\"" + ":" + new_JSON.Due_Time3 + ","
                + "\"Due_Date4\"" + ":" + new_JSON.Due_Date4 + ","
                + "\"Due_Time4\"" + ":" + new_JSON.Due_Time4 + ","
                + "\"Due_Date5\"" + ":" + new_JSON.Due_Date5 + ","
                + "\"Due_Time5\"" + ":" + new_JSON.Due_Time5 + ","
                + "\"Due_Date6\"" + ":" + new_JSON.Due_Date6 + ","
                + "\"Due_Time6\"" + ":" + new_JSON.Due_Time6 + ","
                + "\"Due_Date7\"" + ":" + new_JSON.Due_Date7 + ","
                + "\"Due_Time7\"" + ":" + new_JSON.Due_Time7 + ","
                + "\"C_Remind_Date\"" + ":" + new_JSON.C_Remind_Date + ","
                + "\"C_Remind_Time\"" + ":" + new_JSON.C_Remind_time + ","
                + "\"Priority_Val\"" + ":"+ new_JSON.Priority_Val + ","
                + "\"Alarm\"" + ":" + new_JSON.Alarm + ","
                + "\"On_Time\"" + ":" + new_JSON.On_Time + ","
                + "\"Repeatable\"" + ":" + new_JSON.Repeatable + ","
                + "\"Num_Dates\"" + ":" + new_JSON.NumDates + ","
                + "\"CurrDAy\"" + ":" + new_JSON.currday + ","
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
    public  String get_Due_Date2(){

        //will get the value of Due Date

        return this.Due_Date2;
    }

    public void set_Due_Date2(String _Due_Date){
        this.Due_Date2 = _Due_Date;

    }


    public  String get_Due_Date3(){

        //will get the value of Due Date

        return this.Due_Date3;
    }

    public void set_Due_Date3(String _Due_Date){
        this.Due_Date3 = _Due_Date;

    }public  String get_Due_Date4(){

        //will get the value of Due Date

        return this.Due_Date4;
    }

    public void set_Due_Date4(String _Due_Date){
        this.Due_Date4 = _Due_Date;

    }public  String get_Due_Date5(){

        //will get the value of Due Date

        return this.Due_Date5;
    }

    public void set_Due_Date5(String _Due_Date){
        this.Due_Date5 = _Due_Date;

    }public  String get_Due_Date6(){

        //will get the value of Due Date

        return this.Due_Date6;
    }

    public void set_Due_Date6(String _Due_Date){
        this.Due_Date6 = _Due_Date;

    }public  String get_Due_Date7(){

        //will get the value of Due Date

        return this.Due_Date7;
    }

    public void set_Due_Date7(String _Due_Date){
        this.Due_Date7 = _Due_Date;

    }

    public  String get_Due_Time(){

        //will get the value of Due Time

        return Due_Time;
    }

    public void set_Due_Time(String _Due_Time){
        this.Due_Time = _Due_Time;

    }

    public  String get_Due_Time2(){

        //will get the value of Due Time

        return Due_Time2;
    }

    public void set_Due_Time2(String _Due_Time){
        this.Due_Time2 = _Due_Time;

    }public  String get_Due_Time3(){

        //will get the value of Due Time

        return Due_Time3;
    }

    public void set_Due_Time3(String _Due_Time){
        this.Due_Time3 = _Due_Time;

    }public  String get_Due_Time4(){

        //will get the value of Due Time

        return Due_Time4;
    }

    public void set_Due_Time4(String _Due_Time){
        this.Due_Time4 = _Due_Time;

    }public  String get_Due_Time5(){

        //will get the value of Due Time

        return Due_Time5;
    }

    public void set_Due_Time5(String _Due_Time){
        this.Due_Time5 = _Due_Time;

    }public  String get_Due_Time6(){

        //will get the value of Due Time

        return Due_Time6;
    }

    public void set_Due_Time6(String _Due_Time){
        this.Due_Time6 = _Due_Time;

    }public  String get_Due_Time7(){

        //will get the value of Due Time

        return Due_Time7;
    }

    public void set_Due_Time7(String _Due_Time){
        this.Due_Time7 = _Due_Time;

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

    public int get_curr_day(){

        return this.currday;

    }

    public void set_curr_day(int curr){
        this.currday = curr;
    }

    public String get_JSONSTRING(){
        return this.JSONString;
    }



}