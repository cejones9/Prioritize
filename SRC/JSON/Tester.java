public class Tester{
    
    public static void main(String[] args) {
   
        JSON myJ = new JSON("0123456789", "Testing", "Monday", "800", "Friday", "Noon",
        "5", 1, 1, 1);
        
        System.out.println(myJ.get_JSONSTRING());
        
        JSON newJ = JSON.convert_from_string_to_Object(myJ.get_JSONSTRING());
        
        System.out.println(newJ.get_JSONSTRING());
        
        
        
}
}