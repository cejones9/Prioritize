//Set Alarm
JSON object = new JSON(RID, reminderDescription, datePicked, timePicked, "031222017", "1630", Integer.toString(priority_Value), Integer.parseInt(alarm), Integer.parseInt(onTime), 0);
                String JSONString = object.JSONString;
                saveToDrive(JSONString, RID);
                
//Reminders
public void sync(){
        String JSONString = "";
        JSON a = JSON.convert_from_string_to_Object(JSONString);





    }
    
//JSON
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
                + "\"Alarm\"" + ":" + this.Alarm + ","
                + "\"On_Time\"" + ":" + this.On_Time + ","
                + "\"Repeatable\"" + ":" + this.Repeatable + ","
                + "}");


           /* insertIntoSQLite = ("INSERT into Reminder (" + this.RID + ", " + this.Description + ", "
                    + this.Due_date + ", " + this.Due_time + ", " + this.C_Remind_Date + ", " + this.C_Remind_Time +
                    ", " + this.Priority_Val + ", " + this.Alarm.toString() + ", " + this.On_Time.toString() + ", " + this.Repeatable + ")");
                String insertIntoSQLite = "INSERT into Reminder (" +this.RID+", "
                   + this.Description + ", " + ....
                */



    }

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

        return new_JSON;

    }


    public  String get_RID(){
        //will get the value of RID
        return null;
    }

    private  void set_RID(JSON a){
        a.RID = get_RID();

    }

    public  String get_Description(){

        //will get the value of description
        String description = newAlarm_Information.alarm_Description.getText().toString();
        return description;
    }

    private  void set_Description(JSON a){
        a.Description = get_Description();

    }
    public  String get_Due_Date(){

        //will get the value of Due Date
        String due_Date = newAlarm_Information.date_Picked();
        return due_Date;
    }

    private  void set_Due_Date(JSON a){
        a.Due_Date = get_Due_Date();

    }

    public  String get_Due_Time(){

        //will get the value of Due Time
        String due_Time = newAlarm_Information.time_Picked();
        return due_Time;
    }

    private  void set_Due_Time(JSON a){
        a.Due_Time = get_Due_Time();

    }
    public  String get_C_Remind_Date(){
        //will get the value of Calculated Remind Date
        return null;
    }

    private  void set_C_Remind_Date(JSON a){
        a.C_Remind_Date = get_C_Remind_Date();

    }
    public  String get_C_Remind_Time(){
        //will get the value of calculated Remind Time
        return null;
    }

    private  void set_C_Remind_Time(JSON a){
        a.C_Remind_time = get_C_Remind_Time();

    }

    public  String get_Priority_Val(){

        //will get the value of priority value
        int priority_Val = newAlarm_Information.priority_Value;
        String p_Val =  String.valueOf(priority_Val);
        return p_Val;
    }

    private  void set_Priority_Val(JSON a){
        a.Priority_Val = get_Priority_Val();
    }

    public  int get_Alarm(){
        //will get the value of alarm
        int alarm_Type;
        if (newAlarm_Information.alarm_Type.isChecked() == true) {
            alarm_Type = 1;
        } else{
            alarm_Type=0;}

        return alarm_Type;
    }

    private  void set_Alarm(JSON a){
        a.Alarm = get_Alarm();

    }

    public  int get_On_Time(){
        //will get the value of On_time
        int on_Time;
        if (newAlarm_Information.on_Time.isChecked() == true) {
            on_Time = 1;
        } else{
            on_Time = 0;
        }
        return on_Time;
    }

    private  void set_On_Time(JSON a){
        a.On_Time = get_On_Time();

    }

    public  int get_Repeatable(){
        //will get the value of description
        return 0;
    }

    private  void set_Repeatable(JSON a){
        a.Repeatable = get_Repeatable();

    }

}
