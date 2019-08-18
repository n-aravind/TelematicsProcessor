package utils;

public class Utils {
    public static void sleepMs(long time){
        try{
            Thread.sleep(time);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
