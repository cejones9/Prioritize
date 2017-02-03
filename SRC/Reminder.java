/*
This is the work of Joel Wilhelm. I have created this class
for my Senior Capstone Project.
I will be documenting my design choices through comments, and then a description
in the GitHub Docs page.

This initially will be a skeleton, incomplete, to give an idea for how
it might be completed in the future.
*/

public class Reminder{
    /*
    Fields: 
    */
    String description;
    String time_due; 
    String date_due;
    /*
   Not sure how to represent time yet.
   These likely have their own field, will keep as is.
   Will keep in mind that idea of having an object/interface hold both of them.
   Interface because we may want to have the time_due be null
    */
    double priority;
    /*
    Priority will be based on a percentage, so a number from 1-100.
    This will be a double for the sake of having 1 less operation.\
    For example .5 will more easily take time remaining into a half
    EX: (3000 minutes)*(.5) = 1500 minutes.
    Will give it a range of 0-1
    */
    boolean alarm; //will be true if alarm is wanted.
    boolean alert; // will be true if alert is wanted.
    boolean repeatable; //will be true if repeat notices are allowed.
    
    
    
    
    /**
     * Main (will be used for testing, will be deleted in final installment)
     * @param args 
     */
    public static void main(String[] args) {
        
        
        
        
       
    }
    
    /**
     * get_description will ask the user for a description of their desired
     * reminder.
     * @return - Will return the string entered
     */
    
    public static String get_description(){
     
        
        return "";
    }
    private static String update_description(){
        
        
        return "";
    }
    
    public static String get_time(){
        
        return "";
    }
    
    private static String update_time(){
        
        return"";
       
    }
    
    /*
    SETTING THE PRIORITY WILL BE DONE VIA A SCALE, NOT SURE HOW TO IMPLEMENT
    HERE. For simplicity, I'll just "assume" the user enters a value. We can 
    easily update this later. 
    */
    public static int get_priority(){
     
        return 0;
    }
    
    private static int update_priority(){
        
        return 0;
    }
    
    
    private static void set_alarm(){
        
        
    }
    
    private static void set_alert(){
        
        
    }
    
    private static void set_repeatable(){
        
        
    }
    
    private static void activate_alarm(){
        
    }
    
    private static void activate_alert(){
        
        
    }
    
    
    
}
