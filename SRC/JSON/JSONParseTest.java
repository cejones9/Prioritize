public class JSONParseTest{

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
       // static SetAlarm newAlarm_Information = new SetAlarm();

        //call the variables
        
        public static void main(String[] args) {
            JSON newone = new JSON();
            System.out.println(newone.JSONString);
            
            JSON test = JSON.convert_from_string_to_Object(newone.JSONString);
            System.out.println(test.RID);
            System.out.println(test.Due_Date);
            System.out.println(test.Due_Time);
            System.out.println(test.C_Remind_Date);
            System.out.println(test.C_Remind_time);
            System.out.println(test.Priority_Val);
            System.out.println(test.Alarm);
            System.out.println(test.On_Time);
           System.out.println(test.Repeatable);
            
            
            
}

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
        
        public static JSON convert_from_string_to_Object(String JSONstring){
            JSON new_JSON = new JSON();
            new_JSON.Alarm = null;
            new_JSON.C_Remind_Date = null;
            new_JSON.C_Remind_time = null;
            new_JSON.Description = null;
            new_JSON.Due_Date = null;
            new_JSON.Due_Time = null;
            new_JSON.On_Time = null;
            new_JSON.Priority_Val = null;
            new_JSON.RID = null;
            new_JSON.Repeatable = 111;
                
           
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
                
                if (JSONstring.substring(0, alarmComma).equalsIgnoreCase("True")){
                    new_JSON.Alarm = true;
                } else{
                    new_JSON.Alarm = false;
                }
                
                int on_TimeColon = JSONstring.indexOf(":");
                JSONstring = JSONstring.substring(on_TimeColon+1);
                int on_TimeComma = JSONstring.indexOf(",");
                
                if(JSONstring.substring(0, on_TimeComma).equalsIgnoreCase("true")){
                    new_JSON.On_Time = true;
                }else{
                    new_JSON.On_Time = false;
                }
                
                int repeatColon = JSONstring.indexOf(":");
                JSONstring = JSONstring.substring(repeatColon+1);
                int repeatComma = JSONstring.indexOf(",");
                
                new_JSON.Repeatable = Integer.parseInt(JSONstring.substring(0, repeatComma));
                
                
                
                
                
            
            
            
            
            return new_JSON;
            
        }
        
         public static String get_RID(){
        //will get the value of RID
        return "0123456789";
    }

    private static void set_RID(JSON a){
        a.RID = get_RID();

    }

    public static String get_Description(){

        //will get the value of description
       return "Testing the JSON.";
    }

    private static void set_Description(JSON a){
        a.Description = get_Description();

    }
    public static String get_Due_Date(){

        //will get the value of Due Date
       return "Monday";
    }

    private static void set_Due_Date(JSON a){
        a.Due_Date = get_Due_Date();

    }

    public static String get_Due_Time(){

        //will get the value of Due Time
        
        return "12 PM";
    }

    private static void set_Due_Time(JSON a){
        a.Due_Time = get_Due_Time();

    }
    public static String get_C_Remind_Date(){
        //will get the value of Calculated Remind Date
        return "Tuesday";
    }

    private static void set_C_Remind_Date(JSON a){
        a.C_Remind_Date = get_C_Remind_Date();

    }
    public static String get_C_Remind_Time(){
        //will get the value of calculated Remind Time
        return "10 PM";
    }

    private static void set_C_Remind_Time(JSON a){
        a.C_Remind_time = get_C_Remind_Time();

    }

    public static String get_Priority_Val(){

        //will get the value of priority value
//        int priority_Val = newAlarm_Information.priority_Value;
//        String p_Val =  String.valueOf(priority_Val);
//        return p_Val;
        return "5";
    }

    private static void set_Priority_Val(JSON a){
        a.Priority_Val = get_Priority_Val();
    }

    public static boolean get_Alarm(){
        //will get the value of alarm
//        boolean alarm_Type = false;
//        if (newAlarm_Information.alarm_Type.isChecked() == true) {
//            return alarm_Type = true;
//        } else
//        return alarm_Type;
        return true;
    }

    private static void set_Alarm(JSON a){
        a.Alarm = get_Alarm();

    }

    public static boolean get_On_Time(){
        //will get the value of On_time
//        boolean on_Time = false;
//        if (newAlarm_Information.on_Time.isChecked() == true) {
//            return on_Time = true;
//        } else
//            return on_Time;
        return true;
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
